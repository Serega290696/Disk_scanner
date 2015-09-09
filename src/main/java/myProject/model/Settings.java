package myProject.model;

import java.io.File;

/**
 * Created by serega on 09.09.2015.
 */
public abstract class Settings {

    public final static File DEFAULT_REPORTS_FOLDER = new File("reports");
    public static File CURRENT_REPORTS_FOLDER = DEFAULT_REPORTS_FOLDER;

    public final static File DEFAULT_ANALYZED_FOLDER = new File("..\\");
    public static File CURRENT_ANALYZED_FOLDER = DEFAULT_ANALYZED_FOLDER;

    public static boolean START_APP_WITH_WINDOWS = false;
    public static boolean START_ANALYSIS_WITH_APP_START = false;
    public static boolean SAVE_REPORTS_AUTOMATICALLY = false;


}
