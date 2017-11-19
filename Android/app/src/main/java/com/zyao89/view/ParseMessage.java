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

    @ZCmd("Chart1")
    private void chart_1(String data, IZWebMessageController controller)
    {
        System.out.println(data);
        String param = "{\n" +
                "    backgroundColor: '#2c343c',\n" +
                "    visualMap: {\n" +
                "        show: false,\n" +
                "        min: 80,\n" +
                "        max: 600,\n" +
                "        inRange: {\n" +
                "            colorLightness: [0, 1]\n" +
                "        }\n" +
                "    },\n" +
                "    series : [\n" +
                "        {\n" +
                "            name: '访问来源',\n" +
                "            type: 'pie',\n" +
                "            radius: '55%',\n" +
                "            data:[\n" +
                "                {value:235, name:'视频广告'},\n" +
                "                {value:274, name:'联盟广告'},\n" +
                "                {value:310, name:'邮件营销'},\n" +
                "                {value:335, name:'直接访问'},\n" +
                "                {value:400, name:'搜索引擎'}\n" +
                "            ],\n" +
                "            roseType: 'angle',\n" +
                "            label: {\n" +
                "                normal: {\n" +
                "                    textStyle: {\n" +
                "                        color: 'rgba(255, 255, 255, 0.3)'\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            labelLine: {\n" +
                "                normal: {\n" +
                "                    lineStyle: {\n" +
                "                        color: 'rgba(255, 255, 255, 0.3)'\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            itemStyle: {\n" +
                "                normal: {\n" +
                "                    color: '#c23531',\n" +
                "                    shadowBlur: 200,\n" +
                "                    shadowColor: 'rgba(0, 0, 0, 0.5)'\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "};";
        controller.result(true, JsUtils.json2Obj(param));
    }

    @ZCmd("Chart2")
    private void chart_2(String data, IZWebMessageController controller)
    {
        System.out.println(data);
        String param = "{\n" + "    title: {\n" + "        text: 'ECharts 入门示例'\n" + "    },\n" + "    tooltip: {},\n" + "    xAxis: {\n" + "        data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']\n" + "    },\n" + "    yAxis: {},\n" + "    series: [{\n" + "        name: '销量',\n" + "        type: 'bar',\n" + "        data: [5, 20, 36, 10, 10, 20]\n" + "    }]\n" + "}";
        controller.result(true, JsUtils.json2Obj(param));
    }

}
