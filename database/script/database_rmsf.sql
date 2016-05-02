drop table if exists users;
drop table if exists usersDevices;
drop table if exists usersAlarms;

create table users(
	filename varchar(40),
	userName varchar(255),
	password varchar(255),
	lastMessageTime varchar(40),
	primary key(filename));

create table usersDevices(
	filename varchar(40),
	device varchar(40),
	primary key(filename, device));

create table usersAlarms(
	filename varchar(40),
	alarm varchar(40),
	primary key(filename, alarm));


insert into users values('Bernardo', '56c47b4c9336adb5ba39c9b6', 'dd6bd147da1dcc9e34b4674b0f0be948', '0');
insert into usersDevices values('Bernardo', '56bdd1da9336b182b106d3b0');
insert into usersDevices values('Bernardo', 'teste');
insert into users values('Diogo', '571a89a093368471a5a2e879','e5c2bfa6b523d9028d666f9f1f6ae4de', '0');
insert into usersDevices values('Diogo', '571a18ca9336f164577acaa9');


