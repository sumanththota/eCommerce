package com.sumanthdev.inventory_service;

import com.sumanthdev.inventory_service.model.Inventory;
import com.sumanthdev.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner laodData(InventoryRepository inventoryRepository){
		return arg -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone");
			inventory.setQuantity(2);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("samsung");
			inventory1.setQuantity(0);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}

}
