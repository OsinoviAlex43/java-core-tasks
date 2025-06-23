package by.osinovi.sales_analysis;

import java.util.*;
import java.util.stream.Collectors;

public class OrderAnalyzer {


    public List<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public double getTotalIncome(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public Optional<String> getMostPopularProduct(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProductName,
                        Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public double getAverageOrderValue(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum())
                .average()
                .orElse(0.0);
    }

    public List<Customer> getCustomersWithMoreThanFiveOrders(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomer,
                        Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}