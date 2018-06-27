package com.react.labidc.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.react.labidc.update.model.VersionModel;
import com.react.labidc.update.tool.UpdateDialog;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 更新检查
 */
public class RNReactNativeLabidcUpdate {

    /**
     * 日志TAG
     */
    private final static String TAG = RNReactNativeLabidcUpdate.class.toString();

    /**
     * 错误编码
     */
    public static final int SHOW_ERROR = 1;


    /**
     * 版本请求地址
     */
    private static String versionUrl = "";



    /**
     * 弹窗对象
     */
    private static UpdateDialog updateDialog = null;


    /**
     * 需要下载的apk Url路径
     */
    private static String apkUrl = "";


    /**
     * apk 下载到本地的存储路径
     */
    private static String apkCachePath = null;


    /**
     * 消息处理
     */
    private static Handler handler = null;


    /**
     * 一些初始化操作
     * @param activity
     */
    public static void init(final Activity activity){


        try {
            ApplicationInfo appInfo = activity.getPackageManager()
                    .getApplicationInfo(activity.getPackageName(),
                            PackageManager.GET_META_DATA);
            versionUrl = appInfo.metaData.getString("labidc.update.url","");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(apkCachePath == null) {
            // 这里的目录对应 files-path 设置的目录根目录下
            apkCachePath = (
                    activity.getFilesDir() +
                            File.separator +
                            activity.getPackageName() +
                            ".apk");
        }



        if(updateDialog == null) {
            updateDialog = new UpdateDialog(activity, R.layout.dialog_updataversion,
                    new int[]{R.id.dialog_sure});
        }

        if(handler == null) {
            /**
             * 安卓的消息处理
             */
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        /** 提示错误    */
                        case SHOW_ERROR:
                            Toast.makeText(activity, msg.obj.toString(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                }
            };
        }
    }


    /**
     * 获取当前apk的版本
     * @return
     */
    private static int getVersionCode(final Activity activity)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }



    /**
     * 请求版本更新检查
     * @return
     */
    private static VersionModel execute(final Activity activity) {

        if("".equals(versionUrl)) {
            Log.e(TAG,"没有获取到版本请求url，请检查");
            return null;
        }

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().get().url(versionUrl).
                    header("Content-Type", "application/json; charset=UTF-8").
                    header("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonStr = response.body().string();

                Log.e(TAG,jsonStr);
                if ("".equals(jsonStr))
                    return null;
                else {
                    VersionModel versionModel = JSON.parseObject(jsonStr, new TypeReference<VersionModel>() {
                    });
                    int curVersion = getVersionCode(activity);
                    if (curVersion < versionModel.getVersion()) {
                        return versionModel;
                    }
                    return null;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 打开启动屏
     */
    public static void check(final Activity activity) {
        // 初始化一些参数
        init(activity);
        // 请求版本，返回版本实体
        VersionModel vesionModel = execute(activity);
        // 显示弹窗
        showUpdateDiglog(activity, vesionModel);
    }

    /**
     * 显示更新弹窗
     * @param vesionModel
     */
    private static void showUpdateDiglog(final Activity activity, final VersionModel vesionModel) {


        /**
         * 弹出更新文本框
         */
        if (vesionModel != null) {
            updateDialog.show();
            TextView tvmsg = (TextView) updateDialog.findViewById(R.id.updataVersionMsg);
            TextView tvcode = (TextView) updateDialog.findViewById(R.id.updataVersionCode);
            tvcode.setText(vesionModel.getVersionCode());
            tvmsg.setText(vesionModel.getUpgradePrompt());
            /**
             * 点击按钮出发更新事件
             */
            updateDialog.setOnCenterItemClickListener(new UpdateDialog.OnCenterItemClickListener() {
                @Override
                public void OnCenterItemClick(UpdateDialog dialog, View view) {
                    int i = view.getId();
                    Log.e(TAG,"点击了按钮"+i);
                    if (i == R.id.dialog_sure) {
                        apkUrl = vesionModel.getClientUrl();
                        confirmDownloadApk(activity);
                        //弹窗下载
                    }
                }
            });
        }
    }

    /**
     * 确认下载
     * @param activity
     */
    private static void confirmDownloadApk(final Activity activity) {
        //显示下载进度
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
        //访问网络下载apk
        new Thread(new DownloadApk(activity, dialog)).start();
    }

    /**
     * 访问网络下载apk
     */
    private static class DownloadApk implements Runnable {
        private ProgressDialog dialog;
        private InputStream is;
        private FileOutputStream fos;
        private Activity activity;

        public DownloadApk( Activity activity, ProgressDialog dialog) {

            this.dialog = dialog;
            this.activity =activity;
        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().get().url(apkUrl).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d(TAG, "开始下载apk");
                    //获取内容总长度
                    long contentLength = response.body().contentLength();
                    //设置最大值
                    dialog.setMax((int) contentLength);
                    //保存到sd卡
                    File apkFile = new File(apkCachePath);

                    Log.d(TAG,"目录："+ apkCachePath);

                    fos = new FileOutputStream(apkFile);
                    //获得输入流
                    is = response.body().byteStream();
                    //定义缓冲区大小
                    byte[] bys = new byte[1024];
                    int progress = 0;
                    int len = -1;
                    while ((len = is.read(bys)) != -1) {
                        try {
                            Thread.sleep(1);
                            fos.write(bys, 0, len);
                            fos.flush();
                            progress += len;
                            //设置进度
                            dialog.setProgress(progress);
                        } catch (InterruptedException e) {
                            Message msg = Message.obtain();
                            msg.what = SHOW_ERROR;
                            msg.obj = "下载出错：请选择好的网络环境下载。";
                            handler.sendMessage(msg);
                        }
                    }
                    //下载完成,提示用户安装
                    installApk(activity, apkCachePath);
                }
            } catch (IOException e) {
                Message msg = Message.obtain();
                msg.what = SHOW_ERROR;
                msg.obj = "下载出错：没有文件夹处理权限或者网络不好。";;
                handler.sendMessage(msg);
                e.printStackTrace();
            } finally {
                //关闭io流
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    is = null;
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fos = null;
                }
            }
            dialog.dismiss();
            updateDialog.dismiss();
        }
    }

    /**
     * 安装apk 首先要先下载
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        if (context == null || "".equals(apkPath)) {
            return;
        }

        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {

            Log.e(TAG,"开始安装");
            Uri apkUri = FileProvider.getUriForFile(context, "com.react.labidc.update.fileProvider", file);

            Log.e(TAG,"路径：" + apkUri.toString());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        Log.e(TAG,"开始跳转");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
