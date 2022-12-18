import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class Parser {
    private static final String EPSILON = "epsilon";
    Grammar grammar = new Grammar();

    public Parser() {

    }

    List<String> concatenationOfSizeOne(List<String>l1, List<String> l2){ //TODO:luam doar primu caracter?
        if(l1 == null && l2 == null)
            return List.of();
        if(l1 == null)
            l1 = List.of();
        if(l2==null)
            l2 = List.of();
        List<String> concatenation = new ArrayList<>();
        boolean epsilonInL1 = false;
        for (String terminal : l1){
            if (!Objects.equals(terminal, EPSILON)){
                var symbol = terminal.charAt(0);
                if(!concatenation.contains(String.valueOf(symbol)))
                    concatenation.add(String.valueOf(symbol));
            }
            else{
                epsilonInL1 = true;
            }
        }
        if (epsilonInL1){
            for (String terminal : l2){
                if (!Objects.equals(terminal, EPSILON)){
                    var symbol = terminal.charAt(0);
                    if(!concatenation.contains(String.valueOf(symbol))) {
                        concatenation.add(String.valueOf(symbol));
                    }
                }
            }
        }
        return concatenation;
    }

    public void First(){
        HashMap<String,List<String>> columnPast = new HashMap<>();
        HashMap<String,List<String>> columnCurrent = new HashMap<>();
        // F0
        for(String nonterminal : grammar.getNonterminals()){
            List<Production> productions = grammar.getProductionForNonterminal(nonterminal);
            List<String> cell = new ArrayList<>();
            for (Production production : productions){
                if(grammar.getTerminals().contains(production.right.get(0))){
                    //TODO: folosim doar cel mai din stanga Terminal sau oricare numa sa existe?
                        cell.add(production.right.get(0));
                } else if (production.right.get(0).equals(EPSILON)) {
                        cell.add(production.right.get(0));
                }
                columnCurrent.put(nonterminal, cell);
            }
        }
        // END F0
        // START F1
        boolean ok = true;
        while( ok ){
            ok  = false;
            columnPast = new HashMap<>(columnCurrent);
            columnCurrent = new HashMap<>();
            for(String nonterminal : grammar.getNonterminals()) {
                for(Production production : grammar.getProductionForNonterminal(nonterminal)){
                    var currentRight = production.right;
                    List<String> currentConcatenation;
                    if(grammar.getTerminals().contains(currentRight.get(0))) {
                        currentConcatenation = List.of(currentRight.get(0));
                    } else if (currentRight.get(0).equals(EPSILON)) {
                        currentConcatenation = List.of(EPSILON);
                    }
                    else {
                        currentConcatenation = columnPast.get(currentRight.get(0));
                    }
                    for(int i=1; i<currentRight.size(); i++){
                        if(grammar.getTerminals().contains(currentRight.get(i))) {
                            currentConcatenation = concatenationOfSizeOne(currentConcatenation, List.of(currentRight.get(i)));
                        } else if (currentRight.get(i).equals(EPSILON)) {
                            currentConcatenation = concatenationOfSizeOne(currentConcatenation, List.of(EPSILON));
                        } else {
                            currentConcatenation = concatenationOfSizeOne(currentConcatenation, columnPast.get(currentRight.get(i)));
                        }
                    }
                    var cell = new ArrayList<>(columnPast.get(nonterminal)); //TODO: daca avem mai multe productions pt un nonTerminal ar trebui sa facem reuniune de concatenarile lor?
                    cell.addAll(currentConcatenation);
                    columnCurrent.put(nonterminal, eliminateDuplicate(cell));
                }
                if(columnPast.get(nonterminal).size() != columnCurrent.get(nonterminal).size())
                    ok = true;
            }
        }
        System.out.println(columnCurrent);
    }
    
    private List<String> eliminateDuplicate(List<String> l){
        List<String> rez = new ArrayList<>();
        for(String symbol : l){
            if(!rez.contains(symbol))
                rez.add(symbol);
        }
        return rez;
    }

    void Follow(){

    }
}
