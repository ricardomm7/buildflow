-- First create a product and operation
INSERT INTO Product_Family VALUES ('F1', 'Test Family');
INSERT INTO Product_Type VALUES ('PT1', 'Test Product Type');
INSERT INTO Product VALUES ('PT1', 'Test Product', 'F1');

-- Create an operation for this product
INSERT INTO Operation (Operation_ID, Designation, Product_ID, Expected_Time, Output_Part_ID)
VALUES (1, 'Test Operation', 'PT1', 10.0, 'PT1');

-- This should FAIL - trying to use PT1 as input for its own operation
INSERT INTO Operation_Input VALUES ('PT1', 1, 1.0);

-- This should SUCCEED - using a different part as input
INSERT INTO Product_Type VALUES ('PT2', 'Another Product Type');
INSERT INTO Operation_Input VALUES ('PT2', 1, 1.0);