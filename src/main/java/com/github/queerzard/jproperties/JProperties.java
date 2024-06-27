package com.github.queerzard.jproperties;

import com.github.queerzard.jproperties.config.PropertiesBase;
import com.github.queerzard.jproperties.observer.AsyncPropertiesObserver;
import com.github.sebyplays.logmanager.utils.Logger;
import lombok.Getter;

import java.util.ArrayList;

public class JProperties {

    @Getter
    private ArrayList<Class<? extends PropertiesBase>> properties;
    @Getter
    private ArrayList<Class<? extends AsyncPropertiesObserver>> observers;

    @Getter
    private ArrayList<PropertiesBase> propertiesBases;

    @Getter
    private Logger logger;
    @Getter
    private static JProperties jProperties;

    public static final double version = 1.1;

    public JProperties() {
        jProperties = this;
        this.logger = new Logger(this.getClass().getName());
        this.propertiesBases = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.observers = new ArrayList<>();

    }

    public void registerConfig(Class<? extends PropertiesBase> propertiesClass) {
        /*      properties.stream().anyMatch();*/
    }

    public void unregisterConfig(Class<? extends PropertiesBase> propertiesClass) {

    }

    public void registerObserver(Class<? extends AsyncPropertiesObserver> observerClass) {


    }

    public <T extends PropertiesBase> PropertiesBase parseBase(T p) {
        return p;
    }


}
