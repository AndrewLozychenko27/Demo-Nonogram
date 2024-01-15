CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE cell_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE puzzle_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE solution_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE game_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE suggestion_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE role (
    id int8 PRIMARY KEY,
    name varchar(256) NOT NULL UNIQUE
);

CREATE TABLE users (
    id int8 PRIMARY KEY,
    nickname varchar(256) NOT NULL UNIQUE,
    email varchar(256) NOT NULL UNIQUE,
    password varchar(256) NOT NULL,
    activated boolean NOT NULL DEFAULT FALSE,
    role_id int8 NOT NULL,
    CONSTRAINT FK_users_role FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE cell (
    id int8 PRIMARY KEY,
    x smallint NOT NULL,
    y smallint NOT NULL
);

CREATE TABLE state (
    id int8 PRIMARY KEY,
    name varchar(256) NOT NULL UNIQUE
);

CREATE TABLE puzzle (
    id int8 PRIMARY KEY,
    name varchar(256) NOT NULL,
    width smallint NOT NULL,
    height smallint NOT NULL,
    user_id int8 NOT NULL,
    CONSTRAINT FK_puzzle_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE solution (
    cell_id int8 NOT NULL,
    puzzle_id int8 NOT NULL,
    CONSTRAINT FK_solution_cell FOREIGN KEY (cell_id) REFERENCES cell(id) ON DELETE CASCADE,
    CONSTRAINT FK_solution_puzzle FOREIGN KEY (puzzle_id) REFERENCES puzzle(id) ON DELETE CASCADE
);

CREATE TABLE game (
    id int8 PRIMARY KEY,
    attempts integer NOT NULL DEFAULT 0,
    hints integer NOT NULL DEFAULT 0,
    user_id int8 NOT NULL,
    puzzle_id int8 NOT NULL,
    state_id int8 NOT NULL,
    CONSTRAINT FK_game_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT FK_game_puzzle FOREIGN KEY (puzzle_id) REFERENCES puzzle(id) ON DELETE CASCADE,
    CONSTRAINT FK_game_state FOREIGN KEY (state_id) REFERENCES state(id)
);

CREATE TABLE suggestion (
    cell_id int8 NOT NULL,
    game_id int8 NOT NULL,
    CONSTRAINT FK_suggestion_cell FOREIGN KEY (cell_id) REFERENCES cell(id) ON DELETE CASCADE,
    CONSTRAINT FK_suggestion_game FOREIGN KEY (game_id) REFERENCES game(id) ON DELETE CASCADE
);