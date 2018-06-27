package com.react.labidc.update.model;

import lombok.Data;

/**
 * 版本实体
 */
@Data
public class VersionModel {


    /**
     * 标题文本
     */
    private String title;

    /**
     * 标题文字颜色
     */
    private String titleColor;

    /**
     * 版本号，整数，作为版本比较使用
     */
    private int versionCode;

    /**
     * 版本号名称
     */
    private String versionName;

    /**
     * 版本号名称文字颜色
     */
    private String versionNameColor;

    /**
     * 更新说明
     */
    private String upgradePrompt;

    /**
     * 更新说明文字颜色
     */
    private String upgradePromptColor;

    /**
     * 更新按钮文字
     */
    private String butText;

    /**
     * 更新按钮文字颜色
     */
    private String butTextColor;

    /**
     * 更新按钮背景颜色
     */
    private String butBgColor;

    /**
     * APK 地址
     */
    private String apkUrl;

    /**
     * 0 是直接安装，1，是跳转连接
     */
    private int installType;
}
