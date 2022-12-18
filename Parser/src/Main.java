import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        parser.First();
        parser.Follow();
        System.out.println(parser.first);
        System.out.println(parser.follow);
    }
}
