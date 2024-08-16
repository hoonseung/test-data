insert into table_schema (id, schema_name, user_id, exported_at, created_at, created_by, modified_at, modified_by)
values (1, 'test_schema1', 'devJohn', null, now(), 'shlee', now(), 'shlee');


alter table table_schema
    auto_increment = 2;

insert into schema_field (table_schema_id, field_name, mock_data_type, field_order, blank_percent, type_option_json,
                          force_value,
                          created_at, created_by, modified_at, modified_by)
values (1, 'id', 'ROW_NUMBER', 1, 0, '{"start" : 1, "step" : 1}', null, now(), 'shlee', now(), 'shlee'),
       (1, 'age', 'NUMBER', 3, 0, '{"min" : 1, "max" : 30, "decimal" : 0}', null, now(), 'shlee', now(), 'shlee'),
       (1, 'name', 'STRING', 2, 0, '{"minLength" : 1, "maxLength" : 10}', null, now(), 'shlee', now(), 'shlee'),
       (1, 'car', 'STRING', 4, 0, '{"minLength" : 1, "maxLength" : 10}', null, now(), 'shlee', now(), 'shlee');


insert into mock_data(mock_data_type, mock_data_value)
values ('name', '김민준'),
       ('name', '이서윤'),
       ('name', '박지윤'),
       ('name', '박지후'),
       ('name', '김서진'),
       ('name', '김세진'),
       ('name', '강예진'),
       ('name', '김혜은'),
       ('name', '이서빈'),
       ('car', '제네시스 G70'),
       ('car', '제네시스 G80'),
       ('car', '벤츠 S 클래스'),
       ('car', '벤츠 E 클래스'),
       ('car', '기아 K5'),
       ('car', '기아 K3');
