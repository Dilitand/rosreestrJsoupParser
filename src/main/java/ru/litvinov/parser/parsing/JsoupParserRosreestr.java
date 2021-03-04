package ru.litvinov.parser.parsing;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.litvinov.parser.model.RealtyModel;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

public class JsoupParserRosreestr {

    public void getRealtyModel(RealtyModel realtyModel,String url)  {
        try {
            parseDocument(getJsoupDocument(url),realtyModel);
        } catch (HttpStatusException ex) {
            realtyModel.setParsed(true);
            realtyModel.setNotFound(true);
            realtyModel.setErrorMessage("status:" + ex.getStatusCode() + " message:" + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseRealtyModel(RealtyModel realtyModel,File url) throws IOException {
        parseDocument(parseJsoupDocument(url),realtyModel);
    }

    public Document getJsoupDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public Document parseJsoupDocument(File file) throws IOException {
        return Jsoup.parse(file, String.valueOf(Charset.defaultCharset()));
    }

    public RealtyModel parseDocument(Document document, RealtyModel realtyModel) {
        if(document.toString().contains("Страница устарела, была удалена или не существовала вовсе")){
            realtyModel.setParsed(true);
            realtyModel.setNotFound(true);
            return realtyModel;
        } else {
            Element bigElement = document.select("div.block1__object").select("div.test__data").get(0);
            //List<Element> list = bigElement.children().stream().filter(x -> x.childrenSize() > 0 && x.child(0).tagName().equals("strong")).collect(Collectors.toList());
            List<Element> list = bigElement.children();
            for (int i = 0; i < list.size(); i++) {
                String currentText = list.get(i).text();
                System.out.println(currentText);
                if (currentText.contains("количество владельцев")) {
                    realtyModel.setCountVladelcev(currentText.split(":")[1].trim());
                } else if (currentText.contains("Тип:")) {
                    realtyModel.setType(currentText.split(":")[1].trim());
                } else if (currentText.contains("Кадастровый номер:")) {
                    realtyModel.setRealKarastr(currentText.split("Кадастровый номер:")[1].trim());
                } else if (currentText.contains("Регион:")) {
                    realtyModel.setRegion(currentText.split(":")[1].trim());
                } else if (currentText.contains("Адрес полный:")) {
                    realtyModel.setAddress(currentText.split(":")[1].trim());
                } else if (currentText.contains("Площадь:")) {
                    realtyModel.setArea(currentText.split(":")[1].trim());
                } else if (currentText.contains("Форма собственности:")) {
                    realtyModel.setKindOfPrivice(currentText.split(":")[1].trim());
                } else if (currentText.contains("Кадастровая стоимость:")) {
                    realtyModel.setKadastrStoimost(currentText.split(":")[1].trim());
                } else if (currentText.contains("Дата обновления информации:")) {
                    realtyModel.setDateUpdate(currentText.split(":")[1].trim());
                }
            }
            realtyModel.setParsed(true);
        }
        return realtyModel;
    }
}

