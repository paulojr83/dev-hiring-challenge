ALTER TABLE review add constraint review_product_id_fk foreign key (product_id) references product (product_id);

ALTER TABLE comment add constraint comment_review_id_fk foreign key (review_id) references review (review_id);
