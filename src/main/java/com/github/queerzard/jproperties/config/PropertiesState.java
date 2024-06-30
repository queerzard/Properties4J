package com.github.queerzard.jproperties.config;

import lombok.Getter;

import java.io.NotSerializableException;
import java.util.HashMap;

public class PropertiesState {

    @Getter
    private final PropertiesBase copiedProperties;
    @Getter
    private HashMap<String, String> keyVal;
    @Getter
    private final long time = System.currentTimeMillis();

    public <T extends PropertiesBase> PropertiesState(T propertiesBase) throws CloneNotSupportedException, NotSerializableException {
        this.copiedProperties = (PropertiesBase) propertiesBase.clone();
        this.keyVal = new HashMap<>();

        init();
    }

    private void init() throws NotSerializableException {
        this.keyVal = this.copiedProperties.obtainKeyValuePairs(this.copiedProperties);
    }
}
