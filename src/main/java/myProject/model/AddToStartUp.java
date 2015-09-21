package myProject.model;

import java.io.IOException;

/**
 * Created by serega on 07.09.2015.
 */
public class AddToStartUp {

    private static boolean isAdded;

    public static void add() throws IOException {
//        String cmdCommand = "Set pathBat=D:\\1.bat\n" +
//                "Reg Add \"HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run\" /v \"MyBat\" /t REG_SZ /d \"%pathBat%\" /f";
        String cmdCommand = "src\\main\\resources\\startupCommand\\admin_link";
        String cmdCommand2 = "dir";
        Process pr = Runtime.getRuntime().exec(new String[]{"cmd.exe","/c","start", cmdCommand});
        isAdded = true;
//        Process pr2 = Runtime.getRuntime().exec(new String[]{"cmd.exe","dir"});
    }

    public static void remove() {

    /*
        Set pathBat=C:\Program Files\Test\Mybat.bat
        Reg Add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Run" /v "MyBat" /t REG_SZ /d "%pathBat%" /f*/

    }

    public static boolean isAdded() {
        return isAdded;
    }

}
