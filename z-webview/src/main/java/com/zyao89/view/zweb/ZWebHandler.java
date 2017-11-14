package com.zyao89.view.zweb;

import android.support.annotation.NonNull;

import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.inter.IZWeb;
import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.utils.JsUtils;

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

    /*package*/ void setZWeb(IZWeb zWeb)
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
    public boolean callJS(String js)
    {
        if (mZWeb == null)
        {
            throw new ZWebException("First call onActivityCreate(), Please...");
        }
        return mZWeb.callJS(js);
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

    @Override
    public void quickCallJs(String method, String... params)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:").append(method);
        if (params == null || params.length == 0)
        {
            sb.append("()");
        }
        else
        {
            sb.append("(").append(concat(params)).append(")");
        }
        this.callJS(sb.toString());
    }

    @Override
    public void quickCallJs(String method)
    {
        this.quickCallJs(method, (String[]) null);
    }

    private String concat(String... params)
    {
        StringBuilder mStringBuilder = new StringBuilder();
        for (int i = 0; i < params.length; i++)
        {
            String param = params[i];
            if (!JsUtils.isJson(param))
            {

                mStringBuilder.append("\"").append(param).append("\"");
            }
            else
            {
                mStringBuilder.append(param);
            }
            if (i != params.length - 1)
            {
                mStringBuilder.append(" , ");
            }
        }
        return mStringBuilder.toString();
    }
}
