#import "FlutterPollfishPlugin.h"
#if __has_include(<flutter_pollfish/flutter_pollfish-Swift.h>)
#import <flutter_pollfish/flutter_pollfish-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_pollfish-Swift.h"
#endif

@implementation FlutterPollfishPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPollfishPlugin registerWithRegistrar:registrar];
}
@end
