package com.zyao89.view.zweb;

import android.support.annotation.NonNull;

import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.inter.IZWeb;
import com.zyao89.view.zweb.inter.IZWebHandler;

import org.json.JSONObject;

/**
 * Created by zyao89 on 2017/11/14.
 * Contact me at 305161066@qq.com or zyao89@gmail.com
 * For more projects: https://github.com/zyao89
 * My Blog: https://zyao89.cn
 */
public class ZWebHandler implements IZWebHandler
{
    private IZWeb mZWeb;

    public void setZWeb(IZWeb zWeb)
    {
        mZWeb = zWeb;
    }

    @Override
    public String getFrameworkUUID()
    {
        if (mZWeb == null)
        {
            throw new ZWebException("First call onActivityCreate(), Please...");
        }
        return mZWeb.getFrameworkUUID();
    }

    @Override
    public final boolean execJS(@NonNull String function, JSONObject jsonObject)
    {
        if (mZWeb == null)
        {
            throw new ZWebException("First call onActivityCreate(), Please...");
        }
        return mZWeb.execJS(function, jsonObject);
    }

    @Override
    public final boolean callReceiver(@NonNull String method, JSONObject jsonObject)
    {
        if (mZWeb == null)
        {
            throw new ZWebException("First call onActivityCreate(), Please...");
        }
        return mZWeb.callReceiver(method, jsonObject);
    }
}
