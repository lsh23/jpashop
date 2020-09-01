package com.springjpaexercise.jpashop.controller;

import com.springjpaexercise.jpashop.domain.Member;
import com.springjpaexercise.jpashop.domain.Order;
import com.springjpaexercise.jpashop.domain.item.Item;
import com.springjpaexercise.jpashop.repository.OrderSearch;
import com.springjpaexercise.jpashop.service.ItemService;
import com.springjpaexercise.jpashop.service.MemberService;
import com.springjpaexercise.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberServie;
    private final ItemService itemService;

    @GetMapping("/order")
    public String create(Model model){

        List<Member> members = memberServie.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }


    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
