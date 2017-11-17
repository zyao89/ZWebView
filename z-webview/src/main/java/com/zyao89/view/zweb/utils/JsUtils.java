package com.zyao89.view.zweb.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RawRes;
import android.text.TextUtils;

import com.zyao89.view.zweb.exceptions.ZWebException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public static JSONObject json2Obj (String json)
    {
        try
        {
            return new JSONObject(json);
        }
        catch (JSONException e)
        {
            throw new ZWebException("Json parse error...");
        }
    }

    public static String assetFile2Str (Context c, String urlStr)
    {
        InputStream in = null;
        try
        {
            in = c.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder sb = new StringBuilder();
            do
            {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*"))
                {
                    sb.append(line).append('\n');
                }
            } while (line != null);

            bufferedReader.close();
            in.close();

            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return null;
    }

    public static String rawFile2Str (Context c, @RawRes int rawID)
    {
        InputStream in = null;
        try
        {
            in = c.getResources().openRawResource(rawID);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder sb = new StringBuilder();
            do
            {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*"))
                {
                    sb.append(line).append('\n');
                }
            } while (line != null);

            bufferedReader.close();
            in.close();

            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return null;
    }

    /**
     * js 文件将注入为第一个script引用
     *
     * @param url
     */
    public static String webViewLoadJs (String url)
    {
        String js = "var newScript = document.createElement(\"script\");";
        js += "newScript.src=\"" + url + "\";";
        js += "document.scripts[0].parentNode.insertBefore(newScript,document.scripts[0]);";
        return js;
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
