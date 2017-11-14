package com.zyao89.view.zweb.impl;

import com.zyao89.view.zweb.constants.ZMethodName;
import com.zyao89.view.zweb.inter.IZMethodInterface;
import com.zyao89.view.zweb.inter.IZWebHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
public class DefaultZMethodInterface implements IZMethodInterface
{
    @Override
    public void onZWebCreated(IZWebHandler zWebHandler, int width, int height)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("DATA", "未实现任何接口，无法产生互动操作...");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        zWebHandler.callReceiver(ZMethodName.ON_READY, jsonObject);
    }

    @Override
    public void onZWebException(IZWebHandler zWebHandler, long errorCode, String message)
    {

    }

    @Override
    public void onZWebRequire(IZWebHandler zWebHandler, String url, String method, String data, String type, IZRequireController controller)
    {
        controller.result(false);
    }

    @Override
    public void onZWebMessage(IZWebHandler zWebHandler, String cmd, String oJson, IZMessageController controller)
    {
        controller.result(true);
    }

    @Override
    public void onZWebDestroy(IZWebHandler zWebHandler)
    {

    }

    @Override
    public void saveData(IZWebHandler zWebHandler)
    {

    }

    @Override
    public void loadData(IZWebHandler zWebHandler)
    {

    }

    @Override
    public void showLoading(IZWebHandler zWebHandler)
    {

    }

    @Override
    public void hideLoading(IZWebHandler zWebHandler)
    {

    }

    @Override
    public void tip(IZWebHandler zWebHandler, String msg)
    {

    }
}
