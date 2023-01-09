import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Parser parser = new Parser("g3.in");
        ParserOutput parserOutput = new ParserOutput(parser);
        parserOutput.generateParsingTable();
//        for(Pair<String, String> key: parserOutput.parsingTable.keySet()){
//            System.out.println(key + " -> " + parserOutput.parsingTable.get(key));
//        }
        parserOutput.printParsingTable();
//        System.out.println(parser.first);
//        System.out.println(parser.follow);

        Tests tests = new Tests();
        tests.runTests();
    }
}
