package com.github.queerzard.jproperties.utilities;

import com.github.queerzard.jproperties.annotations.ObjB64;

import java.io.NotSerializableException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Base64;

public class AnnotationProcessor {

    public static boolean isSerializable(Field field) {
        return field.isAnnotationPresent(ObjB64.class) /*? Arrays.stream(field.getDeclaringClass().getInterfaces())
                .anyMatch(type -> type == Serializable.class) : false*/;
    }

    public static AbstractMap.SimpleEntry serializeObject(Field field, Object o) throws NotSerializableException {
        if (!isSerializable(field))
            throw new NotSerializableException(field.getDeclaringClass().getName() + " is not a serializable!");
        return new AbstractMap.SimpleEntry(field.getName(), Base64.getEncoder().encodeToString(Utils.objectToBytes(Utils.getFieldValue(field, o))));
    }

    public static Object deserializeObject(Class type, String base64) {
        return Utils.parseObject(type, base64.getBytes(StandardCharsets.UTF_8));
    }

}
