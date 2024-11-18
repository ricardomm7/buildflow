INSERT INTO Product_Family (Family_ID, Name) VALUES ('130', 'La Belle 22 5l pot');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('125', 'Pro 17 2l pot');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('145', 'Pro 17 lid');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('132', 'Pro 17 2l sauce pan');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('146', 'Pro Clear 17 lid');

-- 1. Insert into Part Table
INSERT INTO Part (Part_ID, Description) VALUES ('PN12344A21', 'Screw M6 35 mm');
INSERT INTO Part (Part_ID, Description) VALUES ('PN52384R50', '300x300 mm 5 mm stainless steel sheet');
INSERT INTO Part (Part_ID, Description) VALUES ('PN52384R10', '300x300 mm 1 mm stainless steel sheet');
INSERT INTO Part (Part_ID, Description) VALUES ('PN18544A21', 'Rivet 6 mm');
INSERT INTO Part (Part_ID, Description) VALUES ('PN18544C21', 'Stainless steel handle model U6');
INSERT INTO Part (Part_ID, Description) VALUES ('PN18324C54', 'Stainless steel handle model R12');
INSERT INTO Part (Part_ID, Description) VALUES ('PN52384R45', '250x250 mm 5mm stainless steel sheet');
INSERT INTO Part (Part_ID, Description) VALUES ('PN52384R12', '250x250 mm 1mm stainless steel sheet');
INSERT INTO Part (Part_ID, Description) VALUES ('PN18324C91', 'Stainless steel handle model S26');
INSERT INTO Part (Part_ID, Description) VALUES ('PN18324C51', 'Stainless steel handle model R11');
INSERT INTO Part (Part_ID, Description) VALUES ('PN94561L67', 'Coolube 2210XP');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945T22', '5l 22 cm aluminium and teflon non stick pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945S22', '5l 22 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12946S22', '5l 22 cm stainless steel pot bottom');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12947S22', '22 cm stainless steel lid');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945S20', '3l 20 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12946S20', '3l 20 cm stainless steel pot bottom');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12947S20', '3l 25 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945S17', '2l 17 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945P17', '2l 17 cm stainless steel sauce pan');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945S48', '17 cm stainless steel lid');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945G48', '17 cm glass lid');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12945A01', '250 mm 5 mm stainless steel disc');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12945A02', '220 mm pot base phase 1');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12945A03', '220 mm pot base phase 2');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12945A04', '220 mm pot base final');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12947A01', '250 mm 1 mm stainless steel disc');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12947A02', '220 mm lid pressed');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12947A03', '220 mm lid polished');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12947A04', '220 mm lid with handle');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12945A32', '200 mm pot base phase 1');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12945A33', '200 mm pot base phase 2');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12945A34', '200 mm pot base final');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12947A32', '200 mm lid pressed');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12947A33', '200 mm lid polished');
INSERT INTO Part (Part_ID, Description) VALUES ('IP12947A34', '200 mm lid with handle');
-- 2. Insert into Raw_Material
INSERT INTO Raw_Material (Part_ID) VALUES ('PN94561L67');

-- 3. Insert into Component
INSERT INTO Component (Part_ID) VALUES ('PN12344A21');
INSERT INTO Component (Part_ID) VALUES ('PN52384R50');
INSERT INTO Component (Part_ID) VALUES ('PN52384R10');
INSERT INTO Component (Part_ID) VALUES ('PN18544A21');
INSERT INTO Component (Part_ID) VALUES ('PN18544C21');
INSERT INTO Component (Part_ID) VALUES ('PN18324C54');
INSERT INTO Component (Part_ID) VALUES ('PN52384R45');
INSERT INTO Component (Part_ID) VALUES ('PN52384R12');
INSERT INTO Component (Part_ID) VALUES ('PN18324C91');
INSERT INTO Component (Part_ID) VALUES ('PN18324C51');


-- 4. Insert into Intermediate_Product
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A01');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A02');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A03');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A04');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A32');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A33');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A34');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A01');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A02');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A03');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A04');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A32');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A33');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A34');

