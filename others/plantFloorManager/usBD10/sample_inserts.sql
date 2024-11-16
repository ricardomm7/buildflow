-- Insert into Product_Family
INSERT INTO Product_Family (Family_ID, Name) VALUES ('FAM001', 'Electronics');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('FAM002', 'Furniture');

-- Insert into Part
INSERT INTO Part (Part_ID) VALUES ('P001');
INSERT INTO Part (Part_ID) VALUES ('P002');
INSERT INTO Part (Part_ID) VALUES ('P003');
INSERT INTO Part (Part_ID) VALUES ('P004');
INSERT INTO Part (Part_ID) VALUES ('PROD001');
INSERT INTO Part (Part_ID) VALUES ('PROD002');
INSERT INTO Part (Part_ID) VALUES ('PROD003');

-- Insert into Product
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('PROD001', 'Basic Circuit', 'FAM001');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('PROD002', 'Table', 'FAM002');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('PROD003', 'Remote', 'FAM001');

-- Insert into Raw_Material
INSERT INTO Raw_Material (Part_ID) VALUES ('P001');
INSERT INTO Raw_Material (Part_ID) VALUES ('P002');

-- Insert into Component
INSERT INTO Component (Part_ID) VALUES ('P001');
INSERT INTO Component (Part_ID) VALUES ('P003');

-- Insert into Intermediate_Product
INSERT INTO Intermediate_Product (Part_ID) VALUES ('P001');

-- Insert into Costumer
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('123456789', 'John Doe', '123 Main St', '12345', 'Metropolis', 'Country A', 'johndoe@example.com', '1234567890');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('987654321', 'Jane Smith', '456 Elm St', '54321', 'Gotham', 'Country B', 'janesmith@example.com', '9876543210');

-- Insert into "Order"
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD001', TO_DATE('2024-01-01', 'YYYY-MM-DD'), TO_DATE('2024-01-10', 'YYYY-MM-DD'), '123456789');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD002', TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2024-02-15', 'YYYY-MM-DD'), '987654321');

-- Insert into Production_Line
INSERT INTO Production_Line (Product_ID, Order_ID, quantity) VALUES ('PROD001', 'ORD001', 10);
INSERT INTO Production_Line (Product_ID, Order_ID, quantity) VALUES ('PROD002', 'ORD002', 5);

-- Insert into Type_Workstation
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('WS001', 'Soldering Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('WS002', 'Assembly Line');

-- Insert into Workstation
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (1, 'Solder Station 1', 'Handles small soldering tasks', 'WS001');
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (2, 'Assembly Line 1', 'Used for furniture assembly', 'WS002');

-- Insert into Operation
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID)
VALUES (1, 'Assemble Circuit', NULL, 'PROD001');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID)
VALUES (2, 'Polish Table', NULL, 'PROD002');

-- Insert into Operation_Type_Workstation
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID)
VALUES (1, 'WS001');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID)
VALUES (2, 'WS002');

-- Insert into Operation_Input
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('P002', 1, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('P004', 2, 3);

-- Insert into Operation_Output
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity)
VALUES ('P001', 1, 10);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity)
VALUES ('P003', 2, 5);
