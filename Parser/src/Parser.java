import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class Parser {
    public static final String EPSILON = "epsilon";
    Grammar grammar;

    public Grammar getGrammar() {
        return grammar;
    }

    public HashMap<String, List<String>> first = new HashMap<>();
    public HashMap<String, List<String>> follow = new HashMap<>();

    Parser(String file){
        grammar = new Grammar(file);
    }

    List<String> concatenationOfSizeOne(List<String> l1, List<String> l2) { //TODO:luam doar primu caracter?
        if (l1 == null && l2 == null)
            return List.of();
        if (l1 == null)
            l1 = List.of();
        if (l2 == null)
            l2 = List.of();
        List<String> concatenation = new ArrayList<>();
        boolean epsilonInL1 = false;
        for (String terminal : l1) {
            if (!Objects.equals(terminal, EPSILON)) {
                var symbol = terminal.charAt(0);
                if (!concatenation.contains(String.valueOf(symbol)))
                    concatenation.add(String.valueOf(symbol));
            } else {
                epsilonInL1 = true;
            }
        }
        if (epsilonInL1) {
            for (String terminal : l2) {
                if (!Objects.equals(terminal, EPSILON)) {
                    var symbol = terminal.charAt(0);
                    if (!concatenation.contains(String.valueOf(symbol))) {
                        concatenation.add(String.valueOf(symbol));
                    }
                }
            }
        }
        return concatenation;
    }

    public void First() {
        HashMap<String, List<String>> columnPast = new HashMap<>();
        HashMap<String, List<String>> columnCurrent = new HashMap<>();

        for (String nonterminal : grammar.getNonterminals()) {
            List<Production> productions = grammar.getProductionForNonterminalInLHS(nonterminal);
            List<String> cell = new ArrayList<>();
            for (Production production : productions) {
                if (grammar.getTerminals().contains(production.right.get(0))) {
                    cell.add(production.right.get(0));
                } else if (production.right.get(0).equals(EPSILON)) {
                    cell.add(production.right.get(0));
                }
                columnCurrent.put(nonterminal, cell);
            }
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            columnPast = new HashMap<>(columnCurrent);
            columnCurrent = new HashMap<>();
            for (String nonterminal : grammar.getNonterminals()) {
                for (Production production : grammar.getProductionForNonterminalInLHS( nonterminal)) {
                    var currentRight = production.right;
                    List<String> currentConcatenation;
                    if (grammar.getTerminals().contains(currentRight.get(0))) {
                        currentConcatenation = List.of(currentRight.get(0));
                    } else if (currentRight.get(0).equals(EPSILON)) {
                        currentConcatenation = List.of(EPSILON);
                    } else {
                        currentConcatenation = columnPast.get(currentRight.get(0));
                    }
                    for (int i = 1; i < currentRight.size(); i++) {
                        if (grammar.getTerminals().contains(currentRight.get(i))) {
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
                if (columnPast.get(nonterminal).size() != columnCurrent.get(nonterminal).size())
                    changed = true;
            }
        }
        first = columnCurrent;

        for(String terminal: grammar.getTerminals()){
            first.put(terminal, List.of(terminal));
        }
        first.put(Parser.EPSILON, List.of(Parser.EPSILON));
    }

    private List<String> eliminateDuplicate(List<String> l) {
        List<String> rez = new ArrayList<>();
        for (String symbol : l) {
            if (!rez.contains(symbol))
                rez.add(symbol);
        }
        return rez;
    }

    public void Follow() {
        HashMap<String, List<String>> columnPast = new HashMap<>();
        HashMap<String, List<String>> columnCurrent = new HashMap<>();
        for (String nonterminal : grammar.getNonterminals()) {
            columnCurrent.put(nonterminal, List.of());
        }
        columnCurrent.put(grammar.getStart(), List.of(EPSILON));
        boolean changed = true;
        while (changed) {
            changed = false;
            columnPast = new HashMap<>(columnCurrent);
            columnCurrent = new HashMap<>();
            for (String nonterminal : grammar.getNonterminals()) {
                var cell = new ArrayList<String>();
                cell.addAll(columnPast.get(nonterminal));
                var productions = grammar.getProductionForNonterminalInRHS(nonterminal);
                for (Production production : productions) {
                    var nonTerminalPosition = production.right.indexOf(nonterminal);
                    if(nonTerminalPosition < production.right.size() - 1){
                        for(String symbol: first.get(production.right.get(nonTerminalPosition + 1))){
                            if(symbol.equals(EPSILON)){
                                cell.addAll(columnPast.get(production.left.get(0)));
                            }
                            else {
                                cell.addAll(first.get(production.right.get(nonTerminalPosition + 1)));
                            }
                        }
                    }
                    else{
                            cell.addAll(columnPast.get(production.left.get(0)));
                    }
                }
                columnCurrent.put(nonterminal, eliminateDuplicate(cell));
                if (columnPast.get(nonterminal).size() != columnCurrent.get(nonterminal).size())
                    changed = true;
            }
        }
        follow = columnCurrent;
    }
}