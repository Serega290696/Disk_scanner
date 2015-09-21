package myProject.model;

import java.io.*;

/**
 * Created by serega on 08.09.2015.
 */
public class FileWorker {

    public String read(String fileName) {
        return read(new File(fileName));
    }

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
                new File(file.getAbsoluteFile().toString().substring(0, file.getAbsoluteFile().toString().lastIndexOf("\\"))).mkdirs();
                file.createNewFile();
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

    public void write(String fileName, String dataWrite) {
//        System.out.println("A");
        write(new File(fileName), dataWrite);
    }
}
