package myProject.model;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public enum SettingsConstants {
    SETTINGS;
    private final Logger LOGGER_SETTINGS = Logger.getLogger(SettingsConstants.class);

    private HashMap<String, Object> defaultSettings = new HashMap<String, Object>() {{
        put("DEFAULT_ANALYZED_FOLDER_1", new File("..\\").getAbsolutePath());
        put("DEFAULT_REPORTS_FOLDER_2", new File("reports").getAbsolutePath());
        put("START_ANALYSIS_WITH_APP_START_3", false);
        put("SAVE_REPORTS_AUTOMATICALLY_4", false);
        put("SLIDER_SIZE_5", 24d);
        put("SLIDER_NUMBER_6", 100d);
        put("START_APP_WITH_WINDOWS_7", false);
    }};
    public HashMap<String, Object> currentSettings = new HashMap<String, Object>() {{
        putAll(defaultSettings);
    }};

    {
        System.out.println(currentSettings.values());
        restoreSettings();
    }

    private FileWorker fw = new FileWorker();
    public final String CONFIG_FILE = "config\\config.dat";

    public void setDefaultAnalyzedFolder1(File DEFAULT_ANALYZED_FOLDER_1) {
        currentSettings.put("DEFAULT_ANALYZED_FOLDER_1", DEFAULT_ANALYZED_FOLDER_1.getAbsolutePath());
        saveSettings();
    }

    public void setDefaultReportsFolder2(File DEFAULT_REPORTS_FOLDER_2) {
        currentSettings.put("DEFAULT_REPORTS_FOLDER_2", DEFAULT_REPORTS_FOLDER_2.getAbsolutePath());
        saveSettings();
    }

    public void setStartAnalysisWithAppStart3(boolean START_ANALYSIS_WITH_APP_START_3) {
        currentSettings.put("START_ANALYSIS_WITH_APP_START_3", START_ANALYSIS_WITH_APP_START_3);
        saveSettings();
    }

    public void setSaveReportsAutomatically4(boolean SAVE_REPORTS_AUTOMATICALLY_4) {
        currentSettings.put("SAVE_REPORTS_AUTOMATICALLY_4", SAVE_REPORTS_AUTOMATICALLY_4);
        saveSettings();
    }

    public void setSliderSize5(double SLIDER_SIZE_5) {
        currentSettings.put("SLIDER_SIZE_5", SLIDER_SIZE_5);
        saveSettings();
    }

    public void setSliderNumber6(double SLIDER_NUMBER_6) {
        currentSettings.put("SLIDER_NUMBER_6", SLIDER_NUMBER_6);
        saveSettings();
    }

    public void setStartAppWithWindows(boolean START_APP_WITH_WINDOWS_7) throws IOException {
        AddToStartUp util = new AddToStartUp();
        if (!util.isAdded())
            util.add();
        currentSettings.put("START_APP_WITH_WINDOWS_7", START_APP_WITH_WINDOWS_7);
        saveSettings();
    }

    public File getDEFAULT_ANALYZED_FOLDER_1() {
        return new File((String) currentSettings.get("DEFAULT_ANALYZED_FOLDER_1"));
    }

    public File getDEFAULT_REPORTS_FOLDER_2() {
//        return new File((String)null);
        return new File((String) currentSettings.get("DEFAULT_REPORTS_FOLDER_2"));
    }

    public boolean isSTART_ANALYSIS_WITH_APP_START_3() {
        return (boolean) currentSettings.get("START_ANALYSIS_WITH_APP_START_3");
    }

    public boolean isSAVE_REPORTS_AUTOMATICALLY_4() {
        return (boolean) currentSettings.get("SAVE_REPORTS_AUTOMATICALLY_4");
    }

    public double getSLIDER_SIZE_5() {
        return (double) currentSettings.get("SLIDER_SIZE_5");
    }

    public double getSLIDER_NUMBER_6() {
        return (double) currentSettings.get("SLIDER_NUMBER_6");
    }


    public boolean isSTART_APP_WITH_WINDOWS_7() {
        return (boolean) currentSettings.get("START_APP_WITH_WINDOWS_7");
    }

    public void saveSettings() {
        JSONObject ob = new JSONObject(
                new HashMap<
                        String,
                        HashMap<String, Object>>()
        );
        if (currentSettings.size() == 0) {
            currentSettings.putAll(defaultSettings);
        }
        ob.put("Current_settings", currentSettings);
        fw = new FileWorker();
        fw.write(CONFIG_FILE, ob.toJSONString());
    }

    public void restoreSettings() {
        JSONParser parser = new JSONParser();
        fw = new FileWorker();
        try {
            if (!new File(CONFIG_FILE).exists() || new File(CONFIG_FILE).length() == 0) {
                System.err.println("Settings file does not exist!");
                saveSettings();
            }
            JSONObject ob = (JSONObject) parser.parse(fw.read(CONFIG_FILE));
            this.currentSettings = (HashMap<String, Object>) ob.get("Current_settings");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
