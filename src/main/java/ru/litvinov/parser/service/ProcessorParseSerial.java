package ru.litvinov.parser.service;

import java.io.*;

public class ProcessorParseSerial {

    public static void serialize(ProcessorParse processorParse, String path) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            outputStream.writeObject(processorParse);
        }
    }

    public static ProcessorParse deserialize(String path) throws IOException {
        try(FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);) {
            return (ProcessorParse) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
