package controllers;

import actions.Authenticated;
import adapters.S3Adapter;
import dtos.FileDTO;
import dtos.FileStream;
import org.apache.commons.io.FileUtils;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Result;
import services.AssetService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AssetController extends BaseController {

    private final HttpExecutionContext httpExecutionContext;
    private AssetService assetService;

    @Inject
    public AssetController(AssetService assetService, HttpExecutionContext httpExecutionContext) {
        this.assetService = assetService;
        this.httpExecutionContext = httpExecutionContext;
    }

    @Authenticated
    public Result uploadAsset() {
        try {
            String urlImg = this.assetService.uploadFile(request().body().asMultipartFormData().getFile("file"));
            return ok(urlImg);
        }
        catch (Exception  e){
            return this.returnError(e);
        }
    }

    public Result getAsset(String file) {
        try {
            FileDTO result = this.assetService.getFile(file);
            return ok(FileUtils.readFileToByteArray(result.getFile())).as(result.getContentType());
        }
        catch (Exception e){
            return this.returnError(e);
        }
    }

}
