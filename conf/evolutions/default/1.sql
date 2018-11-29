# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table notification (
  id                            bigint auto_increment not null,
  created_by                    bigint,
  updated_by                    bigint,
  title                         TEXT,
  message                       TEXT,
  referenced_domain             integer,
  action                        integer,
  reference_id                  varchar(255),
  viewed                        timestamp,
  opened                        timestamp,
  user_id                       bigint,
  created                       timestamp not null,
  updated                       timestamp not null,
  constraint ck_notification_referenced_domain check ( referenced_domain in (0)),
  constraint ck_notification_action check ( action in (0)),
  constraint pk_notification primary key (id)
);

create table refresh_token (
  id                            bigint auto_increment not null,
  created_by                    bigint,
  updated_by                    bigint,
  token                         TEXT,
  user_id                       bigint not null,
  active                        boolean,
  created                       timestamp not null,
  updated                       timestamp not null,
  constraint pk_refresh_token primary key (id)
);

create table user_account (
  id                            bigint auto_increment not null,
  created_by                    bigint,
  updated_by                    bigint,
  name                          varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  picture                       varchar(255),
  thumbnail                     varchar(255),
  notifications_enabled         boolean,
  username                      varchar(255),
  is_on                         boolean,
  phone                         bigint,
  phone_prefix                  varchar(255),
  profile                       varchar(5),
  sms_validation_code           integer,
  lang                          varchar(255),
  created                       timestamp not null,
  updated                       timestamp not null,
  constraint ck_user_account_profile check ( profile in ('ADMIN')),
  constraint pk_user_account primary key (id)
);

create table user_device (
  id                            bigint auto_increment not null,
  created_by                    bigint,
  updated_by                    bigint,
  device_token                  varchar(255),
  device_os                     varchar(255),
  os_version                    varchar(255),
  user_id                       bigint,
  created                       timestamp not null,
  updated                       timestamp not null,
  constraint pk_user_device primary key (id)
);

alter table notification add constraint fk_notification_user_id foreign key (user_id) references user_account (id) on delete restrict on update restrict;
create index ix_notification_user_id on notification (user_id);

alter table refresh_token add constraint fk_refresh_token_user_id foreign key (user_id) references user_account (id) on delete restrict on update restrict;
create index ix_refresh_token_user_id on refresh_token (user_id);

alter table user_device add constraint fk_user_device_user_id foreign key (user_id) references user_account (id) on delete restrict on update restrict;
create index ix_user_device_user_id on user_device (user_id);


# --- !Downs

alter table notification drop constraint if exists fk_notification_user_id;
drop index if exists ix_notification_user_id;

alter table refresh_token drop constraint if exists fk_refresh_token_user_id;
drop index if exists ix_refresh_token_user_id;

alter table user_device drop constraint if exists fk_user_device_user_id;
drop index if exists ix_user_device_user_id;

drop table if exists notification;

drop table if exists refresh_token;

drop table if exists user_account;

drop table if exists user_device;

