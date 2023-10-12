package byog.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final String s = "☣⚔﷽";
    public static final TETile PLAYER =
      new TETile('⚔', Color.white, Color.black, "player");
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");


    public static final TETile LAMP_LIT = new TETile('ψ', Color.orange, Color.black, "lit lamp");
    public static final TETile LAMP_UNLIT = new TETile('ψ', Color.white, Color.black, "unlit lamp");

    public static final TETile WALL_BREAKABLE = new TETile('#', new Color(255, 168, 60), Color.darkGray,
            "breakable wall");
    public static final TETile WALL_DAMAGED = new TETile('#', Color.gray, Color.orange,
            "breakable wall");

    public static final TETile ROMO_UP = new TETile('▲', Color.gray, Color.black, "romo up");
    public static final TETile ROMO_DOWN = new TETile('▼', Color.gray, Color.black, "romo down");
    public static final TETile ROMO_LEFT = new TETile('◀', Color.gray, Color.black, "romo left");
    public static final TETile ROMO_RIGHT = new TETile('▶', Color.gray, Color.black, "romo right");
    public static final TETile ROMO_UP_DAMAGED = new TETile('▲', Color.magenta, Color.black, "romo up damaged");
    public static final TETile ROMO_DOWN_DAMAGED = new TETile('▼', Color.magenta, Color.black, "romo down damaged");
    public static final TETile ROMO_LEFT_DAMAGED = new TETile('◀', Color.magenta, Color.black, "romo left damaged");
    public static final TETile ROMO_RIGHT_DAMAGED = new TETile('▶', Color.magenta, Color.black, "romo right damaged");

    public static final TETile PLAYER_RED = new TETile('⚔', Color.red, Color.black, "player");
    public static final TETile PLAYER_GHOSTED = new TETile('⚔', Color.gray, Color.gray, "player");

    public static final TETile BULLET = new TETile('⚪', Color.white, Color.black, "bullet");
    public static final TETile BULLET_FLOWER = new TETile('☣', Color.green, Color.black, "flower bullet");

    public static final TETile KEY = new TETile('☆', Color.yellow, Color.pink, "key");
    public static final TETile HEART = new TETile('♥', Color.red, Color.black, "heart");
    public static final TETile FLOWER_DEFAULT = new TETile('❀', Color.magenta, Color.black, "flower");
    public static final TETile FLOWER_DAMAGED = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile STONE = new TETile('▢', Color.gray, Color.black, "stone");
    public static final TETile STONE_DAMAGED = new TETile('▢', Color.yellow, Color.black, "stone");

}


