package com.zyao89.view.zweb.utils;

import android.os.Build;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */

public class JsUtils
{
    /**
     * 构建一个“不会重复注入”的js脚本；
     *
     * @param key
     * @param js
     * @return
     */
    public static String buildNotRepeatInjectJS(String key, String js)
    {
        String obj = String.format("__injectFlag_%1$s__", key);
        final StringBuilder sb = new StringBuilder();
        sb.append("javascript:try{(function(){if(window.");
        sb.append(obj);
        sb.append("){console.log('");
        sb.append(obj);
        sb.append(" has been injected');return;}window.");
        sb.append(obj);
        sb.append("=true;");
        sb.append(js);
        sb.append("}())}catch(e){console.warn(e)}");
        return sb.toString();
    }

    /**
     * 构建一个“带try catch”的js脚本；
     *
     * @param js
     * @return
     */
    public static String buildTryCatchInjectJS(String js)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:try{");
        sb.append(js);
        sb.append("}catch(e){console.warn(e)}");
        return sb.toString();
    }

    public static boolean isJson(String target)
    {
        if (TextUtils.isEmpty(target))
        {
            return false;
        }
        boolean tag = false;
        try
        {
            if (target.startsWith("["))
            {
                new JSONArray(target);
            }
            else
            {
                new JSONObject(target);
            }

            tag = true;
        }
        catch (JSONException ignore)
        {
            tag = false;
        }
        return tag;
    }

    /**
     * 不支持版本
     *
     * @return
     */
    public static boolean notSupportInterface()
    {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
