package me.mattlogan.rhymecity;

import android.util.Log;

import com.fly.firefly.AppModule;
import com.fly.firefly.api.ApiService;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.mattlogan.rhymecity.log.DebugLogger;
import retrofit.Endpoint;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@Module(
        overrides = true,
        addsTo = AppModule.class
)
public class DebugAppModule {

    @Provides
    @Singleton
    RestAdapter.Log provideDebugLogger() {
        return new DebugLogger();
    }

    @Provides
    @Singleton
    ApiService provideApiService(RequestInterceptor requestInterceptor, RestAdapter.Log logger,
                                 Endpoint endpoint) {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(90, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(90, TimeUnit.SECONDS);

        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        Log.i("retrofit", msg);
                    }
                })
                .setLog(logger)
                .build()
                .create(ApiService.class);
    }
}
