package repository;

import dtos.PagedListDTO;
import exceptions.FieldsNotExistException;
import helpers.ConfigHelper;
import helpers.ContextHelper;
import helpers.ReflectionUtils;
import io.ebean.ExpressionList;
import io.ebean.Junction;
import io.ebean.Model;
import io.ebean.PagedList;
import models.BaseModel;
import models.User;
import play.Logger;
import play.db.ebean.EbeanConfig;

import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;


public abstract class BaseCRUDRepository<T extends BaseModel> extends BaseRepository implements IBaseCRUDRepository<T> {

    protected final Class<T> modelClass;
    protected final ReflectionUtils reflectionUtils;
    protected final Integer pageSize;
    protected final String dbEngine;
    private static final String POSTGRES_ENGINE = "org.postgresql.Driver";
    protected final ConfigHelper configHelper;
    protected final ContextHelper contextHelper;

    public BaseCRUDRepository(Class<T> modelClass, EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext, ConfigHelper configHelper, ContextHelper contextHelper) {
        super(ebeanConfig, executionContext);
        this.modelClass = modelClass;
        this.reflectionUtils = new ReflectionUtils();
        this.configHelper = configHelper;
        this.pageSize =  Integer.parseInt(this.configHelper.getConfigString("app.pagination.default"));
        this.dbEngine = this.configHelper.getConfigString("db.default.driver");
        this.contextHelper = contextHelper;
    }

    @Override
    public CompletionStage<Optional<T>> lookup(Long id) {
        return supplyAsync(() -> ebeanServer.find(this.modelClass).setId(id).findOneOrEmpty(), executionContext);
    }

    @Override
    public CompletionStage<T> update(T model){
        return supplyAsync(() -> {
            try {
                model.setUpdatedBy(this.contextHelper.getCurrentUser().getId());
            } catch (Exception e) {
                Logger.error(this.getClass().getSimpleName(), e.getMessage());
            }
            model.update();
            return model;
        }, executionContext);
    }

    @Override
    public CompletionStage<T> insert(T model) {
        return supplyAsync(() -> {
            try {
                Long userId = this.contextHelper.getCurrentUser().getId();
                model.setCreatedBy(userId);
                model.setUpdatedBy(userId);
            } catch (Exception e) {
                Logger.error(this.getClass().getSimpleName(), e.getMessage());
            }
            ebeanServer.insert(model);
            return model;
        }, executionContext);
    }

    @Override
    public CompletionStage<Optional<Long>> delete(Long id) {
        return supplyAsync(() -> {
            final Optional<T> modelOptional = ebeanServer.find(this.modelClass).setId(id).findOneOrEmpty();
            modelOptional.ifPresent(Model::delete);
            return modelOptional.map(c -> c.id);
        }, executionContext);
    }


    @Override
    public CompletionStage<PagedListDTO<T>> findAll(Integer page, Integer pageSize, String fields, String search, String sort) {
        return supplyAsync(() -> {
            ExpressionList<T> query =  this.ebeanServer.find(this.modelClass).where();
            try {
                return findAllByQuery(query, page, pageSize, fields, search, sort).toCompletableFuture().get();
            } catch ( InterruptedException | ExecutionException e) {
                throw new CompletionException(e);
            }
        }, executionContext);
    }

    public CompletionStage<PagedListDTO<T>> findAllByUser(User user, Integer pageInt, Integer pageSizeInt, String fields, String search, String sort) {
        ExpressionList<T> query =  this.ebeanServer.find(this.modelClass).where().eq("user", user);
        return this.findAllByQuery(query, pageInt, pageSizeInt, fields, search, sort);
    }

    @Override
    public CompletionStage<PagedListDTO<T>> findAllByQuery(ExpressionList<T> query, Integer page, Integer pageSize,
                                                           String fields, String search, String sort) {
        Integer finalPageSize = (pageSize != null) ? pageSize : this.pageSize;
        Integer finalPage = (page != null) ? page : 0;
        return supplyAsync(() -> {
            String fieldsCleaned = (!Objects.equals(fields, "")) ? fields: null;
            String searchCleaned = (!Objects.equals(search, "")) ? search: null;
            String sortCleaned = (!Objects.equals(sort, "")) ? sort: null;
            try {
                PagedList<T> pagedList = filterFindAllByFieldsAndSorted(query, fieldsCleaned, searchCleaned, sortCleaned)
                        .setFirstRow(finalPage * finalPageSize)
                        .setMaxRows(finalPageSize).findPagedList();
                pagedList.loadCount();
                return new PagedListDTO<>(pagedList);
            } catch (FieldsNotExistException e) {
                throw new CompletionException(e);
            }
        }, executionContext);
    }

    private ExpressionList<T> filterFindAllByFieldsAndSorted(ExpressionList<T> query, String fields, String search,
                                                             String sort) throws FieldsNotExistException {
        this.filterFindAllByFields(query, fields, search);
        if(!Objects.equals(sort, "") && sort!= null) {
            String direction = (Objects.equals(String.valueOf(sort.charAt(0)), "-")) ? "desc" : "asc";
            sort = sort.replaceAll("[^\\dA-Za-z0-9,.]", "");
            checkIfFieldsExist(sort.split(","));
            if(isFieldType(sort, String.class)){
                sort = "UPPER(" + sort + ")";
            }
            query.orderBy(sort + " " + direction);
        }
        return query;
    }

    private ExpressionList<T> filterFindAllByFields(ExpressionList<T> query, String fields, String search)
            throws FieldsNotExistException {
        if(fields != null && search != null){
            String sanitizedSearchString = Normalizer.normalize(search, Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
            String[] listFields = fields.split(",");
            String[] listSearch = sanitizedSearchString.split(" ");

            Junction disjunction = query.disjunction();

            checkIfFieldsExist(listFields);
            for(String field : listFields){
                if(checkIfFieldExist(field)) {
                    for (String s : listSearch) {
                        if(isFieldType(field, String.class)){
                            if(this.dbEngine.equals(POSTGRES_ENGINE)){
                                disjunction.icontains("f_unaccent(" + field + ")", s);
                            }else{
                                disjunction.icontains(field, s);
                            }
                        }else if(isFieldType(field, UUID.class)){
                            disjunction.eq(field, UUID.fromString(s));
                        }else{
                            disjunction.eq(field, s);
                        }
                    }
                }
            }
            disjunction.endJunction();
        }
        return query;
    }

    protected Boolean isFieldType(String stringField, Class type){
        try {
            return this.reflectionUtils.isFieldType(this.modelClass, stringField, type);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            return false;
        }
    }

    private Boolean checkIfFieldExist(String stringField){
        try{
            this.reflectionUtils.getaClass(this.modelClass, stringField);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    private Boolean checkIfFieldsExist(String[] listFields) throws FieldsNotExistException {
        List<String> fieldsNotExist = new ArrayList<>();
        for(String field : listFields){
            if(!checkIfFieldExist(field)){
                fieldsNotExist.add(field);
            }
        }
        if(!fieldsNotExist.isEmpty()){
            throw new FieldsNotExistException(fieldsNotExist);
        }
        return true;
    }

}