package com.zyao89.view.zweb.impl;

import android.webkit.WebResourceResponse;

import com.zyao89.view.zweb.constants.InternalConstantName;
import com.zyao89.view.zweb.constants.ZMethodName;
import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.inter.IZWebOnStateListener;
import com.zyao89.view.zweb.utils.ZLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
public class DefaultZWebOnStateListener implements IZWebOnStateListener
{
    @Override
    public void onZWebCreated (IZWebHandler zWebHandler, int width, int height)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put(InternalConstantName.DATA, "未实现任何接口，无法产生互动操作...");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        zWebHandler.callReceiver(ZMethodName.ON_READY, jsonObject);
    }

    @Override
    public void onZWebException (IZWebHandler zWebHandler, long errorCode, String message)
    {

    }

    @Override
    public void onZWebRequire (IZWebHandler zWebHandler, String url, String method, String data, String type, IZRequireController controller)
    {
        controller.result(false);
    }

    @Override
    public void onZWebMessage (IZWebHandler zWebHandler, String cmd, String oJson, IZMessageController controller)
    {
        controller.result(true, "No Data...");
    }

    @Override
    public void onZWebReceivedTitle (String title)
    {

    }

    @Override
    public void onZWebDestroy (IZWebHandler zWebHandler)
    {

    }

    @Override
    public WebResourceResponse onInterceptRequest (String url)
    {
        ZLog.with(this).d("onInterceptRequest: " + url);
        return null;
    }
}
