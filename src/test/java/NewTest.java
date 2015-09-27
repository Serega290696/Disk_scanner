import myProject.model.FileWorker;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.Random;
import java.util.function.Function;
/**
 * Created by serega on 05.09.2015.
 */
public class NewTest {
    @Test
    public void test5() throws ParseException {
//        System.out.println(map.entrySet().et("a").);
    }

    @Test
    public void test4() throws Exception {
        FileWorker fw = new FileWorker();
        fw.write("a.txt", "\n");//LF
        fw.write("a.txt", "\r");//CR

    }

    @Test
    public void test3() throws Exception {
        String a = "ppp;asd;s;;ds;";
        String b[] = a.split(";");
        b[1] = "NEW";
        String c;
        for (String b1 : b) {
            System.out.println(b1.length());
            System.out.println(b1);

        }
        System.out.println(b);
    }

    @Test
    public void test2() {

        Random r = new Random();
        final int NUMBERS_AMOUNT = 100;
        Function<Double, Double> fun = aDouble -> aDouble * aDouble;
        int counter = 0;

        double mass[][] = new double[2][NUMBERS_AMOUNT];
        for (int i = 0; i < NUMBERS_AMOUNT; i++) {
            mass[0][i] = r.nextDouble();
            mass[1][i] = r.nextDouble();
        }
        for (int i = 0; i < NUMBERS_AMOUNT; i++) {
            if (fun.apply(mass[0][i]) < mass[1][i])
                counter++;
//            mass[1][i] = fun.apply(mass[0][i]);
        }
        System.out.println(counter + " / " + NUMBERS_AMOUNT);
        System.out.println((double) counter / NUMBERS_AMOUNT);
        for (double d : mass[0]) {
            System.out.format("%f3 ", d);
        }
        System.out.println();
        for (double d : mass[1]) {
            System.out.format("%f3 ", d);
        }

//        for(Double d : )
    }

    @Test
    public void test1() {


        Thread shutdownThread = new Thread(() ->
        {
            System.out.println("Bye");
        }
        );
        Runtime.getRuntime().addShutdownHook(shutdownThread);
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (char c : str.toCharArray()) {
            System.out.println(c + ": " + new File(String.valueOf(c) + ":\\").length());

        }


//
//  System.out.println(Runtime.getRuntime().availableProcessors());
////        Runtime.getRuntime().exec("notepad.exe");
//        Runtime.getRuntime().exec("command md aa");
//
//        System.out.println(Runtime.getRuntime().freeMemory());
//        Runtime.getRuntime().halt(55);
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(15);
//        list.add(1);
//        list.add(4);
//        list.add(10);
//        list.add(11);
//        list.add(20);
//
//        System.out.println(list.get(4));
//        System.out.println(list.size());
//        list.add(4, 20);
//        System.out.println(list.get(4));
//        System.out.println(list.get(5));
//        System.out.println(list.size());

//        long a = 0;
//        long b = 100_000_000_000_001L;
//        System.out.println(b-a);
//        System.out.println((int)(b-a));


//        String a = "D:\\Downloads\\2.mkv";
//        File f = new File(a);
//        System.out.println(f.getName());
//        System.out.println(f.isDirectory());
//        System.out.println(f.isFile());
//        System.out.println(f.exists());
//
//        System.out.println(f.getAbsolutePath());
//        long bt = new Date().getTime();
//        System.out.println(f.length());
//        System.out.println(new Date().getTime() - bt);

    }

}
