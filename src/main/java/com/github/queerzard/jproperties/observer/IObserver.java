package com.github.queerzard.jproperties.observer;

import com.github.queerzard.jproperties.config.PropertiesBase;

import java.io.NotSerializableException;
import java.lang.reflect.Field;

public interface IObserver {

    <T extends PropertiesBase> boolean onChange(T propertiesObject, Field field, Object oldValue, Object newValue) throws NotSerializableException;

}
