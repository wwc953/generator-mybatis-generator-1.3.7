drop table AppleTable if exists;
create table AppleTable
(
    My_aaa decimal(10),
    My_Id         int not null,
    My_Name       varchar(20),
    My_Date       date,
    My_Timestamp  timestamp,
    My_Decimal155 decimal(15, 5),
    primary key (My_Id)
);

comment on table AppleTable is 'xxx表';
comment on column AppleTable.My_Id is '主键';
comment on column AppleTable.My_Name is '名称';
comment on column AppleTable.My_Date is 'date时间';
comment on column AppleTable.My_Timestamp is 'Timestamp时间';
comment on column AppleTable.My_Decimal155 is 'decimal(15, 5)类型';


