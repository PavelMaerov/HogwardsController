-- у каждого человека есть машина.
-- Причем несколько человек могут пользоваться одной машиной.
-- У каждого человека есть имя, возраст и признак того, что у него есть права (или их нет).
-- У каждой машины есть марка, модель и стоимость.

CREATE TABLE car (
    id serial PRIMARY KEY,
    brand VARCHAR(30) NOT NULL,
    model VARCHAR(30) NOT NULL,
    price MONEY
);

DROP TABLE driver;
CREATE TABLE driver (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    age INT CHECK (age>=14),
    has_license BOOLEAN NOT NULL DEFAULT(FALSE),
    car_id INT REFERENCES car(id)
);

INSERT INTO car (brand, model, price) VALUES ('aaa','bbb', 1000),('aaaa','bbbb', NULL);
INSERT INTO driver (name, age, has_license, car_id)
VALUES  ('aaa', 111, TRUE, (SELECT MIN(id) FROM car)),
        ('bbb', 222, TRUE, (SELECT MAX(id) FROM car)),
        ('ccc', 333, TRUE, (SELECT MAX(id) FROM car)),
        ('ddd', 444, FALSE, NULL);