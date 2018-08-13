
#import "LabidcUpdate.h"
#import "Tool.h"
// 跳转地址
static NSString *jumpUrl = nil;
// json数据格式
static NSDictionary *dic = nil;

@implementation LabidcUpdate

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()
// 导出方法, RN 通过这里和ios原生交互
RCT_EXPORT_METHOD(check:(NSString *)jsonModel callback:(RCTResponseSenderBlock)callback) {
    
    if (jsonModel == nil) {
        NSLog(@"请求数据不能为空");
        callback(@[@1, @"json数据不能为空"]);
        return;
    }
    
    NSData *jsonData = [jsonModel dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error;
    NSDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                          options:NSJSONReadingMutableContainers
                                                          error:&error];
    if (error) {
        NSLog(@"出现错误:%@",error);
        callback(@[@2, @"json转换出现错误"]);
        return;
    }
    // 调用方法执行
    [self check:jsonDic];
    //回调给js方法
    callback(@[@0, @"请求成功"]);
}

// 按钮点击之后的事件
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSString *btnTitle = [alertView buttonTitleAtIndex:buttonIndex];
    if ([btnTitle isEqualToString: [Tool isBlankString: dic[@"confirmText"]]==NO? dic[@"confirmText"]:@"更新"]) {
        NSString *ipaURL = jumpUrl;
        NSLog(@"获得的下载url路径 %@",jumpUrl);
        NSURL *downURL = [NSURL URLWithString:[NSString stringWithFormat:@"%@", ipaURL]];
        [[UIApplication sharedApplication] openURL:downURL];
    }
}


// 版本号判断
- (void) check:(NSDictionary *) jsonDic
{
    // NSString *plistPath = [[NSBundle mainBundle] pathForResource:@"LabidcUpdate" ofType:@"plist"];
    // NSDictionary *updateDic = [[NSDictionary alloc] initWithContentsOfFile:plistPath];
    NSLog(@"接收到的数据，jsonDic = %@",jsonDic);
    
    //获取工程项目版本号
    NSDictionary *infoDic = [[NSBundle mainBundle] infoDictionary];
    NSLog(@"%@",infoDic);
    NSString *currentVersion = infoDic[@"CFBundleVersion"];
    /*
    NSString *currentVersion=infoDic[@"CFBundleShortVersionString"];
    NSString *bundleVersion = [[[NSBundle mainBundle]infoDictionary] objectForKey:@"CFBundleVersion"];
    BOOL isUpdate = [updateDic[@"update"] boolValue];
    versionUrl = updateDic[@"url"];
    versionUrl = [versionUrl stringByAddingPercentEscapesUsingEncoding: NSUTF8StringEncoding];
    NSURL *url = [NSURL URLWithString: versionUrl];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    // 获取连接的响应信息，可以为nil
    NSURLResponse *respone;
    NSError *error;
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse: &respone error: &error];
 
    if (response == nil) {
        NSLog(@"你没有连接网络哦");
        return @"false";
    }
     
    NSDictionary *appInfoDic = [NSJSONSerialization JSONObjectWithData:response options:NSJSONReadingMutableLeaves error:&error];
    if (error) {
        NSLog(@"出现错误:%@",error);
        return @"false";
    }
     */
    jumpUrl = jsonDic[@"jumpUrl"];
    dic = jsonDic;
    int versionCode = [jsonDic[@"versionCode"] intValue];
    // 判断app版本与实际最新版本的情况，如果app当前版本小于请求得到的版本大小，就返回true
    if([currentVersion intValue] < versionCode)
    {
        UIAlertView *alert = [[UIAlertView alloc]
                              initWithTitle:[Tool isBlankString: jsonDic[@"title"]]==NO? jsonDic[@"title"]:@"版本有更新"
                              message:[Tool isBlankString: jsonDic[@"message"]]==NO? jsonDic[@"message"]:@"检测到新版本,是否更新?"
                              delegate:self
                              cancelButtonTitle: [Tool isBlankString: jsonDic[@"cancelText"]]==NO? jsonDic[@"cancelText"]:@"取消"
                              otherButtonTitles: [Tool isBlankString: jsonDic[@"confirmText"]]==NO? jsonDic[@"confirmText"]:@"更新",
                              nil];
        [alert show];
    }
}
@end
  
