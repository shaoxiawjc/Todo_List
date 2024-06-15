create database todo;
use todo;

create table todo.user
(
    id           bigint auto_increment comment '主键id'
        primary key,
    name         varchar(64)               not null comment '姓名',
    password     varchar(255)              not null comment 'md5盐值加密的密码',
    created_time timestamp default (now()) null,
    updated_time timestamp                 null on update CURRENT_TIMESTAMP,
    deleted      int       default 0       null,
    constraint user_name_uindex
        unique (name)
)
    charset = utf8mb3;



create table todo.todo
(
    id           bigint auto_increment comment '主键id'
        primary key,
    uid          bigint                    not null comment '用户id',
    title        varchar(255)              not null comment '待办标题',
    content      varchar(1024)             not null comment '待办内容',
    is_completed int       default 0       not null comment '是否完成',
    created_time timestamp default (now()) null,
    updated_time timestamp                 null,
    deleted      int       default 0       null
)
    charset = utf8mb3;

create index idx_created_time
    on todo.todo (created_time);

create index idx_is_completed
    on todo.todo (is_completed);

create index uid
    on todo.todo (uid);

