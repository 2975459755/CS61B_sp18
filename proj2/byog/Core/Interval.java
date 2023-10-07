package byog.Core;

import byog.Core.Objects.SingleBlock.Player;

import java.io.Serializable;

/**
 * An interval serves as a time tracker,
 * and it should be updated after each game loop;
 * The outside can access whether it's ended;
 */
public class Interval implements Serializable {
    static final int miniInterval = Game.miniInterval;

    private int start;
    private int end;

    public Interval(int end) {
        this.start = 0;
        this.end = end;
    }
    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public int getRest() {
        return Math.max(end - start, 0);
    }

    /**
     * When a new interval is needed, we don't create a new instance;
     * because the instance fields we use to store intervals
     * are actually fixated to the original one;
     * so instead, we always renew;
     */
    public void renew(int end) {
        renew(0, end);
    }
    public void renew(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public void delay(int time) {
        this.end += time;
    }
    /**
     * When player performs an action,
     * the combo collector has already taken some time,
     * during which the player can't act again,
     * so this should not start at 0;
     */
    @Deprecated
    public void renew(String playerInput) {
        int st = playerInput.length() * Input.inComboInterval;

        renew(st, Player.moveInterval);
    }

    /**
     * Update after each game loop;
     */
    public void update() {
        update(miniInterval);
    }
    public <T> void update(T intOrString) {
        int time;
        if (intOrString instanceof String s) {
            time = s.length() * Input.inComboInterval;
        } else {
            time = (int) intOrString;
        }
        if (!ended()) {
            start += time;
        }
    }
    public boolean ended() {
        return start >= end;
    }

}
