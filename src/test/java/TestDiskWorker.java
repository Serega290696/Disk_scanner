import myProject.model.DiskWorker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

/**
 * Created by serega on 07.09.2015.
 */
public class TestDiskWorker extends Assert {
    DiskWorker dw = new DiskWorker();

    @Test
    public void testGetDisksList() {
        printList();
        Assert.assertArrayEquals("Test failed!",
                new String[]{"C:\\", "D:\\"},
                Arrays.asList(
                        dw.getOftenUsedPaths()
                ).subList(0, 2).toArray(new String[2]));
        System.out.println("Complete!");
    }
    @Test
    public void testIsDisk() {
        Assert.assertTrue(dw.isDisk(new File("C:\\")));
        Assert.assertTrue(dw.isDisk(new File("D:\\")));
        Assert.assertTrue(dw.isDisk(new File("F:\\")));
        Assert.assertFalse(dw.isDisk(new File("T:\\")));
        Assert.assertFalse(dw.isDisk(new File("AAA")));
        Assert.assertFalse(dw.isDisk(new File(":C")));
    }

    private void printList() {
        for(String st : dw.getOftenUsedPaths()) {
            System.out.println(st);
        }
        System.out.println("===============");
    }
}
