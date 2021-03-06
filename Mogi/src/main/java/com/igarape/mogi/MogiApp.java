package com.igarape.mogi;

import android.app.Application;

import com.igarape.mogi.server.ApiClient;
import com.igarape.mogi.utils.FileUtils;
import com.igarape.mogi.utils.WidgetUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.ObjectGraph;
import dagger.Provides;

/**
 * Created by felipeamorim on 08/07/2013.
 */
public class MogiApp extends Application {
    private ObjectGraph objectGraph;
    private Bus mBus;

    public static final String SENDER_ID = "319635303076";

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new MogiModule(this));
        mBus = new Bus(ThreadEnforcer.ANY);
        FileUtils.setPath("/mnt/extSdCard/smartpolicing/");
        WidgetUtils.UpdateWidget(this.getApplicationContext());
        ApiClient.setAppContext(this.getApplicationContext());
    }

    public ObjectGraph objectGraph() {
        return objectGraph;
    }

    @Provides @Singleton
    public Bus providesBus() {
        return mBus;
    }
}

