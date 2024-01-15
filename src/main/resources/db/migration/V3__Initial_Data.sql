insert into role(id, name)
values (1, 'ADMIN'),
       (2, 'PLAYER');

insert into users(id, nickname, email, password, activated, role_id)
values (nextval('users_seq'), 'admin', 'admin@gmail.com', 'qwe123', true, 1),
       (nextval('users_seq'), 'player', 'player@gmail.com', 'qwe123', true, 2);

update users
set password = crypt(password, gen_salt('bf', 8));