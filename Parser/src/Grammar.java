import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Grammar {

    private List<String> terminals;
    private List<String> nonterminals;
    private String start;
    private List<Production> productions;

    public List<String> getTerminals() {
        return terminals;
    }

    public List<String> getNonterminals() {
        return nonterminals;
    }

    public String getStart() {
        return start;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public Grammar(){
        terminals = new ArrayList<>();
        nonterminals = new ArrayList<>();
        start = "";
        productions = new ArrayList<>();
        readData("g3.in");
    }

    public Grammar(String file){
        terminals = new ArrayList<>();
        nonterminals = new ArrayList<>();
        start = "";
        productions = new ArrayList<>();
        readData(file);
    }
    public void run(){
        boolean done = false;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (!done){
            printOptions();
            try {
                var line = reader.readLine();
                if (line.equals("0")) {
                    done = true;
                } else if (line.equals("1")) {
                    readData("g3.in");
                } else if (line.equals("2")) {
                    nonterminals.forEach(System.out::println);
                } else if (line.equals("3")) {
                    terminals.forEach(System.out::println);
                } else if (line.equals("4")) {
                    productions.forEach(System.out::println);
                } else if (line.equals("5")) {
                    System.out.println("Enter Nonterminal: ");
                    var nonterminal = reader.readLine();
                    printProductionForNonterminal(nonterminal);
                } else if (line.equals("6")) {
                    if (checkCFG()) {
                        System.out.println("Is CFG");
                    } else {
                        System.out.println("Is not CFG");
                    }
                }
                else {
                    System.out.println("Wrong Input!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void printOptions() {
        System.out.println("0.Exit");
        System.out.println("1.Read grammar from file");
        System.out.println("2.Print set of nonterminals");
        System.out.println("3.Print set of terminals");
        System.out.println("4.Print set of productions");
        System.out.println("5.Print set of productions for a given nonterminal");
        System.out.println("6.CFG check");
    }

    private void readData(String fileName){
        File program = new File(fileName);
        Scanner reader;
        try {
            reader = new Scanner(program);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(reader.hasNextLine()){
            var line = reader.nextLine();
            nonterminals = List.of(line.split(","));
        }
        if(reader.hasNextLine()){
            var line = reader.nextLine();
            terminals = List.of(line.split(","));
        }
        if(reader.hasNextLine()){
            var line = reader.nextLine();
            start = line;
        }
        while (reader.hasNextLine()){
            var line = reader.nextLine();
            var l = line.split("=",2);
            List<String> left = new ArrayList<>();
            left = List.of(l[0].split(","));
            List<String> right = new ArrayList<>();
            right = List.of(l[1].split(","));
            Production production = new Production(left, right);
            productions.add(production);
        }
    }

    private void printProductionForNonterminal(String nonterminal){
        getProductionForNonterminal(nonterminal).forEach(System.out::println);
    }

    public List<Production> getProductionForNonterminal(String nonterminal){ //TODO: numa in partea stanga sa ne uitam
        List<Production> terminalProductions = new ArrayList<>();
        for (Production production: productions) {
            if (production.left.contains(nonterminal)) {
                terminalProductions.add(production);
            }
//            else if(production.right.contains(nonterminal)){
//                terminalProductions.add(production);
//            }
        }
        return terminalProductions;
    }

    private boolean checkCFG(){
        for (Production production: productions) {
            if(production.left.size() > 1)
                return false;
        }
        return true;
    }
}
