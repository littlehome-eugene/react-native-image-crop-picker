#import "React/RCTBridgeModule.h"
@interface RCT_EXTERN_MODULE(ToastExample, NSObject)

  RCT_EXTERN_METHOD(
    show:(NSString*) message
    duration: (int) duration
                    )

@end
