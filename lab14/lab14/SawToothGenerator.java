package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    int period, state;
    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    @Override
    public double next() {
        state ++;
        return normalize();
    }
    protected double normalize() {
        state %= period;
        return ((double) state) / period * 2 - 1;
    }
}
