package fourcorp.buildflow.repository;

import fourcorp.buildflow.domain.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapLinkedTest {

    @Test
    void newItem() {
        MapLinked<Order, PriorityOrder, String> p1 = new MapLinked();

        Order o1 = new Order(
                new ArrayList<>(),
                0,
                new Client("vfer", 111111111, "Ana Ribeiro", "Rua", "Cidade", "0000-000", 555555555, ClientType.PRIVATE),
                LocalDate.now(),
                LocalDate.MAX
        );

        Order o2 = new Order(
                new ArrayList<>(),
                5,
                new Client("jdoe", 222222222, "João Silva", "Avenida", "Lisboa", "1111-111", 666666666, ClientType.BUSINESS),
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(10)
        );

        Order o3 = new Order(
                new ArrayList<>(),
                3,
                new Client("mrodrigues", 333333333, "Maria Rodrigues", "Praça", "Porto", "2222-222", 777777777, ClientType.PRIVATE),
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        Order o4 = new Order(
                new ArrayList<>(),
                10,
                new Client("pfernandes", 444444444, "Pedro Fernandes", "Rua das Flores", "Coimbra", "3333-333", 888888888, ClientType.BUSINESS),
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(15)
        );

        p1.newItem(o1, PriorityOrder.HIGH);
        p1.newItem(o2, PriorityOrder.NORMAL);
        p1.newItem(o3, PriorityOrder.HIGH);
        p1.newItem(o4, PriorityOrder.LOW);

        List<Order> ordersPriority1 = p1.getByKey(PriorityOrder.HIGH);
        List<Order> ordersPriority2 = p1.getByKey(PriorityOrder.NORMAL);
        List<Order> ordersPriority3 = p1.getByKey(PriorityOrder.LOW);

        assertTrue(ordersPriority1.contains(o1), "Order o1 should be in priority 1");
        assertTrue(ordersPriority1.contains(o3), "Order o3 should be in priority 1");
        assertTrue(ordersPriority2.contains(o2), "Order o2 should be in priority 2");
        assertTrue(ordersPriority3.contains(o4), "Order o4 should be in priority 3");

        assertEquals(2, ordersPriority1.size(), "There should be 2 orders in priority 1");
        assertEquals(1, ordersPriority2.size(), "There should be 1 order in priority 2");
        assertEquals(1, ordersPriority3.size(), "There should be 1 order in priority 3");
    }

    @Test
    void remove() {
        Order o1 = new Order(
                new ArrayList<>(),
                0,
                new Client("vfer", 111111111, "Ana Ribeiro", "Rua", "Cidade", "0000-000", 555555555, ClientType.PRIVATE),
                LocalDate.now(),
                LocalDate.MAX
        );
        MapLinked<Order, PriorityOrder, String> p2 = new MapLinked();
        p2.newItem(o1, PriorityOrder.HIGH);

        List<Order> ordersPriority1 = p2.getByKey(PriorityOrder.HIGH);

        p2.remove(o1, PriorityOrder.HIGH);

        List<Order> ordersPriority2 = p2.getByKey(PriorityOrder.HIGH);


        assertEquals(1, ordersPriority1.size(), "Error 1");
        assertEquals(0, ordersPriority2.size(), "Error 2");
        assertNotEquals(ordersPriority1.size(), ordersPriority2.size(), "The sizes of ordersPriority1 and ordersPriority2 should not be equal");
    }

    @Test
    void searchById() {
        Order o1 = new Order(
                new ArrayList<>(),
                0,
                new Client("vfer", 111111111, "Ana Ribeiro", "Rua", "Cidade", "0000-000", 555555555, ClientType.PRIVATE),
                LocalDate.now(),
                LocalDate.MAX
        );
        MapLinked<Order, PriorityOrder, String> p2 = new MapLinked();
        p2.newItem(o1, PriorityOrder.HIGH);

        assertEquals(p2.searchById(o1.getId()).getId(), o1.getId());
    }

    @Test
    void removeAll() {
        MapLinked<Product, PriorityOrder, String> productPriorityLine = new MapLinked<>();

        productPriorityLine.newItem(new Product("P1", new LinkedList<>(Arrays.asList(new Operation("Op1"), new Operation("Op2")))), PriorityOrder.HIGH);
        productPriorityLine.newItem(new Product("P2", new LinkedList<>(Arrays.asList(new Operation("Op1"), new Operation("Op3")))), PriorityOrder.NORMAL);
        productPriorityLine.newItem(new Product("P3", new LinkedList<>(Arrays.asList(new Operation("Op4"), new Operation("Op5")))), PriorityOrder.LOW);

        assertFalse(productPriorityLine.getByKey(PriorityOrder.HIGH).isEmpty());
        assertFalse(productPriorityLine.getByKey(PriorityOrder.NORMAL).isEmpty());
        assertFalse(productPriorityLine.getByKey(PriorityOrder.LOW).isEmpty());

        productPriorityLine.removeAll();

        assertTrue(productPriorityLine.getByKey(PriorityOrder.HIGH).isEmpty());
        assertTrue(productPriorityLine.getByKey(PriorityOrder.NORMAL).isEmpty());
        assertTrue(productPriorityLine.getByKey(PriorityOrder.LOW).isEmpty());
    }
}