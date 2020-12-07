import Flutter
import UIKit
import Pollfish

public class SwiftPollfishPlugin: NSObject, FlutterPlugin {
    
    private var pollfishChannel: FlutterMethodChannel?
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_pollfish", binaryMessenger: registrar.messenger())
        let instance = SwiftPollfishPlugin()
        instance.pollfishChannel = channel
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "init":
            if let config = call.arguments, let dict = config as? [String: Any] {
                print(config)
                initPollfish(configs: dict)
                addListeners()
            }
            break
            
        case "show":
            Pollfish.show()
            break
        default:
            break
        }
    }
    
    // init Pollfish client
    private func initPollfish(configs: [String: Any]) {
        if let apiKey = configs["api_key"] as? String, let rewardMode = configs["rewardMode"] as? Bool, let releaseMode = configs["releaseMode"] as? Bool, let offerwallMode = configs["offerwallMode"] as? Bool{
            let params = PollfishParams()
            params.rewardMode = rewardMode
            params.releaseMode = releaseMode
            params.offerwallMode = offerwallMode
            params.requestUUID = configs["request_uuid"] as? String ?? ""
            
            Pollfish.initWithAPIKey(apiKey, andParams: params)
        }
    }
    
    // add listeners for Pollfish events
    private func addListeners() {
        let events: [String] = ["PollfishSurveyReceived", "PollfishSurveyCompleted", "PollfishUserNotEligible", "PollfishUserRejectedSurvey", "PollfishOpened", "PollfishClosed", "PollfishSurveyNotAvailable"]
        
        for e: String in events {
            let name = NSNotification.Name(rawValue: e)
            NotificationCenter.default.addObserver(forName: name, object: nil, queue: nil) { notification in
                self.pollfishChannel?.invokeMethod(e, arguments: nil)
            }
        }
    }
}
