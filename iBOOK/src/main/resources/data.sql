insert into tbl_student (stuNum, name, password)
values ('123456', 'syz', 'e10adc3949ba59abbe56e057f20f883e'),
       ('01010102', '01010102', 'e10adc3949ba59abbe56e057f20f883e'),
       ('01010103', '01010103', 'e10adc3949ba59abbe56e057f20f883e'),
       ('01010104', '01010104', 'e10adc3949ba59abbe56e057f20f883e'),
       ('01010105', '01010105', 'e10adc3949ba59abbe56e057f20f883e');

insert into tbl_roles_user (stuId, rid)
values ('123456', 1),
       ('01010102', 2),
       ('01010103', 2),
       ('01010104', 2),
       ('01010105', 2);

insert into tbl_study_room (buildingNum, classRoomNum, startTime, endTime)
values ('JA', '205', 7, 22),
       ('JB', '302', 7, 22),
       ('H3', '108', 1, 23),
       ('HGX', '501', 7, 22);

insert into tbl_seat (studyRoomId)
values ('1'),
       ('1'),
       ('1'),
       ('1'),
       ('1');

insert into tbl_booking (seatId, startTime, bookingPeriod, stuNum, state)
values (1, '2022-04-02 10:00:00', 4, '123456', 4),
       (2, '2022-04-01 10:00:00', 4, '01010102', 0),
       (3, '2022-04-02 10:00:00', 4, '01010103', 1),
       (4, '2022-04-03 10:00:00', 4, '01010104', -1),
       (5, '2022-04-04 10:00:00', 4, '01010105', 4);