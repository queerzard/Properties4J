package com.github.queerzard.jproperties.observer;

import com.github.queerzard.jproperties.JProperties;
import com.github.queerzard.jproperties.config.PropertiesBase;

import java.io.NotSerializableException;
import java.lang.reflect.Field;

public class GlobalPropertiesObserver extends AsyncPropertiesObserver {



    @Override
    protected void onPropertyUnchanged(PropertiesBase propertiesBase, String key, String currentValue) {
    }

    @Override
    public <T extends PropertiesBase> boolean onChange(T propertiesObject, Field field, Object oldValue, Object newValue) throws NotSerializableException {
        propertiesObject.save();
        JProperties.getJProperties().getLogger().name(propertiesObject.getClass().getName()).normal(field.toString());
        return true;
    }
}
