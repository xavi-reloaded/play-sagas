package repositories;

import dtos.PagedListDTO;
import exceptions.FieldsNotExistException;
import models.BaseModel;
import org.junit.Test;
import repository.BaseCRUDRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public abstract class BaseCrudRepositoriesTest<T extends BaseModel, S extends BaseCRUDRepository<T>> extends RepositoryExercises{

    public S sut;

    protected abstract T createModel() throws ExecutionException, InterruptedException;
    protected abstract T updateModel(T created) throws ExecutionException, InterruptedException;
    protected abstract Boolean checkModelUpdate(T original, T updated);
    protected abstract LinkedHashMap<String, String> getExistingField();

    @Test
    public void test_insert() throws ExecutionException, InterruptedException {
        T created = this.createModel();
        assertThat(created.getId()).isGreaterThan(0);
    }

    @Test
    public void test_delete() throws ExecutionException, InterruptedException {
        T created = this.createModel();
        Long id = created.getId();
        Optional<Long> deletedId = this.sut.delete(id).toCompletableFuture().get();
        CompletionStage<Optional<T>> lookup = this.sut.lookup(deletedId.get());
        await().atMost(1, SECONDS).until(() ->
                assertThat(lookup.toCompletableFuture()).isCompletedWithValueMatching(model -> (!model.isPresent() && Objects.equals(id, deletedId.get())))
        );
    }

    @Test
    public void test_delete_failnotexist(){
        CompletionStage<Optional<Long>> delete = this.sut.delete(4564654654L);
        await().atMost(1, SECONDS).until(() ->
                assertThat(delete.toCompletableFuture()).isCompletedWithValueMatching(modelId -> (!modelId.isPresent()))
        );
    }

    @Test
    public void test_lookup() throws ExecutionException, InterruptedException {
        T created = this.createModel();
        CompletionStage<Optional<T>> lookup = this.sut.lookup(created.getId());
        await().atMost(1, SECONDS).until(() ->
                assertThat(lookup.toCompletableFuture()).isCompletedWithValueMatching(modelOptional -> {
                    final T model = modelOptional.get();
                    return (model.getId().equals(created.getId()));
                })
        );
    }

    @Test
    public void test_update() throws ExecutionException, InterruptedException {
        T created = this.createModel();
        T updated = this.updateModel(created);
        this.sut.update(created).toCompletableFuture().get();
        CompletionStage<Optional<T>> lookup = this.sut.lookup(created.getId());
        await().atMost(1, SECONDS).until(() ->
                assertThat(lookup.toCompletableFuture()).isCompletedWithValueMatching(modelOptional -> {
                    final T model = modelOptional.get();
                    return (this.checkModelUpdate(updated, model));
                })
        );
    }

    @Test
    public void test_findAll() throws ExecutionException, InterruptedException, FieldsNotExistException {
        this.createModel();
        this.createModel();
        this.createModel();
        CompletionStage<PagedListDTO<T>> page = this.sut.findAll(0, 10, null, null, null);
        await().atMost(1, SECONDS).until(() ->
                assertThat(page.toCompletableFuture()).isCompletedWithValueMatching(pagedList -> {
                    List<T> data = pagedList.getData();
                    return (data.size()>=3);
                })
        );
    }

    @Test
    public void test_findAll_existingField() throws ExecutionException, InterruptedException, FieldsNotExistException {
        this.createModel();
        this.createModel();
        this.createModel();
        LinkedHashMap<String, String> existingField = this.getExistingField();
        String firstKey = (String) existingField.keySet().toArray()[0];
        CompletionStage<PagedListDTO<T>> page = this.sut.findAll(0, 10, firstKey, existingField.get(firstKey), firstKey);
        await().atMost(1, SECONDS).until(() ->
                assertThat(page.toCompletableFuture()).isCompletedWithValueMatching(pagedList -> {
                    List<T> data = pagedList.getData();
                    return (data.size()>=3);
                })
        );
    }

    @Test
    public void test_findAll_notExistingField() throws ExecutionException, InterruptedException, FieldsNotExistException {
        this.createModel();
        LinkedHashMap<String, String> existingField = this.getExistingField();
        String firstKey = (String) existingField.keySet().toArray()[0];
        CompletionStage<PagedListDTO<T>> page = this.sut.findAll(0, 10, "thisFieldWillNeverExist", "thatValueDontAlso", firstKey);
        await().atMost(1, SECONDS).until(() ->
                assertThat(page.toCompletableFuture()).isCompletedExceptionally()
        );
    }

}
