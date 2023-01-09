import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParserOutput {

    private static final String POP = "POP";
    private static final String ACC = "ACC";
    private static final String EMPTY_STACK = "$";
    HashMap<Pair<String, String>, Pair<List<String>, Integer>> parsingTable = new HashMap<>();

    Parser parser;

    Grammar grammar;

    public ParserOutput(Parser parser){
        this.parser = parser;
        this.parser.First();
        this.parser.Follow();
        grammar = parser.getGrammar();
    }

    private List<String> concatenateProduction(List<String> rightPartProduction){
        var res = parser.first.get(rightPartProduction.get(0));
        for(int i=1; i<rightPartProduction.size(); i++){
            res = parser.concatenationOfSizeOne(res, parser.first.get(rightPartProduction.get(i)));
        }
        return res;
    }

    public void generateParsingTable(){

        for(String terminal : grammar.getTerminals()){
            var key = new Pair<>(terminal, terminal);
            var value = new Pair<>(List.of(POP), -1);
            parsingTable.put(key, value);
        }

        parsingTable.put(new Pair<>(EMPTY_STACK, EMPTY_STACK), new Pair<>(List.of(ACC), -1));

        for(String nonTerminal : grammar.getNonterminals()){
            for(Production production : grammar.getProductionForNonterminalInLHS(nonTerminal)){
                var firstRightPart = concatenateProduction(production.right);
                var hasEpsilon = false;
                for(String symbol: firstRightPart){
                    if(!symbol.equals(Parser.EPSILON)){
                        var key = new Pair<>(nonTerminal, symbol);
                        var value = new Pair<>(production.right, grammar.getProductionIndex(production));
                        if(parsingTable.containsKey(key))
                            throw new RuntimeException("CONFLICT");
                        parsingTable.put(key, value);
                    }
                    else{
                        var key = new Pair<>(nonTerminal, EMPTY_STACK);
                        var value = new Pair<>(production.right, grammar.getProductionIndex(production));
                        if(parsingTable.containsKey(key))
                            throw new RuntimeException("CONFLICT");
                        parsingTable.put(key, value);
                        hasEpsilon = true;
                    }
                }
                if(hasEpsilon){
                    for(String symbol: parser.follow.get(nonTerminal)){
                        var key = new Pair<>(nonTerminal, symbol);
                        var value = new Pair<>(production.right, grammar.getProductionIndex(production));
                        if(parsingTable.containsKey(key))
                            throw new RuntimeException("CONFLICT");
                        parsingTable.put(key, value);
                    }
                }
            }
        }

        var key = new Pair<>(EMPTY_STACK, EMPTY_STACK);
        var value = new Pair<>(List.of(ACC), -1);
        parsingTable.put(key, value);
    }

    public void printParsingTable(){

        var maxLen = 0;
        for(Pair<String, String> key: parsingTable.keySet()){
            if(parsingTable.get(key).toString().length() > maxLen)
                maxLen = parsingTable.get(key).toString().length();
        }

        if(maxLen % 2 == 1)
            maxLen++;

        StringBuilder res = new StringBuilder();

        var l = new ArrayList<>(grammar.getNonterminals());
        l.addAll(grammar.getTerminals());
        l.add(EMPTY_STACK);
        var l2 = new ArrayList<>(grammar.getTerminals());
        l2.add(EMPTY_STACK);

        res.append(" ".repeat(5));
        for(String symbol: l2){
            var space = maxLen / 2;
            res.append(" ".repeat(Math.max(0, space)));
            res.append(symbol);
            res.append(" ".repeat(Math.max(0, space)));
        }
        res.append("\n").append("-".repeat(maxLen * l2.size())).append("\n");

        int i=0;
        for(String symbol: l){
            res.append(" ").append(l.get(i)).append(" |");
            i++;
            for(String terminal: l2){
                var key = new Pair<>(symbol, terminal);
                var isSet = false;
                for(Pair<String, String> tableKey: parsingTable.keySet()){
                    if(tableKey.getFirst().equals(key.getFirst()) && tableKey.getSecond().equals(key.getSecond())){
                        isSet = true;
                        key = tableKey;
                        break;
                    }
                }
                if(isSet){
                    var space = (maxLen - parsingTable.get(key).toString().length()) / 2;
                    res.append(" ".repeat(Math.max(0, space)));
                    res.append(parsingTable.get(key));
                    res.append(" ".repeat(Math.max(0, space)));
                }
                else {
                    res.append("-".repeat(maxLen));
                }
                res.append("|");
            }
            res.append("\n");
        }
        System.out.println(res);
    }

}
