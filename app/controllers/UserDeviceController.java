package controllers;

import helpers.ContextHelper;
import models.UserDevice;
import play.libs.concurrent.HttpExecutionContext;
import services.UserDeviceService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Manage a database of computers
 */
@Singleton
public class UserDeviceController extends BaseCRUDController<UserDevice, UserDeviceService> {

    @Inject
    public UserDeviceController(UserDeviceService service, HttpExecutionContext httpExecutionContext, ContextHelper contextHelper) {
        super(UserDevice.class, service, httpExecutionContext, contextHelper);
    }
}