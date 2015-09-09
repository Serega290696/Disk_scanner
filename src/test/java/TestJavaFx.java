import myProject.view.MainWindowController;
import org.junit.Test;

/**
 * Created by serega on 08.09.2015.
 */
public class TestJavaFx {

    private MainWindowController mwc = new MainWindowController();
    @Test
    public void testOpenNewWindow() throws Exception {
        mwc.showSettingsWindow();
    }
}
