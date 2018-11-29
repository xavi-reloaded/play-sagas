package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import exceptions.NotPossibleValidateTokenException;
import helpers.validator.ValidatorHelper;
import helpers.validator.ValidatorParams;
import helpers.validator.dtos.ValidatorObjectDTO;
import helpers.validator.enumerations.ValidatorsEnum;
import helpers.validator.exceptions.ValidationErrorException;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthorizeService;
import services.IAuthorizeService;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class AuthorizeController extends BaseController {

    private final IAuthorizeService authorizeService;
    private final HttpExecutionContext httpExecutionContext;
    private ValidatorHelper validatorHelper;

    @Inject
    public AuthorizeController(AuthorizeService authorizeService, HttpExecutionContext httpExecutionContext, ValidatorHelper validatorHelper) {
        this.authorizeService = authorizeService;
        this.httpExecutionContext = httpExecutionContext;
        this.validatorHelper = validatorHelper;
    }

    public AuthorizeController(IAuthorizeService authorizeService, HttpExecutionContext httpExecutionContext, Http.Context httpContext, ValidatorHelper validatorHelper) {
        this.authorizeService = authorizeService;
        this.httpExecutionContext = httpExecutionContext;
        this.httpContext = httpContext;
        this.validatorHelper = validatorHelper;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> login() {
        try {
            JsonNode body = this.getHttpContext().request().body().asJson();

            LinkedHashMap<String, List<ValidatorObjectDTO>> validators = new LinkedHashMap<>();
            validators.put("username", Collections.singletonList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED)));
            validators.put("password", Collections.singletonList(new ValidatorObjectDTO(ValidatorsEnum.REQUIRED)));
            this.validatorHelper.validate(new ValidatorParams(body, validators));

            String username = Json.fromJson(body.get("username"), String.class);
            String password = Json.fromJson(body.get("password"), String.class);
            return this.authorizeService.login(username, password)
                    .thenApplyAsync(sessionDTO -> this.returnOk(Json.toJson(sessionDTO)), httpExecutionContext.current())
                    .exceptionally(this::returnError);
        } catch (ValidationErrorException e) {
            return supplyAsync(() -> this.returnError(e), httpExecutionContext.current());
        }
    }

    public CompletionStage<Result> getNewSessionToken(String refreshToken) {
        Optional<String> currentToken = this.getHttpContext().request().getHeaders().get("X-AUTH-TOKEN");
        return currentToken.map(s -> supplyAsync(() -> {
            try {
                String newToken = this.authorizeService.generateNewTokenFromRefreshToken(refreshToken);
                return this.returnOk(Json.toJson(newToken));
            } catch (InterruptedException | ExecutionException | IOException | NotPossibleValidateTokenException e) {
                return unauthorized("No token found");
            }
        }, httpExecutionContext.current())).orElseGet(() -> supplyAsync(() -> unauthorized("No token found"), httpExecutionContext.current()));
    }
}
