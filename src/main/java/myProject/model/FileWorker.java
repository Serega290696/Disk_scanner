package myProject.model;

import myProject.model.interfaces.IFileWorker;

import java.io.*;

/**
 * Created by serega.
 */
public class FileWorker implements IFileWorker {

    public String read(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(
                    new FileInputStream(
                            file
                    )
            );

            int character = in.read();
            while (character != -1) {
                sb.append((char) character);
                character = in.read();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(sb);
    }

    public void write(File file, String dataWrite) {
        try {
            if (!file.exists()) {
                if(new File(file.getParent()).mkdirs()) {
                    System.err.println("Error! File didn't create!");
                    if (file.createNewFile())
                        System.err.println("Error! File didn't create!");
                }
            }
            OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(
                            file
                    )
            );
            out.write(dataWrite);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(file.getAbsolutePath());
            e.printStackTrace();
        }
    }

}
