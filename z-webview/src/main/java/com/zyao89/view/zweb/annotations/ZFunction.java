package com.zyao89.view.zweb.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface ZFunction
{
    String value() default "";
}
