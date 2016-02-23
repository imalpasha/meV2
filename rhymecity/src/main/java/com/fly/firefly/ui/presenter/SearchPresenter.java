package com.fly.firefly.ui.presenter;

import android.util.Log;

import com.fly.firefly.api.obj.tryObj;
import com.fly.firefly.rhymes.RhymesFailureEvent;
import com.fly.firefly.rhymes.RhymesRequestedEvent;
import com.fly.firefly.rhymes.UserRetrieveSuccess;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class SearchPresenter {

    public interface SearchView {
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showRetrieveRhymesError();
        void showNoRhymesFoundError(String word);
       // void goToRhymesViewWithRhymes(String word, List<String> rhymeList);
        void displayUserInfo(tryObj obj);


    }

    private final SearchView view;
    private final Bus bus;

    public SearchPresenter(SearchView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onRhymesForWordRequested(String word) {
        view.showLoadingIndicator();
        bus.post(new RhymesRequestedEvent(word));
    }

    /*@Subscribe
    public void onRhymesSuccess(RhymesSuccessEvent event) {
        view.hideLoadingIndicator();

        List<String> rhymeList = event.getRhymes();
        if (rhymeList.isEmpty()) {
            view.showNoRhymesFoundError(event.getWord());
            Log.e("FAILED","Y");
        } else {
            view.goToRhymesViewWithRhymes(event.getWord(), rhymeList);
            Log.e("FAILED","N");

        }
    }*/

    @Subscribe
    public void onUserSuccess(UserRetrieveSuccess event) {
        view.hideLoadingIndicator();

        tryObj obj = event.getUserObj();
        view.displayUserInfo(obj);
        Log.e("UserName",obj.getLogin());
    }



    @Subscribe
    public void onRhymesFailure(RhymesFailureEvent event) {
        view.hideLoadingIndicator();
        view.showRetrieveRhymesError();
    }
}
