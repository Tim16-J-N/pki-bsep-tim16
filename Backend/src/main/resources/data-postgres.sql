INSERT INTO authority
    (name)
SELECT 'ROLE_ADMIN'
WHERE
    NOT EXISTS (
        SELECT name FROM authority WHERE name = 'ROLE_ADMIN'
    );