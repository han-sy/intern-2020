use block_board;
drop table Comments;
drop table Post;
drop table Users;
drop table FunctionCheck;
drop table BoardFunction;
drop table Board;
drop table Company;

create table Company(
	com_id int(9) not null,
    com_name varchar(20) not null,
    primary key(com_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Users(
	user_id varchar(20) not null,
    com_id int(9) not null,
    user_name varchar(30) not null,
    user_pwd varchar(100) not null,
    user_type varchar(20) not null,
    foreign key(com_id) references Company(com_id),
    primary key(user_id,com_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table BoardFunction(
	func_id int(9) not null,
    func_name varchar(150) not null,
    primary key(func_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table FunctionCheck(
	com_id int(9) not null,
    func_id int(9) not null,
    func_data varchar(300),
    foreign key(com_id) references Company(com_id),
    foreign key(func_id) references BoardFunction(func_id),
    primary key(com_id,func_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Board(
	board_id int(9) not null,
    com_id int(9) not null,
    board_name varchar(150) not null,
	foreign key(com_id) references Company(com_id),
    primary key(board_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Post(
	post_id int(9) not null,
    user_id varchar(20) not null,
    board_id int(9) not null,
    com_id int(9) not null,
    post_title varchar(150) not null,
    post_content varchar(4000) not null,
    post_reg_time datetime not null,
	foreign key(user_id) references Users(user_id),
	foreign key(board_id) references Board(board_id),
	foreign key(com_id) references Company(com_id),
	primary key(post_id) 
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Comments(
	comment_id int(9) not null,
	post_id int(9) not null,
	user_id varchar(20) not null,
	com_id int(9) not null,
	foreign key(post_id) references Post(post_id),
	foreign key(user_id) references Users(user_id),
	foreign key(com_id) references Company(com_id),
    comment_content varchar(4000) not null,
    comment_reg_time datetime not null,
    comment_referenced_id int(9),
    primary key(comment_id)
)ENGINE =InnoDB DEFAULT charset= utf8;

insert into Company values(1,'wm'); 
insert into Company values(2,'naver');
insert into Board values(1,1,"공지사항");
insert into Board values(2,1,"건의사항");
insert into Board values(3,2,"공지사항");
insert into Board values(4,2,"건의사항");
insert into Board values(5,1,"자유게시판");

insert into Users values(1,1,'김동욱','123','관리자');
insert into Users values(2,1,'전우혁','123','사원');
insert into Users values(3,2,'곽대훈','123','관리자');
insert into BoardFunction values(1,'기독기능');
insert into FunctionCheck values(1,1,true);
insert into BoardFunction values(1,'댓글작성');
insert into FunctionCheck values(1,1,null);
insert into Post values(1,1,1,1,'첫게시글','첫내용',now());
insert into Comments values(1,1,1,1,'첫 댓글',now(),null);
insert into Comments values(2,1,1,1,'첫 답글',now(),1);

Select * from Post;
select * from Users;
insert into Post values (2,1,1,1,'두번째 게시글','두번째 게시글내용',now());
insert into Post values (3,2,1,1,'건의사항 게시판 첫글','공지사항이네',now());
insert into Post values (4,2,1,1,'공지사항 게시판 ','ㅎㅎㅎㅎ테스트',now());
insert into Post values (5,1,5,1,'자유1','1111',now());
insert into Post values (6,1,5,1,'자유2','22222',now());
			 
SELECT p.post_id,p.user_id, u.user_name,p.board_id,p.com_id,p.post_title,p.post_content,p.post_reg_time
        FROM Post p , Users u
        WHERE p.user_id = u.user_id and p.board_id=1
        ORDER BY p.post_id DESC;

SELECT p.post_id,p.user_id, u.user_name,p.board_id,p.com_id,p.post_title,p.post_content,p.post_reg_time
        FROM Post p , Users u
        WHERE p.user_id = u.user_id and p.board_id=1
        ORDER BY p.post_id DESC;
