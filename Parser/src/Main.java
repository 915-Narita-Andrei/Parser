import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Parser parser = new Parser("g3.in");
        ParserOutput parserOutput = new ParserOutput(parser);
        parserOutput.generateParsingTable();

//        parserOutput.printParsingTable();
//        System.out.println(parser.first);
//        System.out.println(parser.follow);

        parserOutput.parseSequence(List.of("a","*","(","a","+","a",")"));
//        parserOutput.parseSequence(List.of("a","b","a","b"));

        Tests tests = new Tests();
        tests.runTests();
    }
}
