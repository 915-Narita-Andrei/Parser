import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Parser parser = new Parser("g4.in");
        parser.First();
        parser.Follow();
//        System.out.println(parser.first);
//        System.out.println(parser.follow);

        Tests tests = new Tests();
        tests.runTests();
    }
}
