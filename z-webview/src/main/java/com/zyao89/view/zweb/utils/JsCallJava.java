package com.zyao89.view.zweb.utils;

import android.text.TextUtils;

import com.zyao89.view.zweb.views.WebViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */
public class JsCallJava
{
    private final static String   RETURN_RESULT_FORMAT  = "{\"CODE\": %d, \"result\": %s}";
    private static final String   MSG_PROMPT_HEADER     = "ZWeb:";
    private static final String   KEY_OBJ               = "obj";
    private static final String   KEY_METHOD            = "method";
    private static final String   KEY_TYPES             = "types";
    private static final String   KEY_ARGS              = "args";
    private static final String[] IGNORE_UNSAFE_METHODS = {"getClass", "hashCode", "notify", "notifyAll", "equals", "toString", "wait"};
    private HashMap<String, Method> mMethodsMap;
    private Object                  mInterfaceObj;
    private String                  mInterfacedName;
    private String                  mPreloadInterfaceJS;

    public JsCallJava(Object interfaceObj, String interfaceName)
    {
        try
        {
            if (TextUtils.isEmpty(interfaceName))
            {
                throw new Exception("injected name can not be null");
            }
            mInterfaceObj = interfaceObj;
            mInterfacedName = interfaceName;
            mMethodsMap = new HashMap<String, Method>();
            // getMethods会获得所有继承与非继承的方法
            Method[] methods = mInterfaceObj.getClass().getMethods();
            // 拼接的js脚本可参照备份文件：./library/doc/injected.js
            StringBuilder sb = new StringBuilder("javascript:(function(b){console.log(\"");
            sb.append(mInterfacedName);
            sb.append(" init begin\");var a={queue:[],callback:function(){var d=Array.prototype.slice.call(arguments,0);var c=d.shift();var e=d.shift();this.queue[c].apply(this,d);if(!e){delete this.queue[c]}}};");
            for (Method method : methods)
            {
                ZLog.with(this).z("method:" + method);
                String sign;
                if ((sign = genJavaMethodSign(method)) == null)
                {
                    continue;
                }
                mMethodsMap.put(sign, method);
                sb.append(String.format("a.%s=", method.getName()));
            }
            sb.append("function(){var f=Array.prototype.slice.call(arguments,0);if(f.length<1){throw\"");
            sb.append(mInterfacedName);
            sb.append(" call error, message:miss method name\"}var e=[];for(var h=1;h<f.length;h++){var c=f[h];var j=typeof c;e[e.length]=j;if(j==\"function\"){var d=a.queue.length;a.queue[d]=c;f[h]=d}}var k = new Date().getTime();var l = f.shift();var m=prompt('");
            sb.append(MSG_PROMPT_HEADER);
            sb.append("'+JSON.stringify(");
            sb.append(promptMsgFormat("'" + mInterfacedName + "'", "l", "e", "f"));
            sb.append("));console.log(\"invoke \"+l+\", time: \"+(new Date().getTime()-k));var g=JSON.parse(m);if(g.CODE!=200){throw\"");
            sb.append(mInterfacedName);
            sb.append(" call error, CODE:\"+g.CODE+\", message:\"+g.result}return g.result};Object.getOwnPropertyNames(a).forEach(function(d){var c=a[d];if(typeof c===\"function\"&&d!==\"callback\"){a[d]=function(){return c.apply(a,[d].concat(Array.prototype.slice.call(arguments,0)))}}});b.");
            sb.append(mInterfacedName);
            sb.append("=a;console.log(\"");
            sb.append(mInterfacedName);
            sb.append(" init end\")})(window)");
            mPreloadInterfaceJS = sb.toString();
            sb.setLength(0);
        }
        catch (Exception e)
        {
            ZLog.with(this).e("init js error:" + e.getMessage());
        }
    }

    private String genJavaMethodSign(Method method)
    {
        StringBuilder sign = new StringBuilder(method.getName());
        Class[] argsTypes = method.getParameterTypes();
        for (String ignoreMethod : IGNORE_UNSAFE_METHODS)
        {
            if (ignoreMethod.equals(sign.toString()))
            {
                ZLog.with(this).w("method(" + sign + ") is unsafe, will be pass");
                return null;
            }
        }
        int len = argsTypes.length;
        for (int k = 0; k < len; k++)
        {
            Class cls = argsTypes[k];
            if (cls == String.class)
            {
                sign.append("_S");
            }
            else if (cls == int.class || cls == long.class || cls == float.class || cls == double.class)
            {
                sign.append("_N");
            }
            else if (cls == boolean.class)
            {
                sign.append("_B");
            }
            else if (cls == JSONObject.class)
            {
                sign.append("_O");
            }
            else if (cls == JsCallback.class)
            {
                sign.append("_F");
            }
            else
            {
                sign.append("_P");
            }
        }
        return sign.toString();
    }

    public String getPreloadInterfaceJS()
    {
        return mPreloadInterfaceJS;
    }

