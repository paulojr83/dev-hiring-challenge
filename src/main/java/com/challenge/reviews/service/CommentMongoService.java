package com.challenge.reviews.service;

import com.challenge.reviews.domain.comment.Comment;

public interface CommentMongoService {

	public void saveComment(Comment jpaComment);
}
