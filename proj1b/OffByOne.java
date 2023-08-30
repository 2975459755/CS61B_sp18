public class OffByOne implements CharacterComparator{

    public boolean equals(char x, char y) {
        return x == y;
    }
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs((int) x - (int) y) == 1;
    }
}
