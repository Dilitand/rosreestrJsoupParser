package ru.litvinov.parser.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RealtyModel implements Serializable {
    Boolean parsed = false;
    String inputkadastr;
    String countVladelcev;
    String type;
    String realKarastr;
    String region;
    String address;
    String area;
    String kindOfPrivice;
    String kadastrStoimost;
    String dateUpdate;
    boolean notFound;
    String errorMessage;

    public RealtyModel() {
    }

    public RealtyModel(String inputkadastr) {
        this.inputkadastr = inputkadastr;
    }

    @Override
    public String toString() {
       return (parsed + "~" + inputkadastr + "~" + countVladelcev
                + "~" + type + "~" + realKarastr + "~" + region + "~" + address
                + "~" + area + "~" + kindOfPrivice + "~" + kadastrStoimost + "~" + dateUpdate + "~" + notFound + "~" + errorMessage + "~" + "endOfString").replaceAll("\n","");
    }
}
