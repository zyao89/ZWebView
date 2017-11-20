package com.zyao89.view.zweb.services;

import android.support.annotation.Nullable;

import com.zyao89.view.zweb.ZWebHandler;
import com.zyao89.view.zweb.utils.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zyao89
 * 2017/11/13.
 */

public class ServiceFactory
{
    private final Map<Method, ServiceMethod<?, ?>> serviceMethodCache = new ConcurrentHashMap<>();
    private final ZWebHandler mZWebHandler;

    public ServiceFactory(ZWebHandler zWebHandler)
    {
        mZWebHandler = zWebHandler;
    }

    public <T> T create(final Class<T> service)
    {
        Utils.validateServiceInterface(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new InvocationHandler()
        {
            @Override
            public Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable
            {
                // If the method is a method from Object then defer to normal invocation.
                if (method.getDeclaringClass() == Object.class)
                {
                    return method.invoke(this, args);
                }
                ServiceMethod<Object, Object> serviceMethod = (ServiceMethod<Object, Object>) loadServiceMethod(method);
                return serviceMethod.toRequest(args);
            }
        });
    }

    private ServiceMethod<?, ?> loadServiceMethod(Method method)
    {
        ServiceMethod<?, ?> result = serviceMethodCache.get(method);
        if (result != null)
        {
            return result;
        }
        synchronized (serviceMethodCache)
        {
            result = serviceMethodCache.get(method);
            if (result == null)
            {
                result = new ServiceMethod.Builder<>(mZWebHandler, method).build();
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }
}
