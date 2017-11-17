package com.zyao89.view;

import com.zyao89.view.zweb.annotations.ZCmd;
import com.zyao89.view.zweb.inter.IZWebMessageController;
import com.zyao89.view.zweb.utils.JsUtils;

/**
 * Created by zyao89 on 2017/11/15.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: https://zyao89.cn
 */
public class ParseMessage
{
//    private String CMD(String data)
//    {
//        return "我是返回结果。。。";
//    }

//    private void CMD(String data)
//    {
//
//    }

    private void CMD(String data, IZWebMessageController controller)
    {
        System.out.println(data);
        controller.result(true, "我是返回结果1。。。");
    }

    @ZCmd("InitParam")
    private void initParam (String data, IZWebMessageController controller)
    {
        System.out.println(data);
        String param = "{\n" + "    title: {\n" + "        text: 'ECharts 入门示例'\n" + "    },\n" + "    tooltip: {},\n" + "    xAxis: {\n" + "        data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']\n" + "    },\n" + "    yAxis: {},\n" + "    series: [{\n" + "        name: '销量',\n" + "        type: 'bar',\n" + "        data: [5, 20, 36, 10, 10, 20]\n" + "    }]\n" + "}";
        controller.result(true, JsUtils.json2Obj(param));
    }

}
