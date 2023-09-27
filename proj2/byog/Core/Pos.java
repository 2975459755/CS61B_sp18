package byog.Core;

class Pos {
    int x;
    int y;
    Pos (int xCoor, int yCoor) {
        x = xCoor;
        y = yCoor;
    }
    double distance(Pos des) {
        return Math.sqrt(Math.pow(this.x - des.x, 2)
                + Math.pow(this.y - des.y, 2));
    }
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Pos)){
            return false;
        }
        if (obj == this) {
            return true;
        }

        Pos p = (Pos) obj;
        if (p.x == this.x && p.y == this.y) {
            return true;
        }
        return false;
    }

}
