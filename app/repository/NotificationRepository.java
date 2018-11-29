package repository;

import helpers.ConfigHelper;
import helpers.ContextHelper;
import models.Notification;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;

/**
 * A repository that executes database operations in a different
 * execution context.
 */
public class NotificationRepository extends BaseCRUDRepository<Notification> {

    @Inject
    public NotificationRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext, ConfigHelper configHelper, ContextHelper contextHelper) {
        super(Notification.class, ebeanConfig, executionContext, configHelper, contextHelper);
    }
}
