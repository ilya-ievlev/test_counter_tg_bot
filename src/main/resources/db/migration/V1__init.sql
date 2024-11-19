create table if not exists users
(
    id           bigint      not null primary key auto_increment unique,
    username     varchar(32) not null,
    first_name   varchar(64) not null,
    last_name    varchar(64) not null,
    is_bot_admin boolean     not null
);

create table if not exists tests
(
    id   bigint       not null primary key unique,
    name varchar(150) not null
);

create table if not exists test_results
(
    id                  bigint       not null primary key auto_increment unique,
    test_id             bigint       not null,
    date_of_result      datetime     not null,
    user_executor_id    bigint       not null,
    user_uploaded_by_id bigint       not null,
    message_id          bigint       not null,
    message_text        varchar(500) not null,
    foreign key (test_id) references tests (id),
    foreign key (user_executor_id) references users (id),
    foreign key (user_uploaded_by_id) references users (id)
);

create table if not exists weekly_reports
(
    id                bigint   not null primary key auto_increment unique,
    start_of_the_week datetime not null,
    end_of_the_week   datetime not null
);

create table if not exists weekly_report_results
(
    id                  bigint not null primary key auto_increment unique,
    weekly_report_id    bigint not null,
    user_executor_id    bigint not null,
    user_uploaded_by_id bigint not null,
    foreign key (weekly_report_id) references weekly_reports (id),
    foreign key (user_executor_id) references users (id),
    foreign key (user_uploaded_by_id) references users (id)
)