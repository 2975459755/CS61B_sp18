package byog.Core;

import java.io.Serializable;

public class Pos implements Serializable {
    protected int x;
    protected int y;
    public Pos() {}
    public Pos(int xC, int yC) {
        this.x = xC;
        this.y = yC;
    }
    public boolean inMap() {
        return x >= WG.startWIDTH && x < WG.WIDTH
                && y >= WG.startHEIGHT && y < WG.HEIGHT;
    }
    public double distance(Place des) {
        return Math.sqrt(Math.pow(this.x - des.x, 2)
                + Math.pow(this.y - des.y, 2));
    }
    /**
     * L-shaped distance;
     */
    public int LDistance(Place des) {
        return Math.abs(this.x - des.x) + Math.abs(this.y - des.y);
    }

}
