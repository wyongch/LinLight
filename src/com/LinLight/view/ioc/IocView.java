package com.LinLight.view.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME) 
public @interface IocView {
	public int id();
	public String click() default "";
	public String longClick() default "";
	public String itemClick() default "";
	public String itemLongClick() default "";
	public IocSelect select() default @IocSelect(selected="") ;
}
