
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <Foundation/Foundation.h>
#import <React/RCTLog.h>

@interface LabidcUpdate : NSObject <RCTBridgeModule>
// 检查更新
- (NSString *)check;
@end
