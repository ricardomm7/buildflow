-- Inserts para Costumer
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('123456789', 'John Doe', '123 Elm St', '12345', 'Springfield', 'USA', 'john.doe@example.com', '5551234567');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('987654321', 'Jane Smith', '456 Oak St', '54321', 'Shelbyville', 'USA', 'jane.smith@example.com', '5557654321');

-- Inserts para Product_Type
INSERT INTO Product_Type (Part_ID, Description) VALUES ('COMP0001', 'Component 1');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('COMP0002', 'Component 2');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('EPART001', 'Raw 1');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('EPART002', 'Raw 2');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IPROD001', 'Intermediate 1');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IPROD002', 'Intermediate 2');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PROD01', 'Basic Circuit');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PROD02', 'Table');

-- Product family
INSERT INTO Product_Family (Family_ID, Name) VALUES ('FAM001', 'Electronics');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('FAM002', 'Furniture');

-- Insert into Product
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('PROD01', 'Basic Circuit', 'FAM001');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('PROD02', 'Table', 'FAM002');

-- Inserts para External_Part
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('COMP0001', 50);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('COMP0002', 30);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('EPART001', 50);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('EPART002', 30);

-- Inserts para Component
INSERT INTO Component (Part_ID) VALUES ('COMP0001');
INSERT INTO Component (Part_ID) VALUES ('COMP0002');

-- Inserts para Raw_Material
INSERT INTO Raw_Material (Part_ID) VALUES ('EPART001');
INSERT INTO Raw_Material (Part_ID) VALUES ('EPART002');

-- Inserts para Intermediate_Product
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IPROD001');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IPROD002');

-- Inserts para Operation
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (1, 'Cutting', NULL, 'PROD01', 2.5, 'IPROD001');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (2, 'Assembly', 1, 'PROD01', 1.5, 'IPROD002');

-- Inserts para Operation_Input
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('EPART001', 1, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('EPART002', 2, 3);

-- Inserts para Type_Workstation
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('WST01', 'Cutting Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('WST02', 'Assembly Station');

-- Inserts para Workstation
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (1, 'Workstation 1', 'Cutting tasks', 'WST01');
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (2, 'Workstation 2', 'Assembly tasks', 'WST02');

-- Inserts para Operation_Type_Workstation
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (1, 'WST01', 3.0, 0.5);
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (2, 'WST02', 2.0, 0.5);

-- Inserts para "Order"
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD001', TO_DATE('2023-12-01', 'YYYY-MM-DD'), TO_DATE('2023-12-15', 'YYYY-MM-DD'), '123456789');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD002', TO_DATE('2023-12-05', 'YYYY-MM-DD'), TO_DATE('2023-12-20', 'YYYY-MM-DD'), '987654321');

-- Inserts para Supplier
INSERT INTO Supplier (ID, Name, Email, Phone) VALUES (18349732, 'SuppB','supplier.a@example.com', '5551112222');
INSERT INTO Supplier (ID, Name, Email, Phone) VALUES (18143562, 'SuppA','supplier.b@example.com', '5553334444');

-- Inserts para Procurement
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (18349732, 'EPART001', 15.5, 100, TO_DATE('2023-11-01', 'YYYY-MM-DD'), TO_DATE('2023-12-31', 'YYYY-MM-DD'));
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (18143562, 'EPART002', 12.0, 50, TO_DATE('2023-11-15', 'YYYY-MM-DD'), TO_DATE('2023-12-31', 'YYYY-MM-DD'));

-- Inserts para Production_Line
INSERT INTO Production_Line (Product_ID, Order_ID, quantity) VALUES ('PROD01', 'ORD001', 10);
INSERT INTO Production_Line (Product_ID, Order_ID, quantity) VALUES ('PROD02', 'ORD002', 20);