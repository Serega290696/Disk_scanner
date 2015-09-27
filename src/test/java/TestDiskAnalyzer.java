import myProject.model.DiskAnalyzer;
import org.junit.Test;

/**
 * Created by serega.
 */
public class TestDiskAnalyzer {
    @Test
    public void testGetDateOf() {
        System.out.println(DiskAnalyzer.getDateOf(0));
        for (int i = 0; i < 100; i++) {
            System.out.println(i + ". " + DiskAnalyzer.getDateOf(i));
        }

    }
}
