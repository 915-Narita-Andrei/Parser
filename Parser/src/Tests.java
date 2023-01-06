import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Tests {

    public void runTests(){
        TestFirstG3();
        TestFollowG3();
        TestFirstG4();
        TestFollowG4();
    }

    public static boolean listEqualsIgnoreOrder(List<String> list1, List<String> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    private void TestFirstG3(){
        Parser parser = new Parser("g3.in");
        parser.First();
        var expected = new HashMap<String, List<String>>();
        expected.put("S", List.of("(","a"));
        expected.put("A", List.of("+",Parser.EPSILON));
        expected.put("B", List.of("(","a"));
        expected.put("C", List.of("*",Parser.EPSILON));
        expected.put("D", List.of("(","a"));
        for(String key:parser.grammar.getNonterminals()){
            if(!listEqualsIgnoreOrder(parser.first.get(key), expected.get(key)))
                throw new RuntimeException("G3: First not correct for NonTerminal " + key);
        }
    }

    private void TestFollowG3(){
        Parser parser = new Parser("g3.in");
        parser.First();
        parser.Follow();
        var expected = new HashMap<String, List<String>>();
        expected.put("S", List.of(")",Parser.EPSILON));
        expected.put("A", List.of(Parser.EPSILON,")"));
        expected.put("B", List.of("+",Parser.EPSILON, ")"));
        expected.put("C", List.of("+",Parser.EPSILON, ")"));
        expected.put("D", List.of("*",Parser.EPSILON, "+", ")"));
        for(String key:parser.grammar.getNonterminals()){
            if(!listEqualsIgnoreOrder(parser.follow.get(key), expected.get(key)))
                throw new RuntimeException("G3: Follow not correct for NonTerminal " + key);
        }
    }

    private void TestFirstG4(){
        Parser parser = new Parser("g4.in");
        parser.First();
        var expected = new HashMap<String, List<String>>();
        expected.put("S", List.of("a","b","c"));
        expected.put("A", List.of("a","c"));
        expected.put("B", List.of("b","c"));
        expected.put("C", List.of("c"));
        expected.put("D", List.of("b",Parser.EPSILON));
        for(String key:parser.grammar.getNonterminals()){
            if(!listEqualsIgnoreOrder(parser.first.get(key), expected.get(key)))
                throw new RuntimeException("G4: First not correct for NonTerminal " + key);
        }
    }

    private void TestFollowG4(){
        Parser parser = new Parser("g4.in");
        parser.First();
        parser.Follow();
        var expected = new HashMap<String, List<String>>();
        expected.put("S", List.of(Parser.EPSILON));
        expected.put("A", List.of("b",Parser.EPSILON));
        expected.put("B", List.of("a","c"));
        expected.put("C", List.of("a","c"));
        expected.put("D", List.of("c"));
        for(String key:parser.grammar.getNonterminals()){
            if(!listEqualsIgnoreOrder(parser.follow.get(key), expected.get(key)))
                throw new RuntimeException("G4: Follow not correct for NonTerminal " + key);
        }
    }
}