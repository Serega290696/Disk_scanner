package myProject.model;

/**
 * Created by serega.
 */
public class DataWorker {

    private String[] chosenSeparator = {" bytes", " KB ", " MB ", " GB ", " TB "};
    @SuppressWarnings("all")
    private final int DIVIDE_VALUE = 1024;

    public String convert(long length) {
        return convert(length, false);
    }

    @SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
    public String convert(long length, boolean cut) {
        StringBuilder a = new StringBuilder();
        long c = length % DIVIDE_VALUE;
        try {
            a.insert(0, c + chosenSeparator[0]);
            length -= c;
            length /= DIVIDE_VALUE;
            c = length % DIVIDE_VALUE;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[1]);

            length -= c;
            length /= DIVIDE_VALUE;
            c = length % DIVIDE_VALUE;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[2]);

            length -= c;
            length /= DIVIDE_VALUE;
            c = length % DIVIDE_VALUE;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[3]);

            length -= c;
            length /= DIVIDE_VALUE;
            c = length % DIVIDE_VALUE;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[4]);
        } finally {
            String res = new String(a);
            if (cut) {
                return res.substring(0, res.indexOf('B') + 1);
            } else {
                return res;
            }
        }
    }


}
