public class OffByN implements CharacterComparator {
    private int diff;
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs((int) x - (int) y) == diff;
    }
    public OffByN(int N) {
        diff = N;
    }
}
