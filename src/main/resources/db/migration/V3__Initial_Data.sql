insert into users(id, nickname, email, password, activated, role)
values (nextval('users_seq'), 'admin', 'admin@gmail.com', 'qwe123', true, 'ADMIN'),
       (nextval('users_seq'), 'player', 'player@gmail.com', 'qwe123', true, 'PLAYER');

update users
set password = crypt(password, gen_salt('bf', 8));