package com.github.queerzard.jproperties.annotations;

import com.github.queerzard.jproperties.observer.AsyncPropertiesObserver;
import com.github.queerzard.jproperties.observer.GlobalPropertiesObserver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Observe {

    Class<? extends AsyncPropertiesObserver> observer() default GlobalPropertiesObserver.class;

}
