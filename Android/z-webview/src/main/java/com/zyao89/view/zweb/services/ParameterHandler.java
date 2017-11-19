package com.zyao89.view.zweb.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */

public class ParameterHandler
{
    private final Annotation annotation;
    private final Type       parameterType;
    private final String     parameterName;

    public ParameterHandler(Annotation annotation, Type parameterType, String parameterName)
    {
        this.annotation = annotation;
        this.parameterType = parameterType;
        this.parameterName = parameterName;
    }

    public Annotation getAnnotation()
    {
        return annotation;
    }

    public Type getParameterType()
    {
        return parameterType;
    }

    public String getParameterName()
    {
        return parameterName;
    }
}
