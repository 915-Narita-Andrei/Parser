import java.util.HashMap;
import java.util.List;

public class Tests {

    public void runTests(){
        TestFirst();
//        TestFollow();
    }

    private void TestFirst(){
        Parser parser = new Parser("g3.in");
        parser.First();
        var expected = new HashMap<>();
        expected.put("S", List.of("(","a"));
        expected.put("A", List.of("+",Parser.EPSILON));
        expected.put("B", List.of("(","a"));
        expected.put("C", List.of("*",Parser.EPSILON));
        expected.put("D", List.of("(","a"));
        for(String key:parser.first.keySet()){
            assert parser.first.get(key).equals(expected.get(key)) : "First not correct for NonTerminal " + key;
        }
    }

    private void TestFollow(){
        Parser parser = new Parser("g3.in");
        parser.Follow();
        var expected = new HashMap<>();
        expected.put("S", List.of("(",Parser.EPSILON));
        expected.put("A", List.of("(",")"));
        expected.put("B", List.of("+",Parser.EPSILON, ")"));
        expected.put("C", List.of("+",Parser.EPSILON, ")"));
        expected.put("D", List.of("*",Parser.EPSILON, "+"));
        for(String key:parser.follow.keySet()){
            assert parser.follow.get(key).equals(expected.get(key)) : "Follow not correct for NonTerminal " + key;
        }
    }
}