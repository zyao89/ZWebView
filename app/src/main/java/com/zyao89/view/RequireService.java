package com.zyao89.view;

import com.zyao89.view.zweb.annotations.ZFunction;
import com.zyao89.view.zweb.annotations.ZKey;
import com.zyao89.view.zweb.annotations.ZMethod;
import com.zyao89.view.zweb.constants.ZFunctionName;
import com.zyao89.view.zweb.constants.ZMethodName;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
public interface RequireService
{
    @ZFunction("init")
    boolean a(@ZKey("A") String a, @ZKey("B") String b, @ZKey("C") int c);

    @ZMethod(ZMethodName.ON_READY)
    void initParam(@ZKey("Msg") String msg, @ZKey("Skin") int skin, @ZKey("Color") int color);

    @ZFunction(ZFunctionName.GO_BACK)
    void goBack();

    @ZFunction(ZFunctionName.GO_FORWARD)
    void goForward();

    @ZFunction(ZFunctionName.REFRESH)
    void refresh();
}
