package com.zyao89.view.zweb.impl;

import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.inter.IZWebMethodInterface;
import com.zyao89.view.zweb.utils.ZLog;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
public class DefaultZWebMethodInterface implements IZWebMethodInterface
{
    @Override
    public void saveData(IZWebHandler zWebHandler, String key, String value)
    {
        ZLog.with(this).d("saveData ==> key: " + key);
    }

    @Override
    public void loadData(IZWebHandler zWebHandler, String key)
    {
        ZLog.with(this).d("loadData ==> key: " + key);
    }

    @Override
    public void showLoading(IZWebHandler zWebHandler)
    {
        ZLog.with(this).d("showLoading...");
    }

    @Override
    public void hideLoading(IZWebHandler zWebHandler)
    {
        ZLog.with(this).d("hideLoading...");
    }

    @Override
    public void tip(IZWebHandler zWebHandler, String msg)
    {
        ZLog.with(this).d("tip ==> msg: " + msg);
    }
}
