package myProject.model;

import java.io.File;

/**
 * Created by serega on 05.09.2015.
 */
public class ResultFile extends File implements Comparable<File> {
    private final long finallySize;


    public ResultFile(String pathname, long a) {
        super(pathname);
        finallySize = a;
    }

    public long getFinallySize() {
        return finallySize;
    }

//    public int compareTo(ResultFile o) {
//        return (int) (finallySize - o.getFinallySize());
//    }
}
