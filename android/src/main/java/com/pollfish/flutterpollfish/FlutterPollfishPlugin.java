package com.pollfish.flutterpollfish;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.app.Activity;
import android.util.Log;

import com.pollfish.classes.SurveyInfo;
import com.pollfish.constants.Position;
import com.pollfish.constants.UserProperties;
import com.pollfish.interfaces.PollfishClosedListener;
import com.pollfish.interfaces.PollfishCompletedSurveyListener;
import com.pollfish.interfaces.PollfishOpenedListener;
import com.pollfish.interfaces.PollfishReceivedSurveyListener;
import com.pollfish.interfaces.PollfishSurveyNotAvailableListener;
import com.pollfish.interfaces.PollfishUserNotEligibleListener;
import com.pollfish.interfaces.PollfishUserRejectedSurveyListener;
import com.pollfish.main.PollFish;

import java.util.HashMap;
import java.util.Map;


/**
 * FlutterPollfishPlugin
 */
public class FlutterPollfishPlugin implements MethodCallHandler {

    public static final String TAG = "FlutterPollfishPlugin";

    private final Registrar registrar;
    private final MethodChannel channel;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_pollfish");
        channel.setMethodCallHandler(new FlutterPollfishPlugin(registrar, channel));
    }

    private FlutterPollfishPlugin(Registrar registrar, MethodChannel channel) {
        this.registrar = registrar;
        this.channel = channel;
        this.channel.setMethodCallHandler(this);
    }


    private void initPollfish(final Activity activity, final String apiKey, final int p,
                              final int indPadding, final boolean releaseMode,
                              final boolean rewardMode,
                              final boolean offerwallMode, final String request_uuid) {

        String requestUUIDtemp = null;

        if (request_uuid != null) {
            if (request_uuid.trim().length() > 0) {
                requestUUIDtemp = request_uuid;
            }
        }

        final String requestUUID = requestUUIDtemp;

        final PollfishReceivedSurveyListener pollfishReceivedSurveyListener = new PollfishReceivedSurveyListener() {

            @Override
            public void onPollfishSurveyReceived(SurveyInfo surveyInfo) {

                if(surveyInfo!=null){

                Log.d(TAG, "onPollfishSurveyReceived (CPA: " + surveyInfo.getSurveyCPA() + ", IR: " + String.valueOf(surveyInfo.getSurveyIR()) + ", LOI: " + String.valueOf(surveyInfo.getSurveyLOI()) + ", SurveyClass: " + String.valueOf(surveyInfo.getSurveyClass())  + ", RewardName: " + String.valueOf(surveyInfo.getRewardName())  + ", RewardValue: " + String.valueOf(surveyInfo.getRewardValue())+ ")");

                channel.invokeMethod("PollfishSurveyReceived", String.valueOf(surveyInfo.getSurveyCPA()) + "," + String.valueOf(surveyInfo.getSurveyIR()) + "," + String.valueOf(surveyInfo.getSurveyLOI()) + "," + String.valueOf(surveyInfo.getSurveyClass()  + "," + String.valueOf(surveyInfo.getRewardName())  + "," + String.valueOf(surveyInfo.getRewardValue())));

                }else{

                    Log.d(TAG, "onPollfishSurveyReceived");

                    channel.invokeMethod("PollfishSurveyReceived", "");
                }
            }
        };

        final PollfishCompletedSurveyListener pollfishCompletedSurveyListener = new PollfishCompletedSurveyListener() {

            @Override
            public void onPollfishSurveyCompleted(SurveyInfo surveyInfo) {

                if(surveyInfo!=null){

                    Log.d(TAG, "onPollfishSurveyCompleted (CPA: " + surveyInfo.getSurveyCPA() + ", IR: " + String.valueOf(surveyInfo.getSurveyIR()) + ", LOI: " + String.valueOf(surveyInfo.getSurveyLOI()) + ", SurveyClass: " + String.valueOf(surveyInfo.getSurveyClass())  + ", RewardName: " + String.valueOf(surveyInfo.getRewardName())  + ", RewardValue: " + String.valueOf(surveyInfo.getRewardValue())+ ")");

                    channel.invokeMethod("PollfishSurveyCompleted", String.valueOf(surveyInfo.getSurveyCPA()) + "," + String.valueOf(surveyInfo.getSurveyIR()) + "," + String.valueOf(surveyInfo.getSurveyLOI()) + "," + String.valueOf(surveyInfo.getSurveyClass()  + "," + String.valueOf(surveyInfo.getRewardName())  + "," + String.valueOf(surveyInfo.getRewardValue())));

                }else{

                    Log.d(TAG, "onPollfishSurveyCompleted");

                    channel.invokeMethod("PollfishSurveyCompleted", "");
                }


            }
        };

        final PollfishOpenedListener pollfishOpenedListener = new PollfishOpenedListener() {

            public void onPollfishOpened() {

                Log.d(TAG, "onPollfishOpened");

                channel.invokeMethod("PollfishOpened", null);
            }
        };


        final PollfishClosedListener pollfishClosedListener = new PollfishClosedListener() {

            public void onPollfishClosed() {

                Log.d(TAG, "onPollfishClosed");

                channel.invokeMethod("PollfishClosed", null);
            }
        };

        final PollfishSurveyNotAvailableListener pollfishSurveyNotAvailableListener = new PollfishSurveyNotAvailableListener() {
            public void onPollfishSurveyNotAvailable() {

                Log.d(TAG, "onPollfishSurveyNotAvailable");

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                channel.invokeMethod("PollfishSurveyNotAvailable", null);
                            }
                        });
            }
        };

        final PollfishUserNotEligibleListener pollfishUserNotEligibleListener = new PollfishUserNotEligibleListener() {
            public void onUserNotEligible() {
                Log.d(TAG, "onUserNotEligible");

                channel.invokeMethod("PollfishUserNotEligible", null);
            }
        };

        final PollfishUserRejectedSurveyListener pollfishUserRejectedSurveyListener = new PollfishUserRejectedSurveyListener() {
            public void onUserRejectedSurvey() {
                Log.d(TAG, "onUserRejectedSurvey");

                channel.invokeMethod("PollfishUserRejectedSurvey", null);
            }
        };

        final Position position = Position.values()[p];

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                PollFish.initWith(activity,
                        new PollFish.ParamsBuilder(apiKey)
                                .indicatorPosition(position)
                                .indicatorPadding(indPadding)
                                .pollfishReceivedSurveyListener(pollfishReceivedSurveyListener)
                                .pollfishCompletedSurveyListener(pollfishCompletedSurveyListener)
                                .pollfishSurveyNotAvailableListener(pollfishSurveyNotAvailableListener)
                                .pollfishUserRejectedSurveyListener(pollfishUserRejectedSurveyListener)
                                .pollfishUserNotEligibleListener(pollfishUserNotEligibleListener)
                                .pollfishOpenedListener(pollfishOpenedListener)
                                .pollfishClosedListener(pollfishClosedListener)
                                .requestUUID(requestUUID)
                                .releaseMode(releaseMode)
                                .rewardMode(rewardMode)
                                .offerWallMode(offerwallMode)
                                .build());
            }
        });

    }

    private void exctractPollfishParams(MethodCall call, Result result) {

        String api_key = null;

        if (call.argument("api_key") != null) {
            api_key = call.argument("api_key");
        }

        if (api_key == null) {
            result.error("no_api_key", "a null api_key was provided", null);
            return;

        }

        int pollfishPosition=0;
        int indPadding =50;
        boolean releaseMode= false;
        boolean rewardMode = false;
        boolean offerwallMode = false;
        String request_uuid = null;

        if (call.argument("pollfishPosition") != null) {
            pollfishPosition = call.argument("pollfishPosition");
        }
        if (call.argument("indPadding") != null) {
            indPadding = call.argument("indPadding");
        }
        if (call.argument("releaseMode") != null) {
            releaseMode = call.argument("releaseMode");
        }
        if (call.argument("rewardMode") != null) {
            rewardMode = call.argument("rewardMode");
        }
        if (call.argument("offerwallMode") != null) {
            offerwallMode = call.argument("offerwallMode");
        }

        if (call.argument("request_uuid") != null) {
            request_uuid = call.argument("request_uuid");
        }

        Log.d(TAG, "api_key: " + api_key);
        Log.d(TAG, "pollfishPosition: " + pollfishPosition);
        Log.d(TAG, "indPadding: " + indPadding);
        Log.d(TAG, "releaseMode: " + releaseMode);
        Log.d(TAG, "rewardMode: " + rewardMode);
        Log.d(TAG, "offerwallMode: " + offerwallMode);
        Log.d(TAG, "request_uuid: " + request_uuid);

        initPollfish(registrar.activity(), api_key, pollfishPosition, indPadding, releaseMode, rewardMode,offerwallMode, request_uuid);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        if (call.method.equals("init")) {

            Log.d(TAG, "Pollfish init");

            exctractPollfishParams(call, result);

        } else if (call.method.equals("show")) {

            Log.d(TAG, "Pollfish show");

            PollFish.show();

        } else if (call.method.equals("hide")) {

            Log.d(TAG, "Pollfish hide");

            PollFish.hide();
        }
    }
}
