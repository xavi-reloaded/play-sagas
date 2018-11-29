package repository;

import dtos.PagedListDTO;
import io.ebean.ExpressionList;
import models.BaseModel;
import models.User;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface IBaseCRUDRepository<T extends BaseModel> {
    CompletionStage<Optional<T>> lookup(Long id);

    CompletionStage<T> update(T model);

    CompletionStage<T> insert(T model);

    CompletionStage<Optional<Long>> delete(Long id);

    CompletionStage<PagedListDTO<T>> findAll(Integer page, Integer pageSize, String fields, String search, String sort) ;

    CompletionStage<PagedListDTO<T>> findAllByQuery(ExpressionList<T> query, Integer page, Integer pageSize, String fields, String search, String sort) ;

    CompletionStage<PagedListDTO<T>> findAllByUser(User user, Integer page, Integer pageSize, String fields, String search, String sort);
}
