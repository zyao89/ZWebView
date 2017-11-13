package com.zyao89.view.zweb.inter;

import org.json.JSONObject;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */
public interface IZWeb
{
    /**
     * UUID
     *
     * @return
     */
    String getFrameworkUUID();

    /**
     * 执行JS
     *
     * @param function function string name
     * @param json     Object
     * @return
     */
    boolean execJS(String function, JSONObject json);

    /**
     * 通知接收人
     *
     * @param method method string name
     * @param json   Object
     * @return
     */
    boolean callReceiver(String method, JSONObject json);
}
