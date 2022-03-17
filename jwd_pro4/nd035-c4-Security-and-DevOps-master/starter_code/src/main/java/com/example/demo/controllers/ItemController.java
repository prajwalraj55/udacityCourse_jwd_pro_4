package com.example.demo.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	public static final Logger log = LogManager.getLogger(ItemController.class);
	@Autowired
	private ItemRepository itemRepository;

	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.info("getItemByid"+id);
		return ResponseEntity.of(itemRepository.findById(id));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		log.info("getItemsBy name"+name);
		List<Item> items = itemRepository.findByName(name);
		if (items == null || items.isEmpty()) {
			log.info("item not files"+items);
			return ResponseEntity.notFound().build();
		}
		log.info("returning items by name"+items);
		return ResponseEntity.ok(items);

	}
	
}
