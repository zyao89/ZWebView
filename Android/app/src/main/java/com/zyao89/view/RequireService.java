package com.zyao89.view;

import com.zyao89.view.zweb.annotations.ZFunction;
import com.zyao89.view.zweb.annotations.ZKey;
import com.zyao89.view.zweb.annotations.ZMethod;
import com.zyao89.view.zweb.constants.ZFunctionName;
import com.zyao89.view.zweb.constants.ZMethodName;

/**
 * @author Zyao89
 * 2017/11/8.
 */
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

    @ZFunction(ZFunctionName.GO_BACK)
    void goBack();

    @ZFunction(ZFunctionName.GO_FORWARD)
    void goForward();
}
