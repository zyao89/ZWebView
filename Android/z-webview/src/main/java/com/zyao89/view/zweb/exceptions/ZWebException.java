package com.zyao89.view.zweb.exceptions;

/**
 * @author Zyao89
 * 2017/11/6.
 */
public class ZWebException extends RuntimeException
{
    public ZWebException()
    {
    }

    public ZWebException(String message)
    {
        super(message);
    }

    public ZWebException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ZWebException(Throwable cause)
    {
        super(cause);
    }
}
