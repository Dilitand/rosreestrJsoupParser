package ru.litvinov.parser.service;

import ru.litvinov.parser.model.RealtyModel;
import ru.litvinov.parser.parsing.JsoupParserRosreestr;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessorParse implements Serializable {

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
            ProcessorParse processorParseOld = ProcessorParseSerial.deserialize("output.dat");
            this.realtyModels.replaceAll(x->processorParseOld.realtyModels.stream().filter(z->z.getInputkadastr().equals(x.getInputkadastr())).findFirst().get());
        }
    }

    public void saveResult() throws IOException {
        Files.write(Paths.get("output.txt"), realtyModels.stream().filter(x -> x.getParsed()).map(x -> x.toString()).collect(Collectors.toList()));
        ProcessorParseSerial.serialize(this,"output.dat");
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
