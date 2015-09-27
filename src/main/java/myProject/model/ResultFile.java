package myProject.model;

import java.io.File;
import java.io.Serializable;


public class ResultFile extends File implements Comparable<File>, Serializable {
    private long finallySize;

    public ResultFile(String pathname, long size) {
        super(pathname);
        finallySize = size;
    }

    public long getFinallySize() {
        return finallySize;
    }

    public void setFinallySize(long finallySize) {
        this.finallySize = finallySize;
    }

    public void incFinallySize(long fSize) {
        finallySize += fSize;
    }

//    public int compareTo(ResultFile o) {
//        return (int) (finallySize - o.getFinallySize());
//    }
}
