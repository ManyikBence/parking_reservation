INSERT INTO parking_spots (id, spot_number, spot_type, description) VALUES (1, 'A-101', 'REGULAR', 'Standard parking spot');
INSERT INTO parking_spots (id, spot_number, spot_type, description) VALUES (2, 'A-102', 'REGULAR', 'Standard parking spot');
INSERT INTO parking_spots (id, spot_number, spot_type, description) VALUES (3, 'EV-201', 'ELECTRIC_CHARGING', 'Spot with 22kW EV charger');
INSERT INTO parking_spots (id, spot_number, spot_type, description) VALUES (4, 'DISABLED-01', 'HANDICAPPED', 'Reserved for disabled badge holders');

-- A kezdő foglalás beszúrása explicit ID nélkül:
INSERT INTO reservations (parking_spot_id, applicant_name, start_time, end_time, cancelled)
VALUES (1, 'Kovács Péter', '2026-08-10 08:00:00', '2026-08-10 16:00:00', false);

-- A sequence számlálók igazítása az inicializált adatokhoz:
SELECT setval('parking_spots_id_seq', (SELECT MAX(id) FROM parking_spots));
SELECT setval('reservations_id_seq', (SELECT MAX(id) FROM reservations));