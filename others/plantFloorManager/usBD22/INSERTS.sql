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


INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN12344A21', 120);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN52384R50', 130);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN52384R10', 130);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18544A21', 120);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18544C21', 150);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18324C54', 150);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN94561L67', 140);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN52384R12', 130);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN52384R45', 130);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18324C91', 110);
INSERT INTO External_Part (Part_ID, Minimum_Stock) VALUES ('PN18324C51', 110);


INSERT INTO Component (Part_ID) VALUES ('PN12344A21');
INSERT INTO Component (Part_ID) VALUES ('PN52384R50');
INSERT INTO Component (Part_ID) VALUES ('PN52384R10');
INSERT INTO Component (Part_ID) VALUES ('PN18544A21');
INSERT INTO Component (Part_ID) VALUES ('PN18544C21');
INSERT INTO Component (Part_ID) VALUES ('PN18324C54');
INSERT INTO Component (Part_ID) VALUES ('PN52384R12');
INSERT INTO Component (Part_ID) VALUES ('PN52384R45');
INSERT INTO Component (Part_ID) VALUES ('PN18324C91');
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
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A32');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A33');
INSERT INTO Intermediate_Product (Part_ID) VALUES ('IP12947A34');


-- Operation Type inserts
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5647, 'Disc cutting', 120);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5649, 'Initial pot base pressing', 90);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5651, 'Final pot base pressing', 120);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5653, 'Pot base finishing', 300);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5655, 'Lid pressing', 60);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5657, 'Lid finishing', 240);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5659, 'Pot handles riveting', 600);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5661, 'Lid handle screw', 120);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5663, 'Pot test and packaging', 240);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5665, 'Handle welding', 420);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5667, 'Lid polishing', 1200);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5669, 'Pot base polishing', 1800);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5671, 'Teflon painting', 3200);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5681, 'Initial pan base pressing', 120);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5682, 'Final pan base pressing', 160);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5683, 'Pan base finishing', 180);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5685, 'Handle gluing', 900);
INSERT INTO Operation_Type (ID, Description, Expec_Time) VALUES (5688, 'Pan test and packaging', 1500);


INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (154, 5659, NULL, 'AS12946S20', 'AS12946S20');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (115, 5659, NULL, 'AS12946S22', 'AS12946S22');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (124, 5667, NULL, 'AS12947S22', 'AS12947S22');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (130, 5663, NULL, 'AS12945S22', 'AS12945S22');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (164, 5667, NULL, 'AS12947S20', 'AS12947S20');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (170, 5663, NULL, 'AS12945S20', 'AS12945S20');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (114, 5653, 115, 'AS12946S22', 'IP12945A04');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (112, 5651, 114, 'AS12946S22', 'IP12945A03');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (103, 5649, 112, 'AS12946S22', 'IP12945A02');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (100, 5647, 103, 'AS12946S22', 'IP12945A01');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (123, 5661, 124, 'AS12947S22', 'IP12947A04');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (122, 5657, 123, 'AS12947S22', 'IP12947A03');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (121, 5655, 122, 'AS12947S22', 'IP12947A02');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (120, 5647, 121, 'AS12947S22', 'IP12947A01');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (153, 5653, 154, 'AS12946S20', 'IP12945A34');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (152, 5651, 153, 'AS12946S20', 'IP12945A33');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (151, 5649, 152, 'AS12946S20', 'IP12945A32');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (150, 5647, 151, 'AS12946S20', 'IP12945A01');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (163, 5661, 164, 'AS12947S20', 'IP12947A34');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (162, 5657, 163, 'AS12947S20', 'IP12947A33');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (161, 5655, 162, 'AS12947S20', 'IP12947A32');
INSERT INTO Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
VALUES (160, 5647, 161, 'AS12947S20', 'IP12947A01');


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
VALUES ('PN12344A21', 123, 3);
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


INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5647, 'A4578', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5647, 'A4588', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5647, 'A4598', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5649, 'A4588', 120, 0.3);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5649, 'A4598', 120, 0.3);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5651, 'A4588', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5651, 'A4598', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5653, 'C5637', 350, 1.0);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5655, 'A4588', 120, 0.2);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5655, 'A4598', 120, 0.2);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5657, 'C5637', 250, 0.8);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5659, 'S3271', 900, 2.0);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5661, 'T3452', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5663, 'K3675', 250, 0.8);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5665, 'D9123', 500, 1.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5667, 'Q3547', 1500, 4.0);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5669, 'Q3547', 2000, 6.0);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5671, 'Q5478', 4500, 10.67);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5681, 'A4588', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5681, 'A4598', 150, 0.4);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5682, 'A4588', 200, 0.53);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5682, 'A4598', 200, 0.53);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5683, 'C5637', 250, 0.6);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5685, 'D9123', 1200, 3.0);
INSERT INTO Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
VALUES (5688, 'K3675', 1800, 5.0);


