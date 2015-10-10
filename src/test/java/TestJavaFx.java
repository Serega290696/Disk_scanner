import myProject.view.MainWindowController;
import org.junit.Test;

/**
 * Created by serega.
 */
public class TestJavaFx {

    private MainWindowController mwc = new MainWindowController();
    @Test
    public void testOpenNewWindow() throws Exception {
        mwc.showSettingsWindow();
    }
}
