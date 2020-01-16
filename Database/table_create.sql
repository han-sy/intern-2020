use block_board;
drop table Comments;
drop table Post;
drop table Users;
drop table FunctionCheck;
drop table BoardFunction;
drop table Board;
drop table Company;

create table Company(
	companyID int(9) not null,
    companyName varchar(20) not null,
    primary key(companyID)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Users(
	userID varchar(20) not null,
    companyID int(9) not null,
    userName varchar(30) not null,
    userPassword varchar(100) not null,
    userType varchar(20) not null,
    foreign key(companyID) references Company(companyID),
    primary key(userID,companyID)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table BoardFunction(
	functionID int(9) not null AUTO_INCREMENT,
    functionName varchar(150) not null,
    primary key(functionID)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table FunctionCheck(
	companyID int(9) not null,
    functionID int(9) not null,
    functionData varchar(300),
    foreign key(companyID) references Company(companyID),
    foreign key(functionID) references BoardFunction(functionID),
    primary key(companyID,functionID)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Board(
	boardID int(9) not null AUTO_INCREMENT,
    companyID int(9) not null,
    boardName varchar(150) not null,
	foreign key(companyID) references Company(companyID),
    primary key(boardID)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Post(
	postID int(9) not null AUTO_INCREMENT,
    userID varchar(20) not null,
    boardID int(9) not null,
    companyID int(9) not null,
    postTitle varchar(150) not null,
    postContent varchar(4000) not null,
    postRegisterTime timestamp not null,
	foreign key(userID) references Users(userID),
	foreign key(boardID) references Board(boardID),
	foreign key(companyID) references Company(companyID),
	primary key(postID)
)ENGINE =InnoDB DEFAULT charset= utf8;

create table Comments(
	commentID int(9) not null AUTO_INCREMENT,
	postID int(9) not null,
	userID varchar(20) not null,
	companyID int(9) not null,
	foreign key(postID) references Post(postID),
	foreign key(userID) references Users(userID),
	foreign key(companyID) references Company(companyID),
    commentContent varchar(4000) not null,
    commentRegisterTime timestamp not null,
    commentReferencedID int(9),
    primary key(commentID)
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


SELECT p.postID,p.userID, u.userName,p.boardID,p.companyID,p.postTitle,p.postContent,p.postRegisterTime
        FROM Post p , Users u
        WHERE p.userID = u.userID and p.boardID=1
        ORDER BY p.postID DESC;

