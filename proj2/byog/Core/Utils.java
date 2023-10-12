package byog.Core;

import java.io.Serializable;
import java.util.Random;

public class Utils implements Serializable {
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
            case 0 -> newDirec == 3;
            case 1 -> newDirec == 2;
            case 2 -> newDirec == 0;
            case 3 -> newDirec == 1;
            case 4 -> newDirec == 0 || newDirec == 6 || newDirec == 3;
            case 5 -> newDirec == 1 || newDirec == 7 || newDirec == 2;
            case 6 -> newDirec == 3 || newDirec == 5 || newDirec == 1;
            default -> newDirec == 2 || newDirec == 6 || newDirec == 0;
        };
    }

    public static int randomOrthogonalDirecion(int direc, Random rand) {
        return switch (direc) {
            case 0, 1 -> rand.nextInt(2, 4);
            case 2, 3 -> rand.nextInt(0, 2);
            case 4, 5 -> rand.nextInt(6, 8);
            default -> rand.nextInt(4, 6);
        };
    }
}
