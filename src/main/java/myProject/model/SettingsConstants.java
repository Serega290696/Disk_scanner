package myProject.model;

import java.io.File;
import java.io.IOException;

/**
 * Created by serega on 09.09.2015.
 */
public final class SettingsConstants {

    private static FileWorker fw = new FileWorker();

    public static File DEFAULT_ANALYZED_FOLDER_1;
    public static File DEFAULT_REPORTS_FOLDER_2;
    public static boolean START_ANALYSIS_WITH_APP_START_3;
    public static boolean SAVE_REPORTS_AUTOMATICALLY_4;
    public static double SLIDER_SIZE_5;
    public static double SLIDER_NUMBER_6;
    public static boolean START_APP_WITH_WINDOWS_7;

    public static final String REGEX2 = "\n";
//    public static final String REGEX1 = "***";

    /*
            ..
            reports
            false
            false
            24
            100
            false
             */
    static {
//        System.out.println(new File("config.dat").exists());
//        System.out.println(fw.read("config.dat").split("\n").length);
        if (new File("config.dat").exists() && fw.read("config.dat").split(REGEX2).length == 7) {
            String parameters[] = fw.read("config.dat").split(REGEX2);
            DEFAULT_ANALYZED_FOLDER_1 = new File(
                    parameters[0]
            );
            DEFAULT_REPORTS_FOLDER_2 = new File(
                    parameters[1]
            );
            START_ANALYSIS_WITH_APP_START_3 = Boolean.parseBoolean(parameters[2]);
            SAVE_REPORTS_AUTOMATICALLY_4 = Boolean.valueOf(parameters[3]);
            SLIDER_SIZE_5 = Double.parseDouble(parameters[4].substring(0, parameters[4].length() - 0));
            SLIDER_NUMBER_6 = Double.parseDouble(parameters[5].substring(0, parameters[5].length() - 0));
            START_APP_WITH_WINDOWS_7 = Boolean.parseBoolean(parameters[6]);
        } else {
            System.err.println("Settings file does not exist!");
            DEFAULT_ANALYZED_FOLDER_1 = new File("..\\");
            DEFAULT_REPORTS_FOLDER_2 = new File("reports");
            START_ANALYSIS_WITH_APP_START_3 = false;
            SAVE_REPORTS_AUTOMATICALLY_4 = false;
            SLIDER_SIZE_5 = 24;
            SLIDER_NUMBER_6 = 100;
            START_APP_WITH_WINDOWS_7 = false;
        }
//        System.out.println("========== SETTINGS ==========");
//        System.out.println(DEFAULT_ANALYZED_FOLDER_1);
//        System.out.println(DEFAULT_REPORTS_FOLDER_2);
//        System.out.println(START_ANALYSIS_WITH_APP_START_3);
//        System.out.println(SAVE_REPORTS_AUTOMATICALLY_4);
//        System.out.println(SLIDER_SIZE_5);
//        System.out.println(SLIDER_NUMBER_6);
//        System.out.println(START_APP_WITH_WINDOWS_7);
//        System.out.println("==========          ==========");
//        System.out.println(Arrays.toString(fw.read("config.dat").split(REGEX2)));
//        System.out.println("========== SETTINGS ==========");
    }

    public static void setDefaultAnalyzedFolder1(File defaultAnalyzedFolder1) {
        DEFAULT_ANALYZED_FOLDER_1 = defaultAnalyzedFolder1;
        String newSettings[] = fw.read("config.dat").split(REGEX2);
        newSettings[0] = defaultAnalyzedFolder1.getAbsoluteFile().toString();
        fw.write("config.dat",
                combine(newSettings)
        );
    }

    public static void setDefaultReportsFolder2(File defaultReportsFolder2) {
        DEFAULT_REPORTS_FOLDER_2 = defaultReportsFolder2;
        String newSettings[] = fw.read("config.dat").split(REGEX2);
        newSettings[1] = defaultReportsFolder2.getAbsoluteFile().toString();
        fw.write("config.dat",
                combine(newSettings)
        );
    }

    public static void setStartAnalysisWithAppStart3(boolean startAnalysisWithAppStart3) {
        START_ANALYSIS_WITH_APP_START_3 = startAnalysisWithAppStart3;
        String newSettings[] = fw.read("config.dat").split(REGEX2);
        newSettings[2] = Boolean.toString(startAnalysisWithAppStart3);
        fw.write("config.dat",
                combine(newSettings)
        );
    }

    public static void setSaveReportsAutomatically4(boolean saveReportsAutomatically4) {
        SAVE_REPORTS_AUTOMATICALLY_4 = saveReportsAutomatically4;
        String newSettings[] = fw.read("config.dat").split(REGEX2);
        for(String s : newSettings) {
            System.out.println(s);
        }
        newSettings[3] = Boolean.toString(saveReportsAutomatically4);
        fw.write("config.dat",
                combine(newSettings)
        );
    }

    public static void setSliderSize5(double sliderSize5) {
        SLIDER_SIZE_5 = sliderSize5;
        String newSettings[] = fw.read("config.dat").split(REGEX2);
        newSettings[4] = String.valueOf(sliderSize5);
        fw.write("config.dat",
                combine(newSettings)
        );
    }

    public static void setSliderNumber6(double sliderNumber6) {
        SLIDER_NUMBER_6 = sliderNumber6;
        String newSettings[] = fw.read("config.dat").split(REGEX2);
        newSettings[5] = String.valueOf(sliderNumber6);
        fw.write("config.dat",
                combine(newSettings)
        );
    }

    public static void setStartAppWithWindows(boolean startAppWithWindows) {
        try {
            if (START_APP_WITH_WINDOWS_7 != startAppWithWindows) {
                START_APP_WITH_WINDOWS_7 = startAppWithWindows;
                AddToStartUp.add();
                String newSettings[] = fw.read("config.dat").split(REGEX2);
                newSettings[6] = String.valueOf(startAppWithWindows);
                fw.write("config.dat",
                        combine(newSettings)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String combine(String[] newSettings) {
        String a = "";
        for (String t : newSettings) {
            a += t + REGEX2;
        }
        System.out.println(a);
        return a;
    }
}
