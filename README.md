# ZWebView

ZWebView for Hybird App，建立移动端和Web的JS桥接框架，主要包含了多种常用协议的约束和定义。Android的WebView使用更方便。

## Web

### 分为三种方式

1. Vue插件注入方式

使用`Vue`时，可导入`vue-zweb`插件使用。具体参考 [vue-zweb](https://github.com/zyao89/vue-zweb)

2. H5标签协议注入方式

可通过在html文件中引入 `<script>` 标签，控制引入框架的位置。

协议在标签的 `src` 下，以地址方式注入，具体格式采用：`zweb://` 开头。

目前支持的协议如下：
```
1. zweb://__init__
```

具体引入方式如下：
```html
<script type="text/javascript" src="zweb://__init__"></script>
```

3. 普通手动注入方式 

## Android

### 引入方式：

Gradle > v3.0

```gradle
implementation 'com.zyao89.view:zweb:1.0.0'
```


Gradle < v3.0

```gradle
compile 'com.zyao89.view:zweb:1.0.0'
```

### 安卓也对应采用三种交互方式
对应的3中枚举类型如下：
```java
public enum InjectionMode
{
    // vue插件方式，引入  vue-zweb.js  例如：<script type="text/javascript" src="static/libs/vue-zweb.min.js"></script>
    VuePlugin,
    // 协议方式：zweb://  例如：<script type="text/javascript" src="zweb://__init__"></script>
    Protocol,
    // 普通人工引入 <script type="text/javascript" src="static/zweb.js"></script>
    Normal
}
```

### 基本使用方式：

* Step1，初始化：
在自定义的Application中初始化 `ZWebView`，方法如下：
```java
@Override
public void onCreate()
{
    super.onCreate();

    ZWebInstance.init(this);
}
```

* Step2，构建 `ZWebConfig` 配置文件，并创建 `ZWeb` 对象实例。
```java
String MAIN_HTML = "file:///android_asset/index.html";

// 1. 创建配置文件，并带入主页 URL
ZWebConfig config = new ZWebConfig.Builder(ZWebConstant.MAIN_HTML_TEST)
    // 注册状态监听
    .setOnStateListener(this)
    // 注册原生协议UI实现
    .setNativeMethodImplement(this)
    // 注册一些特殊的实现方法
//  .setOnSpecialStateListener(this)
    // 选择注入模式
    .setInjectionMode(InjectionMode.VuePlugin)
    // 自动注入框架脚本JS（建议配置）
    .autoInjectFramework()
    // 自动注入扩展方法
    .autoInjectExtendsJS()
    // 添加 assets 中js文件注入
//  .addInjectJSAssetsFile("js/test.js")
    // 添加 raw 中js文件注入
//  .addInjectJSRawFile(R.raw.index_test)
    .build();

// 2. 创建ZWeb对象实例.
mZWebInstance = ZWebInstance.createInstance(config);

```

* Step3，传递Activity的生命周期，如下：
```java
// mRootView 为根ViewGroup容器 （必须）
@Override
protected void onCreate(Bundle savedInstanceState)
{
    //...
    mZWebInstance.onActivityCreate(mRootView);
    //...
}

// 分别实现其他生命周期传递
@Override
public void onBackPressed()
{
    if (!mZWebInstance.onActivityBack())
    {
        super.onBackPressed();
    }
}

@Override
protected void onPause()
{
    mZWebInstance.onActivityPause();
    super.onPause();
}

@Override
protected void onResume()
{
    mZWebInstance.onActivityResume();
    super.onResume();
}

@Override
protected void onStart()
{
    mZWebInstance.onActivityStart();
    super.onStart();
}

@Override
protected void onStop()
{
    mZWebInstance.onActivityStop();
    super.onStop();
}

@Override
protected void onDestroy()
{
    mZWebInstance.onActivityDestroy();
    super.onDestroy();
}
```

### 一些内部状态监听实现

以下为实现监听状态接口时，所需要实现的方法。

```java
/**
* 框架初始化成功后回调
*
* @param zWebHandler
* @param width 宽
* @param height 高
*/
void onZWebCreated(IZWebHandler zWebHandler, int width, int height);

/**
* 异常（JS如果有异常会在这里回调）
*
* @param zWebHandler
* @param errorCode 错误码
* @param message 错误信息
*/
void onZWebException(IZWebHandler zWebHandler, long errorCode, String message);

/**
* 网络请求处理
*
* @param zWebHandler
* @param url         链接
* @param method      请求方法
* @param data        数据
* @param type        返回类型
* @param controller
*/
void onZWebRequire(IZWebHandler zWebHandler, String url, String method, String data, String type, IZRequireController controller);

/**
* 异步消息请求
*
* @param zWebHandler
* @param cmd 命令名称
* @param data 数据
*/
void onZWebMessage(IZWebHandler zWebHandler, String cmd, String data, IZMessageController controller);

/**
* JS销毁
*
* @param zWebHandler
*/
void onZWebDestroy(IZWebHandler zWebHandler);

/**
* JS日志信息回调，可在这里记录或打印
*
* @param zWebHandler
* @param type        类型
* @param msg         信息
*/
void onZWebLog(IZWebHandler zWebHandler, String type, String msg);
```

以上接口部分实现方式可参考 [MainActivity.java](https://github.com/zyao89/ZWebView/blob/master/Android/app/src/main/java/com/zyao89/view/MainActivity.java)

### IOC实现 js 请求方法封装

1. 定义调用JS的方法接口
```java
public interface RequireService
{
    @ZMethod("a") // callReceiver
    boolean callA(@ZKey("KeyA") String a, @ZKey("KeyB") String b, @ZKey("Time") long time);

    @ZFunction("init")
    boolean init(@ZKey("A") String a, @ZKey("B") String b, @ZKey("C") int c);

    @ZMethod(ZMethodName.ON_READY)
    void initParam(@ZKey("Msg") String msg, @ZKey("Skin") int skin, @ZKey("Color") int color);

    @ZFunction(ZFunctionName.REFRESH)
    void refresh();
}
```

**各种注解介绍：**
> @ZFunction("方法名称")： 用于调用JS中 `ZWebSDK.extends('方法名称', function(oData){});` 扩展方法的调用。

> @ZMethod("监听名称")：用于JS中 `ZWebSDK.on('监听名称', function(oData){});` 注册监听的方法调用。

> @ZKey("参数名称")：JS中Object参数键值名称。

> @ZCmd("cmd名称")：用于 `onZWebMessage` 方法回调中，cmd参数的映射。具体使用方法可以参考Demo。（针对Message做映射解析时使用）。

2. 创建接口服务对象实例，并进行调用
```java
mRequireService = mZWebInstance.create(RequireService.class);
```

通过使用 `mRequireService` 提供的方法，调用JS。如下：

```java
mRequireService.callA("我是一个坚挺的消息。。。", "小A你好啊！", time);
```

### JS调用原生提供的方法

1. 使用现有的 `void onZWebMessage(IZWebHandler zWebHandler, String cmd, String data, IZMessageController controller);` 进行方法解析，并异步返回结果。

    参数 `cmd` ：定义的命令名称。

    参数 `data `： 定义的参数。

    `controller` ：可进行异步的结果返回。或者利用 `controller.parseMessage(this.mParseMessage);` 此方法进行方法解析。

2. 使用原生 `@JavascriptInterface` 注解的方法进行扩展。

    框架在 ZWebConfig 中提供了 `setExposedName (String exposedName);` 方法，可以扩展一个原生的接口协议名称。

    然后，利用 ` mZWebInstance.addJavascriptInterface(object);` 进行对象中的方法注入。

## iOS

正在筹划中...

## Author Blog

[zyao89.cn](https://zyao89.cn)

## License

    Copyright 2017 Zyao89

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
