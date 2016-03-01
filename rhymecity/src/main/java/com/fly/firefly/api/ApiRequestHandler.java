package com.fly.firefly.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.fly.firefly.Controller;
import com.fly.firefly.MainFragmentActivity;
import com.fly.firefly.api.obj.ChangePasswordReceive;
import com.fly.firefly.api.obj.ChangeSearchFlightReceive;
import com.fly.firefly.api.obj.CheckInListReceive;
import com.fly.firefly.api.obj.ConfirmUpdateReceive;
import com.fly.firefly.api.obj.ContactInfoReceive;
import com.fly.firefly.api.obj.DeviceInfoSuccess;
import com.fly.firefly.api.obj.FlightSummaryReceive;
import com.fly.firefly.api.obj.ListBookingReceive;
import com.fly.firefly.api.obj.ManageChangeContactReceive;
import com.fly.firefly.api.obj.ManageRequestIntinenary;
import com.fly.firefly.api.obj.MobileCheckInPassengerReceive;
import com.fly.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.fly.firefly.api.obj.PushNotificationReceive;
import com.fly.firefly.api.obj.RetrieveBoardingPassReceive;
import com.fly.firefly.api.obj.SplashFailedConnect;
import com.fly.firefly.api.obj.ForgotPasswordReceive;
import com.fly.firefly.api.obj.LoginReceive;
import com.fly.firefly.api.obj.MobileCheckinReceive;
import com.fly.firefly.api.obj.PassengerInfoReveice;
import com.fly.firefly.api.obj.PaymentInfoReceive;
import com.fly.firefly.api.obj.PaymentReceive;
import com.fly.firefly.api.obj.RegisterReceive;
import com.fly.firefly.api.obj.SearchFlightReceive;
import com.fly.firefly.api.obj.SeatSelectionReveice;
import com.fly.firefly.api.obj.SelectFlightReceive;
import com.fly.firefly.api.obj.ItineraryInfoReceive;
import com.fly.firefly.api.obj.TermsReceive;
import com.fly.firefly.api.obj.UpdateProfileReceive;
import com.fly.firefly.base.BaseFragment;
import com.fly.firefly.ui.object.ChangePasswordRequest;
import com.fly.firefly.ui.object.ConfirmUpdateRequest;
import com.fly.firefly.ui.object.ContactInfo;
import com.fly.firefly.ui.object.DeviceInformation;
import com.fly.firefly.ui.object.FlightSummary;
import com.fly.firefly.ui.object.GetChangeFlight;
import com.fly.firefly.ui.object.GetFlightAvailability;
import com.fly.firefly.ui.object.ItineraryObj;
import com.fly.firefly.ui.object.LoginRequest;
import com.fly.firefly.ui.object.ManageContactInfo;
import com.fly.firefly.ui.object.ManageFlightObj;
import com.fly.firefly.ui.object.ManageFlightObjV2;
import com.fly.firefly.ui.object.ManageFlightObjV3;
import com.fly.firefly.ui.object.ManagePassengerInfo;
import com.fly.firefly.ui.object.ManageSeatInfo;
import com.fly.firefly.ui.object.MobileCheckInPassenger;
import com.fly.firefly.ui.object.MobileCheckinObj;
import com.fly.firefly.ui.object.MobileConfirmCheckInPassenger;
import com.fly.firefly.ui.object.Passenger;
import com.fly.firefly.ui.object.PasswordRequest;
import com.fly.firefly.ui.object.Payment;
import com.fly.firefly.ui.object.PushNotificationObj;
import com.fly.firefly.ui.object.RegisterObj;
import com.fly.firefly.ui.object.RetrieveBoardingPassObj;
import com.fly.firefly.ui.object.SearchFlightObj;
import com.fly.firefly.ui.object.SeatAvailabilityRequest;
import com.fly.firefly.ui.object.SeatSelection;
import com.fly.firefly.ui.object.SelectChangeFlight;
import com.fly.firefly.ui.object.SelectFlight;
import com.fly.firefly.ui.object.SendItinenaryObj;
import com.fly.firefly.ui.object.Signature;
import com.fly.firefly.ui.object.TermsRequest;
import com.fly.firefly.ui.object.UpdateProfileRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ApiRequestHandler {

    private final Bus bus;
    private final ApiService apiService;
    Context context;
    ProgressDialog mProgressDialog;
    private int inc;
    private boolean retry;

    public ApiRequestHandler(Bus bus, ApiService apiService) {
        this.bus = bus;
        this.apiService = apiService;
        inc = 0;
        retry = true;
    }

    // ------------------------------------------------------------------------------ //

    @Subscribe
    public void onRegisterNotification(final PushNotificationObj event) {

       //
       // apiService.onRegisterNotification(event, new Callback<PushNotificationReceive>() {
       apiService.onRegisterNotification(event.getCmd(),event.getUser_id(),event.getToken(),event.getName(),event.getCode(),new Callback<PushNotificationReceive>() {

            @Override
            public void success(PushNotificationReceive deviceResponse, Response response) {

                bus.post(new PushNotificationReceive(deviceResponse));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }
    // ------------------------------------------------------------------------------ //

    // - 1
    /* Subscribe From HomePresenter - Send Device Information to server - ImalPasha */
    @Subscribe
    public void onDeviceInfo(final DeviceInformation event) {

        apiService.onSendDeviceInfo(event, new Callback<DeviceInfoSuccess>() {

            @Override
            public void success(DeviceInfoSuccess deviceResponse, Response response) {

                bus.post(new DeviceInfoSuccess(deviceResponse));

            }

            @Override
            public void failure(RetrofitError error) {

                bus.post(new SplashFailedConnect());

            }

        });
    }















    @Subscribe
    public void onLoginRequest(final LoginRequest event) {

        Log.e("Username", event.getUsername());
        Log.e("Password", event.getPassword());

       // initiateLoading();
       // loading(true);


        apiService.onRequestToLogin(event, new Callback<LoginReceive>() {

            @Override
            public void success(LoginReceive rhymesResponse, Response response) {

                bus.post(new LoginReceive(rhymesResponse));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }



    @Subscribe
    public void onPasswordRequest(final PasswordRequest event) {

        Log.e("Email", event.getEmail());


        apiService.onRequestPassword(event, new Callback<ForgotPasswordReceive>() {

            @Override
            public void success(ForgotPasswordReceive rhymesResponse, Response response) {

                bus.post(new ForgotPasswordReceive(rhymesResponse));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

    @Subscribe
    public void onChangePasswordRequest(final ChangePasswordRequest event) {

        apiService.onRequestChangePassword(event, new Callback<ChangePasswordReceive>() {

            @Override
            public void success(ChangePasswordReceive rhymesResponse, Response response) {

                bus.post(new ChangePasswordReceive(rhymesResponse));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

   /* @Subscribe
    public void onUpdateProfileRequest(final UpdateProfileRequest event) {

        Log.e("Email", event.getUsername());
        Log.e("password", event.getNewPassword());
        Log.e("new_password", event.getPassword());


        //initiateLoading();
        //loading(true);


        apiService.onRequestUpdateProfile(event, new Callback<UpdateProfileReceive>() {

            @Override
            public void success(UpdateProfileReceive rhymesResponse, Response response) {

                Log.e("Success", "OK");
                bus.post(new UpdateProfileReceive(rhymesResponse));
               // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                bus.post(new SplashFailedConnect("Unable to connect to server"));
              //  loading(false);
            }

        });
    }*/




    @Subscribe
    public void onUpdateProfileRequest(final UpdateProfileRequest data) {

        apiService.onUpdateProfileRequest(data, new Callback<UpdateProfileReceive>() {

            @Override
            public void success(UpdateProfileReceive rhymesResponse, Response response) {

                bus.post(new UpdateProfileReceive(rhymesResponse));
                // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }




    @Subscribe
    public void onRegisterRequest(final RegisterObj event) {

        apiService.onRegisterRequest(event, new Callback<RegisterReceive>() {

            @Override
            public void success(RegisterReceive rhymesResponse, Response response) {

                Log.e("Success", "True");
                bus.post(new RegisterReceive(rhymesResponse));
                // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }


    @Subscribe
    public void onSearchFlight(final SearchFlightObj event) {

        apiService.onSearchFlightRequest(event, new Callback<SearchFlightReceive>() {

            @Override
            public void success(SearchFlightReceive rhymesResponse, Response response) {

                bus.post(new SearchFlightReceive(rhymesResponse));
                ///BaseFragment.tempResult((new Gson()).toJson(rhymesResponse));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });




    }

    @Subscribe
    public void onMobileCheckin(final MobileCheckinObj event) {

        //initiateLoading();
        //loading(true);

        apiService.onMobileCheckinRequest(event, new Callback<MobileCheckinReceive>() {

            @Override
            public void success(MobileCheckinReceive rhymesResponse, Response response) {

                bus.post(new MobileCheckinReceive(rhymesResponse));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

        @Subscribe
        public void onPassengerCheckIn(final MobileCheckInPassenger event) {

            //initiateLoading();
            //loading(true);

            apiService.onPassengerCheckIn(event, new Callback<MobileCheckInPassengerReceive>() {

                @Override
                public void success(MobileCheckInPassengerReceive rhymesResponse, Response response) {

                    bus.post(new MobileCheckInPassengerReceive(rhymesResponse));

                }

                @Override
                public void failure(RetrofitError error) {

                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


                }

            });
        }


            @Subscribe
            public void onConfirmPassengerCheckIn(final MobileConfirmCheckInPassenger event) {

                //initiateLoading();
                //loading(true);

                apiService.onConfirmPassengerCheckIn(event, new Callback<MobileConfirmCheckInPassengerReceive>() {

                    @Override
                    public void success(MobileConfirmCheckInPassengerReceive rhymesResponse, Response response) {

                        bus.post(new MobileConfirmCheckInPassengerReceive(rhymesResponse));

                    }

                    @Override
                    public void failure(RetrofitError error) {

                        BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


                    }

                });

    }

    @Subscribe
    public void onSearchFlight(final SelectFlight event) {

        apiService.onSelectFlight(event, new Callback<SelectFlightReceive>() {

            @Override
            public void success(SelectFlightReceive xx, Response response) {
                bus.post(new SelectFlightReceive(xx));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }
        });
    }


    @Subscribe
    public void onPassengerInfo(final Passenger event) {

        apiService.onPassengerInfo(event, new Callback<PassengerInfoReveice>() {

            @Override
            public void success(PassengerInfoReveice responseData, Response response) {
                bus.post(new PassengerInfoReveice(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

    @Subscribe
    public void onPassengerInfo(final ContactInfo event) {

        apiService.onContactInfo(event, new Callback<ContactInfoReceive>() {

            @Override
            public void success(ContactInfoReceive responseData, Response response) {

                bus.post(new ContactInfoReceive(responseData));


            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }


    @Subscribe
    public void onTermsRequest(final TermsRequest data) {

        // initiateLoading();
        // loading(true);


        apiService.onTermsRequest(new Callback<TermsReceive>() {

            @Override
            public void success(TermsReceive rhymesResponse, Response response) {

                Log.e("success", "True");
                bus.post(new TermsReceive(rhymesResponse));
                // loading(false);
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }


    @Subscribe
    public void onSeatSelection(final SeatSelection event) {

        apiService.onSeatSelection(event, new Callback<SeatSelectionReveice>() {

            @Override
            public void success(SeatSelectionReveice responseData, Response response) {

                bus.post(new SeatSelectionReveice(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

    @Subscribe
    public void onPaymentInfo(final Signature event) {

        apiService.onPaymentInfo(event, new Callback<PaymentInfoReceive>() {

            @Override
            public void success(PaymentInfoReceive responseData, Response response) {

                bus.post(new PaymentInfoReceive(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }


    @Subscribe
    public void onPaymentRequest(final Payment event) {

        apiService.onPaymentProcess(event, new Callback<PaymentReceive>() {

            @Override
            public void success(PaymentReceive responseData, Response response) {

                bus.post(new PaymentReceive(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

    @Subscribe
    public void onItineraryRequest(final ItineraryObj event) {

        apiService.onItineraryRequest(new Callback<ItineraryInfoReceive>() {

            @Override
            public void success(ItineraryInfoReceive responseData, Response response) {

                bus.post(new ItineraryInfoReceive(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

    @Subscribe
    public void onFlightSummary(final FlightSummary event) {

        apiService.onFlightSummary(event, new Callback<FlightSummaryReceive>() {

            @Override
            public void success(FlightSummaryReceive responseData, Response response) {
                bus.post(new FlightSummaryReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }


    @Subscribe
    public void onManageFlight(final ManageFlightObj event) {

        apiService.onManageFlight(event, new Callback<FlightSummaryReceive>() {

            @Override
            public void success(FlightSummaryReceive responseData, Response response) {
                bus.post(new FlightSummaryReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onManageFlight(final ManageFlightObjV2 event) {

        apiService.onManageFlightV2(event, new Callback<ListBookingReceive>() {

            @Override
            public void success(ListBookingReceive responseData, Response response) {
                bus.post(new ListBookingReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                if (retry) {
                    onManageFlight(event);
                    loop(inc);
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

        });
    }

    @Subscribe
    public void onManageFlight(final ManageFlightObjV3 event) {

        apiService.onManageFlightV3(event, new Callback<CheckInListReceive>() {

            @Override
            public void success(CheckInListReceive responseData, Response response) {
                bus.post(new CheckInListReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }



    @Subscribe
    public void onManageFlight(final ManageContactInfo event) {

        apiService.onChangeContactInfo(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive responseData, Response response) {
                bus.post(new ManageChangeContactReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }


    @Subscribe
    public void onConfirmUpdate(final ConfirmUpdateRequest event) {

        apiService.onChangeRequestConfirm(event, new Callback<ConfirmUpdateReceive>() {

            @Override
            public void success(ConfirmUpdateReceive responseData, Response response) {
                bus.post(new ConfirmUpdateReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }





    /*@Subscribe
    public void onRetrieveManageFlight(final ManageFlightRetrieveObj event) {

        apiService.onRetrieveFlightSummary(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive responseData, Response response) {
                bus.post(new ManageChangeContactReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertDialog(MainFragmentActivity.getContext(), "Unable to connect to server");
                BaseFragment.dismissLoading();
            }

        });
    }*/

    @Subscribe
    public void onChangePassenger(final ManagePassengerInfo event) {

        apiService.onChangePassenger(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive responseData, Response response) {
                bus.post(new ManageChangeContactReceive(responseData));

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }


    @Subscribe
    public void onSeatRequest(final SeatAvailabilityRequest event) {

        apiService.onSeatRequest(event, new Callback<ContactInfoReceive>() {

            @Override
            public void success(ContactInfoReceive responseData, Response response) {
                bus.post(new ContactInfoReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }




    @Subscribe
    public void onChangeSeat(final ManageSeatInfo event) {

        apiService.onChangeSeat(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive responseData, Response response) {
                bus.post(new ManageChangeContactReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }




    @Subscribe
    public void onRequestItinenary(final SendItinenaryObj event) {

        apiService.onRequestItinenary(event, new Callback<ManageRequestIntinenary>() {

            @Override
            public void success(ManageRequestIntinenary responseData, Response response) {
                bus.post(new ManageRequestIntinenary(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }



    @Subscribe
    public void onGetFlightRequest(final GetFlightAvailability event) {

        apiService.onGetFlightRequest(event, new Callback<ChangeSearchFlightReceive>() {

            @Override
            public void success(ChangeSearchFlightReceive responseData, Response response) {
                bus.post(new ChangeSearchFlightReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }


    @Subscribe
    public void onGetChangeFlight(final GetChangeFlight event) {

        apiService.onGetChangeFlight(event, new Callback<SearchFlightReceive>() {

            @Override
            public void success(SearchFlightReceive responseData, Response response) {
                bus.post(new SearchFlightReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }



    @Subscribe
    public void onGetChangeFlight(final SelectChangeFlight event) {

        apiService.onSelectChangeFlight(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive responseData, Response response) {
                bus.post(new ManageChangeContactReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());


            }

        });
    }

    @Subscribe
    public void onRetrieveBoardingPass(final RetrieveBoardingPassObj event) {

        apiService.onRetrieveBoardingPass(event, new Callback<RetrieveBoardingPassReceive>() {

            @Override
            public void success(RetrieveBoardingPassReceive responseData, Response response) {
                bus.post(new RetrieveBoardingPassReceive(responseData));
            }

            @Override
            public void failure(RetrofitError error) {

                if (retry) {
                    onRetrieveBoardingPass(event);
                    loop(inc);
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

        });
    }



    public void loop(int inc){
        inc++;
        if(inc > 2){
            retry = false;
        }else{
            retry = true;
        }
    }






}

//apiService.onRequestToLogin(event.getUsername(),event.getPassword(), new Callback<LoginRequest>() {
//apiService.onRequestToLogin(123,"zhariffadam@me-tech.com.my","P@$$w0rd", new Callback<LoginRequest>() {
//apiService.getFeed2("adam", new Callback<LoginRequest>() {