create table t_user
(
    id            bigint auto_increment
        primary key,
    hash_password varchar(255) null,
    jwt           varchar(255) null,
    login         varchar(255) null
);


create table t_file
(
    id          bigint       not null
        primary key,
    file        longblob     null,
    filename    varchar(255) null,
    size        bigint       null,
    time_create datetime(6)  null,
    user_id bigint not null,
    constraint user_id_FK foreign key (user_id) references diplomDB.t_user(id)
);

INSERT INTO t_user(login, hash_password, jwt)
VALUES ('test', '98f6bcd4621d373cade4e832627b4f6',
        'eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NCwibG9naW4iOiJ0ZXN0IiwianRpIjoiNWVjZTk4ZDgtNTFlOC00YTQ5LWI1OTctZDEzODE3NTJhN2VlIiwiaWF0IjoxNjc2MTk3MDk5LCJleHAiOjE2NzYxOTc2OTl9.XWB10tpWxQ20f4oshZaH-yzaLKAmEEtiu2-CcRMwFsY');


