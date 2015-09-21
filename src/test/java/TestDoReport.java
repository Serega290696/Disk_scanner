import myProject.model.DiskAnalyzer;
import myProject.model.IncorrectFileSelected;
import myProject.model.ReportGenerator;
import org.junit.Test;

/**
 * Created by serega on 19.09.2015.
 */
public class TestDoReport {
    @Test
    public void test1() {
        DiskAnalyzer da = new DiskAnalyzer();
        try {
            da.launch();
        } catch (IncorrectFileSelected incorrectFileSelected) {
            incorrectFileSelected.printStackTrace();
        }
        new ReportGenerator().saveReport(da, false);

    }
}
