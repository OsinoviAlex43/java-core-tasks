package by.osinovi.sales_analysis;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class OrderAnalyzer {

    public Set<String> getUniqueCities(List<Order> orders) {
        if (orders == null) {
            return Collections.emptySet();
        }
        return orders.stream()
                .filter(order -> order != null && order.getCustomer() != null)
                .map(order -> order.getCustomer().getCity())
                .distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public double getTotalIncome(List<Order> orders) {
        if (orders == null) {
            return 0.0;
        }
        return orders.stream()
                .filter(order -> order != null && order.getStatus() == OrderStatus.DELIVERED && order.getItems() != null)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public Optional<String> getMostPopularProduct(List<Order> orders) {
        if (orders == null) {
            return Optional.empty();
        }
        return orders.stream()
                .filter(order -> order != null && order.getItems() != null)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProductName,
                        Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public double getAverageOrderValue(List<Order> orders) {
        if (orders == null) {
            return 0.0;
        }
        return orders.stream()
                .filter(order -> order != null && order.getStatus() == OrderStatus.DELIVERED && order.getItems() != null)
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum())
                .average()
                .orElse(0.0);
    }

    public List<Customer> getCustomersWithMoreThanFiveOrders(List<Order> orders) {
        if (orders == null) {
            return Collections.emptyList();
        }
        return orders.stream()
                .filter(order -> order != null && order.getCustomer() != null)
                .collect(Collectors.groupingBy(
                        Order::getCustomer,
                        Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}