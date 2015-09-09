package myProject.model;

import java.io.*;

/**
 * Created by serega on 08.09.2015.
 */
public class FileWorker {

    public String read(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(
                    new FileInputStream(
                            new File(fileName)
                    )
            );

            int character = in.read();
            while (character != -1) {
                sb.append((char) character);
                character = in.read();
            }
            in.close();
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(sb);
    }

    public void write(File file, String dataWrite) {
        try {
            if (!file.exists())
                file.createNewFile();
            OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(
                            file
                    )
            );
            out.write(dataWrite);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String fileName, String dataWrite) {
        write(new File(fileName), dataWrite);
    }
}
