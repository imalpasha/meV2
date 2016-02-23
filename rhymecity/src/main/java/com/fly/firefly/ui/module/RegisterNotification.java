package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.PushNotification.PushNotificationFragment;
import com.fly.firefly.ui.activity.Register.RegisterFragment;
import com.fly.firefly.ui.presenter.HomePresenter;
import com.squareup.otto.Bus;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(
        injects = PushNotificationFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class RegisterNotification {

    private final HomePresenter.PushNotification registerView;

    public RegisterNotification(HomePresenter.PushNotification registerView) {
        this.registerView = registerView;
    }

    @Provides
    @Singleton
    HomePresenter provideLoginPresenter(Bus bus) {
        return new HomePresenter(registerView, bus);
    }
}
