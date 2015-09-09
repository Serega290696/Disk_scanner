import myProject.model.FileWorker;
import org.junit.Test;

/**
 * Created by serega on 08.09.2015.
 */
public class TestFileWorker {
    @Test
    public void testRead() throws Exception {
        FileWorker fw = new FileWorker();
        String s = fw.read("D:\\GGG2.txt");
        System.out.println(s);
    }
    @Test
    public void testWrite() throws Exception {

        FileWorker fw = new FileWorker();
        fw.write("D:\\GGG.txt", "…÷ивет. “ут русский текст.");
        System.out.println(fw.read("D:\\GGG.txt"));
    }

}
