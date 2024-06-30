package com.github.queerzard.jproperties.config;

import java.io.NotSerializableException;

public interface ISubInstanceHandover {

    <T extends PropertiesBase> void postConstructHandover(T subclass) throws NotSerializableException;

    void postConstruct() throws NotSerializableException;

}
