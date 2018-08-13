package com.react.labidc.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    // private static String versionUrl = "";

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
    public static void init(final Activity activity, final VersionModel versionModel){


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

            /**
             * 点击按钮出发更新事件
             */
            updateDialog.setOnCenterItemClickListener(new UpdateDialog.OnCenterItemClickListener() {
                @Override
                public void OnCenterItemClick(UpdateDialog dialog, View view) {
                    int i = view.getId();
                    if (i == R.id.dialog_sure) {

                        if (versionModel.getInstallType() == 0) {
                            // 直接下载并安装
                            apkUrl = versionModel.getApkUrl();
                            confirmDownloadApk(activity);
                        } else {
                            // 代码实现跳转.浏览器打开
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri url = Uri.parse(versionModel.getApkUrl());
                            intent.setData(url);
                            activity.startActivity(intent);
                        }

                    }
                }
            });
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
     * 打开启动屏
     */
    public static void check(final Activity activity, VersionModel versionModel) {

        // 初始化一些参数
        init(activity, versionModel);

        // 显示弹窗
        showUpdateDiglog(activity, versionModel);
    }

    /**
     * 显示更新弹窗
     * @param versionModel
     */
    private static void showUpdateDiglog(final Activity activity, final VersionModel versionModel) {


        /**
         * 弹出更新文本框
         */
        if (versionModel != null) {

            int curVersion = getVersionCode(activity);
            if (curVersion < versionModel.getVersionCode()) {
                updateDialog.show();
                TextView updateTitle = (TextView) updateDialog.findViewById(R.id.updateTitle);
                TextView updateVersionName = (TextView) updateDialog.findViewById(R.id.updateVersionName);
                TextView updateUpgradePrompt = (TextView) updateDialog.findViewById(R.id.updateUpgradePrompt);
                TextView updateBut = (TextView) updateDialog.findViewById(R.id.updateBut);
                LinearLayout dialog_sure = (LinearLayout) updateDialog.findViewById(R.id.dialog_sure);

                if(!"".equals(versionModel.getTitle()))
                    updateTitle.setText(versionModel.getTitle());

                if(!"".equals(versionModel.getTitleColor()))
                    updateTitle.setTextColor(Color.parseColor(versionModel.getTitleColor()));

                if(!"".equals(versionModel.getUpgradePrompt()))
                    updateUpgradePrompt.setText(versionModel.getUpgradePrompt());

                if(!"".equals(versionModel.getUpgradePromptColor()))
                    updateUpgradePrompt.setTextColor(Color.parseColor(versionModel.getUpgradePromptColor()));

                if(!"".equals(versionModel.getVersionName()))
                    updateVersionName.setText(versionModel.getVersionName());

                if(!"".equals(versionModel.getVersionNameColor()))
                    updateVersionName.setTextColor(Color.parseColor(versionModel.getVersionNameColor()));

                if(!"".equals(versionModel.getButText()))
                    updateBut.setText(versionModel.getButText());

                if(!"".equals(versionModel.getButTextColor()))
                    updateBut.setTextColor(Color.parseColor(versionModel.getButTextColor()));

                if(!"".equals(versionModel.getButBgColor())) {
                    dialog_sure.setBackgroundColor(Color.parseColor(versionModel.getButBgColor()));
                    updateBut.setBackgroundColor(Color.parseColor(versionModel.getButBgColor()));
                    GradientDrawable gradientDrawable = (GradientDrawable)dialog_sure.getBackground();
                    gradientDrawable.setColor(Color.parseColor(versionModel.getButBgColor()));
                }

               
            }
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
