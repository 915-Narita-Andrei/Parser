import java.util.List;

public class Production {
    List<String> left;
    List<String> right;

    public Production(List<String> left, List<String> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " -> " + right;
    }
}
