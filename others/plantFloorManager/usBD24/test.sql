-- Criar a família de produtos
INSERT INTO Product_Family (Family_ID, Name) VALUES ('F1', 'Test Family');

-- Criar os tipos de produtos
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PT1', 'Test Product Type');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PT2', 'Another Product Type');

-- Criar o produto
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID)
VALUES ('PT1', 'Test Product', 'F1');

-- Operation Type
INSERT INTO Operation_Type (ID, Description, Expec_Time)
VALUES (1, 'Base Press', 10.5);

-- Criar a operação para o produto
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (1, 1, NULL, 'PT1', 'PT1');

-- Adicionar PT1 como entrada para sua própria operação
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PT1', 1, 2.0);

-- Adicionar uma outra entrada para a operação (PT2)
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PT2', 1, 1.0);
