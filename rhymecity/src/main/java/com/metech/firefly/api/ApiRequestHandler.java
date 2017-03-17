package com.metech.firefly.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;
import com.metech.firefly.MainFragmentActivity;
import com.metech.firefly.api.obj.AboutUsReceive;
import com.metech.firefly.api.obj.ChangePasswordReceive;
import com.metech.firefly.api.obj.ChangeSearchFlightReceive;
import com.metech.firefly.api.obj.CheckInListReceive;
import com.metech.firefly.api.obj.ConfirmUpdateReceive;
import com.metech.firefly.api.obj.ContactInfoReceive;
import com.metech.firefly.api.obj.DeleteCCReceive;
import com.metech.firefly.api.obj.DeleteFFReceive;
import com.metech.firefly.api.obj.DeviceInfoSuccess;
import com.metech.firefly.api.obj.FlightSummaryReceive;
import com.metech.firefly.api.obj.ListBookingReceive;
import com.metech.firefly.api.obj.ManageChangeContactReceive;
import com.metech.firefly.api.obj.ManageRequestIntinenary;
import com.metech.firefly.api.obj.MobileCheckInPassengerReceive;
import com.metech.firefly.api.obj.MobileConfirmCheckInPassengerReceive;
import com.metech.firefly.api.obj.PushNotificationReceive;
import com.metech.firefly.api.obj.RetrieveBoardingPassReceive;
import com.metech.firefly.api.obj.SSRReceive;
import com.metech.firefly.api.obj.SplashFailedConnect;
import com.metech.firefly.api.obj.ForgotPasswordReceive;
import com.metech.firefly.api.obj.LoginReceive;
import com.metech.firefly.api.obj.MobileCheckinReceive;
import com.metech.firefly.api.obj.PassengerInfoReveice;
import com.metech.firefly.api.obj.PaymentInfoReceive;
import com.metech.firefly.api.obj.PaymentReceive;
import com.metech.firefly.api.obj.RegisterReceive;
import com.metech.firefly.api.obj.SearchFlightReceive;
import com.metech.firefly.api.obj.SeatSelectionReveice;
import com.metech.firefly.api.obj.SelectFlightReceive;
import com.metech.firefly.api.obj.ItineraryInfoReceive;
import com.metech.firefly.api.obj.TermsReceive;
import com.metech.firefly.api.obj.UpdateProfileReceive;
import com.metech.firefly.base.BaseFragment;
import com.metech.firefly.ui.object.AboutUs;
import com.metech.firefly.ui.object.ChangePasswordRequest;
import com.metech.firefly.ui.object.ChangeSSR;
import com.metech.firefly.ui.object.ConfirmUpdateRequest;
import com.metech.firefly.ui.object.ContactInfo;
import com.metech.firefly.ui.object.DefaultPassengerObj;
import com.metech.firefly.ui.object.DeleteCCRequest;
import com.metech.firefly.ui.object.DeviceInformation;
import com.metech.firefly.ui.object.FlightSummary;
import com.metech.firefly.ui.object.FriendFamilyDelete;
import com.metech.firefly.ui.object.GetChangeFlight;
import com.metech.firefly.ui.object.GetFlightAvailability;
import com.metech.firefly.ui.object.GetSSR;
import com.metech.firefly.ui.object.ItineraryObj;
import com.metech.firefly.ui.object.LoginRequest;
import com.metech.firefly.ui.object.ManageContactInfo;
import com.metech.firefly.ui.object.ManageFlightObj;
import com.metech.firefly.ui.object.ManageFlightObjV2;
import com.metech.firefly.ui.object.ManageFlightObjV3;
import com.metech.firefly.ui.object.ManagePassengerInfo;
import com.metech.firefly.ui.object.ManageSeatInfo;
import com.metech.firefly.ui.object.MobileCheckInPassenger;
import com.metech.firefly.ui.object.MobileCheckinObj;
import com.metech.firefly.ui.object.MobileConfirmCheckInPassenger;
import com.metech.firefly.ui.object.Passenger;
import com.metech.firefly.ui.object.PassengerInfo;
import com.metech.firefly.ui.object.PasswordRequest;
import com.metech.firefly.ui.object.Payment;
import com.metech.firefly.ui.object.PushNotificationObj;
import com.metech.firefly.ui.object.RegisterObj;
import com.metech.firefly.ui.object.RetrieveBoardingPassObj;
import com.metech.firefly.ui.object.SearchFlightObj;
import com.metech.firefly.ui.object.SeatAvailabilityRequest;
import com.metech.firefly.ui.object.SeatSelection;
import com.metech.firefly.ui.object.SelectChangeFlight;
import com.metech.firefly.ui.object.SelectFlight;
import com.metech.firefly.ui.object.SendItinenaryObj;
import com.metech.firefly.ui.object.Signature;
import com.metech.firefly.ui.object.TermsRequest;
import com.metech.firefly.ui.object.UpdateProfileRequest;
import com.metech.firefly.utils.RealmObjectController;
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
        retry = false;
    }

    // ------------------------------------------------------------------------------ //

    @Subscribe
    public void onRegisterNotification(final PushNotificationObj event) {

       //
       // apiService.onRegisterNotification(event, new Callback<PushNotificationReceive>() {
       apiService.onRegisterNotification(event.getCmd(), event.getUser_id(), event.getToken(), event.getName(), event.getCode(), new Callback<PushNotificationReceive>() {

           @Override
           public void success(PushNotificationReceive retroResponse, Response response) {

               if(retroResponse != null){
                   bus.post(new PushNotificationReceive(retroResponse));
                   RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
               }else{
                   BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
               }

           }

           @Override
           public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
           }

       });
    }
    // ------------------------------------------------------------------------------ //

    /* Subscribe From HomePresenter - Send Device Information to server - ImalPasha */
    @Subscribe
    public void onDeviceInfo(final DeviceInformation event) {

        apiService.onSendDeviceInfo(event, new Callback<DeviceInfoSuccess>() {

            @Override
            public void success(DeviceInfoSuccess retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new DeviceInfoSuccess(retroResponse));
                    //ealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                    RealmObjectController.cachedResultWithType(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse),"SplashInfo");

                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }

    @Subscribe
    public void onLoginRequest(final LoginRequest event) {

        apiService.onRequestToLogin(event, new Callback<LoginReceive>() {

            @Override
            public void success(LoginReceive rhymesResponse, Response response) {

                if(rhymesResponse != null) {
                    bus.post(new LoginReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {

                 BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }

    @Subscribe
    public void onPasswordRequest(final PasswordRequest event) {

        apiService.onRequestPassword(event, new Callback<ForgotPasswordReceive>() {

            @Override
            public void success(ForgotPasswordReceive rhymesResponse, Response response) {

                if(rhymesResponse != null) {
                    bus.post(new ForgotPasswordReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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

                if(rhymesResponse != null) {
                    bus.post(new ChangePasswordReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));

                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }

    @Subscribe
    public void onUpdateProfileRequest(final UpdateProfileRequest data) {

        apiService.onUpdateProfileRequest(data, new Callback<UpdateProfileReceive>() {

            @Override
            public void success(UpdateProfileReceive rhymesResponse, Response response) {

                if(rhymesResponse != null) {
                    bus.post(new UpdateProfileReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));

                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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

                if(rhymesResponse != null) {
                    bus.post(new RegisterReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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

                if(rhymesResponse != null) {
                    bus.post(new SearchFlightReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                 BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });




    }

    @Subscribe
    public void onMobileCheckin(final MobileCheckinObj event) {

        apiService.onMobileCheckinRequest(event, new Callback<MobileCheckinReceive>() {

            @Override
            public void success(MobileCheckinReceive rhymesResponse, Response response) {

                if(rhymesResponse != null) {
                    bus.post(new MobileCheckinReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }

    @Subscribe
    public void onPassengerCheckIn(final MobileCheckInPassenger event) {

            apiService.onPassengerCheckIn(event, new Callback<MobileCheckInPassengerReceive>() {

                @Override
                public void success(MobileCheckInPassengerReceive rhymesResponse, Response response) {

                    if(rhymesResponse != null) {
                        bus.post(new MobileCheckInPassengerReceive(rhymesResponse));
                        RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                    }else{
                        BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                    }

                }

                @Override
                public void failure(RetrofitError error) {

                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

                }

            });
        }

    @Subscribe
    public void onConfirmPassengerCheckIn(final MobileConfirmCheckInPassenger event) {

               apiService.onConfirmPassengerCheckIn(event, new Callback<MobileConfirmCheckInPassengerReceive>() {

                   @Override
                   public void success(MobileConfirmCheckInPassengerReceive rhymesResponse, Response response) {

                       if(rhymesResponse != null) {
                           bus.post(new MobileConfirmCheckInPassengerReceive(rhymesResponse));
                           RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                       }else{
                           BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                       }

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
            public void success(SelectFlightReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new SelectFlightReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(PassengerInfoReveice retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new PassengerInfoReveice(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                  BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onContactInfo(final ContactInfo event) {

        apiService.onContactInfo(event, new Callback<ContactInfoReceive>() {

            @Override
            public void success(ContactInfoReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ContactInfoReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                 BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onTermsRequest(final TermsRequest data) {

        apiService.onTermsRequest(new Callback<TermsReceive>() {

            @Override
            public void success(TermsReceive rhymesResponse, Response response) {

                if(rhymesResponse != null) {
                    bus.post(new TermsReceive(rhymesResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(rhymesResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(SeatSelectionReveice retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new SeatSelectionReveice(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(PaymentInfoReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new PaymentInfoReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(PaymentReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new PaymentReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(ItineraryInfoReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ItineraryInfoReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(FlightSummaryReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new FlightSummaryReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(FlightSummaryReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new FlightSummaryReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(ListBookingReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    if(retroResponse.getStatus().equals("retry")){
                        onManageFlight(event);
                    }else{

                        if(event.getModule().equals("manage_booking")){
                            //save manage flight list to realm.
                            RealmObjectController.saveManageFlightList(MainFragmentActivity.getContext(),retroResponse);
                        }else if(event.getModule().equals("check_in")){
                            //save mobile-checkin list to realm.
                            RealmObjectController.saveMobileCheckInList(MainFragmentActivity.getContext(),retroResponse);
                        }else if(event.getModule().equals("boarding_pass")){
                            //save mobile-checkin list to realm.
                            RealmObjectController.saveBoardingPassPNRList(MainFragmentActivity.getContext(),retroResponse);
                        }

                        bus.post(new ListBookingReceive(retroResponse));
                        RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                    }

                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {

                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onManageFlight(final ManageFlightObjV3 event) {

        apiService.onManageFlightV3(event, new Callback<CheckInListReceive>() {

            @Override
            public void success(CheckInListReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new CheckInListReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }



    @Subscribe
    public void onManageContactInfo(final ManageContactInfo event) {

        apiService.onChangeContactInfo(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(ConfirmUpdateReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ConfirmUpdateReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
            }

        });
    }

    @Subscribe
    public void onChangePassenger(final ManagePassengerInfo event) {

        apiService.onChangePassenger(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(ContactInfoReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ContactInfoReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(ManageRequestIntinenary retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ManageRequestIntinenary(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(ChangeSearchFlightReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ChangeSearchFlightReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(SearchFlightReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new SearchFlightReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                  BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }


    @Subscribe
    public void onSelectChangeFlight(final SelectChangeFlight event) {

        apiService.onSelectChangeFlight(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

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
            public void success(RetrieveBoardingPassReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new RetrieveBoardingPassReceive(retroResponse));
                    if(!retroResponse.getStatus().equals("error")){
                        RealmObjectController.cachedBoardingPass(MainFragmentActivity.getContext(),retroResponse);
                    }
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                   BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onRetrieveAboutUs(final AboutUs event) {

        apiService.onRetrieveAboutUs(event, new Callback<AboutUsReceive>() {

            @Override
            public void success(AboutUsReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new AboutUsReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                 BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onRetrieveSSR(final GetSSR event) {

        apiService.onRetrieveSSR(event, new Callback<SSRReceive>() {

            @Override
            public void success(SSRReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new SSRReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                   BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onChangeSSR(final ChangeSSR event) {

        apiService.onChangeSSR(event, new Callback<ManageChangeContactReceive>() {

            @Override
            public void success(ManageChangeContactReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new ManageChangeContactReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                 BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }


    @Subscribe
    public void onEditFF(final PassengerInfo event) {

        apiService.onRequestEditFF(event, new Callback<SelectFlightReceive>() {

            @Override
            public void success(SelectFlightReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new SelectFlightReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                Log.e("error",error.getMessage());
            }

        });
    }

    @Subscribe
    public void onDeleteFF(final FriendFamilyDelete event) {

        apiService.onRequestDeleteFF(event, new Callback<DeleteFFReceive>() {

            @Override
            public void success(DeleteFFReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new DeleteFFReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }


    @Subscribe
    public void onDeleteCC(final DeleteCCRequest event) {

        apiService.onRequestDeleteCC(event, new Callback<DeleteCCReceive>() {

            @Override
            public void success(DeleteCCReceive retroResponse, Response response) {

                if(retroResponse != null) {
                    bus.post(new DeleteCCReceive(retroResponse));
                    RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                }else{
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }

            }

            @Override
            public void failure(RetrofitError error) {

                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }



}
