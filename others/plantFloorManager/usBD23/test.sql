-- Product Families
INSERT INTO Product_Family (Family_ID, Name) VALUES ('FAM001', 'Electronics');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('FAM002', 'Furniture');

-- Product Types
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PROD001', 'unknown5');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PROD002', 'unknown6');

-- Products
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('PROD001', 'Basic Circuit', 'FAM001');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('PROD002', 'Table', 'FAM002');

-- Type Workstation
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('WS1', 'Assembly');

-- Workstation
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (1001, 'Workstation 1', 'High precision assembly workstation', 'WS1');

-- Operation Type
INSERT INTO Operation_Type (ID, Description, Expec_Time)
VALUES (324, 'Base Press', 10.5);

-- Operation
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (1, 324, NULL, 'PROD001', 'PROD002');

-- Operation-Type Workstation Relationship
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (324, 'WS1', 8.0, 1.5);
