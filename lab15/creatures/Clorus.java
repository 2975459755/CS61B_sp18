package creatures;

import huglife.*;

import java.awt.*;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
    private static final int r = 34, g = 0, b = 231;

    private static double validateEnergy(double e) {
        return Math.max(0, e);
    }
    public Clorus(double e) {
        super("clorus");
        energy = validateEnergy(e);
    }

    @Override
    public void move() {
        energy = validateEnergy(energy - 0.03);
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Creature replicate() {
        energy = validateEnergy(energy / 2);
        return new Clorus(energy);
    }

    @Override
    public void stay() {
        energy = validateEnergy(energy - 0.01);
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");

        if (!empties.isEmpty()) {
            if (!plips.isEmpty()) {
                return new Action(Action.ActionType.ATTACK, HugLifeUtils.randomEntry(plips));
            } else if (energy > 1) {
                return new Action(Action.ActionType.REPLICATE, HugLifeUtils.randomEntry(empties));
            } else {
                return new Action(Action.ActionType.MOVE, HugLifeUtils.randomEntry(empties));
            }
        }
        return new Action(Action.ActionType.STAY);
    }

    @Override
    public Color color() {
        return color(r, g, b);
    }
}
