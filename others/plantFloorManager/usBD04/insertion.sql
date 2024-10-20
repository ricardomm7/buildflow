-- Inserir dados na tabela Product_Family
INSERT INTO Product_Family (Family_ID, Name) VALUES ('F001', 'Electronics');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('F002', 'Furniture');

-- Inserir dados na tabela Product (referenciando Product_Family já existente)
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID)
VALUES ('P001', 'Laptop', 'High performance laptop', 'F001');
INSERT INTO Product (Product_ID, Name, Description, Product_FamilyFamily_ID)
VALUES ('P002', 'Chair', 'Ergonomic office chair', 'F002');

-- Inserir dados na tabela Costumer
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country)
VALUES ('123456789', 'John Doe', '123 Street Name', '12345', 'CityA', 'CountryA');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country)
VALUES ('987654321', 'Jane Smith', '456 Another St', '67890', 'CityB', 'CountryB');

-- Inserir dados na tabela "Order" (referenciando Costumer já existente)
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD001', TO_DATE('2024-10-10', 'YYYY-MM-DD'), TO_DATE('2024-11-01', 'YYYY-MM-DD'), '123456789');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD002', TO_DATE('2024-09-15', 'YYYY-MM-DD'), TO_DATE('2024-10-25', 'YYYY-MM-DD'), '987654321');

-- Inserir múltiplos dados na tabela Production_Order de uma só vez
INSERT ALL
  INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('P001', 'ORD001', 2)  -- 2 laptops para a ordem ORD001
  INTO Production_Order (ProductProduct_ID, OrderOrder_ID, quantity) VALUES ('P002', 'ORD002', 1)  -- 1 cadeira para a ordem ORD002
SELECT 1 FROM DUAL;

-- Inserir dados na tabela BOO (referenciando Product_Family já existente)
INSERT INTO BOO (Product_FamilyFamily_ID, Operation_Sequence) VALUES ('F001', 1);
INSERT INTO BOO (Product_FamilyFamily_ID, Operation_Sequence) VALUES ('F002', 1);

-- Inserir dados na tabela Part
INSERT INTO Part (Part_ID, Descriprion) VALUES ('PA001', 'Motherboard');
INSERT INTO Part (Part_ID, Descriprion) VALUES ('PA002', 'Screws');

-- Inserir dados na tabela Product_Part (referenciando Product e Part já existentes)
INSERT ALL
    INTO Product_Part (ProductProduct_ID, PartPart_ID, Quantity) VALUES ('P001', 'PA001', 1) -- Laptop inclui 1 motherboard
	INTO Product_Part (ProductProduct_ID, PartPart_ID, Quantity) VALUES ('P002', 'PA002', 4) -- Chair inclui 4 screws
SELECT 1 FROM DUAL;

-- Inserir dados na tabela Operation (referenciando BOO já existente)
INSERT INTO Operation (Designation, BOOProduct_FamilyFamily_ID, BOOOperation_Sequence)
VALUES ('Assembly', 'F001', 1);
INSERT INTO Operation (Designation, BOOProduct_FamilyFamily_ID, BOOOperation_Sequence)
VALUES ('Packaging', 'F002', 1);

-- Inserir dados na tabela Type_Workstation (referenciando Operation já existente)
INSERT ALL
    INTO Type_Workstation (WorkstationType_ID, Designation, OperationOperation_ID) VALUES ('WS001', 'Assembly Station', 1)  -- Referencia a Operation ID 1 (Assembly)
	INTO Type_Workstation (WorkstationType_ID, Designation, OperationOperation_ID) VALUES ('WS002', 'Packaging Station', 2)  -- Referencia a Operation ID 2 (Packaging)
SELECT 1 FROM DUAL;

-- Inserir dados na tabela Workstation (referenciando Type_Workstation já existente)
INSERT INTO Workstation (Name, Description, Type_WorkstationWorkstationType_ID)
VALUES ('Assembly Workstation 1', 'Used for assembling laptops', 'WS001');
INSERT INTO Workstation (Name, Description, Type_WorkstationWorkstationType_ID)
VALUES ('Packaging Workstation 1', 'Used for packaging furniture', 'WS002');