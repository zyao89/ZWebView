package com.zyao89.view.zweb.constants;

/**
 * Config的注入方式
 * <p>
 * Created by zyao89 on 2017/11/19.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: https://zyao89.cn
 */
public enum InjectionMode
{
    // vue插件方式，引入  vue-zweb.js  例如：<script type="text/javascript" src="static/libs/vue-zweb.min.js"></script>
    VuePlugin,
    // 协议方式：zweb://  例如：<script type="text/javascript" src="zweb://__init__"></script>
    Protocol,
    // 普通人工引入 <script type="text/javascript" src="static/zweb.js"></script>
    Normal
}
