-- 2. Insert into Part
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
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945T22', '5l 22 cm aluminium and teflon non stick pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945S22', '5l 22 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12946S22', '5l 22 cm stainless steel pot bottom');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12947S22', '22 cm stainless steel lid');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945S20', '3l 20 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12946S20', '3l 22 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12947S20', '3l 25 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945S17', '2l 17 cm stainless steel pot');
INSERT INTO Part (Part_ID, Description) VALUES ('AS12945P17', '2l 17 cm stainless steel souce pan');
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
INSERT INTO Part (Part_ID, Description) VALUES ('PN94561L67', 'Coolube 2210XP');

-- 3. Insert into Raw_Material
INSERT INTO Raw_Material (Part_ID) VALUES ('PN94561L67');

-- 3. Insert into Component
INSERT INTO Component (Part_ID) VALUES ('PN12344A21');
INSERT INTO Component (Part_ID) VALUES ('PN52384R50');
INSERT INTO Component (Part_ID) VALUES ('PN52384R10');
INSERT INTO Component (Part_ID) VALUES ('PN18544C21');
INSERT INTO Component (Part_ID) VALUES ('PN18324C54');

-- 3. Insert into Intermediate_Product
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

-- 4. Insert into Product
INSERT INTO Product (Part_ID, Name) VALUES ('AS12945S22', '5l 22 cm stainless steel pot');
INSERT INTO Product (Part_ID, Name) VALUES ('AS12946S22', '5l 22 cm stainless steel pot bottom');
INSERT INTO Product (Part_ID, Name) VALUES ('AS12947S22', '22 cm stainless steel lid');
INSERT INTO Product (Part_ID, Name) VALUES ('AS12945S20', '3l 20 cm stainless steel pot');
INSERT INTO Product (Part_ID, Name) VALUES ('AS12946S20', '3l 20 cm stainless steel pot bottom');
INSERT INTO Product (Part_ID, Name) VALUES ('AS12947S20', '20 cm stainless steel lid');

-- 8. Insert into Type_Workstation
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('A4588', 'Cutting Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('C5637', 'Finishing Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('S3271', 'Riveting Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('T3452', 'Screwing Station');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation) VALUES ('K3675', 'Packaging Station');

-- 10. Insert into Operation (COMEÇAR PELA QUE TEM NULL)
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (115, 'Pot handles riveting', NULL, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (114, 'Pot base finishing', 115, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (112, 'Final pot base pressing', 114, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (103, 'Initial pot base pressing', 112, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID) VALUES (100, 'Disc cutting', 103, 'AS12946S22');

-- 11. Insert into Operation_Type_Workstation
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (100, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (103, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (112, 'A4588');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (114, 'C5637');
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID) VALUES (115, 'S3271');

-- 12. Insert into Operation_Input
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN52384R50', 100, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A01', 103, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity) VALUES ('PN94561L67', 103, 5);

-- 13. Insert into Operation_Output
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A01', 100, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A02', 103, 1);
INSERT INTO Operation_Output (Part_ID, Operation_ID, Quantity) VALUES ('IP12945A03', 112, 1);
