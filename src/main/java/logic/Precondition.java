package logic;

public class Precondition {
    public static void checkArgument(boolean toCheck) {
        if (!toCheck) {
            throw new IllegalArgumentException();
        }
    }
}
