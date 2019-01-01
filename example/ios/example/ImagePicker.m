#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(ImagePicker, NSObject)

  RCT_EXTERN_METHOD(
    openPicker:(NSDictionary *)options
    resolve: (RCTPromiseResolveBlock)resolve
    rejecter: (RCTPromiseRejectBlock)reject
                    )


@end
