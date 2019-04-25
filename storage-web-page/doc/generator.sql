create table pmt_tb_generator (
         sequence_name varchar(255) not null ,
         sequence_next_hi_value bigint,
        primary key ( sequence_name ) 
) ENGINE=InnoDB ;