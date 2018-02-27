package com.zyao89.view.zweb.services;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zyao89.view.zweb.annotations.ZFunction;
import com.zyao89.view.zweb.annotations.ZJson;
import com.zyao89.view.zweb.annotations.ZKey;
import com.zyao89.view.zweb.annotations.ZMethod;
import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.inter.IZWeb;
import com.zyao89.view.zweb.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author Zyao89
 *         2017/11/13.
 */
class ServiceMethod<R, T>
{
    private final IZWeb              mZWebInstance;
    private final ParameterHandler[] mParameterHandlers;

    private final TYPE   type;
    private final String functionName;
    private final String methodName;

    ServiceMethod(Builder<R, T> builder)
    {
        this.mZWebInstance = builder.zWebInstance;
        this.mParameterHandlers = builder.parameterHandlers;

        this.type = builder.type;
        this.functionName = builder.functionName;
        this.methodName = builder.methodName;
    }

    boolean toRequest(@Nullable Object... args)
    {
        JSONObject szArgs = null;
        if (args != null && args.length > 0)
        {
            ParameterHandler parameterHandler = this.mParameterHandlers[0];
            if (args.length == 1 && parameterHandler.getAnnotation() instanceof ZJson)
            {
                try
                {
                    String parameterName = parameterHandler.getParameterName();
                    szArgs = new JSONObject(args);
                }
                catch (JSONException e)
                {
                    throw new ZWebException("ZJson 参数转化失败了...", e);
                }
            }
            else
            {
                try
                {
                    szArgs = new JSONObject();
                    for (int i = 0; i < args.length; i++)
                    {
                        parameterHandler = this.mParameterHandlers[i];
                        String parameterName = parameterHandler.getParameterName();
                        szArgs.put(parameterName, args[i]);
                    }
                }
                catch (JSONException e)
                {
                    throw new ZWebException("ZKey 参数转化失败了...", e);
                }
            }
        }
        switch (this.type)
        {
            case MethodName:
                return this.mZWebInstance.callReceiver(this.methodName, szArgs);
            case FunctionName:
                return this.mZWebInstance.execJS(this.functionName, szArgs);
            default:
                throw new ZWebException("解析类型不正确...调用失败...");
        }
    }

    static final class Builder<T, R>
    {
        final IZWeb          zWebInstance;
        final Method         method;
        final Annotation[]   methodAnnotations;
        final Type[]         parameterTypes;
        final Annotation[][] parameterAnnotationsArray;

        private TYPE               type;
        private String             functionName;
        private String             methodName;
        private ParameterHandler[] parameterHandlers;

        Builder(IZWeb zWebInstance, Method method)
        {
            this.zWebInstance = zWebInstance;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        public ServiceMethod build()
        {
            for (Annotation annotation : methodAnnotations)
            {
                parseMethodAnnotation(annotation);
            }

            int parameterCount = parameterAnnotationsArray.length;
            parameterHandlers = new ParameterHandler[parameterCount];
            for (int p = 0; p < parameterCount; p++)
            {
                Type parameterType = parameterTypes[p];

                if (!Utils.checkSupportType(parameterType))
                {
                    throw new ZWebException("类型不支持...");
                }

                Annotation[] parameterAnnotations = parameterAnnotationsArray[p];
                if (parameterAnnotations == null)
                {
                    throw new ZWebException("No ZWeb annotation found.");
                }

                parameterHandlers[p] = parseParameter(p, parameterType, parameterAnnotations);
            }

            return new ServiceMethod<>(this);
        }

        private void parseMethodAnnotation(Annotation annotation)
        {
            if (annotation instanceof ZFunction)
            {
                parseFunctionName(((ZFunction) annotation).value());
            }
            else if (annotation instanceof ZMethod)
            {
                parseMethodName(((ZMethod) annotation).value());
            }
            else
            {
                throw new ZWebException("Not Found annotation...");
            }
        }

        private void parseMethodName(String methodName)
        {
            if (TextUtils.isEmpty(methodName))
            {
                throw new ZWebException("ZMethod annotation Value is null");
            }
            this.type = TYPE.MethodName;
            this.methodName = methodName;
        }

        private void parseFunctionName(String functionName)
        {
            if (TextUtils.isEmpty(functionName))
            {
                throw new ZWebException("ZFunction annotation Value is null");
            }
            this.type = TYPE.FunctionName;
            this.functionName = functionName;
        }

        private ParameterHandler parseParameter(int p, Type parameterType, Annotation[] annotations)
        {
            ParameterHandler result = null;
            for (Annotation annotation : annotations)
            {
                if (annotation instanceof ZKey)
                {
                    String parameterName = ((ZKey) annotation).value();

                    if (TextUtils.isEmpty(parameterName))
                    {
                        throw new ZWebException("ZKey annotation Value is null");
                    }

                    result = new ParameterHandler(annotation, parameterType, parameterName);
                }
                else if (annotation instanceof ZJson)
                {
                    String parameterName = "Body";
                    result = new ParameterHandler(annotation, parameterType, parameterName);
                }
            }

            if (result == null)
            {
                throw new ZWebException("No ZWeb Parameter annotation found.");
            }

            return result;
        }
    }
}
