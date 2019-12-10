package com.challenge.reviews.product;

import com.challenge.reviews.domain.product.Product;
import com.challenge.reviews.config.H2TestProfileJPAConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.challenge.reviews.ReviewsApplication;
import com.challenge.reviews.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@SpringBootTest(classes = {
        ReviewsApplication.class,
        H2TestProfileJPAConfig.class })
public class ProductApplicationTests {

    @Autowired
    private MockMvc mvc;

    private JacksonTester<Product> json;

    @MockBean
    private ProductRepository productRepository;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        Product product = getProduct();
        given(productRepository.save(any())).willReturn(product);
        given(productRepository.findById(product.getProductId())).willReturn(Optional.of(product));
        given(productRepository.findAll()).willReturn(Collections.singletonList(product));

    }

    @Test
    public void createProduct() throws Exception {
        Product product = getProduct();
        mvc.perform(
                post(new URI("/products"))
                        .content(json.write(product).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void findById() throws Exception {
        Product product = getProduct();
        mvc.perform(
                post(new URI("/products"))
                        .content(json.write(product).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        mvc.perform(
                get(new URI("/products/1"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("productId", is(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void listProducts() throws Exception {
        Product product = getProduct();

        mvc.perform(
                post(new URI("/products"))
                        .content(json.write(product).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        mvc.perform(
                get(new URI("/products"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(result -> hasSize(1))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProduct() throws Exception{
        Product product = getProduct();

        mvc.perform(
                post(new URI("/products"))
                        .content(json.write(product).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        mvc.perform(
                delete(new URI("/products/1"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(result -> hasSize(1))
                .andExpect(status().isOk());
    }

    private Product getProduct() {
        Product product = new Product();
       product.setProductId(1L);
        product.setName("Teste Produtos");
        product.setDescription("Teste Description");
        return product;
    }

}
