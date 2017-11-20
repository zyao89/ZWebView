package com.zyao89.view.zweb.impl;

import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.inter.IZWebMethodInterface;
import com.zyao89.view.zweb.utils.ZLog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zyao89
 * 2017/11/8.
 */
public class DefaultZWebMethodInterface implements IZWebMethodInterface
{
    private final Map<String, String> mDatabase = new ConcurrentHashMap<>();

    @Override
    public void saveData (IZWebHandler zWebHandler, String key, String value, IZDatabaseController zController)
    {
        String result = mDatabase.put(key, value);
        if (result == null)
        {
            zController.result(false);
        }
        else
        {
            zController.result(true, result);
        }
    }

    @Override
    public void loadData (IZWebHandler zWebHandler, String key, IZDatabaseController zController)
    {
        String result = mDatabase.get(key);
        if (result == null)
        {
            zController.result(false);
        }
        else
        {
            zController.result(true, result);
        }
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
