package com.zyao89.view.zweb.impl;

import com.zyao89.view.zweb.constants.ZMethodName;
import com.zyao89.view.zweb.inter.IZMethodInterface;
import com.zyao89.view.zweb.inter.IZWeb;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
public class DefaultZMethodInterface implements IZMethodInterface
{
    @Override
    public void onZWebCreated(IZWeb zWeb, int width, int height)
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("Data", "未实现任何接口，无法产生互动操作...");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        zWeb.callReceiver(ZMethodName.ON_READY, jsonObject);
    }

    @Override
    public void onZWebException(IZWeb zWeb, long errorCode, String message)
    {

    }

    @Override
    public void onZWebRequire(IZWeb zWeb, String url, String method, String data, String type, IZRequireController controller)
    {
        controller.result(false);
    }

    @Override
    public void onZWebMessage(IZWeb zWeb, String oJson)
    {

    }

    @Override
    public void onZWebDestroy(IZWeb zWeb)
    {

    }

    @Override
    public void saveData(IZWeb zWeb)
    {

    }

    @Override
    public void loadData(IZWeb zWeb)
    {

    }

    @Override
    public void showLoading(IZWeb zWeb)
    {

    }

    @Override
    public void hideLoading(IZWeb zWeb)
    {

    }

    @Override
    public void tip(IZWeb zWeb, String msg)
    {

    }
}
