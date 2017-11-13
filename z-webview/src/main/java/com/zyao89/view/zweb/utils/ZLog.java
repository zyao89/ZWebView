package com.zyao89.view.zweb.utils;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Zyao89
 * @Created on 2017/11/6.
 */
public class ZLog implements IZLog
{
    private static final String MSG     = "[ZLog]调试信息：\n";
    private static       IZLog  mSingle = null;
    private static       String mTAG    = null;
    private static       STATUS mStatus = STATUS.DEBUG;

    private ZLog()
    {

    }

    /**
     * 设置是否打开日志(默认打开)
     *
     * @param status DEBUG, WARN, ERROR, FULL, NONE
     */
    public static void setStatus(STATUS status)
    {
        mStatus = status;
    }

    public static IZLog with(@NonNull Object tag)
    {
        if (mSingle == null)
        {
            synchronized (ZLog.class)
            {
                if (mSingle == null)
                {
                    mSingle = new ZLog();
                }
            }
        }
        synchronized (ZLog.class)
        {
            mTAG = tag.getClass().getSimpleName();
        }
        return mSingle;
    }

    @Override
    public void d(String msg)
    {
        logHeaderContent(STATUS.DEBUG, msg);
    }

    @Override
    public void e(String msg)
    {
        logHeaderContent(STATUS.ERROR, msg);
    }

    @Override
    public void w(String msg)
    {
        logHeaderContent(STATUS.WARN, msg);
    }

    @Override
    public void z(String msg)
    {
        logChunk(STATUS.DEBUG, msg);
    }

    private void logHeaderContent(STATUS logType, String msg)
    {
        if (mStatus == STATUS.NONE)
        {
            return;
        }

        if (mStatus.ordinal() < logType.ordinal())
        {
            return;
        }

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String level = "  ";

        int stackOffset = getStackOffset(trace);

        int methodCount = MAX_SHOW_LENGTH_NUM;

        StringBuilder builder = new StringBuilder();
        builder.append(msg);//打印信息
        builder.append("\n");
        for (int i = methodCount; i > 0; i--)
        {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length)
            {
                continue;
            }
            builder.append("==>");
            builder.append(level).append(getSimpleClassName(trace[stackIndex].getClassName())).append(".").append(trace[stackIndex].getMethodName()).append(" ").append(" (").append(trace[stackIndex].getFileName()).append(":").append(trace[stackIndex].getLineNumber()).append(")");
            builder.append("\n");
            level += "  ";
        }
        logChunk(logType, builder.toString());
    }

    private void logChunk(STATUS logType, String msg)
    {
        if (mStatus == STATUS.NONE)
        {
            return;
        }

        if (mStatus.ordinal() < logType.ordinal())
        {
            return;
        }

        switch (logType)
        {
            case ERROR:
                Log.e(mTAG, MSG + msg);
                break;
            case WARN:
                Log.w(mTAG, MSG + msg);
                break;
            case DEBUG:
                Log.d(mTAG, MSG + msg);
                break;
            default:
                break;
        }
    }

    private String getSimpleClassName(String name)
    {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private int getStackOffset(StackTraceElement[] trace)
    {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++)
        {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(ZLog.class.getName()) && !name.equals(IZLog.class.getName()))
            {
                return --i;
            }
        }
        return -1;
    }
}
