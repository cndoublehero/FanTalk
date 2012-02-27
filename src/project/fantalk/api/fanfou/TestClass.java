package project.fantalk.api.fanfou;

import java.util.Date;

public class TestClass {

    public static void main(String[] args) {
        String a = "Thu Dec 16 16:47:24 +0000 2010";
        String b = "Thu Dec 12 06:47:24 +0000 2010";
        Date da = Parser.parseDate(a);
        Date db = Parser.parseDate(b);
        if (da != null) {
            System.out.println("da is " + da.toString());
        }
        if (db != null) {
            System.out.println("db is " + db.toString());
        }
    }

}
