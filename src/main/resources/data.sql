INSERT INTO surface_type (name, minute_price, deleted) VALUES
( 'Clay', 1.5, false),
( 'Grass', 2.0, false);

INSERT INTO court (court_number, surface_type_id, deleted) VALUES
( 101, 1, false),
( 102, 2, false);

INSERT INTO app_user (phone_number, user_name, deleted) VALUES
('777111222', 'Alice Novak', false),
('777222333', 'Bob Smith', false),
('777333444', 'Charlie Johnson', false),
('777444555', 'Dana Black', false);

INSERT INTO reservation (start_time, end_time, created_at, price, game_type, app_user_id, court_id, deleted) VALUES
('2025-05-25 09:00:00', '2025-05-25 10:00:00', '2025-05-20 15:00:00', 20.00, 'SINGLES', 1, 1, FALSE),
('2025-05-25 10:30:00', '2025-05-25 12:00:00', '2025-05-20 16:00:00', 30.00, 'DOUBLES', 2, 2, FALSE),
('2025-04-26 12:00:00', '2025-04-26 13:30:00', '2025-04-21 10:00:00', 25.00, 'SINGLES', 3, 1, FALSE),
('2025-05-26 16:00:00', '2025-05-26 17:30:00', '2025-05-19 11:30:00', 35.00, 'DOUBLES', 4, 1, FALSE);