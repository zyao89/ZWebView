package com.zyao89.view.zweb.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 参数key
 *
 * @author Zyao89
 * 2017/11/8.
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface ZJson
{
}
