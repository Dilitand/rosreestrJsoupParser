package ru.litvinov.parser;

import ru.litvinov.parser.service.ProcessorParseI;
import ru.litvinov.parser.service.ProcessorParseMT;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessorParseI processorParse = new ProcessorParseMT();
        processorParse.init();
        processorParse.processor();
    }
}

