package by.osinovi.skynet;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

class Inventory {
    private ConcurrentHashMap<PartType, Integer> parts;

    public Inventory() {
        parts = new ConcurrentHashMap<>();
        for (PartType part : PartType.values()) {
            parts.put(part, 0);
        }
    }

    public void addPart(PartType part) {
        parts.compute(part, (k, v) -> v + 1);
    }

    public boolean takePart(PartType part) {
        return parts.computeIfPresent(part, (k, v) -> v > 0 ? v - 1 : v) > 0;
    }

    public int getPartCount(PartType part) {
        return parts.get(part);
    }

    public boolean canAssembleRobot() {
        return Arrays.stream(PartType.values()).allMatch(part -> parts.get(part) > 0);
    }

    public void assembleRobot() {
        for (PartType part : PartType.values()) {
            parts.compute(part, (k, v) -> v - 1);
        }
    }

    @Override
    public String toString() {
        return parts.toString();
    }
}