# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table meal (
  id                        integer auto_increment not null,
  name                      varchar(255) not null,
  description               varchar(255) not null,
  image                     varchar(255) not null,
  current_priority          integer not null,
  priority_gain             integer not null,
  vetod                     tinyint(1) default 0 not null,
  constraint pk_meal primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  email                     varchar(255) not null,
  name                      varchar(255) not null,
  password                  varchar(255) not null,
  api_token                 varchar(255) not null,
  vetod                     tinyint(1) default 0 not null,
  admin                     tinyint(1) default 0 not null,
  constraint uq_user_email unique (email),
  constraint uq_user_api_token unique (api_token),
  constraint pk_user primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table meal;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

