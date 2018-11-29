package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import play.db.ebean.EbeanConfig;


public abstract class BaseRepository {

    protected final EbeanServer ebeanServer;
    protected final DatabaseExecutionContext executionContext;

    public BaseRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

}
