
# react-native-labidc-update
# 安卓： 1 --> 2/3 --> 4.1
# IOS： 1 --> 2/3 --> 4.2
## 1.安装开发包

`$ npm install react-native-labidc-update --save`

## 2. 自动安装，使用 react-native  link命令安装（3. 手动安装）

`$ react-native link react-native-labidc-update`

## 3. 手动安装

### Android 手动安装

1. 打开 `android/app/src/main/java/[...]/MainApplication.java`
  - 在文件头部添加 `import com.react.labidc.update.RNReactNativeLabidcUpdatePackage;` 
  - 在`getPackages()` 方法里把  `new RNReactNativeLabidcUpdatePackage()` 添加到 list 对象里 
2. 打开 `android/settings.gradle` 添加包路径到settings.gradle 文件:
  	```
  	include ':react-native-labidc-update'
  	project(':react-native-labidc-update').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-labidc-update/android')
  	```
3. 打开 `android/app/build.gradle` 文件添加一下代码，以下代码的含义是编译的时候加入该包:
  	```
      compile project(':react-native-labidc-update')
  	```
##  iOS 手动安装

1. 打开XCode, 进入项目导航, 右击 `Libraries` ➜ `Add Files to [your project's name]`
2. 打开目录 `node_modules` ➜ `react-native-labidc-update` 选择 添加该工程 `RNReactNativeLabidcUpdate.xcodeproj`
3. 打开XCode, 选择项目导航, 选择你的项目. Add `libRNReactNativeLabidcUpdate.a` 加入到你的项目的设置 `Build Phases` ➜ `Link Binary With Libraries`



## 4.1 Android 配置说明

1. 背景图片的更换：该组件提供了默认背景图片，如果您需要更换图片，请在你的app工程中添加目录
```res/drawable-hdpi``` （有可能目录已存在）然后放入背景图片, `roctet_bg.png`。 注意：图片文件名必须是这个名称。
建议图片文件规格大小：492 * 620

## 4.1.1 Android React 里的使用方法
```javascript
import LabidcUpdate from 'react-native-labidc-update';

// TODO: 该传递进入插件的数据，请自行通过自己开发后端api调用返回数据，然后包装成下面的数据格式
 // versionCode 是您们自己的api接口获取当前最新版本号
    let json = {};
    json.title ="标题"; //标题，选填
    json.titleColor ="#FF1527"; // 标题颜色 选填
    json.versionCode = 3; //编译版本号 必填，因为拿使用该版本号和apk文件版本号比对，versionCode 大于 apk的版本，会出现提示框
    json.versionName = "1.0.1"; // 版本号名 建议必填
    json.versionNameColor = "#FF1527"; //版本号颜色 选填
    json.upgradePrompt = "这次版本更新了 \n 1.长帅了 \n 2.长高了 " // 版本更新说明, "\n" 换行 选填
    json.upgradePromptColor = "#FF1527"; //版本更新说明颜色 选填
    json.butText = "点击更新"; //按钮文本 选填
    json.butTextColor = "#FF1527"; //按钮文本颜色 选填
    json.butBgColor = "#FF1527"; //按钮背景颜色
    json.apkUrl = "http://client.download.qishixingqiu.com/0client/express/1.1.1.apk"; //apk下载地址，必填
    //确认更新后的更新方式。
    //填 0 自动下载并安装,必须是apk文件。
    //填 1 跳转到 apkUrl 提供的地址，打开浏览器，可以是一个页面
    json.installType = 1; 
    /**
     * statusCode 状态编码 == 0 正确，number类型
     * statusMsg 状态内容 String类型
     */
    LabidcUpdate.check(JSON.stringify(json), (statusCode, statusMsg) => {
        console.warn(statusCode);
        console.warn(typeof (statusCode));
        console.warn(statusMsg);
    });

```


## 4.2 IOS 配置说明
1. 不需要任何配置
## 4.2.1 IOS React 里的使用方法
```javascript
// TODO: 该传递进入插件的数据，请自行通过自己开发后端api调用返回数据，然后包装成下面的数据格式
import LabidcUpdate from 'react-native-labidc-update';
 let json = {};
    // plist 地址，企业证书地址，或者苹果商店跳转地址
    json.jumpUrl = "http://client.download.qishixingqiu.com/0client/express/1.1.1.apk"; // 跳转地址，必填
    // 通过你们自己的接口地址得到最新版本号, 传入将会和Build版本进行比较
    // 如果versionCode 大于Build大，就会出现弹出更新提示框
    json.versionCode = 3; //版本号，必填
    json.title = "标题"; //标题，选填
    json.message = "弹出框内容"; // 弹出框内容，选填
    json.cancelText = "取消按钮2222文本";// 选填
    json.confirmText = "确认按钮2222文本"; // 选填


    /**
     * statusCode 状态编码 == 0 正确，number类型
     * statusMsg 状态内容 String类型
     */
    LabidcUpdate.check(JSON.stringify(json), (statusCode, statusMsg) => {
        console.warn(statusCode);
        console.warn(typeof (statusCode));
        console.warn(statusMsg);
	});
	
```





  