import by.osinovi.sales_analysis.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class OrderAnalyzerTest {

    private OrderAnalyzer analyzer;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        analyzer = new OrderAnalyzer();
        orders = createTestOrders();
    }

    private List<Order> createTestOrders() {
        Customer customer1 = new Customer("C1", "Alice", "alice@example.com", LocalDateTime.now(), 30, "New York");
        Customer customer2 = new Customer("C2", "Bob", "bob@example.com", LocalDateTime.now(), 25, "Los Angeles");
        Customer customer3 = new Customer("C3", "Charlie", "charlie@example.com", LocalDateTime.now(), 35, "Chicago");

        OrderItem item1 = new OrderItem("Laptop", 2, 1000.0, Category.ELECTRONICS);
        OrderItem item2 = new OrderItem("Book", 3, 20.0, Category.BOOKS);
        OrderItem item3 = new OrderItem("Laptop", 1, 1000.0, Category.ELECTRONICS);
        OrderItem item4 = new OrderItem("Book", 2, 20.0, Category.BOOKS);

        List<Order> testOrders = new ArrayList<>();

        testOrders.add(new Order("O1", LocalDateTime.now(), customer1, Arrays.asList(item1, item2), OrderStatus.DELIVERED));
        testOrders.add(new Order("O2", LocalDateTime.now(), customer2, Arrays.asList(item3), OrderStatus.DELIVERED));
        testOrders.add(new Order("O3", LocalDateTime.now(), customer1, Arrays.asList(item4), OrderStatus.CANCELLED));

        for (int i = 4; i <= 8; i++) {
            testOrders.add(new Order("O" + i, LocalDateTime.now(), customer1, Arrays.asList(item4), OrderStatus.DELIVERED));
        }
        testOrders.add(new Order("O9", LocalDateTime.now(), customer3, Arrays.asList(item4), OrderStatus.DELIVERED));

        return testOrders;
    }

    @Test
    void testGetUniqueCities() {
        Set<String> cities = analyzer.getUniqueCities(orders);
        assertEquals(Set.of("New York", "Los Angeles", "Chicago"), cities);
        assertEquals(3, cities.size());
    }

    @Test
    void testGetUniqueCitiesWithNullCustomer() {
        List<Order> ordersWithNull = new ArrayList<>(orders);
        ordersWithNull.add(new Order("O10", LocalDateTime.now(), null, Collections.emptyList(), OrderStatus.DELIVERED));
        Set<String> cities = analyzer.getUniqueCities(ordersWithNull);
        assertEquals(Set.of("New York", "Los Angeles", "Chicago"), cities);
    }

    @Test
    void testGetUniqueCitiesEmptyList() {
        Set<String> cities = analyzer.getUniqueCities(Collections.emptyList());
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetUniqueCitiesNullList() {
        Set<String> cities = analyzer.getUniqueCities(null);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetTotalIncome() {
        double totalIncome = analyzer.getTotalIncome(orders);
        // (2*1000 + 3*20) + (1*1000) + (2*20 * 5) = 2060 + 1000 + 200 = 3260
        assertEquals(3300.0, totalIncome, 0.01);
    }

    @Test
    void testGetTotalIncomeNoDeliveredOrders() {
        List<Order> cancelledOrders = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.DELIVERED)
                .collect(Collectors.toList());
        double totalIncome = analyzer.getTotalIncome(cancelledOrders);
        assertEquals(0.0, totalIncome, 0.01);
    }

    @Test
    void testGetTotalIncomeEmptyItems() {
        List<Order> emptyItemsOrders = List.of(
                new Order("O10", LocalDateTime.now(), new Customer("C4", "Dave", "dave@example.com", LocalDateTime.now(), 40, "Boston"),
                        Collections.emptyList(), OrderStatus.DELIVERED)
        );
        double totalIncome = analyzer.getTotalIncome(emptyItemsOrders);
        assertEquals(0.0, totalIncome, 0.01);
    }

    @Test
    void testGetTotalIncomeNullList() {
        double totalIncome = analyzer.getTotalIncome(null);
        assertEquals(0.0, totalIncome, 0.01);
    }

    @Test
    void testGetMostPopularProduct() {
        Optional<String> mostPopular = analyzer.getMostPopularProduct(orders);
        assertTrue(mostPopular.isPresent());
        assertEquals("Book", mostPopular.get());
    }

    @Test
    void testGetMostPopularProductEmptyList() {
        Optional<String> mostPopular = analyzer.getMostPopularProduct(Collections.emptyList());
        assertFalse(mostPopular.isPresent());
    }

    @Test
    void testGetMostPopularProductNullList() {
        Optional<String> mostPopular = analyzer.getMostPopularProduct(null);
        assertFalse(mostPopular.isPresent());
    }

    @Test
    void testGetAverageOrderValue() {
        double avgOrderValue = analyzer.getAverageOrderValue(orders);
        assertEquals(412.5, avgOrderValue, 0.01);
    }

    @Test
    void testGetAverageOrderValueNoDeliveredOrders() {
        List<Order> cancelledOrders = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.DELIVERED)
                .collect(Collectors.toList());
        double avgOrderValue = analyzer.getAverageOrderValue(cancelledOrders);
        assertEquals(0.0, avgOrderValue, 0.01);
    }

    @Test
    void testGetAverageOrderValueNullList() {
        double avgOrderValue = analyzer.getAverageOrderValue(null);
        assertEquals(0.0, avgOrderValue, 0.01);
    }

    @Test
    void testGetCustomersWithMoreThanFiveOrders() {
        List<Customer> customers = analyzer.getCustomersWithMoreThanFiveOrders(orders);
        assertEquals(1, customers.size());
        assertEquals("C1", customers.get(0).getCustomerId()); // Alice имеет 6 заказов
    }

    @Test
    void testGetCustomersWithMoreThanFiveOrdersEmptyList() {
        List<Customer> customers = analyzer.getCustomersWithMoreThanFiveOrders(Collections.emptyList());
        assertTrue(customers.isEmpty());
    }

    @Test
    void testGetCustomersWithMoreThanFiveOrdersNullList() {
        List<Customer> customers = analyzer.getCustomersWithMoreThanFiveOrders(null);
        assertTrue(customers.isEmpty());
    }
}