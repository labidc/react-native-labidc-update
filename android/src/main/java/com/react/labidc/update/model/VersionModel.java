package com.react.labidc.update.model;

import lombok.Data;

/**
 * 版本实体
 */
@Data
public class VersionModel {


    /**
     * 文件包名称
     */
    private String clientName;

    /**
     * 文件包大小
     */
    private String clientSize;

    /**
     * 文件包下载路径
     */
    private String clientUrl;


    /**
     * 扩展字段
     */
    private Integer promote;

    /**
     * 更新内容
     */
    private String upgradePrompt;

    /**
     * 编译版本号
     */
    private Integer version;

    /**
     * 软件版本号
     */
    private String versionCode;

}
