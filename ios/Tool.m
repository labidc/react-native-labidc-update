//
//  Tool.m
//  RNReactNativeLabidcUpdate
//
//  Created by 陈兴亮 on 2018/6/29.
//  Copyright © 2018年 Facebook. All rights reserved.
//
#import "Tool.h"
@implementation Tool

+ (BOOL)isBlankString: (NSString *)string {
    
        if (string == nil || string == NULL) {
            return YES;
        }
        if ([string isKindOfClass:[NSNull class]]) {
            return YES;
        }
        if ([[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length]==0) {
            return YES;
        }
        return NO;
}

@end
