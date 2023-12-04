package byog.Core;

import java.io.Serializable;
import java.util.Random;

public class Utils implements Serializable {

    public static final int Right = 0, Left = 1, Up = 2, Down = 3,
            UpRight = 4, DownLeft = 5, DownRight = 6, UpLeft = 7;

    public static final int Available = 1, Unavailable = 0, Special = -1;
    /**
     * Find the opposite direction;
     */
    public static int oppositeDirec(int direc) {
        int a = direc % 2;
        if (a == 1) {
            return direc - 1;
        } else {
            return direc + 1;
        }
    }

    /**
     * Determine to how to rotate to new direction;
     * True for clockwise, false for counter-clockwise;
     */
    public static boolean clockwise(int newDirec, int direction) {
        return switch (direction) {
            case Right -> newDirec == Down;
            case Left -> newDirec == Up;
            case Up -> newDirec == Right;
            case Down -> newDirec == Left;
            case UpRight -> newDirec == Right || newDirec == DownRight || newDirec == Down;
            case DownLeft -> newDirec == Left || newDirec == UpLeft || newDirec == Up;
            case DownRight -> newDirec == Down || newDirec == DownLeft || newDirec == Left;
            default -> newDirec == Up || newDirec == DownRight || newDirec == Right; // UpLeft
        };
    }

    public static int randomOrthogonalDirecion(int direc, Random rand) {
        return switch (direc) {
            case Right, Left -> rand.nextInt(2, 4);
            case Up, Down -> rand.nextInt(0, 2);
            case UpRight, DownLeft -> rand.nextInt(6, 8);
            default -> rand.nextInt(4, 6);
        };
    }
}
