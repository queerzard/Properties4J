package com.github.queerzard.jproperties.observer;

import com.github.queerzard.jproperties.config.PropertiesBase;
import com.github.queerzard.jproperties.config.PropertiesState;
import lombok.Getter;

import java.io.NotSerializableException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public abstract class AsyncPropertiesObserver implements IObserver {


    @Getter
    private final ArrayList<PropertiesBase> properties;
    @Getter
    private ArrayList<PropertiesState> cachedProperties = new ArrayList<>();

    public AsyncPropertiesObserver() {
        this.properties = new ArrayList<>();
        this.cachedProperties = new ArrayList<>();
        observe();
    }

    @Getter
    private final boolean active = true;

    public void observe() {
        new Thread(() -> {
            while (active) {
                synchronized (properties) {
                    // Update the cache if there are new properties
                    if (cachedProperties.size() < properties.size()) {
                        for (int i = cachedProperties.size(); i < properties.size(); i++) {
                            try {
                                cachedProperties.add(new PropertiesState(properties.get(i)));
                            } catch (CloneNotSupportedException | NotSerializableException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    // Check for changes in properties
                    for (int i = 0; i < properties.size(); i++) {
                        PropertiesBase propertiesBase = properties.get(i);
                        PropertiesState cachedState = cachedProperties.get(i);

                        Map<String, String> currentKeyVal;
                        try {
                            currentKeyVal = propertiesBase.obtainKeyValuePairs(propertiesBase);
                        } catch (NotSerializableException e) {
                            throw new RuntimeException(e);
                        }

                        for (Map.Entry<String, String> entry : currentKeyVal.entrySet()) {
                            String key = entry.getKey();
                            String currentValue = entry.getValue();
                            String cachedValue = cachedState.getKeyVal().get(key);

                            if (!equals(currentValue, cachedValue)) {
                                try {
                                    Field field = propertiesBase.getClass().getDeclaredField(key);
                                    field.setAccessible(true);

                                    if (onChange(propertiesBase, field, cachedValue, currentValue)) {
                                        cachedState.getKeyVal().put(key, currentValue);
                                    } else {
                                        field.set(propertiesBase, cachedValue);
                                    }
                                } catch (NoSuchFieldException | IllegalAccessException | NotSerializableException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                onPropertyUnchanged(propertiesBase, key, currentValue);
                            }
                        }
                    }
                }

                try {
                    Thread.sleep(100); // Adjust the sleep duration as necessary
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null || o2 == null) return false;
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            int length = Array.getLength(o1);
            if (length != Array.getLength(o2)) return false;
            for (int i = 0; i < length; i++) {
                if (!equals(Array.get(o1, i), Array.get(o2, i))) return false;
            }
            return true;
        }
        return o1.equals(o2);
    }


    protected abstract void onPropertyUnchanged(PropertiesBase propertiesBase, String key, String currentValue);
}