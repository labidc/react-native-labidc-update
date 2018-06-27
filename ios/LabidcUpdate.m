
#import "LabidcUpdate.h"

// 新版本检查请求url
static NSString *versionUrl = nil;
// 有版本更新之后，下载新版本的url
static NSString *clientUrl = nil;

@implementation LabidcUpdate

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()
// 导出方法, RN 通过这里和ios原生交互
RCT_EXPORT_METHOD(check
                  : (RCTResponseSenderBlock)callback)
{
    NSLog(@"调用请求了");
    NSString *callBackStr = nil;
    callBackStr = self.check;

    NSLog(@"打印的结果:%@", callBackStr);
    //回调给js方法
    callback(@[ [NSNull null], callBackStr ]);
}

// 按钮点击之后的事件
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSString *btnTitle = [alertView buttonTitleAtIndex:buttonIndex];
    if ([btnTitle isEqualToString:@"更新"])
    {
        NSString *ipaURL = clientUrl;
        NSLog(@"获得的下载url路径 %@", clientUrl);
        NSURL *downURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@", ipaURL]];
        [[UIApplication sharedApplication] openURL:downURL];
    }
}

// 获取版本号
- (NSString *)check
{

    NSString *plistPath = [[NSBundle mainBundle] pathForResource:@"LabidcUpdate" ofType:@"plist"];
    NSDictionary *updateDic = [[NSDictionary alloc] initWithContentsOfFile:plistPath];
    NSLog(@"获取到的值，dictionary = %@", updateDic);

    //获取工程项目版本号
    NSDictionary *infoDic = [[NSBundle mainBundle] infoDictionary];

    NSLog(@"%@", infoDic);
    // NSString *currentVersion=infoDic[@"CFBundleShortVersionString"];

    NSString *currentVersion = infoDic[@"CFBundleVersion"];
    // NSString *bundleVersion = [[[NSBundle mainBundle]infoDictionary] objectForKey:@"CFBundleVersion"];

    BOOL isUpdate = [updateDic[@"update"] boolValue];
    versionUrl = updateDic[@"url"];
    versionUrl = [versionUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSURL *url = [NSURL URLWithString:versionUrl];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    // 获取连接的响应信息，可以为nil
    NSURLResponse *respone;
    NSError *error;
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:&respone error:&error];

    if (response == nil)
    {
        NSLog(@"你没有连接网络哦");
        return @"false";
    }

    NSDictionary *appInfoDic = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableLeaves error:&error];
    if (error)
    {
        NSLog(@"出现错误:%@", error);
        return @"false";
    }

    NSString *versionCode = appInfoDic[@"versionCode"];
    NSString *appStoreVersion = appInfoDic[@"version"];
    clientUrl = appInfoDic[@"clientUrl"];
    // 判断app版本与实际最新版本的情况，如果app当前版本小于请求得到的版本大小，就返回true
    if ([currentVersion floatValue] < [appStoreVersion floatValue])
    {
        if (isUpdate == YES)
        {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"版本有更新" message:[NSString stringWithFormat:@"检测到新版本(%@),是否更新?", versionCode] delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"更新", nil];
            [alert show];
        }
        return @"true";
    }
    else
    {
        NSLog(@"检测到不需要更新");
        return @"false";
    }
}
@end
