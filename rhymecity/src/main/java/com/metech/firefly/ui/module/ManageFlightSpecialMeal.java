package com.metech.firefly.ui.module;

import com.metech.firefly.AppModule;
import com.metech.firefly.ui.activity.ManageFlight.MF_SpecialServiceRequestFragment;
import com.metech.firefly.ui.presenter.ManageFlightPrenter;
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
