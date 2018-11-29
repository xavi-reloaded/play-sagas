package repository;

import models.RefreshToken;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class RefreshTokenRepository extends BaseRepository {

    @Inject
    public RefreshTokenRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext);
    }

    public CompletionStage<Optional<RefreshToken>> lookup(Long id) {
        return supplyAsync(() -> ebeanServer.find(RefreshToken.class).setId(id).findOneOrEmpty(), executionContext);
    }

    public CompletionStage<RefreshToken> update(RefreshToken refreshToken){
        return supplyAsync(() -> {
            refreshToken.update();
            return refreshToken;
        }, executionContext);
    }

    public CompletionStage<RefreshToken> insert(RefreshToken refreshToken) {
        return supplyAsync(() -> {
             ebeanServer.insert(refreshToken);
             return refreshToken;
        }, executionContext);
    }
}
