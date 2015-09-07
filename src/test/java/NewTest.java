import org.junit.Test;

import java.io.File;

/**
 * Created by serega on 05.09.2015.
 */
public class NewTest {

    @Test
    public void test1() {



        Thread shutdownThread = new Thread(()->
                {
                        System.out.println("Bye");
                }
        );
        Runtime.getRuntime().addShutdownHook(shutdownThread);
String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(char c : str.toCharArray()) {
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
