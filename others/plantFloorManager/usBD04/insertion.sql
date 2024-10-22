INSERT INTO Product_Family (Family_ID, Name) VALUES ('F001', 'Electronics');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('F002', 'Furniture');

-- Inserir dados na tabela Product
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID)
VALUES ('P001', 'Laptop', 'High performance laptop', 'F001');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID)
VALUES ('P002', 'Chair', 'Ergonomic office chair', 'F002');

-- Inserir dados na tabela Costumer
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('123456789', 'John Doe', '123 Street Name', '12345', 'CityA', 'CountryA', 'john@example.com', '1234567890');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('987654321', 'Jane Smith', '456 Another St', '67890', 'CityB', 'CountryB', 'jane@example.com', '0987654321');

-- Inserir dados na tabela "Order"
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD001', TO_DATE('2024-10-10', 'YYYY-MM-DD'), TO_DATE('2024-11-01', 'YYYY-MM-DD'), '123456789');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD002', TO_DATE('2024-09-15', 'YYYY-MM-DD'), TO_DATE('2024-10-25', 'YYYY-MM-DD'), '987654321');

-- Inserir dados na tabela Production_Order
INSERT ALL
  INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('P001', 'ORD001', 2)
  INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('P002', 'ORD002', 1)
SELECT 1 FROM DUAL;

-- Inserir dados na tabela BOO
INSERT INTO BOO (Product_FamilyFamily_ID, Operation_Sequence) VALUES ('F001', 0);
INSERT INTO BOO (Product_FamilyFamily_ID, Operation_Sequence) VALUES ('F002', 0);

-- Inserir dados na tabela Part
INSERT INTO Part (Part_ID, Description) VALUES ('PA001', 'Motherboard');
INSERT INTO Part (Part_ID, Description) VALUES ('PA002', 'Screws');

-- Inserir dados na tabela Product_Part
INSERT ALL
    INTO Product_Part (ProductProduct_ID, PartPart_ID, Quantity) VALUES ('P001', 'PA001', 1)
    INTO Product_Part (ProductProduct_ID, PartPart_ID, Quantity) VALUES ('P002', 'PA002', 4)
SELECT 1 FROM DUAL;

-- Inserir dados na tabela Type_Workstation (antes de Workstation)
INSERT ALL
    INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('WS001', 'Assembly Station')
    INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('WS002', 'Packaging Station')
SELECT 1 FROM DUAL;

-- Inserir dados na tabela Operation (referenciando Type_Workstation)
INSERT INTO Operation (Operation_ID, Designation, BOOProduct_FamilyFamily_ID, BOOOperation_Sequence, Type_WorkstationWorkstationType_ID)
VALUES (1, 'Assembly', 'F001', 1, 'WS001');
INSERT INTO Operation (Operation_ID, Designation, BOOProduct_FamilyFamily_ID, BOOOperation_Sequence, Type_WorkstationWorkstationType_ID)
VALUES (2, 'Packaging', 'F002', 2, 'WS002');

-- Agora, inserir dados na tabela Workstation
INSERT INTO Workstation (Workstation_ID, Name, Description, Type_WorkstationWorkstationType_ID)
VALUES (1, 'Assembly Workstation 1', 'Used for assembling laptops', 'WS001');

INSERT INTO Workstation (Workstation_ID, Name, Description, Type_WorkstationWorkstationType_ID)
VALUES (2, 'Packaging Workstation 1', 'Used for packaging furniture', 'WS002');
