drop table if exists users;
drop table if exists usersDevices;
drop table if exists usersAlarms;

create table users(
	filename varchar(40),
	userName varchar(255),
	password varchar(255),
	primary key(filename));

create table usersDevices(
	filename varchar(40),
	device varchar(40),
	primary key(filename, device));

create table usersAlarms(
	filename varchar(40),
	alarme varchar(40),
	primary key(filename));


insert into users values('Bernardo', '56c47b4c9336adb5ba39c9b6', 'dd6bd147da1dcc9e34b4674b0f0be948');
insert into usersDevices values('Bernardo', '56bdd1da9336b182b106d3b0');
insert into usersDevices values('Bernardo', 'teste');


