create database if not exists CampDB;

create table if not exists Users (
     id int primary key,
     username varchar(255) not null unique,
     password varchar(225) not null,
     create_at datetime not null,
     private boolean not null default false
);

-- Create NationalParks table
create table if not exists NationalParks (
     id int primary key,
     name varchar(255) not null unique
);

-- Create UserFavoriteParks table
create table if not exists UserFavoriteParks (
     user_id int not null,
     park_id int not null,
     api_id int not null,
     primary key (user_id, park_id),
     foreign key (user_id) references Users(id),
     foreign key (park_id) references NationalParks(id)
);
