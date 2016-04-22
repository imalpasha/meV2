package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.ManageFlight.MF_SpecialServiceRequestFragment;
import com.fly.firefly.ui.activity.MobileCheckIn.MobileCheckInFragment1;
import com.fly.firefly.ui.presenter.ManageFlightPrenter;
import com.fly.firefly.ui.presenter.MobileCheckInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_SpecialServiceRequestFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageFlightSpecialMeal {

    private final ManageFlightPrenter.ChangeSpecialMealView view;

    public ManageFlightSpecialMeal(ManageFlightPrenter.ChangeSpecialMealView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideMobileCheckInPresenter1(Bus bus) {
        return new ManageFlightPrenter(view, bus);
    }
}
