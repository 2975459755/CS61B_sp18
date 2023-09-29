package byog.Core;

class Intervals {
    static final int actionInterval = Game.actionInterval;
    static final int inComboInterval = Game.inComboInterval;
    static final int miniInterval = Game.miniInterval;
    static final int comboLength = Game.comboLength;

    int playerAction;

    Intervals() {
        playerAction = actionInterval;
    }

    /**
     * When player performs an action,
     * the combo collector has already taken some time,
     * during which the player can't act again;
     */
    void update(String playerInput) {
        playerAction = (playerInput.length() - 1) * inComboInterval;
    }

    /**
     * Update after each miniInterval;
     */
    void update() {
        if (!canPlayerAct()) {
            playerAction += miniInterval;
        }
    }
    boolean canPlayerAct() {
        return playerAction >= actionInterval;
    }
}
