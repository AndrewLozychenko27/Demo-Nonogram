DO
$$
    BEGIN
        FOR x IN 0..39
            LOOP
                FOR y IN 0..39
                    LOOP
                        INSERT INTO cell(id, x, y) VALUES (NEXTVAL('cell_seq'), x, y);
                    END LOOP;
            END LOOP;
    END;
$$;

INSERT INTO users(id, nickname, email, password, activated, role)
VALUES (NEXTVAL('users_seq'), 'maker', 'maker@test.org', 'qwe123', true, 'PLAYER');

DO
$$
    BEGIN
        FOR i IN 1..5
            LOOP
                INSERT INTO puzzle(id, name, width, height, visible, user_id)
                VALUES (NEXTVAL('puzzle_seq'), CONCAT('Puzzle S ', i), 10, 10, true,
                        (SELECT id FROM users WHERE nickname = 'maker')),
                       (NEXTVAL('puzzle_seq'), CONCAT('Puzzle M ', i), 15, 15, true,
                        (SELECT id FROM users WHERE nickname = 'maker')),
                       (NEXTVAL('puzzle_seq'), CONCAT('Puzzle L ', i), 20, 20, true,
                        (SELECT id FROM users WHERE nickname = 'maker')),
                       (NEXTVAL('puzzle_seq'), CONCAT('Puzzle XL ', i), 25, 25, true,
                        (SELECT id FROM users WHERE nickname = 'maker')),
                       (NEXTVAL('puzzle_seq'), CONCAT('Puzzle XXL ', i), 30, 30, true,
                        (SELECT id FROM users WHERE nickname = 'maker'));
            END LOOP;
    END;
$$;

INSERT INTO solution(puzzle_id, cell_id)
SELECT p.id, cell.id
FROM (SELECT id
      FROM cell
      WHERE x < 10 AND y < 10
      ORDER BY RANDOM()
      LIMIT 30) cell
         CROSS JOIN puzzle p
WHERE p.name LIKE 'Puzzle S%';

INSERT INTO solution(puzzle_id, cell_id)
SELECT p.id, cell.id
FROM (SELECT id
      FROM cell
      WHERE x < 15 AND y < 15
      ORDER BY RANDOM()
      LIMIT 50) cell
         CROSS JOIN puzzle p
WHERE p.name LIKE 'Puzzle M%';

INSERT INTO solution(puzzle_id, cell_id)
SELECT p.id, cell.id
FROM (SELECT id
      FROM cell
      WHERE x < 20 AND y < 20
      ORDER BY RANDOM()
      LIMIT 80) cell
         CROSS JOIN puzzle p
WHERE p.name LIKE 'Puzzle L%';

INSERT INTO solution(puzzle_id, cell_id)
SELECT p.id, cell.id
FROM (SELECT id
      FROM cell
      WHERE x < 25 AND y < 25
      ORDER BY RANDOM()
      LIMIT 100) cell
         CROSS JOIN puzzle p
WHERE p.name LIKE 'Puzzle XL%';

INSERT INTO solution(puzzle_id, cell_id)
SELECT p.id, cell.id
FROM (SELECT id
      FROM cell
      WHERE x < 30 AND y < 30
      ORDER BY RANDOM()
      LIMIT 200) cell
         CROSS JOIN puzzle p
WHERE p.name LIKE 'Puzzle XXL%';