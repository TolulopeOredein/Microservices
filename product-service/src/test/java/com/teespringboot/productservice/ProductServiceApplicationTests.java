package com.teespringboot.productservice;

import com.teespringboot.productservice.DTO.ProductRequest;
import com.teespringboot.productservice.DTO.ProductResponse;
import com.teespringboot.productservice.model.Product;
import com.teespringboot.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	//@Autowired
	//private static ObjectMapper objectMapper;
	@Autowired
	ProductRepository productRepository;
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.url", mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void shouldCreateProduct() throws Exception {
		 ObjectMapper objectMapper1 = new ObjectMapper();
		 ProductRequest productRequest = getProductRequest();
		 String productRequestString = objectMapper1.writeValueAsString(productRequest);
		 mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());
		 Assertions.assertEquals(11, productRepository.findAll().size());
	}
	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Samsung1 x30")
				.description("Mobile Phones")
				.price(BigDecimal.valueOf(1800))
				.build();
	}
}
