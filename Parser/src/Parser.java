import java.util.*;


public class Parser {

    Grammar grammar;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    List<String> concatenationOfSizeOne(List<String>L1, List<String> L2){
        List<String> concat = new ArrayList<>();
        boolean epsilonInL1 = false;
        for (String terminal : L1){
            if (!Objects.equals(terminal, "epsilon")){
                if(!concat.contains(terminal))
                    concat.add(terminal);
            }
            else{
                epsilonInL1 = true;
            }
        }
        if (epsilonInL1){
            for (String terminal2 : L2){
                if (!Objects.equals(terminal2, "epsilon")){
                    if(!concat.contains(terminal2))
                        concat.add(terminal2);
                }
            }
        }
        return concat;
    }

    void First(){
        List<Pair<String,List<String>>> tabelPast = new ArrayList<>();
        List<Pair<String,List<String>>> tabelCurrent = new ArrayList<>();
        // F0
        for(String nonterminal : grammar.getNonterminals()){
            List<Production> productions = grammar.getProductionForNonterminal(nonterminal);
            for (Production production : productions){
                List<String> cell = new ArrayList<>();
                for (var rightPart: production.right) {
                    if (grammar.getTerminals().contains(rightPart)) {
                        cell.add(rightPart);
                    }
                }
                tabelCurrent.add(new Pair<>(nonterminal,cell));
            }
        }
        // END F0
        // START F1
        boolean OK = true;
        tabelPast = tabelCurrent;
        tabelCurrent = new ArrayList<>();
        while( OK){
            //for (String nonterminal : tabelPast.)
        }

    }

    void Follow(){

    }
}
