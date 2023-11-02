package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * Order
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // Search Entity
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // Create Delivery Information
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // Create OrderItem
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // Create Order
        Order order = Order.createOrder(member, delivery, orderItem);

        // Save Order
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * Cancel
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // Find Order Entity
        Order order = orderRepository.findOne(orderId);
        // Cancel Order
        order.cancel();
    }

    // Search
    public List<Order> findOrders (OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
