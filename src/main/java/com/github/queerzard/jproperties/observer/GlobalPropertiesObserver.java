package com.github.queerzard.jproperties.observer;

import com.github.queerzard.jproperties.config.PropertiesBase;

import java.io.NotSerializableException;
import java.lang.reflect.Field;

public class GlobalPropertiesObserver extends AsyncPropertiesObserver {


    @Override
    public <T extends PropertiesBase> boolean postPropertiesChanged(T properties, Field field, Object oldValue, Object newValue) throws NotSerializableException {
        properties.save();
        return false;
    }

    @Override
    public <T extends PropertiesBase> boolean prePropertiesChange(T propertiesObject, Field field, Object oldValue, Object newValue) {
        return false;
    }
}
