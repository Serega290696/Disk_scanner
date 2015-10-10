package myProject.model;

import java.io.File;
import java.io.Serializable;


public class ResultFile extends File implements Comparable<File>, Serializable {
    private final long finallySize;

    public ResultFile(String pathname, long size) {
        super(pathname);
        finallySize = size;
    }

    public long getFinallySize() {
        return finallySize;
    }

}
