package services;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import dtos.PictureWithThumbDTO;
import exceptions.*;
import models.WithPicture;
import org.junit.Test;
import org.junit.runner.RunWith;
import repository.IBaseCRUDRepository;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public abstract class WithPictureServiceTests<T extends WithPicture, S extends IBaseCRUDService<T>, W extends IBaseCRUDRepository<T>> extends BaseServiceTests<T, S, W> {

    AssetService assetServiceMock;

    WithPictureServiceTests(Class<T> modelClass, Class<W> repositoryClass) throws AmazonException, NotPossibleCreateDirException, NotPossibleCropImgException, CouldNotCompletelyReadFileException, NotPossibleSaveFileException, CouldNotCompletelyReadFileItIsTooLongException {
        super(modelClass, repositoryClass);

        this.assetServiceMock = mock(AssetService.class);
        when(this.assetServiceMock.commitImage(anyString())).thenReturn(new PictureWithThumbDTO("large", "thumbnail"));
        when(this.assetServiceMock.removeFile(anyString())).thenReturn(true);
    }

    @DataProvider
    public static Object[][] dataProviderInsert() {
        return new Object[][]{
                {10010111L, new PictureWithThumbDTO("tmpFileTest", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("large", "thumbnail")},
                {10010111L, new PictureWithThumbDTO("tmpFileTest2", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("tmpFileTest2", null)},
                {10011111L, new PictureWithThumbDTO("tmpFileTest2", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("tmpFileTest2", null)},
                {10010111L, new PictureWithThumbDTO(null, null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO(null, null)},
                {10010111L, new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("file2", "file2"), new PictureWithThumbDTO(null, null)},
                {null, new PictureWithThumbDTO("tmpFileTest", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("large", "thumbnail")}
        };
    }

    @Test
    @UseDataProvider("dataProviderInsert")
    public void test_insert_Model(Long entityId, PictureWithThumbDTO entityPictures, PictureWithThumbDTO oldEntityPictures, PictureWithThumbDTO expected) throws AmazonException, NotPossibleCreateDirException, ExecutionException, InterruptedException, NotPossibleCropImgException, CouldNotCompletelyReadFileException, NotPossibleSaveFileException, CouldNotCompletelyReadFileItIsTooLongException {

        T oldEntity = this.getModel();
        oldEntity.setId(10010110L);
        oldEntity.setPicture(oldEntityPictures.getLarge());
        oldEntity.setThumbnail(oldEntityPictures.getThumbnail());

        when(this.repositoryMock.lookup(10010111L)).thenAnswer(invocation -> supplyAsync(() -> Optional.of(oldEntity)));
        when(this.assetServiceMock.commitImage("tmpFileTest2")).thenThrow(new NotPossibleCreateDirException());

        T entity = this.getModel();
        entity.setId(entityId);
        entity.setPicture(entityPictures.getLarge());
        entity.setThumbnail(entityPictures.getThumbnail());

        T actual = this.sut.insert(entity).toCompletableFuture().get();
        assertThat(actual.getPicture()).isEqualTo(expected.getLarge());
        assertThat(actual.getThumbnail()).isEqualTo(expected.getThumbnail());
    }

    @DataProvider
    public static Object[][] dataProviderUpdate() {
        return new Object[][]{
                {10010111L, new PictureWithThumbDTO("tmpFileTest", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("large", "thumbnail")},
                {10010111L, new PictureWithThumbDTO("tmpFileTest2", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("tmpFileTest2", null)},
                {10011111L, new PictureWithThumbDTO("tmpFileTest2", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("tmpFileTest2", null)},
                {10010111L, new PictureWithThumbDTO(null, null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO(null, null)},
                {10010111L, new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("file2", "file2"), new PictureWithThumbDTO(null, null)},
                {10010112L, new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("file2", "file2"), new PictureWithThumbDTO(null, null)},
                {null, new PictureWithThumbDTO("tmpFileTest", null), new PictureWithThumbDTO(null, null), new PictureWithThumbDTO("tmpFileTest", null)}
        };
    }

    @Test
    @UseDataProvider("dataProviderUpdate")
    public void test_update_Model(Long entityId, PictureWithThumbDTO entityPictures, PictureWithThumbDTO oldEntityPictures, PictureWithThumbDTO expected) throws AmazonException, NotPossibleCreateDirException, ExecutionException, InterruptedException, NotPossibleCropImgException, CouldNotCompletelyReadFileException, NotPossibleSaveFileException, CouldNotCompletelyReadFileItIsTooLongException {

        T oldEntity = this.getModel();
        oldEntity.setId(10010110L);
        oldEntity.setPicture(oldEntityPictures.getLarge());
        oldEntity.setThumbnail(oldEntityPictures.getThumbnail());

        when(this.repositoryMock.lookup(10010111L)).thenAnswer(invocation -> supplyAsync(() -> Optional.of(oldEntity)));
        when(this.repositoryMock.lookup(10010112L)).thenAnswer(invocation -> supplyAsync(() -> {
            throw new CompletionException(new InterruptedException());
        }));
        when(this.assetServiceMock.commitImage("tmpFileTest2")).thenThrow(new NotPossibleCreateDirException());

        T entity = this.getModel();
        entity.setId(entityId);
        entity.setPicture(entityPictures.getLarge());
        entity.setThumbnail(entityPictures.getThumbnail());

        T actual = this.sut.update(entity).toCompletableFuture().get();
        assertThat(actual.getPicture()).isEqualTo(expected.getLarge());
        assertThat(actual.getThumbnail()).isEqualTo(expected.getThumbnail());
    }

    @Test
    public void test_deleteById_UUID() throws ExecutionException, InterruptedException {
        T entity = this.getModel();
        entity.setPicture("tmpFileTest");
        entity.setThumbnail("tmpFileTest");
        when(this.repositoryMock.lookup(10010111L)).thenAnswer(invocation -> supplyAsync(() -> Optional.of(entity)));
        CompletionStage<Optional<Long>> actual = this.sut.delete(10010111L);
        assertThat(actual.toCompletableFuture().get().get()).isEqualTo(10010111L);
    }

    @Test
    public void test_deleteById_UUID1() {
        T entity = this.getModel();
        entity.setPicture("tmpFileTest");
        entity.setThumbnail("tmpFileTest");
        when(this.repositoryMock.lookup(10010111L)).thenAnswer(invocation -> supplyAsync(() -> {
            throw new CompletionException(new InterruptedException());
        }));

        String actual = "";
        try {
            this.sut.delete(10010111L).toCompletableFuture();
        } catch (CompletionException e) {
            actual = e.getMessage();
        }

        assertThat(actual).isEqualTo("exceptions.ObjectNotFoundException: Object does not exist");
    }
}
