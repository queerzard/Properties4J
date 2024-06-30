package com.github.queerzard.jproperties;

import com.github.queerzard.jproperties.annotations.Observe;
import com.github.queerzard.jproperties.annotations.Register;
import com.github.queerzard.jproperties.config.PropertiesBase;
import com.github.queerzard.jproperties.observer.AsyncPropertiesObserver;
import com.github.queerzard.jproperties.utilities.PropertiesObserverWorker;
import com.github.queerzard.jproperties.utilities.Utils;
import com.github.sebyplays.logmanager.utils.Logger;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;

public class JProperties {

    @Getter
    private ArrayList<Class<? extends PropertiesBase>> properties;
    @Getter
    private ArrayList<Class<? extends AsyncPropertiesObserver>> observers;
    @Getter
    private ArrayList<PropertiesBase> propertiesBases;

    @Getter
    private PropertiesObserverWorker propertiesObserverTask;
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

        this.propertiesObserverTask = new PropertiesObserverWorker();
    }

    @SneakyThrows
    public void registerConfigurations(String packageName) {
        Class[] classes = Utils.getClasses(packageName);
        for (Class clazz : classes)
            if (clazz.isAnnotationPresent(Register.class))
                registerConfig(clazz);
    }

    @SneakyThrows
    public <T extends PropertiesBase> void registerConfig(Class<? extends PropertiesBase> propertiesClass) throws InstantiationException, IllegalAccessException {
        if (properties.stream().anyMatch(propClass -> propertiesClass.getName().equalsIgnoreCase(propClass.getName())))
            throw new IllegalArgumentException("instance already registered!");

        this.properties.add(propertiesClass);

        T propertiesInstance = (T) propertiesClass.newInstance();
        propertiesInstance.postConstructHandover(propertiesInstance);

        this.propertiesBases.add(propertiesClass.newInstance());

        if (propertiesClass.isAnnotationPresent(Observe.class)) {
            Observe observe = propertiesClass.getAnnotation(Observe.class);
            registerObserver(observe.observer());
            linkObserver(observe.observer(), propertiesClass);
        }
    }

    public <T extends PropertiesBase> PropertiesBase obtainConfig(Class<? extends PropertiesBase> propertiesClass) {
        return this.propertiesBases.stream().filter(propertiesBase -> propertiesBase.getClass().equals(propertiesClass)).findAny().get();
    }

    public void unregisterConfig(Class<? extends PropertiesBase> propertiesClass) {
        if (!properties.removeIf(propClass -> propClass.getName().equalsIgnoreCase(propertiesClass.getName())))
            throw new NullPointerException("No such class registered!");

        this.logger.info("Unregistered {}", propertiesClass.getName());
    }

    public void registerObserver(Class<? extends AsyncPropertiesObserver> observerClass) throws InstantiationException, IllegalAccessException {
        if (properties.stream().anyMatch(obsClass -> observerClass.getName().equalsIgnoreCase(obsClass.getName())))
            throw new IllegalArgumentException("instance already registered!");

        this.observers.add(observerClass);
        this.propertiesObserverTask.getPropertiesObservers().add(observerClass.newInstance());
    }

    public void linkObserver(Class<? extends AsyncPropertiesObserver> observerClass, Class<? extends PropertiesBase> propertiesClass) {
        this.propertiesObserverTask.getPropertiesObservers().stream().filter(obs -> obs.getClass().equals(observerClass))
                .findAny().get().getProperties().add(obtainConfig(propertiesClass));
    }

}
