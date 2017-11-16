package com.zyao89.view.zweb.inter;

/**
 * Native View 协议
 *
 * @author Zyao89
 * @date 2017/11/7.
 */
public interface IZWebMethodInterface
{
    /**
     * 存数数据
     *
     * @param zWebHandler
     * @param key
     * @param value
     */
    void saveData(IZWebHandler zWebHandler, String key, String value);

    /**
     * 从数据库读取数据
     *
     * @param zWebHandler
     * @param key
     */
    void loadData(IZWebHandler zWebHandler, String key);

    /**
     * 显示等待框
     *
     * @param zWebHandler
     */
    void showLoading(IZWebHandler zWebHandler);

    /**
     * 隐藏等待框
     *
     * @param zWebHandler
     */
    void hideLoading(IZWebHandler zWebHandler);

    /**
     * 提示
     *
     * @param zWebHandler
     * @param msg
     */
    void tip(IZWebHandler zWebHandler, String msg);
}
