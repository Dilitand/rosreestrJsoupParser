import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.litvinov.parser.model.RealtyModel;
import ru.litvinov.parser.parsing.JsoupParserRosreestr;

import java.io.File;
import java.io.IOException;

public class JsoupParserRosreesrtTest {

    File fileSuccess;
    File fileNotFound;
    JsoupParserRosreestr jsoupParserRosreestr;

    @Before
    public void init(){

        fileNotFound = new File("src\\test\\resources\\notFoundPage.html");
        fileSuccess = new File("src\\test\\resources\\successfulParse.html");
        jsoupParserRosreestr = new JsoupParserRosreestr();
    }

    @Test
    public void testResourcesExists(){
        Assert.assertTrue(fileSuccess.exists() && fileNotFound.exists());
    }

    @Test
    public void successfulParse() throws IOException {
        RealtyModel expectedRealtyModel = new RealtyModel();
        expectedRealtyModel.setInputkadastr("61-44-0010312-700");
        expectedRealtyModel.setParsed(true);
        expectedRealtyModel.setInputkadastr("61-44-0010312-700");
        expectedRealtyModel.setCountVladelcev("1");
        expectedRealtyModel.setType("квартира");
        expectedRealtyModel.setRealKarastr("61:44:0010312:700");
        expectedRealtyModel.setRegion("Ростовская область");
        expectedRealtyModel.setAddress("ул Добровольского , д. 22/3 , кв.33 , Ростовская обл , г Ростов-на-Дону");
        expectedRealtyModel.setArea("35,00 м2 (квадратный метр)");
        expectedRealtyModel.setKindOfPrivice("частная");
        expectedRealtyModel.setKadastrStoimost("2 175 035 руб.");
        expectedRealtyModel.setNotFound(false);

        RealtyModel testRealtyModel = new RealtyModel();
        testRealtyModel.setInputkadastr("61-44-0010312-700");

        jsoupParserRosreestr.parseRealtyModel(testRealtyModel,fileSuccess);
        Assert.assertEquals(expectedRealtyModel,testRealtyModel);
    }

    @Test
    public void notFoundParse() throws IOException {
        RealtyModel expectedRealtyModel = new RealtyModel();
        expectedRealtyModel.setInputkadastr("61-44-0010312-700");
        expectedRealtyModel.setNotFound(true);
        expectedRealtyModel.setParsed(true);

        RealtyModel testRealtyModel = new RealtyModel();
        testRealtyModel.setInputkadastr("61-44-0010312-700");

        jsoupParserRosreestr.parseRealtyModel(testRealtyModel,fileNotFound);

        Assert.assertEquals(expectedRealtyModel,testRealtyModel);

    }

}
