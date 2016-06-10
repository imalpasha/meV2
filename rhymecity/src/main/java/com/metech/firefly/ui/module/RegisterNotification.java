package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.PushNotification.PushNotificationFragment;
import com.metech.firefly.ui.presenter.HomePresenter;
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
