package com.springjpaexercise.jpashop.service;

import com.springjpaexercise.jpashop.domain.item.Book;
import com.springjpaexercise.jpashop.domain.item.Item;
import com.springjpaexercise.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 상품_저장() throws Exception {
        // Given
        Item item = new Book();
        item.setName("little_princess");

        // When
        itemService.saveItem(item);

        // Then
        assertEquals(item, itemRepository.findAll().get(0));

    }


}