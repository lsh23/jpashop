package com.springjpaexercise.jpashop.service;

import com.springjpaexercise.jpashop.domain.Address;
import com.springjpaexercise.jpashop.domain.Member;
import com.springjpaexercise.jpashop.domain.Order;
import com.springjpaexercise.jpashop.domain.OrderStatus;
import com.springjpaexercise.jpashop.domain.item.Book;
import com.springjpaexercise.jpashop.domain.item.Item;
import com.springjpaexercise.jpashop.exception.NotEnoughStockException;
import com.springjpaexercise.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문 () throws Exception {
        // Given
        Member member = createMember();

        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(),book.getId(),orderCount);

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getOrderStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, getOrder.getOrderItemList().size());
        assertEquals("주문한 가격은 가격 * 수량", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문한 가격은 가격 * 수량", 8 , book.getStockQuantity());

    }

    private Book createBook(final String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","경기","123-123"));
        em.persist(member);
        return member;
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // Given
        Member member = createMember();
        Item item = createBook("JPA",10000,10);

        int orderCount = 11;

        // When
        orderService.order(member.getId(),item.getId(),orderCount);

        // Then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문_취소() throws Exception {
        // Given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        // When
        orderService.cancelOrder(orderId);

        // Then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getOrderStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야한다.",10,book.getStockQuantity());

    }
}