-- orders missing
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD001', TO_DATE('2023-11-20', 'YYYY-MM-DD'), TO_DATE('2023-11-30', 'YYYY-MM-DD'), 'PT501245987');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD002', TO_DATE('2023-11-25', 'YYYY-MM-DD'), TO_DATE('2023-12-05', 'YYYY-MM-DD'), 'PT501245488');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD003', TO_DATE('2023-12-01', 'YYYY-MM-DD'), TO_DATE('2023-12-10', 'YYYY-MM-DD'), 'PT501242417');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD004', TO_DATE('2023-11-15', 'YYYY-MM-DD'), TO_DATE('2023-11-25', 'YYYY-MM-DD'), 'CZ6451237810');
-- Additional Orders (avoiding duplicate Order_IDs)
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD009', TO_DATE('2023-12-05', 'YYYY-MM-DD'), TO_DATE('2023-12-15', 'YYYY-MM-DD'), 'PT501245987');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD010', TO_DATE('2023-12-10', 'YYYY-MM-DD'), TO_DATE('2024-01-10', 'YYYY-MM-DD'), 'CZ6451237810');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD011', TO_DATE('2023-12-15', 'YYYY-MM-DD'), TO_DATE('2023-12-30', 'YYYY-MM-DD'), 'PT501245488');
INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
VALUES ('ORD012', TO_DATE('2023-12-20', 'YYYY-MM-DD'), TO_DATE('2024-01-05', 'YYYY-MM-DD'), 'PT501242417');


INSERT INTO Supplier (ID, Name, Email, Phone) VALUES (12345, 'Supplier 12345', NULL, NULL);
INSERT INTO Supplier (ID, Name, Email, Phone) VALUES (12298, 'Supplier 12298', NULL, NULL);

-- Additional Supplier inserts (avoiding duplicate IDs)
INSERT INTO Supplier (ID, Name, Email, Phone)
VALUES (12349, 'MetalPro Industries', 'orders@metalpro.com', '+351234567890');
INSERT INTO Supplier (ID, Name, Email, Phone)
VALUES (12350, 'KitchenSupplies Co.', 'sales@kitchensupplies.co.uk', '+441234567890');
INSERT INTO Supplier (ID, Name, Email, Phone)
VALUES (12351, 'Steel Masters GmbH', 'info@steelmasters.de', '+491234567890');


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
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12945S22', 'ORD001', 5);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12945S17', 'ORD001', 10);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12945P17', 'ORD002', 8);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12946S20', 'ORD002', 12);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12945S20', 'ORD003', 15);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12947S22', 'ORD003', 7);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12947S20', 'ORD004', 10);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity) VALUES ('AS12945S48', 'ORD004', 5);
-- Order Lines (usando os novos Order_IDs)
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12945S22', 'ORD009', 50);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12945S20', 'ORD009', 25);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12946S22', 'ORD010', 30);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12947S22', 'ORD010', 30);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12945S17', 'ORD011', 15);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12945P17', 'ORD011', 15);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12945S48', 'ORD012', 40);
INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
VALUES ('AS12945G48', 'ORD012', 40);

--reservation missing
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12945S22', 'ORD001', 'PN18544C21', 10);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12946S20', 'ORD002', 'PN18324C54', 5);

-- Reservations (usando os novos Order_IDs e garantindo combinações únicas)
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12945S22', 'ORD009', 'PN18544C21', 100);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12945S22', 'ORD009', 'PN52384R50', 50);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12946S22', 'ORD010', 'PN18324C54', 30);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12947S22', 'ORD010', 'PN12344A21', 90);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12945S17', 'ORD011', 'PN18544C21', 30);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12945P17', 'ORD011', 'PN18324C51', 15);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12945S48', 'ORD012', 'PN18324C54', 40);
INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
VALUES ('AS12945G48', 'ORD012', 'PN18324C91', 40);