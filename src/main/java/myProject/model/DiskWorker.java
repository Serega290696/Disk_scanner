package myProject.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by serega.
 */
public class DiskWorker {

    private final static List<String> finalList = Collections.unmodifiableList(new ArrayList<String>(){{
        add("C:\\Program Files");
        add("C:\\Program Files (x86)");
        add("C:\\Users");
    }});

    public String[] getOftenUsedPaths() {
        LinkedHashSet<String> linkedSetList = getDisks();
        File tmpFile;
        for(String fname : finalList) {
            tmpFile = new File(fname);
            if(tmpFile.exists())
                linkedSetList.add(fname);
        }
        return linkedSetList.toArray(new String[2]);
    }

    private LinkedHashSet<String> getDisks() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toUpperCase();
        LinkedHashSet <String> linkedSetList = new LinkedHashSet<>();
        for (char c : str.toCharArray()) {
            if (new File(String.valueOf(c + ":\\")).exists())
                linkedSetList.add(c + ":\\");
        }
        return linkedSetList;
    }

    public boolean isDisk(File tmpFile) {
        return getDisks().stream()
                .anyMatch(
                        (d) ->
            tmpFile.getAbsoluteFile().toString().equals(d)
        );
    }
}
