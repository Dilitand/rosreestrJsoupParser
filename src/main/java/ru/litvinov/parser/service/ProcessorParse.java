package ru.litvinov.parser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.litvinov.parser.model.RealtyModel;
import ru.litvinov.parser.parsing.JsoupParserRosreestr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProcessorParse implements ProcessorParseI{

    private static final Logger LOGGER = LoggerFactory.getLogger("JCG");

    private ArrayList<RealtyModel> realtyModels = new ArrayList<>();
    private JsoupParserRosreestr jsoupParserRosreestr;

    public void init() throws IOException {
        Files.lines(Paths.get("input.txt"))
                .map(x -> x.replaceAll(":", "-").replaceAll("\\s", "").trim())
                .forEach(x -> realtyModels.add(new RealtyModel(x)));
        jsoupParserRosreestr = new JsoupParserRosreestr();
        updateAndDeleteAlreadyParsed();
        System.out.println("input models:" + realtyModels.size());
    }

    public void updateAndDeleteAlreadyParsed() throws IOException {
        if (Files.exists(Paths.get("output.dat"))) {
            LOGGER.info("Found some downloaded data, deserializing");
            ArrayList<RealtyModel> processorParseOld = ProcessorParseSerial.deserialize("output.dat");
            AtomicInteger downloaded = new AtomicInteger(0);
            this.realtyModels.replaceAll(x -> processorParseOld.stream().filter(z -> z.getInputkadastr().equals(x.getInputkadastr()))
                    .peek(z -> downloaded.getAndIncrement()).findFirst().get());
            if (downloaded.intValue() > 0) {
                LOGGER.info("Update " + downloaded.intValue() + " from cache");
            }
        }
    }

    public void saveResult() throws IOException {
        Files.write(Paths.get("output.txt"), realtyModels.stream().filter(x -> x.getParsed()).map(x -> x.toString()).collect(Collectors.toList()));
        ProcessorParseSerial.serialize(realtyModels, "output.dat");
    }


    public void processor() throws IOException, InterruptedException {
        for (int i = 0; i < realtyModels.size(); i++) {
            if (!realtyModels.get(i).getParsed()) {
                jsoupParserRosreestr.getRealtyModel(realtyModels.get(i), "https://rosreester.net/kadastr/" + realtyModels.get(i).getInputkadastr());
            }
            if (i % 100 == 0) {
                saveResult();
            }
            Thread.sleep(100);
            System.out.println("downloaded " + i + " of " + realtyModels.size());
        }
        saveResult();
        System.out.println("Parsing compleete");
    }

}
