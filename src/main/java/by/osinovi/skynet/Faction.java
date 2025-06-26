package by.osinovi.skynet;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Faction implements Runnable {
    private final String name;
    private final Inventory factoryInventory;
    private final Inventory factionInventory;
    private final ReentrantLock lock;
    private final Condition condition;
    private final Random random;
    private final int days;
    private int robots;

    public Faction(String name, Inventory factoryInventory, ReentrantLock lock, Condition condition, int days) {
        this.name = name;
        this.factoryInventory = factoryInventory;
        this.factionInventory = new Inventory();
        this.lock = lock;
        this.condition = condition;
        this.random = new Random();
        this.days = days;
        this.robots = 0;
    }

    @Override
    public void run() {
        for (int day = 1; day <= days; day++) {
            lock.lock();
            try {
                condition.await();
                int partsTaken = 0;
                List<PartType> availableParts = Arrays.stream(PartType.values())
                        .filter(part -> factoryInventory.getPartCount(part) > 0)
                        .collect(Collectors.toList());
                while (partsTaken < 5 && !availableParts.isEmpty()) {
                    PartType part = availableParts.get(random.nextInt(availableParts.size()));
                    if (factoryInventory.takePart(part)) {
                        factionInventory.addPart(part);
                        partsTaken++;
                    }
                    availableParts = Arrays.stream(PartType.values())
                            .filter(par -> factoryInventory.getPartCount(part) > 0)
                            .toList();
                }
                // Собираем роботов
                while (factionInventory.canAssembleRobot()) {
                    factionInventory.assembleRobot();
                    robots++;
                }
                System.out.printf("День %d: %s инвентарь=%s, роботов=%d%n", day, name, factionInventory, robots);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
        System.out.printf("%s завершил с %d роботами%n", name, robots);
    }

    public int getRobots() {
        return robots;
    }
}

