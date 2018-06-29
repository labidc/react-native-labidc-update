
package com.react.labidc.update;

import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.react.labidc.update.model.VersionModel;
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
     * jsonVersionModel
     * @param jsonVersionModel
     * @param callBack
     */
  @ReactMethod
  public void check(String jsonVersionModel, Callback callBack) {

      try {
          VersionModel versionModel = JSON.parseObject(jsonVersionModel, new TypeReference<VersionModel>() {
          });
          RNReactNativeLabidcUpdate.check(getCurrentActivity(), versionModel);
          callBack.invoke(0, "");
      }catch (Exception ex) {
          callBack.invoke(1, "versionModel 数据格式错误，请检查传入的json数据格式");
          ex.printStackTrace();
      }


  }

  /**
   * 主动关闭更新模块
   */
  @ReactMethod
  public void hide() {
     //暂时没有做主动隐藏
  }
}