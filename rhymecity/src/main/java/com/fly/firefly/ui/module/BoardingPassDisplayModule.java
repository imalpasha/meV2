package com.fly.firefly.ui.module;

import com.fly.firefly.AppModule;
import com.fly.firefly.ui.activity.BoardingPass.BoardingPassDisplayFragment;
import com.fly.firefly.ui.activity.BoardingPass.BoardingPassFragment;
import com.fly.firefly.ui.activity.PasswordExpired.ChangePasswordFragment;
import com.fly.firefly.ui.presenter.BoardingPassPresenter;
import com.fly.firefly.ui.presenter.ChangePasswordPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = BoardingPassDisplayFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class BoardingPassDisplayModule {

    private final BoardingPassPresenter.RetrieveBoardingPassView boardingPassView;

    public BoardingPassDisplayModule(BoardingPassPresenter.RetrieveBoardingPassView view) {
        this.boardingPassView = view;
    }

    @Provides
    @Singleton
    BoardingPassPresenter provideChangePasswordPresenter(Bus bus) {
        return new BoardingPassPresenter(boardingPassView, bus);
    }
}
