INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('PT501245987', 'Carvalho & Carvalho, Lda', 'Tv. Augusto Lessa 23', '4200-047', 'Porto', 'Portugal', 'idont@care.com', '003518340500');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('PT501245488', 'Tudo para a casa, Lda', 'R. Dr. Barros 93', '4465-219', 'São Mamede de Infesta', 'Portugal', 'me@neither.com', '003518340500');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('PT501242417', 'Sair de Cena', 'EDIFICIO CRISTAL lj18, R. António Correia de Carvalho 88', '4400-023', 'Vila Nova de Gaia', 'Portugal', 'some@email.com', '003518340500');
INSERT INTO Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
VALUES ('CZ6451237810', 'U Fleku', 'Křemencova 11', '110 00', 'Nové Město', 'Czechia', 'some.random@email.cz', '004201234567');


INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN12344A21', 'Screw M6 35 mm');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN52384R50', '300x300 mm 5 mm stainless steel sheet');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN52384R10', '300x300 mm 1 mm stainless steel sheet');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN18544A21', 'Rivet 6 mm');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN18544C21', 'Stainless steel handle model U6');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN18324C54', 'Stainless steel handle model R12');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN52384R45', '250x250 mm 5mm stainless steel sheet');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN52384R12', '250x250 mm 1mm stainless steel sheet');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN18324C91', 'Stainless steel handle model S26');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN18324C51', 'Stainless steel handle model R11');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12945T22', '5l 22 cm aluminium and teflon non stick pot');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12945S22', '5l 22 cm stainless steel pot');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12946S22', '5l 22 cm stainless steel pot bottom');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12947S22', '22 cm stainless steel lid');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12945S20', '3l 20 cm stainless steel pot');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12946S20', '3l 20 cm stainless steel pot bottom');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12947S20', '20 cm stainless steel lid');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12945S17', '2l 17 cm stainless steel pot');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12945P17', '2l 17 cm stainless steel sauce pan');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12945S48', '17 cm stainless steel lid');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('AS12945G48', '17 cm glass lid');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12945A01', '250 mm 5 mm stainless steel disc');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12945A02', '220 mm pot base phase 1');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12945A03', '220 mm pot base phase 2');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12945A04', '220 mm pot base final');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12947A01', '250 mm 1 mm stainless steel disc');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12947A02', '220 mm lid pressed');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12947A03', '220 mm lid polished');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12947A04', '220 mm lid with handle');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12945A32', '200 mm pot base phase 1');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12945A33', '200 mm pot base phase 2');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12945A34', '200 mm pot base final');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12947A32', '200 mm lid pressed');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12947A33', '200 mm lid polished');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('IP12947A34', '200 mm lid with handle');
INSERT INTO Product_Type (Part_ID, Description) VALUES ('PN94561L67', 'Coolube 2210XP');


INSERT INTO Product_Family (Family_ID, Name) VALUES ('130', 'Kitchenware');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('125', 'Stainless Steel Cookware');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('145', 'Lids');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('132', 'Sauce Pans');
INSERT INTO Product_Family (Family_ID, Name) VALUES ('146', 'Glass Lids');


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


INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN12344A21', 50);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN52384R50', 30);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN52384R10', 30);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18544A21', 20);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18544C21', 15);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18324C54', 15);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN94561L67', 40);
--added to work in the procurement
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18324C51', 10);


INSERT INTO Component (Part_ID) VALUES ('PN12344A21');
INSERT INTO Component (Part_ID) VALUES ('PN52384R50');
INSERT INTO Component (Part_ID) VALUES ('PN52384R10');
INSERT INTO Component (Part_ID) VALUES ('PN18544A21');
INSERT INTO Component (Part_ID) VALUES ('PN18544C21');
INSERT INTO Component (Part_ID) VALUES ('PN18324C54');
--added to work in the procurement
INSERT INTO Component (Part_ID) VALUES ('PN18324C51');


INSERT INTO Raw_Material (Part_ID) VALUES ('PN94561L67');


INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A01');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A02');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A03');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A04');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A01');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A02');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A03');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A04');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A32');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A33');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12945A34');


INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (130, 'Pot test and packaging', NULL, 'AS12945S22', 240, 'AS12945S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (124, 'Lid polishing', NULL, 'AS12947S22', 1200, 'AS12947S22');
--duplicate operation
--INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
--VALUES (124, 'Lid polishing', NULL, 'AS12947S22', 1200, 'AS12947S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (123, 'Lid handle screw', 124, 'AS12947S22', 120, 'IP12947A04');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (122, 'Lid finishing', 123, 'AS12947S22', 240, 'IP12947A03');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (121, 'Lid pressing', 122, 'AS12947S22', 60, 'IP12947A02');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (120, 'Disc cutting', 121, 'AS12947S22', 120, 'IP12947A01');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (115, 'Pot handles riveting', NULL, 'AS12946S22', 600, 'AS12946S22');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (114, 'Pot base finishing', 115, 'AS12946S22', 300, 'IP12945A04');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (112, 'Final pot base pressing', 114, 'AS12946S22', 120, 'IP12945A03');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (103, 'Initial pot base pressing', 112, 'AS12946S22', 90, 'IP12945A02');
INSERT INTO Operation (Operation_ID, Designation, NextOperation_ID, Product_ID, Expected_Time, Output_Part_ID)
VALUES (100, 'Disc cutting', 103, 'AS12946S22', 120, 'IP12945A01');


INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PN52384R50', 100, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12945A01', 103, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PN94561L67', 103, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12945A02', 112, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PN94561L67', 112, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12945A03', 114, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12945A04', 115, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PN18544C21', 115, 2);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PN52384R10', 120, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12947A01', 121, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PN94561L67', 121, 5);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12947A02', 122, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12947A03', 123, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('PN18324C54', 123, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('IP12947A04', 124, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('AS12946S22', 130, 1);
INSERT INTO Operation_Input (Part_ID, Operation_ID, Quantity)
VALUES ('AS12947S22', 130, 1);


INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('A4578', 'Disc Cutting Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('A4588', 'Pressing Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('A4598', 'Pot Pressing Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('C5637', 'Finishing Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('S3271', 'Riveting Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('T3452', 'Screw Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('K3675', 'Packaging Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('D9123', 'Welding Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('Q3547', 'Polishing Workstation');
INSERT INTO Type_Workstation (WorkstationType_ID, Designation)
VALUES ('Q5478', 'Painting Workstation');


INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (1, 'Cutting Station 1', 'Handles disc cutting tasks', 'A4578');
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (2, 'Pressing Station 1', 'Handles initial and final pressing tasks', 'A4588');
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (3, 'Finishing Station 1', 'Handles pot base and lid finishing tasks', 'C5637');
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (4, 'Riveting Station 1', 'Handles handle riveting tasks', 'S3271');
INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
VALUES (5, 'Packaging Station 1', 'Handles final packaging tasks', 'K3675');


INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (100, 'A4578', 2.0, 0.5);
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (103, 'A4588', 1.5, 0.3);
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (112, 'A4588', 1.5, 0.3);
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (114, 'C5637', 5.0, 1.0);
INSERT INTO Operation_Type_Workstation (OperationOperation_ID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (115, 'S3271', 10.0, 2.0);


-- orders missing


INSERT INTO Supplier (ID, Name, Email, Phone) VALUES (12345, 'Supplier 12345', NULL, NULL);
INSERT INTO Supplier (ID, Name, Email, Phone) VALUES (12298, 'Supplier 12298', NULL, NULL);


INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12345, 'PN18544C21', 1.25, 20, TO_DATE('2023-10-01', 'YYYY-MM-DD'), NULL);
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12345, 'PN18324C54', 1.70, 10, TO_DATE('2023-10-01', 'YYYY-MM-DD'), TO_DATE('2024-02-29', 'YYYY-MM-DD'));
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12345, 'PN18324C54', 1.80, 16, TO_DATE('2024-04-01', 'YYYY-MM-DD'), NULL);
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12345, 'PN18324C51', 1.90, 30, TO_DATE('2023-07-01', 'YYYY-MM-DD'), TO_DATE('2024-03-31', 'YYYY-MM-DD'));
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12345, 'PN18324C51', 1.90, 20, TO_DATE('2024-04-01', 'YYYY-MM-DD'), NULL);
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12298, 'PN18544C21', 1.35, 10, TO_DATE('2023-09-01', 'YYYY-MM-DD'), NULL);
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12298, 'PN18324C54', 1.80, 10, TO_DATE('2023-08-01', 'YYYY-MM-DD'), TO_DATE('2024-01-29', 'YYYY-MM-DD'));
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12298, 'PN18324C54', 1.75, 20, TO_DATE('2024-02-15', 'YYYY-MM-DD'), NULL);
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12298, 'PN18324C51', 1.80, 40, TO_DATE('2023-08-01', 'YYYY-MM-DD'), TO_DATE('2024-05-31', 'YYYY-MM-DD'));
INSERT INTO Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
VALUES (12298, 'PN12344A21', 0.65, 200, TO_DATE('2023-07-01', 'YYYY-MM-DD'), NULL);


--order-line missing


--reservation missing
