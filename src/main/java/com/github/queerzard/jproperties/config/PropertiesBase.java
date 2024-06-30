package com.github.queerzard.jproperties.config;

import com.github.queerzard.jproperties.annotations.ObjB64;
import com.github.queerzard.jproperties.annotations.Properties;
import com.github.queerzard.jproperties.utilities.AnnotationProcessor;
import com.github.queerzard.jproperties.utilities.Utils;

import java.io.NotSerializableException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class PropertiesBase implements Cloneable, ISubInstanceHandover {


    //fieldName=path.to.object.type(value)

    private PropertiesFile propertiesFile;

    private PropertiesBase propertiesBase;


    public PropertiesBase() {
    }


    @Override
    public <T extends PropertiesBase> void postConstructHandover(T subclass) throws NotSerializableException {
        this.propertiesBase = subclass;
        Properties subProp = propertiesBase.getClass().getAnnotation(Properties.class);
        this.propertiesFile = new PropertiesFile(subProp.name().equalsIgnoreCase("{nameOfClass}") ? propertiesBase.getClass().getSimpleName() : subProp.name());
        checkFieldIntegrity();
        parse();
        postConstruct();
    }

    private void checkFieldIntegrity() throws NotSerializableException {
        for (Field field : getFields(true)) {
            if (field.isAnnotationPresent(Deprecated.class) && this.propertiesFile.existing(field.getName())) {
                this.propertiesFile.getProperties().remove(field.getName());
                save();
                continue;
            }
            if (this.propertiesFile.existing(field.getName()))
                continue;

            this.propertiesFile.set(field.getName(), "");
        }
    }

    public void parse() {
        for (Field field : getFields(false)) {
            Utils.setFieldValue(this.propertiesBase, field.getName(), this.propertiesFile.existing(field.getName()) ?
                    (!AnnotationProcessor.isSerializable(field) ? this.propertiesFile.get(field.getName()) :
                            AnnotationProcessor.deserializeObject(field.getType(),
                                    ((String) this.propertiesFile.get(field.getName())))) : null);

        }
    }

    public Field[] getFields(boolean includeDeprecated) {
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (!Utils.isTransient(field)) {
                if (field.isAnnotationPresent(Deprecated.class) && !includeDeprecated)
                    continue;
                fields.add(field);
            }
        }
        return fields.toArray(new Field[fields.size()]);
    }

    public <T extends PropertiesBase> HashMap<String, String> obtainKeyValuePairs(T propertiesBase) throws NotSerializableException {
        HashMap<String, String> keyValuePairs = new HashMap<>();

        for (Field field : propertiesBase.getFields(false)) {
            String temp = "";
            String fieldName = field.getName();
            String value = String.valueOf(field.isAnnotationPresent(ObjB64.class) ?
                    (temp = (String) AnnotationProcessor.serializeObject(field, propertiesBase).getValue()).substring(0, temp.length()) : Utils.getFieldValue(field, propertiesBase));
            keyValuePairs.put(fieldName, value);
        }

        return keyValuePairs;
    }

    public void save() throws NotSerializableException {
        HashMap<String, String> kvp = obtainKeyValuePairs(this);
        for (String key : kvp.keySet())
            this.propertiesFile.set(key, String.valueOf(kvp.get(key)));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
