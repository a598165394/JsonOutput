package rubiconproject;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test Case for {@code DataProcessor}
 * <p>
 * Copyright © Hengming Dai
 */

@RunWith(MockitoJUnitRunner.class)
public class DataProcessorTest {
    @Test
    public void mainWithWrongInput() {
        DataProcessor dataProcessor = new DataProcessor();
        String[] emptyInput = new String[1];
        try{
            dataProcessor.main(emptyInput);
        }catch(Exception e) {
            assertThat(e.getMessage(), is("Input format is not right should be `pathToDirectory`, `outputFile`"));
        }
    }
}
