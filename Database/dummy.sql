use block_board;

DELIMITER $$
DROP PROCEDURE IF EXISTS dummyPostInsert$$
CREATE PROCEDURE dummyPostInsert()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10000 DO
	INSERT INTO posts (
		user_id, 
        board_id, 
        company_id, 
        post_title, 
        post_content, 
        post_content_except_htmltag, 
        post_register_time) 
	VALUES (
		1,
        1,
        1,
        CONCAT('Dummy',i),
        CONCAT('<p>DummyContent', i , '</p>'),
        CONCAT('DummyContent', i),
        now());
	SET i = i + 1;
    END WHILE;
END$$
DELIMITER $$

DELIMITER $$
DROP PROCEDURE IF EXISTS dummyCommentInsert$$
CREATE PROCEDURE dummyCommentInsert()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10000 DO
	INSERT INTO comments (
		post_id,
		user_id,
		company_id,
		comment_content,
		comment_content_except_htmltag,
		comment_register_time,
		comment_referenced_id)
	VALUES (
		1,
        2,
        1,
        CONCAT('<p>DummyContent', i , '</p>'),
        CONCAT('DummyContent', i),
        now(),
        null);
	SET i = i + 1;
    END WHILE;
END$$
DELIMITER $$

DELIMITER $$
DROP PROCEDURE IF EXISTS dummyCommentInsert2$$
CREATE PROCEDURE dummyCommentInsert2()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10000 DO
	INSERT INTO comments (
		post_id,
		user_id,
		company_id,
		comment_content,
		comment_content_except_htmltag,
		comment_register_time,
		comment_referenced_id)
	VALUES (
		2,
        2,
        1,
        CONCAT('<p>DummyContent', i , '</p>'),
        CONCAT('DummyContent', i),
        now(),
        null);
	SET i = i + 1;
    END WHILE;
END$$
DELIMITER $$

DELIMITER $$
DROP PROCEDURE IF EXISTS dummyReplyInsert$$
CREATE PROCEDURE dummyReplyInsert()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10000 DO
	INSERT INTO comments (
		post_id,
		user_id,
		company_id,
		comment_content,
		comment_content_except_htmltag,
		comment_register_time,
		comment_referenced_id)
	VALUES (
		1,
        2,
        1,
        CONCAT('<p>DummyContent', i , '</p>'),
        CONCAT('DummyContent', i),
        now(),
        1);
	SET i = i + 1;
    END WHILE;
END$$
DELIMITER $$

DELIMITER $$
DROP PROCEDURE IF EXISTS dummyUserInsert$$
CREATE PROCEDURE dummyUserInsert()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 500 DO
	INSERT INTO users (
		user_id,
		company_id,
		user_name,
		user_password,
		user_type,
		image_url,
		image_file_name,
		thumbnail_url,
		thumbnail_file_name)
	VALUES (
		CONCAT('dummy', i),
        1,
        CONCAT('dummy', i),
        '123',
        '일반',
        'https://block-board-user.s3.amazonaws.com/admin.png',
        'admin.png',
        'https://block-board-user-thumbnail.s3.amazonaws.com/admin.png',
        'admin.png');
	SET i = i + 1;
    END WHILE;
END$$
DELIMITER $$

DELIMITER $$
DROP PROCEDURE IF EXISTS dummyAlarmInsert$$
CREATE PROCEDURE dummyAlarmInsert()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 10000 DO
	INSERT INTO alarms (
		tagged_user_id,
		post_id,
		comment_id, 
		is_read)
	VALUES (
        1,
        1,
        null,
        false);
	SET i = i + 1;
    END WHILE;
END$$
DELIMITER $$

CALL dummyPostInsert;
CALL dummyCommentInsert;
CALL dummyCommentInsert2;
CALL dummyReplyInsert;
CALL dummyUserInsert;
CALL dummyAlarmInsert;