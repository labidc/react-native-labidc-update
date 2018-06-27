
# react-native-labidc-update
# 安卓： 1 --> 2 或者 3 --> 4.1
# IOS： 1 --> 2 或者 3 --> 4.2
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

1. 打开 `AndroidManifest.xml`

2. 在application 节点里添加，  android:value 内容填写你的请求版本信息的地址
`<meta-data android:value=""
            android:name="labidc.update.url"></meta-data>`

   #该url请求必须是一个json的get请求，返回以下json格式
	```
     {
		 "clientName":"express-android",
		 "clientSize":"-",
		 "clientUrl":"http://www.xxx.com/1.1.0.apk",
		 "promote":5,
		 "upgradePrompt":"",
		 "version":5,
		 "versionCode":"v1.1.0"
	}
  	```
   #上面json数据的 version 是检查的数字版本号，该插件会通过该版本号去和 您的app的 `AndroidManifest.xml` 里的android:versionCode="版本号"进行比较，如果大于android:versionCode="版本号", 就会提示更新
   

3. 背景图片的更换：该组件提供了默认背景图片，如果您需要更换图片，请在你的app工程中添加目录
```res/drawable-hdpi``` （有可能目录已存在）然后放入背景图片, `roctet_bg.png`。 注意：图片文件名必须是这个名称。
建议图片文件规格大小：492 * 620

## 4.1.1 Android React 里的使用方法
```javascript
import LabidcUpdate from 'react-native-labidc-update';

// TODO: 触发更新检查
LabidcUpdate.check();
```


## 4.2 IOS 配置说明

1. 使用XCode 打开你的项目，然后添加plist文件到的项目根目录流程：
`File——>New——>File，选择iOS-->Resource-->Property List`
文件完成名称 LabidcUpdate.plist
2. 选择该 LabidcUpdate.plist 文件，在Root下创建两个键值对:
    一个叫url, 类型是String
    一个叫update,类型Boolean

![image](https://github.com/labidc/react-native-labidc-update/blob/master/img/plist.png)
  
3. url 是请求判断是否有更新的接口
   #该url请求必须是一个json的get请求，返回以下json格式
	```
     {
		 "clientName":"express-android",
		 "clientSize":"-",
		 "clientUrl":"http://www.xxx.com/1.1.0.apk",
		 "promote":5,
		 "upgradePrompt":"",
		 "version":5,
		 "versionCode":"v1.1.0"
	}
  	```
#上面json数据的 version 是检查的数字版本号，该插件会通过该版本号去和 您的IOS 项目的 TARGETS 设置里面的 Build 值进行比较，如果大于Build 值, 就会提示更新
4. update 填写YES, 该组件会自动出现选择提示框，用户点击更新按钮会自动跳转到上面json数据格式的 clientUrl 去，适合企业级证书跳转plist自动安装的模式。如果填写NO，该组件不做任何操作，只会通过回调函数的方式返回，true和false，表示是否需要更新，您的js代码得到回调请求之后，自己用RN编写需要更新的业务逻辑与UI交互。

![image](https://github.com/labidc/react-native-labidc-update/blob/master/img/setting.png)

## 4.2.1 IOS React 里的使用方法
```javascript
import LabidcUpdate from 'react-native-labidc-update';
  LabidcUpdate.check((error, events) => {
	    // events 只会返回true或者false，表示是否需要更新。
        console.info(events);
    });
```





  