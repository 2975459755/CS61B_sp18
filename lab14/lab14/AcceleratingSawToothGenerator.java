package lab14;

public class AcceleratingSawToothGenerator extends SawToothGenerator {
    double acceleration;
    public AcceleratingSawToothGenerator(int period, double acceleration) {
        super(period);
        this.acceleration = acceleration;
    }

    @Override
    protected double normalize() {
        if (state >= period) {
            state %= period;
            period *= acceleration;
        } else {
            state %= period;
        }
        return ((double) state) / period * 2 - 1;
    }
}
