package byog.Core;

class Interval {
    static final int miniInterval = Game.miniInterval;

    int start;
    int end;

    Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }
    void renew(int start, int end) {
        this.start = start;
        this.end = end;
    }
    /**
     * When player performs an action,
     * the combo collector has already taken some time,
     * during which the player can't act again,
     * so this should not start at 0;
     */
    void renew(String playerInput) {
        int l = playerInput.length();
        if (l == Input.comboLength) {
            this.start = (l - 1) * Input.inComboInterval;
        } else {
            this.start = l * Input.inComboInterval;
        }

        this.end = Player.actionInterval;
    }

    /**
     * Update after each miniInterval;
     */
    void update() {
        if (!ended()) {
            start += miniInterval;
        }
    }
    boolean ended() {
        return start >= end;
    }

}
