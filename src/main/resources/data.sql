INSERT INTO usuario (nombre_completo, username, password) values
    ('Jose Antonio Marí Martín', 'jose@email.com', '$2a$10$lBzSDeiPcfCrOXBljvc1BOyI32oa9BXfhks6xBx8WubO5WdfLnh3a'),
    ('Juan Sebastian Gonzalez Sanchez', 'jsgonzalez@email.com', '$2a$11$TckEYxY/0DPf6OfpQhXKP./hl45UlgXYs0jQFsZZCwBEjUCo7bUKy'),
    ('Sara Martínez Martínez', 'sara@email.com', '$2a$10$lBzSDeiPcfCrOXBljvc1BOyI32oa9BXfhks6xBx8WubO5WdfLnh3a'),
    ('Alejandro Tur Costa', 'al@email.com', '$2a$10$lBzSDeiPcfCrOXBljvc1BOyI32oa9BXfhks6xBx8WubO5WdfLnh3a'),
    ('Marta Carrasco Carrasco', 'marta@email.com', '$2a$10$lBzSDeiPcfCrOXBljvc1BOyI32oa9BXfhks6xBx8WubO5WdfLnh3a')
    ;

INSERT INTO cuenta (iban, saldo, fecha_creacion) values
    ('ES00 0000 2000 00 0000000000', 3000.0, '2020-02-24'),
    ('ES00 0000 3000 10 0000000001', 6000.0, '2020-03-24'),
    ('ES00 0000 3000 20 0000000002', 6000.0, '2020-03-24'),
    ('ES00 0000 3000 20 0000000003', 3514.54, '2020-01-20')
    ;

INSERT INTO categoria (nombre) values
    ('Ocio'),
    ('Bares y Restaurantes'),
    ('Deporte'),
    ('Viajes'),
    ('SuperMercados'),
    ('Otros')
    ;

INSERT INTO tarjeta (numero,cuenta_id) VALUES
    ("1234987656785432", 1),
	("1234987656785433", 1),
	("1234987656785434", 4),
	("1234987656785435", 4),
	("1234987656785436", 4),
	("1234987656785437", 4),
	("1234987656785438", 2)
	;

INSERT INTO movimiento (cantidad, tipo, concepto, saldo_actual, categoria_id, cuenta_id, tarjeta_id, fecha) values
    (35.04, 0, 'Clases', 3000+35.04 , 6, 1, null, '2021-05-09'),
    (20.2, 1, 'Raff Tapas', 3000+35.04-20.2 , 2, 1, 1, '2021-05-10'),
    (60.0, 1, 'Decathlon', 3000+35.04-20.2-60.0 , 3, 1, null, '2021-05-12'),
    (15.6, 1, 'Gasto Casa', 3514.54-15.6 , 4, 4, 4, '2021-05-12'),
    (1321.56, 0, 'Nomina', 4820.44, 6, 4, null, '2021-05-03'),
	(15.6, 1, 'Gasto Casa', 3498.94, 5, 4, 4, '2021-05-12'),
	(53.65, 1, 'Carrefour', 4766.79, 5, 4, 4, '2021-05-26'),
	(15.6, 1, 'Bar Manolo', 4751.19 , 2, 4, 4, '2021-05-30'),
	(35.04, 1, 'Cine', 6000-35.04 , 1, 2, 2, '2021-03-10'),
	(20.0, 0, 'Bizum', 6000-35.04+20.0 , 1, 2, null, '2021-03-10'),
	(33.5, 1, 'Mercadona', 3000+35.04-20.2-60.0-33.5 , 3, 1, 2, '2021-04-12')
    ;

-- actualizo saldo de las cuentas según los movimientos realizados
UPDATE cuenta SET saldo = 3000+35.04-20.2-60.0-33.5 WHERE id=1;
UPDATE cuenta SET saldo = 4751.19 WHERE id=4;
UPDATE cuenta SET saldo = 6000-35.04+20.0 WHERE id=2;


-- relaciones usuario_cuenta
INSERT INTO usuario_cuenta (usuario_id, cuenta_id) values
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 4),
    (3, 3),
    (4, 2),
    (5, 3)
    ;