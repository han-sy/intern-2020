use block_board;
drop table comments;
drop table posts;
drop table users;
drop table functions_check;
drop table functions;
drop table boards;
drop table companies;

create table companies(
	company_id int(9) not null,
    company_name varchar(20) not null,
    primary key(company_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table users(
	user_id varchar(20) not null,
    company_id int(9) not null,
    user_name varchar(30) not null,
    user_password varchar(100) not null,
    user_type varchar(20) not null,
    foreign key(company_id) references companies(company_id),
    primary key(user_id,company_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table functions(
	function_id int(9) not null AUTO_INCREMENT,
    function_name varchar(150) not null,
    primary key(function_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table functions_check(
	company_id int(9) not null,
    function_id int(9) not null,
    function_data varchar(300),
    foreign key(company_id) references companies(company_id),
    foreign key(function_id) references functions(function_id),
    primary key(company_id,function_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table boards(
	board_id int(9) not null AUTO_INCREMENT,
    company_id int(9) not null,
    board_name varchar(150) not null,
	foreign key(company_id) references companies(company_id),
    primary key(board_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table posts(
	post_id int(9) not null AUTO_INCREMENT,
    user_id varchar(20) not null,
    board_id int(9) not null,
    company_id int(9) not null,
    post_title varchar(150) not null,
    post_content text character set utf8mb4 collate utf8mb4_unicode_ci not null,
	post_content_except_htmltag varchar(10000) character set utf8mb4 collate utf8mb4_unicode_ci not null,
    post_register_time timestamp not null,
    is_temp boolean not null,
	foreign key(user_id) references users(user_id) ON DELETE CASCADE,
	foreign key(board_id) references boards(board_id) ON DELETE CASCADE,
	foreign key(company_id) references companies(company_id) ON DELETE CASCADE,
	primary key(post_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table comments(
	comment_id int(9) not null AUTO_INCREMENT,
	post_id int(9) not null,
	user_id varchar(20) not null,
	company_id int(9) not null,
    comment_content varchar(4000) character set utf8mb4 collate utf8mb4_unicode_ci not null,
    comment_register_time timestamp not null,
    comment_referenced_id int(9),
    comment_referenced_user_id varchar(20),
	foreign key(post_id) references posts(post_id) ON DELETE CASCADE ,
	foreign key(user_id) references users(user_id) ON DELETE CASCADE,
	foreign key(company_id) references companies(company_id) ON DELETE CASCADE,
    primary key(comment_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

alter table boards auto_increment=1;
alter table posts auto_increment=1;
alter table comments auto_increment=1;

insert into companies values(1,'WORKS MOBILE');
insert into companies values(2,'naver');

insert into boards (company_id,board_name) values(1,"공지사항");
insert into boards (company_id,board_name) values(2,"건의사항");
insert into boards (company_id,board_name) values(2,"공지사항");
insert into boards (company_id,board_name) values(2,"건의사항");
insert into boards (company_id,board_name) values(1,"자유게시판");

insert into users values(1,1,'김동욱','123','관리자');
insert into users values(2,1,'전우혁','123','사원');
insert into users values(3,2,'곽대훈','123','관리자');

insert into functions values(1,'댓글');
insert into functions values(2,'대댓글');
insert into functions values(3,'파일첨부');
insert into functions values(4,'inline 이미지');
insert into functions values(5,'임시저장');
insert into functions values(6,'스티커');

insert into functions_check values(1,1,null);
insert into functions_check values(2,4,null);

insert into posts values(1,1,1,1,'첫게시글','<p>첫내용</p>', '첫내용', now(),false);
insert into posts values (2,1,1,1,'두번째 게시글','<p>두번째 게시글내용</p>','두번째 게시글내용',now(),false);
insert into posts values (3,2,1,1,'건의사항 게시판 첫글','<p>공지사항이네</p>', '공지사항이네',now(),false);
insert into posts values (4,2,1,1,'공지사항 게시판 ','<p>ㅎㅎㅎㅎ테스트</p>','ㅎㅎㅎㅎ테스트',now(),false);
insert into posts values (5,1,5,1,'자유1','<p>1111</p>','1111',now(),false);
insert into posts values (6,1,5,1,'자유2','<p>22222</p>','22222',now(),false);

insert into comments values(1,1,1,1,'첫 댓글',now(),null,null);
insert into comments values(2,1,1,1,'첫 답글',now(),1,1);

select * from posts;