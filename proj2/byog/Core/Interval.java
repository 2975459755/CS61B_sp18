package byog.Core;

import byog.Core.Objects.Player;

import java.io.Serializable;

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
    public void renew(int end) {
        renew(0, end);
    }
    public void renew(int start, int end) {
        this.start = start;
        this.end = end;
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

        renew(st, Player.actionInterval);
    }

    /**
     * Update after each miniInterval;
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
