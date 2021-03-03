package ru.litvinov.parser;

import ru.litvinov.parser.model.RealtyModel;
import ru.litvinov.parser.parsing.JsoupParserRosreestr;
import ru.litvinov.parser.service.ProcessorParse;

import java.io.IOException;

public class Main {



    public static void main(String[] args) throws IOException, InterruptedException {
        ProcessorParse processorParse = new ProcessorParse();
        processorParse.init();
        processorParse.processor();
    }
}

