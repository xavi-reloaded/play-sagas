package services;

import dtos.PagedListDTO;
import models.BaseModel;
import models.User;
import org.junit.Test;
import repository.IBaseCRUDRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseServiceTests<T extends BaseModel, S extends IBaseCRUDService<T>, W extends IBaseCRUDRepository<T>> {

    S sut;
    W repositoryMock;
    private Class<T> modelClass;

    BaseServiceTests(Class<T> modelClass, Class<W> repositoryClass) {
        this.modelClass = modelClass;

        this.repositoryMock = mock(repositoryClass);
        when(this.repositoryMock.lookup(any(Long.class))).thenAnswer(invocation -> supplyAsync(() -> {
            T entity = this.getModel();
            entity.setId(invocation.getArgument(0));
            return Optional.of(entity);
        }));
        when(repositoryMock.update(any(modelClass))).thenAnswer(invocation -> supplyAsync(() -> {
            T entity = invocation.getArgument(0);
            entity.setId((entity.getId() != null) ? entity.getId() : 101010L);
            return entity;
        }));
        when(repositoryMock.insert(any(modelClass))).thenAnswer(invocation -> supplyAsync(() -> {
            T entity = invocation.getArgument(0);
            entity.setId(101011L);
            return entity;
        }));
        when(repositoryMock.delete(any(Long.class))).thenAnswer(invocation -> supplyAsync(() -> Optional.of(invocation.getArgument(0))));
        when(repositoryMock.findAll(anyInt(), anyInt(), anyString(), anyString(), anyString())).thenAnswer(invocation -> supplyAsync(() -> {
            ArrayList<T> entities = new ArrayList<>();
            entities.add(this.getModel());
            return new PagedListDTO<>(entities);
        }));
        when(repositoryMock.findAllByUser(any(), anyInt(), anyInt(), anyString(), anyString(), anyString())).thenAnswer(invocation -> supplyAsync(() -> {
            ArrayList<T> entities = new ArrayList<>();
            entities.add(this.getModel());
            return new PagedListDTO<>(entities);
        }));
    }

    T getModel() {
        T model = null;
        try {
            model = this.modelClass.newInstance();
            model.setId(0L);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

    @Test
    public void test_lookup() throws ExecutionException, InterruptedException {
        CompletionStage<Optional<T>> found = this.sut.lookup(101000L);
        T actual = found.toCompletableFuture().get().get();
        assertThat(actual.getId()).isEqualTo(101000L);
    }

    @Test
    public void test_update() throws ExecutionException, InterruptedException {
        T entity = this.getModel();
        entity.setId(12123L);
        CompletionStage<T> updated = this.sut.update(entity);
        T actual = updated.toCompletableFuture().get();
        assertThat(actual.getId()).isEqualTo(12123L);

        await().atMost(1, SECONDS).until(() ->
                assertThat(updated.toCompletableFuture()).isCompletedWithValueMatching(model -> (model.getId() != null))
        );
    }

    @Test
    public void test_insert() throws ExecutionException, InterruptedException {
        CompletionStage<T> inserted = this.sut.insert(this.getModel());
        T actual = inserted.toCompletableFuture().get();
        assertThat(actual.getId()).isEqualTo(101011L);
    }

    @Test
    public void test_delete() throws ExecutionException, InterruptedException {
        CompletionStage<Optional<Long>> deleted = this.sut.delete(101110L);
        Long actual = deleted.toCompletableFuture().get().get();
        assertThat(actual).isEqualTo(101110L);
    }

    @Test
    public void test_findAll() throws ExecutionException, InterruptedException {
        CompletionStage<PagedListDTO<T>> list = this.sut.findAll(0, 10, "", "", "");
        PagedListDTO<T> actual = list.toCompletableFuture().get();
        assertThat(actual.getData().size()).isEqualTo(1);
    }

    @Test
    public void test_findAllByUser() {
        CompletionStage<PagedListDTO<T>> list = this.sut.findAllByUser(new User(), 0, 10, "", "", "");
        await().atMost(1, SECONDS).until(() ->
                assertThat(list.toCompletableFuture()).isCompletedWithValueMatching(pagedList -> (pagedList.getData().size() >= 1))
        );
    }
}
