package testdir;

import myProject.model.DiskAnalyzer;
import org.junit.Test;

import java.io.File;

/**
 * Created by serega on 10.09.2015.
 */
public class TestDiskScanner {

    @Test
    public void testScanner() throws Exception {

        new DiskAnalyzer().launch(new File("D:\\1-Programming\\1-Projects\\Part1"));
//        new DiskAnalyzer().launch(new File("C:\\Program Files"));

    }
}
