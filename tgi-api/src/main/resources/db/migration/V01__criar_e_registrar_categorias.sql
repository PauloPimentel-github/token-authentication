CREATE TABLE categories 
(
	category_id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	category_name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categories (category_name) VALUES ('Lazer');
INSERT INTO categories (category_name) VALUES ('Alimentação');
INSERT INTO categories (category_name) VALUES ('Supermercado');
INSERT INTO categories (category_name) VALUES ('Farmácia');
INSERT INTO categories (category_name) VALUES ('Outros');