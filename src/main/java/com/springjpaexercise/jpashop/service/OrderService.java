package com.springjpaexercise.jpashop.service;

import com.springjpaexercise.jpashop.domain.Delivery;
import com.springjpaexercise.jpashop.domain.Member;
import com.springjpaexercise.jpashop.domain.Order;
import com.springjpaexercise.jpashop.domain.OrderItem;
import com.springjpaexercise.jpashop.domain.item.Item;
import com.springjpaexercise.jpashop.repository.ItemRepository;
import com.springjpaexercise.jpashop.repository.MemberRepository;
import com.springjpaexercise.jpashop.repository.OrderRepository;
import com.springjpaexercise.jpashop.repository.OrderSearch;
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
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createdOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
