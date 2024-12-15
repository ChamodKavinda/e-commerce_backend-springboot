
SET @userId1 = 8;

SET @userId2 = 9;

DELETE FROM web_order_quantities;
DELETE FROM web_order;
DELETE FROM inventory;
DELETE FROM product;
DELETE FROM address;

INSERT INTO product (name, short_description, long_description, price)
VALUES
('Product #1', 'Product one short description.', 'This is a very long description of product #1.', 5.50),
('Product #2', 'Product two short description.', 'This is a very long description of product #2.', 10.56),
('Product #3', 'Product three short description.', 'This is a very long description of product #3.', 2.74),
('Product #4', 'Product four short description.', 'This is a very long description of product #4.', 15.69),
('Product #5', 'Product five short description.', 'This is a very long description of product #5.', 42.59);

-- Retrieve product IDs
SET @product1 = (SELECT id FROM product WHERE name = 'Product #1' LIMIT 1);
SET @product2 = (SELECT id FROM product WHERE name = 'Product #2' LIMIT 1);
SET @product3 = (SELECT id FROM product WHERE name = 'Product #3' LIMIT 1);
SET @product4 = (SELECT id FROM product WHERE name = 'Product #4' LIMIT 1);
SET @product5 = (SELECT id FROM product WHERE name = 'Product #5' LIMIT 1);

INSERT INTO inventory (product_id, quantity)
VALUES
(@product1, 5), (@product2, 8), (@product3, 12), (@product4, 73), (@product5, 2);

INSERT INTO address (address_line_1, city, country, user_id)
VALUES
('123 Tester Hill', 'Testerton', 'England', @userId1),
('312 Spring Boot', 'Hibernate', 'England', @userId2);

-- Retrieve address IDs
SET @address1 = (SELECT id FROM address WHERE user_id = @userId1 ORDER BY id DESC LIMIT 1);
SET @address2 = (SELECT id FROM address WHERE user_id = @userId2 ORDER BY id DESC LIMIT 1);

INSERT INTO web_order (address_id, user_id)
VALUES
(@address1, @userId1), (@address1, @userId1), (@address1, @userId1),
(@address2, @userId2), (@address2, @userId2);

-- Retrieve order IDs
SET @order1 = (SELECT id FROM web_order WHERE address_id = @address1 AND user_id = @userId1 ORDER BY id DESC LIMIT 1 OFFSET 0);
SET @order2 = (SELECT id FROM web_order WHERE address_id = @address1 AND user_id = @userId1 ORDER BY id DESC LIMIT 1 OFFSET 1);
SET @order3 = (SELECT id FROM web_order WHERE address_id = @address1 AND user_id = @userId1 ORDER BY id DESC LIMIT 1 OFFSET 2);
SET @order4 = (SELECT id FROM web_order WHERE address_id = @address2 AND user_id = @userId2 ORDER BY id DESC LIMIT 1 OFFSET 0);
SET @order5 = (SELECT id FROM web_order WHERE address_id = @address2 AND user_id = @userId2 ORDER BY id DESC LIMIT 1 OFFSET 1);

-- Insert into web_order_quantities
INSERT INTO web_order_quantities (order_id, product_id, quantity)
VALUES
(@order1, @product1, 5), (@order1, @product2, 5),
(@order2, @product3, 5), (@order2, @product2, 5), (@order2, @product5, 5),
(@order3, @product3, 5), (@order4, @product4, 5), (@order4, @product2, 5),
(@order5, @product3, 5), (@order5, @product1, 5);
