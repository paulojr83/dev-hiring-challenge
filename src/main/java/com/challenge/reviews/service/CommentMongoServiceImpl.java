package com.challenge.reviews.service;

import com.challenge.reviews.domain.comment.Comment;
import com.challenge.reviews.domain.comment.CommentDocument;
import com.challenge.reviews.repository.CommentsMongoRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentMongoServiceImpl implements CommentMongoService{

	private final CommentsMongoRepository commentsMongoRepository;

	public CommentMongoServiceImpl(CommentsMongoRepository commentsMongoRepository) {
		this.commentsMongoRepository = commentsMongoRepository;
	}

	@Override
	public void saveComment(Comment jpaComment) {
		CommentDocument commentDocument = new CommentDocument(jpaComment);
		this.commentsMongoRepository.save(commentDocument);
	}
}
