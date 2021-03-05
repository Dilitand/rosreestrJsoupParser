package ru.litvinov.parser.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.litvinov.parser.model.RealtyModel;
import ru.litvinov.parser.parsing.JsoupParserRosreestr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ProcessorParseMT implements ProcessorParseI {

    private static final Logger LOGGER = LoggerFactory.getLogger("JCG");

    private Queue<RealtyModel> realtyModels = new ConcurrentLinkedQueue<>();
    private Queue<RealtyModel> realtyModelsParsed = new ConcurrentLinkedQueue<>();

    private JsoupParserRosreestr jsoupParserRosreestr;

    public void init() throws IOException {
        Files.lines(Paths.get("input.txt"))
                .map(x -> x.replaceAll(":", "-").replaceAll("\\s", "").trim())
                .forEach(x -> realtyModels.add(new RealtyModel(x)));
        jsoupParserRosreestr = new JsoupParserRosreestr();
        updateAndDeleteAlreadyParsed();
        LOGGER.info("input models:" + realtyModels.size());
    }

    public void updateAndDeleteAlreadyParsed() throws IOException {
        if (Files.exists(Paths.get("output.dat"))) {
            LOGGER.info("Found some downloaded data, deserializing");
            realtyModelsParsed.addAll(ProcessorParseSerial.deserializeQueue("output.dat"));

            int currentSize = realtyModels.size();
            this.realtyModels.removeAll(realtyModelsParsed);
            if (currentSize - realtyModels.size() > 0) {
                LOGGER.info("Update " + (currentSize - realtyModels.size()) + " from cache");
            }
        }
    }

    public synchronized void saveResult() throws IOException {
        System.out.println("saving result");
        Files.write(Paths.get("output.txt"), realtyModelsParsed.stream().map(x -> x.toString()).collect(Collectors.toList()));
        ProcessorParseSerial.serializeQueue(realtyModelsParsed, "output.dat");
    }

    public void processor() throws IOException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor(); /*Можно подкинуть реализацию другую и будет многопоточность*/
        for (int i = 0; i < realtyModels.size(); i++) {
            service.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    while (!realtyModels.isEmpty()) {
                        RealtyModel realtyModel = realtyModels.poll();
                        jsoupParserRosreestr.getRealtyModel(realtyModel, "https://rosreester.net/kadastr/" + realtyModel.getInputkadastr());
                        realtyModelsParsed.add(realtyModel);
                        Thread.sleep(100);
                        System.out.println("downloaded " + realtyModelsParsed.size() + " of " + (realtyModels.size() + realtyModelsParsed.size()));
                        if (realtyModelsParsed.size() % 100 == 0) {
                            saveResult();
                        }
                        if (realtyModels.isEmpty()) {
                            saveResult();
                            System.out.println("Parsing complete");
                        }
                    }
                    ;
                }
            });
        }
    }
}
