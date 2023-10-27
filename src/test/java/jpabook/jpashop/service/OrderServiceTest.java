package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @PersistenceContext EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void order() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("JPA Book", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "Order Status should be ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "The number of item category should be correct");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "Total price is price * item count");
        assertEquals(8, book.getStockQuantity(), "Inventory count should be reduced by the order count");
    }

    @Test
    public void cancel() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook("Book", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "Order status should be CANCEL");
        assertEquals(10, book.getStockQuantity(), "Inventory count should be restored to 10");
    }

    @Test(expected = NotEnoughStockException.class)
    public void order_more_than_available_quantity() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook("JPA BOOK", 10000, 10);

        int orderCount = 11;

        //when
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("NotEnoughStockException should be thrown");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("Kim");
        member.setAddress(new Address("Seoul", "Street", "10101"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

}
