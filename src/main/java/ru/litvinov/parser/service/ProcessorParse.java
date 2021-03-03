package ru.litvinov.parser.service;

import ru.litvinov.parser.model.RealtyModel;
import ru.litvinov.parser.parsing.JsoupParserRosreestr;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessorParse implements Serializable {

    private List<RealtyModel> realtyModels = new ArrayList<>();
    private JsoupParserRosreestr jsoupParserRosreestr;

    public void init() throws IOException {
        Files.lines(Paths.get("input.txt"))
                .map(x -> x.replaceAll(":", "-").replaceAll("\\s","").trim())
                .forEach(x -> realtyModels.add(new RealtyModel(x)));
        jsoupParserRosreestr = new JsoupParserRosreestr();

        deleteAlreadyparsed();

        System.out.println("input models:");
        System.out.println(realtyModels);
    }

    public void deleteAlreadyparsed() {

    }

    public void saveResult() throws IOException {
        Files.write(Paths.get("output.txt"),realtyModels.stream().map(x->x.toString()).collect(Collectors.toList()));
    }

    public void processor() throws IOException, InterruptedException {
        for (int i = 0; i < realtyModels.size(); i++) {
            jsoupParserRosreestr.getRealtyModel(realtyModels.get(i), "https://rosreester.net/kadastr/" + realtyModels.get(i).getInputkadastr());
            System.out.println("downloaded " + i + " of " + realtyModels.size());
            if (i%500 == 0) {
                saveResult();
            }
            Thread.sleep(100);
        }
       saveResult();
    }
}
