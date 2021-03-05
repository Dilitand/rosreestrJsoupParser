package ru.litvinov.parser.service;

import java.io.IOException;

public interface ProcessorParseI {

    void init() throws IOException;
    void updateAndDeleteAlreadyParsed() throws IOException;
    void saveResult() throws IOException;
    void processor() throws IOException, InterruptedException;

}
