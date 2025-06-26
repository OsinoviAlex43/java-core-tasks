package by.osinovi.skynet;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Factory implements Runnable {
    private final Inventory inventory;
    private final ReentrantLock lock;
    private final Condition condition;
    private final Random random;
    private final int days;

    public Factory(Inventory inventory, ReentrantLock lock, Condition condition, int days) {
        this.inventory = inventory;
        this.lock = lock;
        this.condition = condition;
        this.random = new Random();
        this.days = days;
    }

    @Override
    public void run() {
        for (int day = 1; day <= days; day++) {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    PartType part = PartType.values()[random.nextInt(PartType.values().length)];
                    inventory.addPart(part);
                }
                System.out.printf("День %d: Фабрика произвела %s%n", day, inventory);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            lock.lock();
            try {
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}