    public String call(WebViewEx webView, JSONObject jsonObject)
    {
        long time = android.os.SystemClock.uptimeMillis();
        if (jsonObject != null)
        {
            try
            {
                String methodName = jsonObject.getString(KEY_METHOD);
                JSONArray argsTypes = jsonObject.getJSONArray(KEY_TYPES);
                JSONArray argsVals = jsonObject.getJSONArray(KEY_ARGS);
                StringBuilder sign = new StringBuilder(methodName);
                int len = argsTypes.length();
                Object[] values = new Object[len];
                int numIndex = 0;
                String currType;

                for (int k = 0; k < len; k++)
                {
                    currType = argsTypes.optString(k);
                    if ("string".equals(currType))
                    {
                        sign.append("_S");
                        values[k] = argsVals.isNull(k) ? null : argsVals.getString(k);
                    }
                    else if ("number".equals(currType))
                    {
                        sign.append("_N");
                        numIndex = numIndex * 10 + k + 1;
                    }
                    else if ("boolean".equals(currType))
                    {
                        sign.append("_B");
                        values[k] = argsVals.getBoolean(k);
                    }
                    else if ("object".equals(currType))
                    {
                        sign.append("_O");
                        values[k] = argsVals.isNull(k) ? null : argsVals.getJSONObject(k);
                    }
                    else if ("function".equals(currType))
                    {
                        sign.append("_F");
                        values[k] = new JsCallback(webView, mInterfacedName, argsVals.getInt(k));
                    }
                    else
                    {
                        sign.append("_P");
                    }
                }

                Method currMethod = mMethodsMap.get(sign.toString());

                // 方法匹配失败
                if (currMethod == null)
                {
                    return getReturn(jsonObject, 500, "not found method(" + sign + ") with valid parameters", time);
                }
                // 数字类型细分匹配
                if (numIndex > 0)
                {
                    Class[] methodTypes = currMethod.getParameterTypes();
                    int currIndex;
                    Class currCls;
                    while (numIndex > 0)
                    {
                        currIndex = numIndex - numIndex / 10 * 10 - 1;
                        currCls = methodTypes[currIndex];
                        if (currCls == int.class)
                        {
                            values[currIndex] = argsVals.getInt(currIndex);
                        }
                        else if (currCls == long.class)
                        {
                            //WARN: argsJson.getLong(k + defValue) will return a bigger incorrect number
                            values[currIndex] = Long.parseLong(argsVals.getString(currIndex));
                        }
                        else
                        {
                            values[currIndex] = argsVals.getDouble(currIndex);
                        }
                        numIndex /= 10;
                    }
                }

                return getReturn(jsonObject, 200, currMethod.invoke(mInterfaceObj, values), time);
            }
            catch (Exception e)
            {
                ZLog.with(this).e("call:" + e.getMessage());
                //优先返回详细的错误信息
                if (e.getCause() != null)
                {
                    return getReturn(jsonObject, 500, "method execute error:" + e.getCause().getMessage(), time);
                }
                return getReturn(jsonObject, 500, "method execute error:" + e.getMessage(), time);
            }
        }
        else
        {
            return getReturn(null, 500, "call data empty", time);
        }
    }

    private String getReturn(JSONObject reqJson, int stateCode, Object result, long time)
    {
        String insertRes;
        if (result == null)
        {
            insertRes = "null";
        }
        else if (result instanceof String)
        {
            result = ((String) result).replace("\"", "\\\"");
            insertRes = "\"".concat(String.valueOf(result)).concat("\"");
        }
        else
        { // 其他类型直接转换
            insertRes = String.valueOf(result);

            // 兼容：如果在解决WebView注入安全漏洞时，js注入采用的是XXX:function(){return prompt(...)}的形式，函数返回类型包括：void、int、boolean、String；
            // 在返回给网页（onJsPrompt方法中jsPromptResult.confirm）的时候强制返回的是String类型，所以在此将result的值加双引号兼容一下；
            // insertRes = "\"".concat(String.valueOf(result)).concat("\"");
        }
        String resStr = String.format(Locale.getDefault(), RETURN_RESULT_FORMAT, stateCode, insertRes);
        ZLog.with(this).d("call time: " + (android.os.SystemClock.uptimeMillis() - time) + ", request: " + reqJson + ", result:" + resStr);
        return resStr;
    }

    private static String promptMsgFormat(String object, String method, String types, String args)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(KEY_OBJ).append(":").append(object).append(",");
        sb.append(KEY_METHOD).append(":").append(method).append(",");
        sb.append(KEY_TYPES).append(":").append(types).append(",");
        sb.append(KEY_ARGS).append(":").append(args);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 是否是“Java接口类中方法调用”的内部消息；
     *
     * @param message
     * @return
     */
    public static boolean isSafeWebViewCallMsg(String message)
    {
        return message.startsWith(MSG_PROMPT_HEADER);
    }

    public static JSONObject getMsgJSONObject(String message)
    {
        message = message.substring(MSG_PROMPT_HEADER.length());
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(message);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    public static String getInterfacedName(JSONObject jsonObject)
    {
        return jsonObject.optString(KEY_OBJ);
    }
}
