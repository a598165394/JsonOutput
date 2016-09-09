package rubiconproject;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test Case for {@code TxtBuilder}
 * <p>
 * Copyright © Hengming Dai
 */

@RunWith(MockitoJUnitRunner.class)
public class TxtBuilderTest {
    private KeywordService keywordService = new KeywordService();
    private String TestJsonOutPutFile = "outputJson.txt";
    private String TestCsvOutPutFile = "outputCsv.txt";

    @Test
    public void buildTxtWithWrongFile() {
        TxtBuilder txtBuilder = new TxtBuilder();
        try{
            txtBuilder.buildTxt("./../../input/input2.txt", "outputJson4.txt");
        }catch(Exception e) {
            assertThat(e.getMessage(), is("Input format is not csv or json"));
        }
    }

    @Test
    public void buildTxtFromJson() throws Exception{
        TxtBuilder txtBuilder = new TxtBuilder();
        txtBuilder.buildTxt("../../../input/input2.json", TestJsonOutPutFile);

        verify(txtBuilder, never()).convertCvsToTxt();

        File origin = new File("../../../output/outputJson.txt");
        File produce = new File(TestJsonOutPutFile);
        assertTrue("The file should be same", FileUtils.contentEqualsIgnoreEOL(origin, produce));
    }

    @Test
    public void buildTxtFromCvs() throws Exception{
        TxtBuilder txtBuilder = new TxtBuilder();
        txtBuilder.buildTxt("./../../input/input1.csv", TestJsonOutPutFile);

        verify(txtBuilder, never()).convertJsonToTxt();

        File origin = new File("../../../output/outputCsv.txt");
        File produce = new File(TestCsvOutPutFile);
        assertTrue("The file should be same", FileUtils.contentEqualsIgnoreEOL(origin, produce));
    }
}