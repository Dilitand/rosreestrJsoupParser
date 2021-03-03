package ru.litvinov.parser.model;

import lombok.Data;

@Data
public class RealtyModel {
    boolean parsed;
    String inputkadastr;
    String countVladelcev;
    String type;
    String realKarastr;
    String region;
    String address;
    String area;
    String kindOfPrivice;
    String kadastrStoimost;
    boolean notFound;
    String errorMessage;

    public RealtyModel() {
    }

    public RealtyModel(String inputkadastr) {
        this.inputkadastr = inputkadastr;
    }

    @Override
    public String toString() {
       return parsed + "~" + inputkadastr + "~" + countVladelcev
                + "~" + type + "~" + realKarastr + "~" + region + "~" + address
                + "~" + area + "~" + kindOfPrivice + "~" + kadastrStoimost + "~" + notFound + "~" + errorMessage;
    }
}
