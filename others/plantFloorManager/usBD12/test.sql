INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5647, 'Construct', 500);

-- Insert Product Families
INSERT INTO Product_Family (Family_ID, Name) VALUES ('F001', 'Kitchenware');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('F002', 'Utensils');

-- Insert Product Types
INSERT INTO Product_Type (Part_ID, Description) VALUES ('P001', 'Descrição do Produto A');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('RM001', 'raw1');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('RM002', 'raw2');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('C001', 'comp1');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('C002', 'comp2');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('RM003', 'raw3');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('C003', 'comp3');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('P002', 'Descrição do Produto B');


INSERT INTO External_Part (Part_ID, Stock, Minimum_Stock) VALUES ('RM001', 100, 50);
INSERT INTO External_Part (Part_ID, Stock, Minimum_Stock) VALUES ('RM002', 100, 50);
INSERT INTO External_Part (Part_ID, Stock, Minimum_Stock) VALUES ('RM003', 100, 50);
INSERT INTO External_Part (Part_ID, Stock, Minimum_Stock) VALUES ('C001', 100, 50);
INSERT INTO External_Part (Part_ID, Stock, Minimum_Stock) VALUES ('C002', 100, 50);
INSERT INTO External_Part (Part_ID, Stock, Minimum_Stock) VALUES ('C003', 100, 50);

-- Insert Raw Materials
INSERT INTO Raw_Material (Part_ID) VALUES ('RM001');
INSERT INTO Raw_Material (Part_ID) VALUES ('RM002');
INSERT INTO Raw_Material (Part_ID) VALUES ('RM003');

-- Insert Components
INSERT INTO Component (Part_ID) VALUES ('C001');
INSERT INTO Component (Part_ID) VALUES ('C002');
INSERT INTO Component (Part_ID) VALUES ('C003');

-- Insert Products
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('P001', 'Produto A', 'F001');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('P002', 'Produto B', 'F002');

-- Insert Operations and Operation Inputs for Produto A (P001)
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (1, 5647, NULL, 'P001', 'P001');

-- Operation Inputs for P001
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('C001', 1, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('RM001', 1, 2);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('RM002', 1, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('P002', 1, 1);

-- Insert Operations and Operation Inputs for Produto B (P002)
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (2, 5647, NULL, 'P002', 'P002');

-- Operation Inputs for P002
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('C003', 2, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('RM003', 2, 2);
