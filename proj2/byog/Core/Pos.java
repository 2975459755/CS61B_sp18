package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

class Pos {
    int x;
    int y;
    Pos (int xCoor, int yCoor) {
        this.x = xCoor;
        this.y = yCoor;
    }
    double distance(Pos des) {
        return Math.sqrt(Math.pow(this.x - des.x, 2)
                + Math.pow(this.y - des.y, 2));
    }

    /**
     * L-shaped distance;
     */
    int LDistance(Pos des) {
        return Math.abs(this.x - des.x) + Math.abs(this.y - des.y);
    }
    int isLuminator(TETile[][] world) {
        TETile t = world[this.x][this.y];

        if (t == Tileset.PLAYER) {
            return 3;
        } else if (t == Tileset.UNLOCKED_DOOR){
            return 2;
        } else if (t == Tileset.LAMP_LIT) {
            return 5;
        }

        return 0;
    }
    boolean collectable(TETile[][] world) {
        TETile t = world[x][y];

        if (t == Tileset.LAMP_UNLIT) {
            return true;
        } else if (t == Tileset.LAMP_LIT) {
            return true;
        }

        return false;
    }
    void luminate(WG wg) {
        luminate(wg, isLuminator(wg.world));
    }
    void luminate(WG wg, int radius) {
        if (radius > 0) {

            for (int i = x - radius; i <= x + radius; i ++) {
                for (int j = y - radius; j <= y + radius; j ++) {

                    if (new Pos(i, j).inMap()) {
                        wg.isVisible[i][j] = true; // update invisibility
                    }
                }
            }

        } else {
            wg.isVisible[x][y] = false;
        }
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
    /**
     * Search for a random NOTHING tile next to this;
     * Always assuming there is one! Use it after `hasNext`;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param rand: Instance of Random;
     * @param border: insert 4 or 8;
     */
    Pos searchNextNOTHING(TETile[][] world, Random rand, int border) {
        return searchNext(world, rand, border, Tileset.NOTHING);
    }
    /**
     * Search for a random `tile` next to this;
     * Always assuming there is one! Use it after `hasNext`;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param rand: Instance of Random;
     * @param border: insert 4 or 8;
     */
    Pos searchNext(TETile[][] world, Random rand, int border, TETile tile) {
        Pos p;
        do {
            p = next(rand.nextInt(border));
        } while (! p.isTile(world, tile));
        return p;
    }
    /**
     * Whether there is a NOTHING tile next to this;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     */
    boolean hasNextNOTHING(TETile[][] world, int border) {
        return hasNext(world, border, Tileset.NOTHING);
    }
    /**
     * Whether there is a FLOOR tile next to this;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     */
    boolean hasNextFLOOR(TETile[][] world, int border) {
        return hasNext(world, border, Tileset.FLOOR);
    }

    /**
     * Whether there is a `tile` next to this;
     * border == 4: 4 directions; border == 8: 8 directions;
     * @param border: insert 4 or 8;
     * @param tile: any tile object;
     */
    boolean hasNext(TETile[][] world, int border, TETile tile) {
        /*
        When border == 4: the last four of the loop will be identical
        to the first four;
         */
        for (int i = 0; i < 8; i ++) {
            if (hasDirec(world, i % border, tile)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the position has a 'tile' next to it, in the specified direction;
     * @param direction: 0: right; 1: left; 2: up; 3: down;
     *                 4: up right; 5: down right; 6: down left; 7: up left;
     * @param tile: any tile object;
     */
    boolean hasDirec(TETile[][] world, int direction, TETile tile) {
        Pos p = next(direction);
        return p.isTile(world, tile);
    }

    /**
     * Get a position next to this, towards some direction;
     * @param direct: 0: right; 1: left; 2: up; 3: down;
     *         4: up right; 5: down right; 6: down left; 7: up left;
     */
    Pos next(int direct) {
        int xC = x;
        int yC = y;
        switch (direct) {
            case 0: xC ++; break; // right
            case 1: xC --; break; // left
            case 2: yC ++; break; // up
            case 3: yC --; break; // down
            case 4: xC ++; yC ++; break; // up right
            case 5: xC ++; yC --; break; // down right
            case 6: xC --; yC --; break; // down left
            case 7: xC --; yC ++; break; // up left
        }
        return new Pos(xC, yC);
    }

    boolean isFLOOR(TETile[][] world) {
        return isTile(world, Tileset.FLOOR);
    }

    boolean isNOTHING(TETile[][] world) {
        return isTile(world, Tileset.NOTHING);
    }

    /**
     * Whether this position is the specified `tile`;
     * @param tile: any tile object;
     */
    boolean isTile(TETile[][] world, TETile tile) {
        return inMap() && world[x][y] == tile;
    }

    /**
     * Whether a Pos is in the boundaries;
     */
    boolean inMap() {
        return x >= WG.startWIDTH && x < WG.WIDTH
                && y >= WG.startHEIGHT && y < WG.HEIGHT;
    }

}
