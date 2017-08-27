package com.draconra.bakingapp;

import android.app.Application;

import com.draconra.bakingapp.api.ApiInterface;
import com.draconra.bakingapp.util.Constant;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by draconra on 8/12/17.
 */

public class AppController extends Application {

    private Retrofit retrofit;
    private EventBus eventBus;
    private static Gson sGson;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        eventBus = new EventBus();
        sGson = new Gson();

        createRetrofitClient();
    }

    public Gson gson() {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson;
    }


    private void createRetrofitClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public EventBus getEventBus() {
        if (null == eventBus) {
            eventBus = new EventBus();
        }
        return eventBus;
    }

    public Retrofit getRetrofitClient() {
        return retrofit;
    }

    public ApiInterface getApiService() {
        return getRetrofitClient().create(ApiInterface.class);
    }
}
