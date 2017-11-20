package com.zyao89.view.zweb.utils;

/**
 * @author Zyao89
 * 2017/11/13.
 */
public interface IZLog
{
    /**
     * 最大显示数
     */
    int MAX_SHOW_LENGTH_NUM = 3;

    /**
     * The minimum stack trace index_test, starts at this class after two native calls.
     */
    int MIN_STACK_OFFSET = 3;

    /**
     * debug
     *
     * @param msg
     */
    void d(String msg);

    /**
     * error
     *
     * @param msg
     */
    void e(String msg);

    /**
     * warm
     *
     * @param msg
     */
    void w(String msg);

    /**
     * 普通输出
     *
     * @param msg
     */
    void z(String msg);

    enum STATUS
    {
        DEBUG, WARN, ERROR, FULL, NONE;
    }
}
