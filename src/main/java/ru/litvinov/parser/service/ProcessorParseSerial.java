package ru.litvinov.parser.service;

import ru.litvinov.parser.model.RealtyModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class ProcessorParseSerial {

    public static void serialize(ArrayList<RealtyModel> processorParse, String path) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            outputStream.writeObject(processorParse);
        }
    }


    public static ArrayList deserialize(String path) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);) {
            return (ArrayList) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void serializeQueue(Collection<RealtyModel> processorParse, String path) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            outputStream.writeObject(processorParse);
        }
    }


    public static Collection deserializeQueue(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path);
             ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);) {
            return (Collection) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
