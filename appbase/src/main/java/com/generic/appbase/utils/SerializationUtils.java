package com.generic.appbase.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class SerializationUtils {

    public static InputStream serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(obj);

        oos.flush();
        oos.close();

        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static Object deserialize(InputStream data) throws IOException, ClassNotFoundException {
        ObjectInputStream is = new ObjectInputStream(data);
        return is.readObject();
    }

    public static byte[] serializeToByteArray(Object object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();

            return bos.toByteArray();
        } catch (IOException ignored) {

        }

        return null;
    }

    public static Object deSerializeFromByteArray(byte[] arr) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(arr)) {
            ObjectInput in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (ClassNotFoundException | IOException ignored) {
        }

        return null;
    }

}
