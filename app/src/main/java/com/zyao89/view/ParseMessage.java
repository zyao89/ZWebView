package com.zyao89.view;

import com.zyao89.view.zweb.annotations.ZCmd;
import com.zyao89.view.zweb.inter.IZWebMessageController;

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

    private void CMD (String data, IZWebMessageController controller)
    {
        System.out.println(data);
        controller.result(true, "我是返回结果1。。。");
    }

    @ZCmd("CMD")
    private void CMD1 (String data, IZWebMessageController controller)
    {
        System.out.println(data);
        controller.result(true, "我是返回结果2。。。");
    }

}
