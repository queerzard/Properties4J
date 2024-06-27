package com.github.queerzard.jproperties.observer;

import com.github.queerzard.jproperties.config.PropertiesBase;
import com.github.queerzard.jproperties.config.PropertiesState;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AsyncPropertiesObserver implements IObserver {

    //TODO create a cache containing a copy of all active files/instances; then iterate through all live instances and compare the fields for changes.
    // if changes are detected, call the according interface-method and then refresh the cache

    @Getter
    private HashMap<String, PropertiesBase> properties;
    @Getter
    private ArrayList<PropertiesState> cachedProperties = new ArrayList<>();

    @Getter
    private boolean active = true;

    public AsyncPropertiesObserver() {
        this.properties = new HashMap<>();
        observe();
    }

    public void kill() {
        this.active = false;
    }

    public void observe() {
        new Thread(new Runnable() {
            public void run() {
                while (active) {
                    ArrayList<PropertiesBase> propsCopy = (ArrayList<PropertiesBase>) properties.clone();


                }
            }
        }).start();

    }


}
