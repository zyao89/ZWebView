# ZWebView

ZWebView for Hybird App，建立移动端和Web的JS桥接框架，主要包含了多种常用协议的约束和定义。Android的WebView使用更方便。

## Web

#### 分为三种方式

1. Vue插件注入方式

使用`Vue`时，可导入`vue-zweb`插件使用。

具体参考 [vue-zweb](https://github.com/zyao89/vue-zweb)

2. H5标签协议注入方式

可通过在html文件中引入 `<script>` 标签，控制引入框架的位置。

协议在 `src` 下，以地址方式注入，具体格式采用：`zweb://` 开头。

目前支持的协议如下：
```
zweb://__init__
```

具体引入方式如下：
```html
<script type="text/javascript" src="zweb://__init__"></script>
```

3. 普通手动注入方式 

## Android

#### 安卓也对应采用三种交互方式
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

使用方式如下：
```java
String MAIN_HTML = "file:///android_asset/index.html";

// 1. 创建配置文件，并带入主页 URL
ZWebConfig config = new ZWebConfig.Builder(ZWebConstant.MAIN_HTML_TEST)
    // 注册状态监听
    .setOnStateListener(this)
    // 注册原生协议UI实现
    .setNativeMethodImplement(this)
    // 注册一些特殊的实现方法
//  .setOnSpecialStateListener(this)
    // 选择注入模式
    .setInjectionMode(InjectionMode.VuePlugin)
    // 自动注入框架脚本JS（建议配置）
    .autoInjectFramework()
    // 自动注入扩展方法
    .autoInjectExtendsJS()
    // 添加 assets 中js文件注入
//  .addInjectJSAssetsFile("js/test.js")
    // 添加 raw 中js文件注入
//  .addInjectJSRawFile(R.raw.index_test)
    .build();

// 2. 创建ZWeb对象实例.
mZWebInstance = ZWebInstance.createInstance(config);

```

## iOS

正在筹划中...

## Author Blog

[zyao89.cn](https://zyao89.cn)

## License

[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2017 Zyao89