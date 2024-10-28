package com.sumanthdev.product_service;

import com.sumanthdev.product_service.dto.ProductRequest;
import com.sumanthdev.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.1")
			.withExposedPorts(27017); // Expose a different port

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", () ->
				"mongodb://" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017) + "/test"
		);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());
	}


//	@Test
//	void shouldGetProduct() throws Exception {
//		ProductRequest productRequest = getProductRequest();
//		String productRequestString = objectMapper.writeValueAsString(productRequest);
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(productRequestString))
//				.andExpect(status().isCreated());
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/products")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$[0].name").value("samsung"))
//				.andExpect(jsonPath("$[0].description").value("phone"))
//				.andExpect(jsonPath("$[0].price").value(1000));
//
//
//	}
	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("samsung")
				.description("phone")
				.price(BigDecimal.valueOf(1000))
				.build();
	}
}