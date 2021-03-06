package com.metech.firefly;

import android.util.Log;

import com.metech.firefly.api.ApiEndpoint;
import com.metech.firefly.api.ApiRequestInterceptor;
import com.metech.firefly.api.ApiService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Module(
        injects = FireFlyApplication.class
)
public class AppModule {

    private final String apiKey;

    public AppModule(String apiKey) {
        this.apiKey = apiKey;
    }

    @Provides
    @Singleton
    RequestInterceptor provideRequestInterceptor() {
        return new ApiRequestInterceptor(apiKey);
    }

    @Provides
    @Singleton
    Endpoint provideEndpoint() {
        return new ApiEndpoint();
    }

    @Provides
    @Singleton
    ApiService provideApiService(RequestInterceptor requestInterceptor, Endpoint endpoint) {

        int MAX_IDLE_CONNECTIONS = 30 * 60 * 1000;
        int KEEP_ALIVE_DURATION_MS = 3 * 60 * 1000;

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);

        //okHttpClient.setWriteTimeout(60, TimeUnit.SECONDS);
        //okHttpClient.setConnectionPool(new com.squareup.okhttp.ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION_MS));
        //.setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
        //    public void log(String msg) {
        //        Log.i("retrofit", msg);
        //    }
        //})

        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(okHttpClient))
                .build()
                .create(ApiService.class);
    }


   /* builder.setLogLevel(LogLevel.FULL).setLog(new RestAdapter.Log() {
        public void log(String msg) {
            Log.i("retrofit", msg);
        }
    });*/

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }
}
