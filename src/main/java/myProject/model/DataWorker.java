package myProject.model;

/**
 * Created by serega on 05.09.2015.
 */
public class DataWorker {

    private String[] separator1 = {"", ".", ". ", ".", "."};
    private String[] separator2 = {"bytes", "KB ", "MB ", "GB ", "TB "};
    private String[] separator3 = {" bytes", " KB ", " MB ", " GB ", " TB "};
    private String[] chosenSeparator = separator3;
    private final int divideValue = 1024;

    public String convert(long length) {
        return convert(length, false);
    }

    public String convert(long length, boolean cut) {
        StringBuilder a = new StringBuilder();
//        ArrayList<String> b = new ArrayList<>();
        long c = length % divideValue;
        try {
            a.insert(0, c + chosenSeparator[0]);
            length -= c;
            length /= divideValue;
            c = length % divideValue;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[1]);

            length -= c;
            length /= divideValue;
            c = length % divideValue;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[2]);

            length -= c;
            length /= divideValue;
            c = length % divideValue;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[3]);

            length -= c;
            length /= divideValue;
            c = length % divideValue;
            if (c == 0) return new String(a);
            a.insert(0, c + chosenSeparator[4]);
        } finally {

            String res = new String(a);
            if (cut) {
//                System.out.println(res.indexOf('B'));
                return res.substring(0, res.indexOf('B') + 1);
            } else {
                return res;
            }
        }
    }


}
