import 'dart:async';

import 'package:flutter/services.dart';

// Pollfish notifications

typedef void PollfishReceivedSurveyListener(String result);
typedef void PollfishCompletedSurveyListener(String result);
typedef void PollfishUserNotEligibleListener();
typedef void PollfishUserRejectedSurveyListener();
typedef void PollfishSurveyOpenedListener();
typedef void PollfishSurveyClosedListener();
typedef void PollfishSurveyNotAvailableListener();

class FlutterPollfish {
  /// The single shared instance of this plugin.
  static FlutterPollfish get instance => _instance;

  final MethodChannel _channel;

  static final FlutterPollfish _instance = FlutterPollfish.private(
    const MethodChannel('flutter_pollfish'),
  );

  FlutterPollfish.private(MethodChannel channel) : _channel = channel {
    _channel.setMethodCallHandler(_platformCallHandler);
  }

  static PollfishReceivedSurveyListener _pollfishReceivedSurveyListener;
  static PollfishCompletedSurveyListener _pollfishCompletedSurveyListener;
  static PollfishUserNotEligibleListener _pollfishUserNotEligibleListener;
  static PollfishUserRejectedSurveyListener _pollfishUserRejectedSurveyListener;
  static PollfishSurveyOpenedListener _pollfishSurveyOpenedListener;
  static PollfishSurveyClosedListener _pollfishSurveyClosedListener;
  static PollfishSurveyNotAvailableListener _pollfishSurveyNotAvailableListener;

  Future<void> init(
      {String apiKey,
        int pollfishPosition,
        int indPadding,
        bool rewardMode,
        bool releaseMode,
        bool offerwallMode,
        String requestUUID,}) async {
    assert(apiKey != null && apiKey.isNotEmpty);

    print(' invokeMethod FlutterPollfish.init()... ');

    return _channel.invokeMethod("init", <String, dynamic>{
      'api_key': apiKey,
      'pollfishPosition': pollfishPosition,
      'indPadding':  indPadding,
      'rewardMode': rewardMode,
      'releaseMode': releaseMode,
      'offerwallMode': offerwallMode,
      'request_uuid': requestUUID,
    });
  }

  Future<void> show() {
    print(' invokeMethod FlutterPollfish.show()... ');
    return _channel.invokeMethod('show');
  }

  Future<void> hide() {
    print(' invokeMethod FlutterPollfish.hide()... ');
    return _channel.invokeMethod('hide');
  }

  Future _platformCallHandler(MethodCall call) async {
    switch (call.method) {
      case "PollfishSurveyReceived":
        _pollfishReceivedSurveyListener(call.arguments);
        print("pollfishSurveyReceived");

        break;

      case "PollfishSurveyCompleted":
        _pollfishCompletedSurveyListener(call.arguments);
        print("pollfishSurveyCompleted");

        break;

      case "PollfishUserNotEligible":
        _pollfishUserNotEligibleListener();
        print("pollfishUserNotEligible");

        break;

      case "PollfishUserRejectedSurvey":
        _pollfishUserRejectedSurveyListener();
        print("pollfishUserRejectedSurvey");

        break;
      case "PollfishOpened":
        _pollfishSurveyOpenedListener();
        print("pollfishSurveyOpened");

        break;
      case "PollfishClosed":
        _pollfishSurveyClosedListener();
        print("pollfishSurveyClosedListener");

        break;
      case "PollfishSurveyNotAvailable":
        _pollfishSurveyNotAvailableListener();
        print("pollfishSurveyNotAvailable");

        break;

      default:
        print('Unknown method ${call.method} ');
    }
  }

  void setPollfishReceivedSurveyListener(
      PollfishReceivedSurveyListener pollfishReceivedSurveyListener) =>
      _pollfishReceivedSurveyListener = pollfishReceivedSurveyListener;

  void setPollfishCompletedSurveyListener(
      PollfishCompletedSurveyListener pollfishCompletedSurveyListener) =>
      _pollfishCompletedSurveyListener = pollfishCompletedSurveyListener;

  void setPollfishUserNotEligibleListener(
      PollfishUserNotEligibleListener pollfishUserNotEligibleListener) =>
      _pollfishUserNotEligibleListener = pollfishUserNotEligibleListener;

  void setPollfishUserRejectedSurveyListener(
      PollfishUserRejectedSurveyListener
      pollfishUserRejectedSurveyListener) =>
      _pollfishUserRejectedSurveyListener = pollfishUserRejectedSurveyListener;

  void setPollfishSurveyOpenedListener(
      PollfishSurveyOpenedListener pollfishSurveyOpenedListener) =>
      _pollfishSurveyOpenedListener = pollfishSurveyOpenedListener;

  void setPollfishSurveyClosedListener(
      PollfishSurveyClosedListener pollfishSurveyClosedListener) =>
      _pollfishSurveyClosedListener = pollfishSurveyClosedListener;

  void setPollfishSurveyNotAvailableSurveyListener(
      PollfishSurveyNotAvailableListener
      pollfishSurveyNotAvailableListener) =>
      _pollfishSurveyNotAvailableListener = pollfishSurveyNotAvailableListener;
}
