
package com.react.labidc.update;

import android.os.Handler;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.react.labidc.update.tool.UpdateDialog;

/**
 * 更新模块上下文
 */
public class RNReactNativeLabidcUpdateModule extends ReactContextBaseJavaModule {

  /**
   * 模块构造函数
   * @param reactContext
   */
  public RNReactNativeLabidcUpdateModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  /**
   * 模块名称
   * @return
   */
  @Override
  public String getName() {
    return "LabidcUpdate";
  }

  /**
   * 检查更新
   */
  @ReactMethod
  public void check() {
    RNReactNativeLabidcUpdate.check(getCurrentActivity());
  }

  /**
   * 主动关闭更新模块
   */
  @ReactMethod
  public void hide() {
    //暂时没有做主动隐藏
  }
}