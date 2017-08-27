package com.draconra.bakingapp.controller;

import com.draconra.bakingapp.AppController;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseController {

    protected AppController appController = AppController.getInstance();
    protected EventBus eventBus = appController.getEventBus();

}
