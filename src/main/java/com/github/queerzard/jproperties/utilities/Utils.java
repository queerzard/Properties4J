package com.github.queerzard.jproperties.utilities;

import com.github.queerzard.jproperties.annotations.Ignore;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Utils {

    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    public static List findClasses(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists())
            return classes;
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static Annotation getAnnotation(Class clazz, Class annotation) {
        if (clazz.isAnnotationPresent(annotation))
            return clazz.getAnnotation(annotation);
        return null;
    }

    public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
        try {
            Class<?> clazz = obj.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, (field.getType().cast(fieldValue)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            // Handle the exception as needed, such as rethrowing or logging
        }
    }

    @SneakyThrows
    public static Object getFieldValue(Class clazz, String fieldName) {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(clazz);
    }


    @SneakyThrows
    public static Object getFieldValue(Field field, Object o) {
        field.setAccessible(true);
        return field.get(o);
    }

    public static boolean isTransient(Field field) {
        return Modifier.isTransient(field.getModifiers()) || field.isAnnotationPresent(Ignore.class);
    }

    public static byte[] encryptBytes(String password, byte[] plainBytes) throws Exception {
        Key encryptionKey = new SecretKeySpec((password).getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
        byte[] encryptedBytes = cipher.doFinal(plainBytes);
        return encryptedBytes;
    }

    public static byte[] decryptBytes(String password, byte[] encryptedBytes) throws Exception {
        Key encryptionKey = new SecretKeySpec((password).getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return decryptedBytes;
    }

    private static String hash(String hashType, String inputHash) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance(hashType);
        messageDigest.update(inputHash.getBytes());

        byte[] digest = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte byt : digest)
            stringBuffer.append(String.format("%02x", byt & 0xff));
        return stringBuffer.toString();
    }

    @SneakyThrows
    public static String md5(String input) {
        return hash("MD5", input);
    }

    @SneakyThrows
    public static String sha1(String input) {
        return hash("SHA-1", input);
    }

    @SneakyThrows
    public static String sha256(String input) {
        return hash("SHA-256", input);
    }

    @SneakyThrows
    public static String sha384(String input) {
        return hash("SHA-384", input);
    }

    @SneakyThrows
    public static String sha512(String input) {
        return hash("SHA-512", input);
    }


    public static byte[] objectToBytes(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public static Object parseObject(byte[] data) {
        Object object;
        try {
            object = new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
        } catch (Exception e) {
            return null;
        }
        return object;
    }

    public static Object parseObject(Class type, byte[] data) {
        return type.cast(parseObject(data));
    }

    public static byte[] parseByte(String byteString) {
        String[] byteValues = byteString.substring(1, byteString.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];

        for (int i = 0, length = bytes.length; i < length; i++) {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }
        return bytes;
    }


}
