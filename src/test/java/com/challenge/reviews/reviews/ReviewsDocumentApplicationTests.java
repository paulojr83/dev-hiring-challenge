package com.challenge.reviews.reviews;

import com.challenge.reviews.domain.review.ReviewDocument;
import com.challenge.reviews.config.H2TestProfileJPAConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.challenge.reviews.ReviewsApplication;
import com.challenge.reviews.repository.ReviewMongoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@SpringBootTest(classes = {
        ReviewsApplication.class,
        H2TestProfileJPAConfig.class })
public class ReviewsDocumentApplicationTests {

    @Autowired
    private MockMvc mvc;

    private JacksonTester<ReviewDocument> json;

    @MockBean
    private ReviewMongoRepository reviewRepository;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        ReviewDocument review = getReviewDocument();
        given(reviewRepository.save(any())).willReturn(review);
        given(reviewRepository.findById(review.getReviewId())).willReturn(Optional.of(review));
        given(reviewRepository.findAll()).willReturn(Collections.singletonList(review));
    }


    @Test
    public void createReviewForProduct() throws Exception {
        ReviewDocument reviewDocument = getReviewDocument();
        ReviewDocument save = reviewRepository.save(reviewDocument);
        assertThat(save.getReviewId()).isEqualTo(reviewDocument.getReviewId());
    }

   private ReviewDocument getReviewDocument() {

       ReviewDocument review = new ReviewDocument();
        review.setReviewId("dsdasdsadsadsadasdsa");
        review.setRecommended(true);
        review.setReviewText("Review text");
        review.setReviewTitle("Review title");
        return review;
    }

}
