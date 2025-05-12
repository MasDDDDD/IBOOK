drop table if exists tbl_student;
create table tbl_student
(
    id       int         not null auto_increment,
    stuNum   varchar(32) not null,
    name     varchar(32) not null,
    password varchar(256) not null,
    primary key (id),
    unique (stuNum)
);

drop table if exists tbl_roles_user;
create table tbl_roles_user
(
    id       int         not null auto_increment,
    stuId	 varchar(32) not null,
    rid		 int not null default 2,
    primary key (id),
    foreign key (stuId) references tbl_student (stuNum) ON DELETE CASCADE
);

drop table if exists tbl_study_room;
create table tbl_study_room
(
    id           int         not null auto_increment,
    buildingNum  varchar(16) not null,
    classRoomNum varchar(16) not null,
    startTime int not null default 7,
    endTime int not null default 22,
    state int default 1 comment '0 不可用，1 可用',
    primary key (id)
);

drop table if exists tbl_seat;
create table tbl_seat
(
    id          int not null auto_increment,
    studyRoomId int not null,
    state int default 0 comment '0 未预约，1 已预约，2 已签到',
    primary key (id, studyRoomId),
    foreign key (studyRoomId) references tbl_study_room (id) ON DELETE CASCADE
);

drop table if exists tbl_booking;
create table tbl_booking
(
    id            int not null auto_increment,
    seatId        int not null,
    startTime timestamp not null default current_timestamp,
    bookingPeriod int not null,
    stuNum         varchar(32) not null,
    state int default 0 comment '0 待签到，1 已签到，2 已取消，3 违约，4 已完成',
    opTime timestamp not null default current_timestamp,
    detail varchar(256),
    primary key (id),
    foreign key (seatId) references tbl_seat (id) ON DELETE CASCADE,
    foreign key (stuNum) references tbl_student (stuNum) ON DELETE CASCADE
);