-- 5. Insert into Product
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12945T22', 'La Belle 22 5l pot', '130');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12945S22', 'Pro 22 5l pot', '125');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12946S22', 'Pro 22 5l pot bottom', '125');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12947S22', 'Pro 22 lid', '145');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12945S20', 'Pro 20 3l pot', '125');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12946S20', 'Pro 20 3l pot bottom', '125');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12947S20', 'Pro 20 lid', '145');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12945S17', 'Pro 17 2l pot', '125');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12945P17', 'Pro 17 2l sauce pan', '132');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12945S48', 'Pro 17 lid', '145');
INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID) VALUES ('AS12945G48', 'Pro Clear 17 lid', '146');

-- 6. Insert into Type_Workstation
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('A4578', 'Cutting Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('A4588', 'Cutting Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('A4598', 'Base Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('C5637', 'Finishing Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('S3271', 'Riveting Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('T3452', 'Screwing Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('K3675', 'Packaging Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('D9123', 'Gluing Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('Q3547', 'Polishing Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES('Q5478', 'Painting Station');


-- 7. Insert into Workstation (if any)

-- 8. Insert into Operation
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (115, 'Pot handles riveting', NULL, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (124, 'Lid polishing', NULL, 'AS12947S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (130, 'Pot test and packaging', NULL, 'AS12945S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (154, 'Pot handles riveting', NULL, 'AS12946S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (164, 'Lid polishing', NULL, 'AS12947S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (170, 'Pot test and packaging', NULL, 'AS12945S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (114, 'Pot base finishing', 115, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (123, 'Lid handle screw', 124, 'AS12947S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (112, 'Final pot base pressing', 114, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (122, 'Lid finishing', 123, 'AS12947S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (103, 'Initial pot base pressing', 112, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (121, 'Lid pressing', 122, 'AS12947S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (100, 'Disc cutting', 103, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (120, 'Disc cutting', 121, 'AS12947S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (153, 'Pot base finishing', 154, 'AS12946S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (152, 'Final pot base pressing', 153, 'AS12946S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (151, 'Initial pot base pressing', 152, 'AS12946S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (150, 'Disc cutting', 151, 'AS12946S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (163, 'Lid handle screw', 164, 'AS12947S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (162, 'Lid finishing', 163, 'AS12947S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (161, 'Lid pressing', 162, 'AS12947S20');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (160, 'Disc cutting', 161, 'AS12947S20');


-- 9. Insert into Operation_Type_Workstation
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (100, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (103, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (112, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (114, 'C5637');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (115, 'S3271');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (120, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (121, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (122, 'C5637');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (123, 'T3452');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (124, 'Q3547');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (150, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (151, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (152, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (153, 'C5637');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (154, 'S3271');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (160, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (161, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (162, 'C5637');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (163, 'T3452');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (164, 'Q3547');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (170, 'K3675');


-- 10. Insert into Operation_Input
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN52384R50', 100, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A01', 103, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN94561L67', 103, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A02', 112, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN94561L67', 112, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A03', 114, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A04', 115, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN18544C21', 115, 2);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN52384R10', 120, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A01', 121, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN94561L67', 121, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A02', 122, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A03', 123, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN18324C54', 123, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A04', 124, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN52384R50', 150, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A01', 151, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN94561L67', 151, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A32', 152, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN94561L67', 152, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A33', 153, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A34', 154, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN18544C21', 154, 2);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN52384R10', 160, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A01', 161, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN94561L67', 161, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A32', 162, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A33', 163, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN18324C51', 163, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A34', 164, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('AS12946S22', 130, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('AS12947S22', 130, 1);

-- 11. Insert into Operation_Output
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A01', 100, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A02', 103, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A03', 112, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A04', 114, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('AS12946S22', 115, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A01', 120, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A02', 121, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A03', 122, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A04', 123, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('AS12947S22', 124, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A01', 150, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A32', 151, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A33', 152, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A34', 153, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('AS12946S20', 154, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A01', 160, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A32', 161, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A33', 162, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12947A34', 163, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('AS12947S20', 164, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('AS12945S22', 130, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('AS12945S20', 170, 1);