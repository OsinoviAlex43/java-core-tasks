package by.osinovi.skynet;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RobotSimulation {
    public static void main(String[] args) {
        Inventory factoryInventory = new Inventory();
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        int days = 100;

        // Создаем потоки
        Factory factory = new Factory(factoryInventory, lock, condition, days);
        Faction world = new Faction("World", factoryInventory, lock, condition, days);
        Faction wednesday = new Faction("Wednesday", factoryInventory, lock, condition, days);

        // Запускаем потоки
        Thread factoryThread = new Thread(factory);
        Thread worldThread = new Thread(world);
        Thread wednesdayThread = new Thread(wednesday);

        factoryThread.start();
        worldThread.start();
        wednesdayThread.start();

        // Ждем завершения
        try {
            factoryThread.join();
            worldThread.join();
            wednesdayThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }



        // Определяем победителя
        int worldRobots = world.getRobots();
        int wednesdayRobots = wednesday.getRobots();
        System.out.printf("Итог: World имеет %d роботов, Wednesday имеет %d роботов.%n", worldRobots, wednesdayRobots);
        if (worldRobots > wednesdayRobots) {
            System.out.println("World имеет самую сильную армию!");
        } else if (wednesdayRobots > worldRobots) {
            System.out.println("Wednesday имеет самую сильную армию!");
        } else {
            System.out.println("Ничья!");
        }
    }
}
