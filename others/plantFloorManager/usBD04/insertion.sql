-- Inserir dados na tabela Type_Product
INSERT INTO Type_Product (Designation) VALUES ('Electronics');
INSERT INTO Type_Product (Designation) VALUES ('Furniture');
INSERT INTO Type_Product (Designation) VALUES ('Automobile');

-- Inserir dados na tabela Product_Family
INSERT INTO Product_Family (Family_ID) VALUES ('Family_E1');
INSERT INTO Product_Family (Family_ID) VALUES ('Family_F1');
INSERT INTO Product_Family (Family_ID) VALUES ('Family_A1');

-- Inserir dados na tabela Product
INSERT INTO Product (Product_ID, Name, Type_ProductDesignation, Product_FamilyFamily_ID) VALUES ('P001', 'Smartphone', 'Electronics', 'Family_E1');
INSERT INTO Product (Product_ID, Name, Type_ProductDesignation, Product_FamilyFamily_ID) VALUES ('P002', 'Laptop', 'Electronics', 'Family_E1');
INSERT INTO Product (Product_ID, Name, Type_ProductDesignation, Product_FamilyFamily_ID) VALUES ('P003', 'Chair', 'Furniture', 'Family_F1');
INSERT INTO Product (Product_ID, Name, Type_ProductDesignation, Product_FamilyFamily_ID) VALUES ('P004', 'Car', 'Automobile', 'Family_A1');

-- Inserir dados na tabela BOM
INSERT INTO BOM (ProductProduct_ID, ProductType_ProductDesignation, ProductProduct_FamilyFamily_ID) VALUES ('P001', 'Electronics', 'Family_E1');
INSERT INTO BOM (ProductProduct_ID, ProductType_ProductDesignation, ProductProduct_FamilyFamily_ID) VALUES ('P002', 'Electronics', 'Family_E1');
INSERT INTO BOM (ProductProduct_ID, ProductType_ProductDesignation, ProductProduct_FamilyFamily_ID) VALUES ('P003', 'Furniture', 'Family_F1');

-- Inserir dados na tabela Component
INSERT INTO Component (Component_ID, Name, Quantity, BOMProductProduct_ID) VALUES ('C001', 'Screen', 2, 'P001');
INSERT INTO Component (Component_ID, Name, Quantity, BOMProductProduct_ID) VALUES ('C002', 'Battery', 1, 'P001');
INSERT INTO Component (Component_ID, Name, Quantity, BOMProductProduct_ID) VALUES ('C003', 'Keyboard', 1, 'P002');
INSERT INTO Component (Component_ID, Name, Quantity, BOMProductProduct_ID) VALUES ('C004', 'Leg', 4, 'P003');

-- Inserir dados na tabela Raw_Materials
INSERT INTO Raw_Materials (Name, Quantity, BOMProductProduct_ID) VALUES ('Plastic', 10, 'P001');
INSERT INTO Raw_Materials (Name, Quantity, BOMProductProduct_ID) VALUES ('Metal', 5, 'P003');

-- Inserir dados na tabela Costumer
INSERT INTO Costumer (Name, Address, Contact) VALUES ('John Doe', '123 Main St', 912345678);
INSERT INTO Costumer (Name, Address, Contact) VALUES ('Jane Smith', '456 Oak St', 923456789);

-- Inserir dados na tabela "Order"
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerNIF) VALUES ('O001', TO_DATE('2024-10-01', 'YYYY-MM-DD'), TO_DATE('2024-10-15', 'YYYY-MM-DD'), 1);
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerNIF) VALUES ('O002', TO_DATE('2024-10-02', 'YYYY-MM-DD'), TO_DATE('2024-10-18', 'YYYY-MM-DD'), 2);

-- Inserir dados na tabela Production_Order
INSERT INTO Production_Order (quantity, ProductProduct_ID, ProductType_ProductDesignation, ProductProduct_FamilyFamily_ID, OrderOrder_ID) VALUES (100, 'P001', 'Electronics', 'Family_E1', 'O001');
INSERT INTO Production_Order (quantity, ProductProduct_ID, ProductType_ProductDesignation, ProductProduct_FamilyFamily_ID, OrderOrder_ID) VALUES (50, 'P003', 'Furniture', 'Family_F1', 'O002');

-- Inserir dados na tabela BOO
INSERT INTO BOO (Production_OrderProductProduct_ID, Production_OrderProductType_ProductDesignation, Production_OrderProductProduct_FamilyFamily_ID, Production_OrderOrderOrder_ID) VALUES ('P001', 'Electronics', 'Family_E1', 'O001');
INSERT INTO BOO (Production_OrderProductProduct_ID, Production_OrderProductType_ProductDesignation, Production_OrderProductProduct_FamilyFamily_ID, Production_OrderOrderOrder_ID) VALUES ('P003', 'Furniture', 'Family_F1', 'O002');

-- Inserir dados na tabela Type_Operation
INSERT INTO Type_Operation (Designation) VALUES ('Assembly');
INSERT INTO Type_Operation (Designation) VALUES ('Testing');
INSERT INTO Type_Operation (Designation) VALUES ('Packaging');

-- Inserir dados na tabela Operation
INSERT INTO Operation (BOOProduction_OrderProductProduct_ID, Type_OperationDesignation) VALUES ('P001', 'Assembly');
INSERT INTO Operation (BOOProduction_OrderProductProduct_ID, Type_OperationDesignation) VALUES ('P003', 'Testing');

-- Inserir dados na tabela Type_Workstation
INSERT INTO Type_Workstation (Designation) VALUES ('Station_A');
INSERT INTO Type_Workstation (Designation) VALUES ('Station_B');

-- Inserir dados na tabela Workstation
INSERT INTO Workstation (Workstation_ID, ExecutionTime, OperationType_OperationDesignation, Type_WorkstationDesignation) VALUES ('W001', 60, 'Assembly', 'Station_A');
INSERT INTO Workstation (Workstation_ID, ExecutionTime, OperationType_OperationDesignation, Type_WorkstationDesignation) VALUES ('W002', 120, 'Testing', 'Station_B');
