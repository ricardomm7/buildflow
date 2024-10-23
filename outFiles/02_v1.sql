INSERT INTO Product_Family (Family_ID, Name) VALUES ('125', 'Pro Line pots');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('130', 'La Belle pots');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('132', 'Pro Line pans');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('145', 'Pro Line lids');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('146', 'Pro Clear lids');

INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('AS12945T22', 'La Belle 22 5l pot', '5l 22 cm aluminium and teflon non stick pot', '130');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('AS12945S22', 'Pro 22 5l pot', '5l 22 cm stainless steel pot', '125');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('AS12945S20', 'Pro 20 3l pot', '3l 20 cm stainless steel pot', '125');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('AS12945S17', 'Pro 17 2l pot', '2l 17 cm stainless steel pot', '125');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('AS12945P17', 'Pro 17 2l sauce pan', '2l 17 cm stainless steel souce pan', '132');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('AS12945S48', 'Pro 17 lid', '17 cm stainless steel lid', '145');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID) VALUES ('AS12945G48', 'Pro Clear 17 lid', '17 cm glass lid', '146');

INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone) VALUES ('PT501245987', 'Carvalho & Carvalho, Lda', 'Tv. Augusto Lessa 23', '4200-047', 'Porto', 'Portugal', 'idont@care.com', '003518340500');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone) VALUES ('PT501245488', 'Tudo para a casa, Lda', 'R. Dr. Barros 93', '4465-219', 'São Mamede de Infesta', 'Portugal', 'me@neither.com', '003518340500');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone) VALUES ('PT501242417', 'Sair de Cena', 'EDIFICIO CRISTAL lj18, R. António Correia de Carvalho 88', '4400-023', 'Vila Nova de Gaia', 'Portugal', 'some@email.com', '003518340500');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone) VALUES ('CZ6451237810', 'U Fleku', 'Křemencova 11', '110 00', 'Nové Město', 'Czechia', 'some.random@email.cz', '004201234567');

INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('1', TO_DATE('2024-09-15', 'YYYY-MM-DD'), TO_DATE('2024-09-23', 'YYYY-MM-DD'), 'PT501245488');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('1', TO_DATE('2024-09-15', 'YYYY-MM-DD'), TO_DATE('2024-09-23', 'YYYY-MM-DD'), 'PT501245488');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('2', TO_DATE('2024-09-15', 'YYYY-MM-DD'), TO_DATE('2024-09-25', 'YYYY-MM-DD'), 'PT501242417');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('3', TO_DATE('2024-09-15', 'YYYY-MM-DD'), TO_DATE('2024-09-25', 'YYYY-MM-DD'), 'CZ6451237810');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('3', TO_DATE('2024-09-15', 'YYYY-MM-DD'), TO_DATE('2024-09-25', 'YYYY-MM-DD'), 'CZ6451237810');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('4', TO_DATE('2024-09-18', 'YYYY-MM-DD'), TO_DATE('2024-09-25', 'YYYY-MM-DD'), 'PT501245488');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('5', TO_DATE('2024-09-18', 'YYYY-MM-DD'), TO_DATE('2024-09-25', 'YYYY-MM-DD'), 'PT501242417');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('6', TO_DATE('2024-09-18', 'YYYY-MM-DD'), TO_DATE('2024-09-26', 'YYYY-MM-DD'), 'CZ6451237810');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT) VALUES ('7', TO_DATE('2024-09-21', 'YYYY-MM-DD'), TO_DATE('2024-09-26', 'YYYY-MM-DD'), 'PT501245987');

INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '1', 5);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S20', '1', 15);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '2', 10);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '3', 10);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S20', '3', 10);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '4', 4);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '5', 12);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S17', '6', 8);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '7', 7);

INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '1', 5);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S20', '1', 15);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '2', 10);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '3', 10);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S20', '3', 10);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '4', 4);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '5', 12);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S17', '6', 8);
INSERT INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('AS12945S22', '7', 7);
