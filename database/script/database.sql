drop table if exists users;

create table users(
	filename varchar(40),
	userName varchar(255),
	password varchar(255),
	device varchar(255),
	primary key(filename));