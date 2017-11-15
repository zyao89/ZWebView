package com.zyao89.view.zweb.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.zyao89.view.zweb.exceptions.ZWebException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
public class Utils
{
    public static <T> void validateServiceInterface(Class<T> service)
    {
        if (!service.isInterface())
        {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
        // Android (http://b.android.com/58753) but it forces composition of API declarations which is
        // the recommended pattern.
        if (service.getInterfaces().length > 0)
        {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }

    public static JSONObject json2Obj(String json)
    {
        try
        {
            return new JSONObject(json);
        }
        catch (JSONException e)
        {
            throw new ZWebException("Json parse error...");
        }
    }

    public static Class<?> getRawType(Type type)
    {
        checkNotNull(type, "type == null");

        if (type instanceof Class<?>)
        {
            // TYPE is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType)
        {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns TYPE instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class))
            {
                throw new IllegalArgumentException();
            }
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType)
        {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable)
        {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType)
        {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }

    public static <T> T checkNotNull(@Nullable T object, String message)
    {
        if (object == null)
        {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static boolean checkSupportType(Type parameterType)
    {
        if (parameterType.equals(Boolean.class) || parameterType.equals(boolean.class))
        {
            return true;
        }
        if (parameterType.equals(Character.class) || parameterType.equals(char.class))
        {
            return true;
        }
        if (parameterType.equals(Byte.class) || parameterType.equals(byte.class))
        {
            return true;
        }
        if (parameterType.equals(Double.class) || parameterType.equals(double.class))
        {
            return true;
        }
        if (parameterType.equals(Float.class) || parameterType.equals(float.class))
        {
            return true;
        }
        if (parameterType.equals(Integer.class) || parameterType.equals(int.class))
        {
            return true;
        }
        if (parameterType.equals(Long.class) || parameterType.equals(long.class))
        {
            return true;
        }
        if (parameterType.equals(Short.class) || parameterType.equals(short.class))
        {
            return true;
        }
        if (parameterType.equals(String.class))
        {
            return true;
        }
        return false;
    }

    public static String assetFile2Str(Context c, String urlStr)
    {
        InputStream in = null;
        try
        {
            in = c.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder sb = new StringBuilder();
            do
            {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*"))
                {
                    sb.append(line).append('\n');
                }
            } while (line != null);

            bufferedReader.close();
            in.close();

            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                }
            }
        }
        return null;
    }
}
