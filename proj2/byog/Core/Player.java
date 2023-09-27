package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

class Player {
    TETile[][] world;
    Pos pos;

    Player(TETile[][] world, Pos position) {
        this.world = world;
        this.pos = position;
    }
    void move(String direc) {
        switch(direc) {
            case "d":
            case "right": move(0); break;
            case "a":
            case "left": move(1); break;
            case "w":
            case "up": move(2); break;
            case "s":
            case "down": move(3); break;
            case "up-right": move(4); break;
            case "down-right": move(5); break;
            case "down-left": move(6); break;
            case "up-left": move(7); break;
        }
    }
    void move(int direc) {
        move(pos.next(direc));
    }
    void move(Pos des) {
        if (des.isFLOOR(world)) {
            /*
            Swap two tiles;
             */
            world[pos.x][pos.y] = Tileset.FLOOR;
            world[des.x][des.y] = Tileset.PLAYER;
            /*
            Update character status;
             */
            pos = des;
        }
    }
}
