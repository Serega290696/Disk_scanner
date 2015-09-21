import myProject.model.SettingsConstants;
import org.junit.Test;

/**
 * Created by serega on 21.09.2015.
 */
public class TestJSONSettings {
    @Test
    public void testSave() throws Exception {
        SettingsConstants.SETTINGS.saveSettings();
    }
    @Test
    public void testRestore() throws Exception {
        System.out.println(SettingsConstants.SETTINGS.currentSettings.get("DEFAULT_ANALYZED_FOLDER_1"));
        System.out.println(SettingsConstants.SETTINGS.currentSettings.get("DEFAULT_REPORTS_FOLDER_2"));
        SettingsConstants.SETTINGS.restoreSettings();
        System.out.println(SettingsConstants.SETTINGS.currentSettings.get("DEFAULT_ANALYZED_FOLDER_1"));
        System.out.println(SettingsConstants.SETTINGS.currentSettings.get("DEFAULT_REPORTS_FOLDER_2"));
    }
}
