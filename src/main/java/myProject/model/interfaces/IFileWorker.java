package myProject.model.interfaces;

import java.io.*;

/**
 * Created by serega.
 */
public interface IFileWorker {
    default String read(String fileName) {
        return read(new File(fileName));
    }

    String read(File file) ;

    void write(File file, String dataWrite) ;

    default void write(String fileName, String dataWrite) {
        write(new File(fileName), dataWrite);
    }
}
