package services;

import com.google.inject.Singleton;
import dtos.PictureWithThumbDTO;
import exceptions.*;
import models.WithPicture;
import play.Logger;
import repository.IBaseCRUDRepository;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

/**
 * Created by ecobos on 7/4/17.
 */

@Singleton
public abstract class WithPictureService<T extends WithPicture, S extends IBaseCRUDRepository<T>> extends BaseCRUDService<T, S> {

    private final AssetService assetService;

    WithPictureService(S repository, AssetService assetService) {
        super(repository);
        this.assetService = assetService;
    }

    @Override
    public CompletionStage<T> insert(T model) {
        this.createPicture(model);
        return super.insert(model);
    }

    @Override
    public CompletionStage<T> update(T model) {
        try {
            CompletionStage<Optional<T>> t = this.lookup(model.getId());
            if (t != null) {
                Optional<T> optionalT = t.toCompletableFuture().get();
                if (optionalT.isPresent()) {
                    T oldEntity = optionalT.get();
                    this.updatePicture(oldEntity, model);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            Logger.error(this.getClass().getSimpleName(), e);
        }
        return super.update(model);
    }

    @Override
    public CompletionStage<Optional<Long>> delete(Long id) {
        T entity = null;
        try {
            Optional<T> t = this.lookup(id).toCompletableFuture().get();
            if (t.isPresent()) {
                entity = t.get();
                if (entity.getPicture() != null) {
                    this.assetService.removeFile(entity.getPicture());
                }
                if (entity.getThumbnail() != null) {
                    this.assetService.removeFile(entity.getThumbnail());
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            Logger.error(this.getClass().getSimpleName(), e);
        }
        if (entity == null) {
            throw new CompletionException(new ObjectNotFoundException());
        }
        return super.delete(id);
    }

    private T updatePicture(T oldEntity, T entity) {
        if (entity.getPicture() != null) {
            this.createPicture(entity);
        } else if (oldEntity.getPicture() != null && entity.getPicture() == null) {
            entity.setPicture(null);
            entity.setThumbnail(null);
        }

        if (oldEntity != null && oldEntity.getPicture() != null && !oldEntity.getPicture().equals(entity.getPicture())) {
            this.assetService.removeFile(oldEntity.getPicture());
        }
        if (oldEntity != null && oldEntity.getThumbnail() != null && !oldEntity.getThumbnail().equals(entity.getThumbnail())) {
            this.assetService.removeFile(oldEntity.getThumbnail());
        }

        return entity;
    }

    private T createPicture(T entity) {
        if (entity.getPicture() != null && entity.getPicture().contains("tmpFile")) {
            try {
                PictureWithThumbDTO imageDTO = this.assetService.commitImage(entity.getPicture());
                entity.setPicture(imageDTO.getLarge());
                entity.setThumbnail(imageDTO.getThumbnail());
            } catch (AmazonException | CouldNotCompletelyReadFileItIsTooLongException | NotPossibleCreateDirException | CouldNotCompletelyReadFileException | NotPossibleCropImgException | NotPossibleSaveFileException e) {
                Logger.error(this.getClass().getSimpleName(), e);
            }
        }
        return entity;
    }
}
