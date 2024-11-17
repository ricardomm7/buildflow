-- Insert into Product_Family
insert into Product_Family (Family_ID, Name) values ('FAM001', 'Electronics');
insert into Product_Family (Family_ID, Name) values ('FAM002', 'Furniture');

-- Insert into Part
insert into Part (Part_ID, Description) values ('P001', 'unknown1');
insert into Part (Part_ID, Description) values ('P002', 'unknown2');
insert into Part (Part_ID, Description) values ('P003', 'unknown3');
insert into Part (Part_ID, Description) values ('P004', 'unknown4');
insert into Part (Part_ID, Description) values ('PROD001', 'unknown5');
insert into Part (Part_ID, Description) values ('PROD002', 'unknown6');
insert into Part (Part_ID, Description) values ('PROD003', 'unknown7');

-- Insert into Product
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('PROD001', 'Basic Circuit', 'FAM001');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('PROD002', 'Table', 'FAM002');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('PROD003', 'Remote', 'FAM001');

-- Insert into Raw_Material
insert into Raw_Material (Part_ID) values ('P001');
insert into Raw_Material (Part_ID) values ('P002');

-- Insert into Component
insert into Component (Part_ID) values ('P001');
insert into Component (Part_ID) values ('P003');

-- Insert into Intermediate_Product
insert into Intermediate_Product (Part_ID) values ('P001');

-- Insert into Costumer
insert into Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
values ('123456789', 'John Doe', '123 Main St', '12345', 'Metropolis', 'Country A', 'johndoe@example.com', '1234567890');
insert into Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
values ('987654321', 'Jane Smith', '456 Elm St', '54321', 'Gotham', 'Country B', 'janesmith@example.com', '9876543210');

-- Insert into "Order"
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD001', to_date('2024-01-01', 'YYYY-MM-DD'), to_date('2024-01-10', 'YYYY-MM-DD'), '123456789');
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD002', to_date('2024-02-01', 'YYYY-MM-DD'), to_date('2024-02-15', 'YYYY-MM-DD'), '987654321');

-- Insert into Production_Line
insert into Production_Line (Product_ID, Order_ID, quantity) values ('PROD001', 'ORD001', 10);
insert into Production_Line (Product_ID, Order_ID, quantity) values ('PROD002', 'ORD002', 5);

-- Insert into Type_Workstation
insert into Type_Workstation (WorkstationType_ID, Designation) values ('WS001', 'a');
insert into Type_Workstation (WorkstationType_ID, Designation) values ('WS002', 'v');

-- Insert into Workstation
insert into Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
values (1, 'Solder Station 1', 'Handles small soldering tasks', 'WS001');
insert into Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
values (2, 'Assembly Line 1', 'Used for furniture assembly', 'WS002');

-- Insert into Operation
insert into Operation (Operation_ID, Designation, NextOperation_ID, Product_ID)
values (1, 'Assemble Circuit', null, 'PROD001');
insert into Operation (Operation_ID, Designation, NextOperation_ID, Product_ID)
values (2, 'Polish Table', null, 'PROD002');

-- Insert into Operation_Type_Workstation
insert into Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID)
values (1, 'WS001');
insert into Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID)
values (2, 'WS002');

-- Insert into Operation_Input
insert into Operation_Input (Part_ID, Operation_ID, Quantity)
values ('P002', 1, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity)
values ('PROD002', 1, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity)
values ('P004', 2, 3);

-- Insert into Operation_Output
insert into Operation_Output (Part_ID, Operation_ID, Quantity)
values ('P001', 1, 10);
insert into Operation_Output (Part_ID, Operation_ID, Quantity)
values ('P003', 2, 5);
