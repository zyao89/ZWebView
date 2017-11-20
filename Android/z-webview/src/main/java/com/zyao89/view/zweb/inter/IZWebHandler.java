package com.zyao89.view.zweb.inter;

/**
 * @author Zyao89
 * 2017/11/13.
 */
public interface IZWebHandler extends IZWeb
{
    void quickCallJs(String method, String... params);

    void quickCallJs(String method);
}
