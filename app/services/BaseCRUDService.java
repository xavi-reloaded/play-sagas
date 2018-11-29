package services;

import dtos.PagedListDTO;
import models.BaseModel;
import models.User;
import repository.IBaseCRUDRepository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class BaseCRUDService<T extends BaseModel, S extends IBaseCRUDRepository<T>> implements IBaseCRUDService<T> {

    protected final S repository;

    public BaseCRUDService(S repository){
        this.repository = repository;
    }

    @Override
    public CompletionStage<Optional<T>> lookup(Long id) {
        return this.repository.lookup(id);
    }

    @Override
    public CompletionStage<T> update(T model){
        return this.repository.update(model);
    }

    @Override
    public CompletionStage<T> insert(T model){ return this.repository.insert(model); }

    @Override
    public CompletionStage<Optional<Long>> delete(Long id){ return this.repository.delete(id); }

    @Override
    public CompletionStage<PagedListDTO<T>> findAll(Integer page, Integer pageSize, String fields, String search, String sort){
        return this.repository.findAll(page, pageSize, fields, search, sort);
    }

    @Override
    public CompletionStage<PagedListDTO<T>> findAllByUser(User user, Integer page, Integer pageSize, String fields, String search, String sort){
        return this.repository.findAllByUser(user, page, pageSize, fields, search, sort);
    }
}
