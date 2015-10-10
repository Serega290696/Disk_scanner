package myProject.model;

import java.io.File;
import java.io.IOException;

/**
 * Created by serega.
 */
public class AddToStartUp {

    private static boolean isAdded;

    public static void add() throws IOException {
        String cmdCommand;
        if (!new File(cmdCommand = "src\\main\\resources\\startupCommand").exists()) {
            if (!new File(cmdCommand = "config\\startupCommand").exists())
                cmdCommand = "startupCommand";
        }
        cmdCommand+="\\admin_link";
        Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start", cmdCommand});
        isAdded = true;
    }


    public static boolean isAdded() {
        return isAdded;
    }

}
