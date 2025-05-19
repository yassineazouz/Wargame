package tn.isty.wargame.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Unit> units;
    private boolean isAI;

    public Player(String name, boolean isAI) {
        this.name = name;
        this.isAI = isAI;
        this.units = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public boolean hasUnitsAlive() {
        return units.stream().anyMatch(unit -> unit.getHealth() > 0);
    }

    public void resetUnitsMovement() {
        for (Unit unit : units) {
            unit.resetMovement();
        }
    }
    public boolean isAI() {
        return isAI;
    }
}
