create table consultations (id bigint not null auto_increment,
                      title varchar(255) not null,
                      time date not null,
                      mentor_id bigint not null,
                      subject varchar(1000),
                      primary key (id));