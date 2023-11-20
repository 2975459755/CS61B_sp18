package lab14;

public class StrangeBitwiseGenerator extends SawToothGenerator {
    public StrangeBitwiseGenerator(int period) {
        super(period);
    }
    @Override
    protected double normalize() {
        state %= period;
        int weirdState = state & (state >> 3) % period;
//        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return ((double) weirdState) / period * 2 - 1;
    }
}
