DO $$
    BEGIN
        FOR i IN 1..20 LOOP
                INSERT INTO users(id, nickname, email, password, activated, role)
                VALUES(NEXTVAL('users_seq'), CONCAT('Dummie #', i), CONCAT(CONCAT('dummie', i), '@test.org'), 'qwe123', true, 'PLAYER');
            END LOOP;
    END;
$$;

DO $$
    DECLARE
        puzzles CURSOR FOR SELECT * FROM puzzle;
        players CURSOR FOR SELECT * FROM users WHERE nickname LIKE 'Dummie%';
    BEGIN
        FOR puzzle IN puzzles LOOP
                FOR player IN players LOOP
                        INSERT INTO game(id, user_id, puzzle_id, attempts, state)
                        VALUES(NEXTVAL('game_seq'), player.id, puzzle.id, FLOOR(RANDOM() * (20 - 2 + 1)) + 2, 'SOLVED');
                    END LOOP;
            END LOOP;
    END;
$$;

DO $$
    DECLARE
        games CURSOR FOR SELECT * FROM game;
    BEGIN
        FOR game IN games LOOP
                FOR i IN 1..FLOOR(RANDOM() * (20 - 1 + 1)) + 1 LOOP
                        INSERT INTO hint(game_id, cell_id) VALUES(game.id, i);
                    END LOOP;
            END LOOP;
    END;
$$;