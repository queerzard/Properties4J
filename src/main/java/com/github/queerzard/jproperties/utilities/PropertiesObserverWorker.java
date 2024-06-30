package com.github.queerzard.jproperties.utilities;

import com.github.queerzard.jproperties.observer.AsyncPropertiesObserver;
import lombok.Getter;

import java.util.ArrayList;

public class PropertiesObserverWorker {

    @Getter
    private final PropertiesObserverWorker globalTask;

    @Getter
    private ArrayList<AsyncPropertiesObserver> propertiesObservers;

    @Getter
    private boolean active = true;


    public PropertiesObserverWorker() {
        globalTask = this;
        this.propertiesObservers = new ArrayList<>();
        observe();
    }

    public void kill() {
        this.active = false;
    }


    public void observe() {
        new Thread(new Runnable() {
            public void run() {
                while (active) {
                    for (AsyncPropertiesObserver asyncPropertiesObserver : (ArrayList<AsyncPropertiesObserver>) propertiesObservers.clone())
                        asyncPropertiesObserver.observe();
                }
            }
        }).start();

    }

    public boolean removeObserverByType(Class<? extends AsyncPropertiesObserver> observer) {
        return this.propertiesObservers.removeIf(obs -> obs.getClass() == observer);
    }

}
