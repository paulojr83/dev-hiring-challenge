package com.challenge.reviews.comments;

import com.challenge.reviews.domain.comment.Comment;
import com.challenge.reviews.domain.product.Product;
import com.challenge.reviews.config.H2TestProfileJPAConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.challenge.reviews.ReviewsApplication;
import com.challenge.reviews.domain.review.Review;
import com.challenge.reviews.repository.CommentRepository;
import com.challenge.reviews.repository.ProductRepository;
import com.challenge.reviews.repository.ReviewRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@SpringBootTest(classes = {
		ReviewsApplication.class,
		H2TestProfileJPAConfig.class })
public class CommentsApplicationTests {

	@Autowired
	private MockMvc mvc;

	private JacksonTester<Comment> json;

	private JacksonTester<Product> jsonProduct;
	private JacksonTester<Review> jsonReview;

	@MockBean
	private CommentRepository commentRepository;

	@MockBean
	private ReviewRepository reviewRepository;

	@MockBean
	private ProductRepository productRepository;

	@Before
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		Comment comment = getComment();
		given(commentRepository.save(any())).willReturn(comment);
		given(commentRepository.findById(comment.getCommentId())).willReturn(Optional.of(comment));
		given(commentRepository.findAll()).willReturn(Collections.singletonList(comment));

		Product product = getProduct();
		given(productRepository.save(any())).willReturn(product);
		given(productRepository.findById(product.getProductId())).willReturn(Optional.of(product));
		given(productRepository.findAll()).willReturn(Collections.singletonList(product));

		Review review = getReview();
		given(reviewRepository.save(any())).willReturn(review);
		given(reviewRepository.findById(review.getReviewId())).willReturn(Optional.of(review));
		given(reviewRepository.findAll()).willReturn(Collections.singletonList(review));
	}

	@Test
	public void createCommentForReview() throws Exception {
		Review review = getReview();
		Product product = getProduct();
		Comment comment = getComment();

		mvc.perform(
				post(new URI("/products"))
						.content(jsonProduct.write(product).getJson())
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());

		mvc.perform(
				post(new URI("/reviews/products/1"))
						.content(jsonReview.write(review).getJson())
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());

		mvc.perform(
				post(new URI("/comments/reviews/1"))
						.content(json.write(comment).getJson())
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());
	}


	@Test
	public void listCommentsForReview() throws Exception {

		Review review = getReview();
		Product product = getProduct();
		Comment comment = getComment();

		comment.setReview(review);
		review.setProduct(product);

		mvc.perform(
				post(new URI("/products"))
						.content(jsonProduct.write(product).getJson())
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());

		mvc.perform(
				post(new URI("/reviews/products/1"))
						.content(jsonReview.write(review).getJson())
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());

		mvc.perform(
				post(new URI("/comments/reviews/1"))
						.content(json.write(comment).getJson())
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());

		mvc.perform(
				get(new URI("/comments/reviews/1"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(result -> hasSize(1))
				.andExpect(status().isOk());
	}



	private Review getReview() {
		Review review = new Review();
		review.setReviewId(1L);
		review.setScore(4);
		return review;
	}

	private Product getProduct() {
		Product product = new Product();
		product.setProductId(1L);
		product.setName("Teste Produtos");
		product.setDescription("Teste Description");
		return product;
	}

	private Comment getComment() {
		Comment comment = new Comment();
		comment.setCommentId(1L);
		comment.setComment("Teste Comment");
		comment.setTitle("Teste title");
		return comment;
	}
}
