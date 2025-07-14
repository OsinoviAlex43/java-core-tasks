import by.osinovi.my_linked_list.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LinkedListTest {
    private LinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new LinkedList<>();
    }

    @Test
    void testSizeEmptyList() {
        assertEquals(0, list.size());
    }

    @Test
    void testSizeAfterAdd() {
        list.addFirst(1);
        list.addLast(2);
        assertEquals(2, list.size());
    }

    @Test
    void testAddFirst() {
        list.addFirst(1);
        assertEquals(1, list.getFirst());
        assertEquals(1, list.size());
        list.addFirst(2);
        assertEquals(2, list.getFirst());
        assertEquals(2, list.size());
    }

    @Test
    void testAddLast() {
        list.addLast(1);
        assertEquals(1, list.getLast());
        assertEquals(1, list.size());
        list.addLast(2);
        assertEquals(2, list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    void testAddAtIndex() {
        list.addLast(1);
        list.addLast(3);
        list.add(1, 2);
        assertEquals(2, list.get(1));
        assertEquals(3, list.size());
    }

    @Test
    void testAddAtInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, 1));
    }

    @Test
    void testGetFirst() {
        list.addFirst(1);
        assertEquals(1, list.getFirst());
        list.addFirst(2);
        assertEquals(2, list.getFirst());
    }

    @Test
    void testGetFirstEmptyList() {
        assertThrows(IllegalStateException.class, () -> list.getFirst());
    }

    @Test
    void testGetLast() {
        list.addLast(1);
        assertEquals(1, list.getLast());
        list.addLast(2);
        assertEquals(2, list.getLast());
    }

    @Test
    void testGetLastEmptyList() {
        assertThrows(IllegalStateException.class, () -> list.getLast());
    }

    @Test
    void testGet() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(2, list.get(1));
    }

    @Test
    void testGetInvalidIndex() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void testRemoveFirst() {
        list.addLast(1);
        list.addLast(2);
        assertEquals(1, list.removeFirst());
        assertEquals(1, list.size());
        assertEquals(2, list.getFirst());
    }

    @Test
    void testRemoveFirstEmptyList() {
        assertThrows(IllegalStateException.class, () -> list.removeFirst());
    }

    @Test
    void testRemoveLast() {
        list.addLast(1);
        list.addLast(2);
        assertEquals(2, list.removeLast());
        assertEquals(1, list.size());
        assertEquals(1, list.getLast());
    }

    @Test
    void testRemoveLastEmptyList() {
        assertThrows(IllegalStateException.class, () -> list.removeLast());
    }

    @Test
    void testRemoveAtIndex() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(2, list.remove(1));
        assertEquals(2, list.size());
        assertEquals(3, list.get(1));
    }

    @Test
    void testRemoveAtInvalidIndex() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }
}