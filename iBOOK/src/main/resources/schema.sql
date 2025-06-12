drop table if exists tbl_booking;
drop table if exists tbl_seat;
drop table if exists tbl_study_room;
drop table if exists tbl_roles_user;
drop table if exists tbl_student;

create table tbl_student (
    id int not null auto_increment primary key,
    stuNum varchar(32) not null unique,
    name varchar(32) not null,
    password varchar(256) not null
);

create table tbl_roles_user (
    id int not null auto_increment primary key,
    stuId varchar(32) not null,
    rid int not null default 2,
    foreign key (stuId) references tbl_student (stuNum) on delete cascade
);

create table tbl_study_room (
    id int not null auto_increment primary key,
    buildingNum varchar(16) not null,
    classRoomNum varchar(16) not null,
    startTime int not null default 7,
    endTime int not null default 22,
    state int default 1
);

create table tbl_seat (
    id int not null auto_increment primary key,
    studyRoomId int not null,
    state int default 0,
    foreign key (studyRoomId) references tbl_study_room (id) on delete cascade
);

create table tbl_booking (
    id int not null auto_increment primary key,
    seatId int not null,
    startTime timestamp not null default current_timestamp,
    bookingPeriod int not null,
    stuNum varchar(32) not null,
    state int default 0,
    opTime timestamp not null default current_timestamp,
    detail varchar(256),
    foreign key (seatId) references tbl_seat (id) on delete cascade,
    foreign key (stuNum) references tbl_student (stuNum) on delete cascade
);