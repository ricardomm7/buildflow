insert into Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
values ('PT501245987', 'Carvalho & Carvalho, Lda', 'Tv. Augusto Lessa 23', '4200-047', 'Porto', 'Portugal', 'idont@care.com', '003518340500');
insert into Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
values ('PT501245488', 'Tudo para a casa, Lda', 'R. Dr. Barros 93', '4465-219', 'São Mamede de Infesta', 'Portugal', 'me@neither.com', '003518340500');
insert into Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
values ('PT501242417', 'Sair de Cena', 'EDIFICIO CRISTAL lj18, R. António Correia de Carvalho 88', '4400-023', 'Vila Nova de Gaia', 'Portugal', 'some@email.com', '003518340500');
insert into Costumer (VAT, Name, Address, "Zip-Code", City, Country, Email, Phone)
values ('CZ6451237810', 'U Fleku', 'Křemencova 11', '110 00', 'Nové Město', 'Czechia', 'some.random@email.cz', '004201234567');


insert into Product_Type (Part_ID, Description) values ('PN12344A21', 'Screw M6 35 mm');
insert into Product_Type (Part_ID, Description) values ('PN52384R50', '300x300 mm 5 mm stainless steel sheet');
insert into Product_Type (Part_ID, Description) values ('PN52384R10', '300x300 mm 1 mm stainless steel sheet');
insert into Product_Type (Part_ID, Description) values ('PN18544A21', 'Rivet 6 mm');
insert into Product_Type (Part_ID, Description) values ('PN18544C21', 'Stainless steel handle model U6');
insert into Product_Type (Part_ID, Description) values ('PN18324C54', 'Stainless steel handle model R12');
insert into Product_Type (Part_ID, Description) values ('PN52384R45', '250x250 mm 5mm stainless steel sheet');
insert into Product_Type (Part_ID, Description) values ('PN52384R12', '250x250 mm 1mm stainless steel sheet');
insert into Product_Type (Part_ID, Description) values ('PN18324C91', 'Stainless steel handle model S26');
insert into Product_Type (Part_ID, Description) values ('PN18324C51', 'Stainless steel handle model R11');
insert into Product_Type (Part_ID, Description) values ('AS12945T22', '5l 22 cm aluminium and teflon non stick pot');
insert into Product_Type (Part_ID, Description) values ('AS12945S22', '5l 22 cm stainless steel pot');
insert into Product_Type (Part_ID, Description) values ('AS12946S22', '5l 22 cm stainless steel pot bottom');
insert into Product_Type (Part_ID, Description) values ('AS12947S22', '22 cm stainless steel lid');
insert into Product_Type (Part_ID, Description) values ('AS12945S20', '3l 20 cm stainless steel pot');
insert into Product_Type (Part_ID, Description) values ('AS12946S20', '3l 20 cm stainless steel pot bottom');
insert into Product_Type (Part_ID, Description) values ('AS12947S20', '20 cm stainless steel lid');
insert into Product_Type (Part_ID, Description) values ('AS12945S17', '2l 17 cm stainless steel pot');
insert into Product_Type (Part_ID, Description) values ('AS12945P17', '2l 17 cm stainless steel sauce pan');
insert into Product_Type (Part_ID, Description) values ('AS12945S48', '17 cm stainless steel lid');
insert into Product_Type (Part_ID, Description) values ('AS12945G48', '17 cm glass lid');
insert into Product_Type (Part_ID, Description) values ('IP12945A01', '250 mm 5 mm stainless steel disc');
insert into Product_Type (Part_ID, Description) values ('IP12945A02', '220 mm pot base phase 1');
insert into Product_Type (Part_ID, Description) values ('IP12945A03', '220 mm pot base phase 2');
insert into Product_Type (Part_ID, Description) values ('IP12945A04', '220 mm pot base final');
insert into Product_Type (Part_ID, Description) values ('IP12947A01', '250 mm 1 mm stainless steel disc');
insert into Product_Type (Part_ID, Description) values ('IP12947A02', '220 mm lid pressed');
insert into Product_Type (Part_ID, Description) values ('IP12947A03', '220 mm lid polished');
insert into Product_Type (Part_ID, Description) values ('IP12947A04', '220 mm lid with handle');
insert into Product_Type (Part_ID, Description) values ('IP12945A32', '200 mm pot base phase 1');
insert into Product_Type (Part_ID, Description) values ('IP12945A33', '200 mm pot base phase 2');
insert into Product_Type (Part_ID, Description) values ('IP12945A34', '200 mm pot base final');
insert into Product_Type (Part_ID, Description) values ('IP12947A32', '200 mm lid pressed');
insert into Product_Type (Part_ID, Description) values ('IP12947A33', '200 mm lid polished');
insert into Product_Type (Part_ID, Description) values ('IP12947A34', '200 mm lid with handle');
insert into Product_Type (Part_ID, Description) values ('PN94561L67', 'Coolube 2210XP');


insert into Product_Family (Family_ID, Name) values ('130', 'Kitchenware');
insert into Product_Family (Family_ID, Name) values ('125', 'Stainless Steel Cookware');
insert into Product_Family (Family_ID, Name) values ('145', 'Lids');
insert into Product_Family (Family_ID, Name) values ('132', 'Sauce Pans');
insert into Product_Family (Family_ID, Name) values ('146', 'Glass Lids');


insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12945T22', 'La Belle 22 5l pot', '130');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12945S22', 'Pro 22 5l pot', '125');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12946S22', 'Pro 22 5l pot bottom', '125');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12947S22', 'Pro 22 lid', '145');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12945S20', 'Pro 20 3l pot', '125');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12946S20', 'Pro 20 3l pot bottom', '125');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12947S20', 'Pro 20 lid', '145');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12945S17', 'Pro 17 2l pot', '125');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12945P17', 'Pro 17 2l sauce pan', '132');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12945S48', 'Pro 17 lid', '145');
insert into Product (Part_ID, Name, Product_FamilyFamily_ID) values ('AS12945G48', 'Pro Clear 17 lid', '146');


insert into External_Part (Part_ID, Minimum_Stock) values ('PN12344A21', 120);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN52384R50', 130);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN52384R10', 130);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN18544A21', 120);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN18544C21', 150);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN18324C54', 150);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN94561L67', 140);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN52384R12', 130);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN52384R45', 130);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN18324C91', 110);
insert into External_Part (Part_ID, Minimum_Stock) values ('PN18324C51', 110);


insert into Component (Part_ID) values ('PN12344A21');
insert into Component (Part_ID) values ('PN52384R50');
insert into Component (Part_ID) values ('PN52384R10');
insert into Component (Part_ID) values ('PN18544A21');
insert into Component (Part_ID) values ('PN18544C21');
insert into Component (Part_ID) values ('PN18324C54');
insert into Component (Part_ID) values ('PN52384R12');
insert into Component (Part_ID) values ('PN52384R45');
insert into Component (Part_ID) values ('PN18324C91');
insert into Component (Part_ID) values ('PN18324C51');


insert into Raw_Material (Part_ID) values ('PN94561L67');


insert into Intermediate_Product (Part_ID) values ('IP12945A01');
insert into Intermediate_Product (Part_ID) values ('IP12945A02');
insert into Intermediate_Product (Part_ID) values ('IP12945A03');
insert into Intermediate_Product (Part_ID) values ('IP12945A04');
insert into Intermediate_Product (Part_ID) values ('IP12947A01');
insert into Intermediate_Product (Part_ID) values ('IP12947A02');
insert into Intermediate_Product (Part_ID) values ('IP12947A03');
insert into Intermediate_Product (Part_ID) values ('IP12947A04');
insert into Intermediate_Product (Part_ID) values ('IP12945A32');
insert into Intermediate_Product (Part_ID) values ('IP12945A33');
insert into Intermediate_Product (Part_ID) values ('IP12945A34');
insert into Intermediate_Product (Part_ID) values ('IP12947A32');
insert into Intermediate_Product (Part_ID) values ('IP12947A33');
insert into Intermediate_Product (Part_ID) values ('IP12947A34');


-- Operation Type inserts
insert into Operation_Type (ID, Description, Expec_Time) values (5647, 'Disc cutting', 120);
insert into Operation_Type (ID, Description, Expec_Time) values (5649, 'Initial pot base pressing', 90);
insert into Operation_Type (ID, Description, Expec_Time) values (5651, 'Final pot base pressing', 120);
insert into Operation_Type (ID, Description, Expec_Time) values (5653, 'Pot base finishing', 300);
insert into Operation_Type (ID, Description, Expec_Time) values (5655, 'Lid pressing', 60);
insert into Operation_Type (ID, Description, Expec_Time) values (5657, 'Lid finishing', 240);
insert into Operation_Type (ID, Description, Expec_Time) values (5659, 'Pot handles riveting', 600);
insert into Operation_Type (ID, Description, Expec_Time) values (5661, 'Lid handle screw', 120);
insert into Operation_Type (ID, Description, Expec_Time) values (5663, 'Pot test and packaging', 240);
insert into Operation_Type (ID, Description, Expec_Time) values (5665, 'Handle welding', 420);
insert into Operation_Type (ID, Description, Expec_Time) values (5667, 'Lid polishing', 1200);
insert into Operation_Type (ID, Description, Expec_Time) values (5669, 'Pot base polishing', 1800);
insert into Operation_Type (ID, Description, Expec_Time) values (5671, 'Teflon painting', 3200);
insert into Operation_Type (ID, Description, Expec_Time) values (5681, 'Initial pan base pressing', 120);
insert into Operation_Type (ID, Description, Expec_Time) values (5682, 'Final pan base pressing', 160);
insert into Operation_Type (ID, Description, Expec_Time) values (5683, 'Pan base finishing', 180);
insert into Operation_Type (ID, Description, Expec_Time) values (5685, 'Handle gluing', 900);
insert into Operation_Type (ID, Description, Expec_Time) values (5688, 'Pan test and packaging', 1500);


insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (115, 5659, null, 'AS12946S22', 'AS12946S22');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (124, 5667, null, 'AS12947S22', 'AS12947S22');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (130, 5663, null, 'AS12945S22', 'AS12945S22');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (164, 5667, null, 'AS12947S20', 'AS12947S20');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (170, 5663, null, 'AS12945S20', 'AS12945S20');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (154, 5659, null, 'AS12946S20', 'AS12946S20');

insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (114, 5653, 115, 'AS12946S22', 'IP12945A04');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (112, 5651, 114, 'AS12946S22', 'IP12945A03');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (103, 5649, 112, 'AS12946S22', 'IP12945A02');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (100, 5647, 103, 'AS12946S22', 'IP12945A01');

insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (123, 5661, 124, 'AS12947S22', 'IP12947A04');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (122, 5657, 123, 'AS12947S22', 'IP12947A03');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (121, 5655, 122, 'AS12947S22', 'IP12947A02');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (120, 5647, 121, 'AS12947S22', 'IP12947A01');


insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (153, 5653, 154, 'AS12946S20', 'IP12945A34');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (152, 5651, 153, 'AS12946S20', 'IP12945A33');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (151, 5649, 152, 'AS12946S20', 'IP12945A32');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (150, 5647, 151, 'AS12946S20', 'IP12945A01');

insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (163, 5661, 164, 'AS12947S20', 'IP12947A34');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (162, 5657, 163, 'AS12947S20', 'IP12947A33');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (161, 5655, 162, 'AS12947S20', 'IP12947A32');
insert into Operation (Operation_ID, Operation_TypeID, NextOperation_ID, Product_ID, Output_Part_ID)
values (160, 5647, 161, 'AS12947S20', 'IP12947A01');



insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN52384R50', 100, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A01', 103, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN94561L67', 103, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A02', 112, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN94561L67', 112, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A03', 114, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A04', 115, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN18544A21', 115, 4);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN18544C21', 115, 2);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN52384R10', 120, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A01', 121, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN94561L67', 121, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A02', 122, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A03', 123, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN18324C54', 123, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN12344A21', 123, 3);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A04', 124, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('AS12947S22', 130, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('AS12946S22', 130, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN52384R50', 150, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A01', 151, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN94561L67', 151, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A32', 152, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN94561L67', 152, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A33', 153, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12945A34', 154, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN18544C21', 154, 2);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN18544A21', 154, 4);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN52384R10', 160, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A01', 161, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN94561L67', 161, 5);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A32', 162, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A33', 163, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN18324C51', 163, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('PN12344A21', 163, 3);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('IP12947A34', 164, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('AS12946S20', 170, 1);
insert into Operation_Input (Part_ID, Operation_ID, Quantity) values ('AS12947S20', 170, 1);


insert into Type_Workstation (WorkstationType_ID, Designation)
values ('A4578', 'Disc Cutting Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('A4588', 'Pressing Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('A4598', 'Pot Pressing Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('C5637', 'Finishing Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('S3271', 'Riveting Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('T3452', 'Screw Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('K3675', 'Packaging Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('D9123', 'Welding Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('Q3547', 'Polishing Workstation');
insert into Type_Workstation (WorkstationType_ID, Designation)
values ('Q5478', 'Painting Workstation');

-- Workstations missing
insert into Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
values (1, 'Cutting Station 1', 'Handles disc cutting tasks', 'A4578');
insert into Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
values (2, 'Pressing Station 1', 'Handles initial and final pressing tasks', 'A4588');
insert into Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
values (3, 'Finishing Station 1', 'Handles pot base and lid finishing tasks', 'C5637');
insert into Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
values (4, 'Riveting Station 1', 'Handles handle riveting tasks', 'S3271');
insert into Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
values (5, 'Packaging Station 1', 'Handles final packaging tasks', 'K3675');


insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5647, 'A4578', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5647, 'A4588', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5647, 'A4598', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5649, 'A4588', 120, 0.3);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5649, 'A4598', 120, 0.3);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5651, 'A4588', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5651, 'A4598', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5653, 'C5637', 350, 1.0);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5655, 'A4588', 120, 0.2);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5655, 'A4598', 120, 0.2);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5657, 'C5637', 250, 0.8);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5659, 'S3271', 900, 2.0);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5661, 'T3452', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5663, 'K3675', 250, 0.8);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5665, 'D9123', 500, 1.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5667, 'Q3547', 1500, 4.0);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5669, 'Q3547', 2000, 6.0);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5671, 'Q5478', 4500, 10.67);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5681, 'A4588', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5681, 'A4598', 150, 0.4);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5682, 'A4588', 200, 0.53);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5682, 'A4598', 200, 0.53);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5683, 'C5637', 250, 0.6);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5685, 'D9123', 1200, 3.0);
insert into Operation_Type_Workstation (Operation_TypeID, WorkstationType_ID, Max_Exec_Time, Setup_Time)
values (5688, 'K3675', 1800, 5.0);


-- orders missing
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD001', to_date('2023-11-20', 'YYYY-MM-DD'), to_date('2023-11-30', 'YYYY-MM-DD'), 'PT501245987');
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD002', to_date('2023-11-25', 'YYYY-MM-DD'), to_date('2023-12-05', 'YYYY-MM-DD'), 'PT501245488');
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD003', to_date('2023-12-01', 'YYYY-MM-DD'), to_date('2023-12-10', 'YYYY-MM-DD'), 'PT501242417');
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD004', to_date('2023-11-15', 'YYYY-MM-DD'), to_date('2023-11-25', 'YYYY-MM-DD'), 'CZ6451237810');
-- Additional Orders (avoiding duplicate Order_IDs)
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD009', to_date('2023-12-05', 'YYYY-MM-DD'), to_date('2023-12-15', 'YYYY-MM-DD'), 'PT501245987');
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD010', to_date('2023-12-10', 'YYYY-MM-DD'), to_date('2024-01-10', 'YYYY-MM-DD'), 'CZ6451237810');
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD011', to_date('2023-12-15', 'YYYY-MM-DD'), to_date('2023-12-30', 'YYYY-MM-DD'), 'PT501245488');
insert into "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
values ('ORD012', to_date('2023-12-20', 'YYYY-MM-DD'), to_date('2024-01-05', 'YYYY-MM-DD'), 'PT501242417');


insert into Supplier (ID, Name, Email, Phone) values (12345, 'Supplier 12345', null, null);
insert into Supplier (ID, Name, Email, Phone) values (12298, 'Supplier 12298', null, null);

-- Additional Supplier inserts (avoiding duplicate IDs)
insert into Supplier (ID, Name, Email, Phone)
values (12349, 'MetalPro Industries', 'orders@metalpro.com', '+351234567890');
insert into Supplier (ID, Name, Email, Phone)
values (12350, 'KitchenSupplies Co.', 'sales@kitchensupplies.co.uk', '+441234567890');
insert into Supplier (ID, Name, Email, Phone)
values (12351, 'Steel Masters GmbH', 'info@steelmasters.de', '+491234567890');


insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12345, 'PN18544C21', 1.25, 20, to_date('2023-10-01', 'YYYY-MM-DD'), null);
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12345, 'PN18324C54', 1.70, 10, to_date('2023-10-01', 'YYYY-MM-DD'), to_date('2024-02-29', 'YYYY-MM-DD'));
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12345, 'PN18324C54', 1.80, 16, to_date('2024-04-01', 'YYYY-MM-DD'), null);
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12345, 'PN18324C51', 1.90, 30, to_date('2023-07-01', 'YYYY-MM-DD'), to_date('2024-03-31', 'YYYY-MM-DD'));
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12345, 'PN18324C51', 1.90, 20, to_date('2024-04-01', 'YYYY-MM-DD'), null);
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12298, 'PN18544C21', 1.35, 10, to_date('2023-09-01', 'YYYY-MM-DD'), null);
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12298, 'PN18324C54', 1.80, 10, to_date('2023-08-01', 'YYYY-MM-DD'), to_date('2024-01-29', 'YYYY-MM-DD'));
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12298, 'PN18324C54', 1.75, 20, to_date('2024-02-15', 'YYYY-MM-DD'), null);
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12298, 'PN18324C51', 1.80, 40, to_date('2023-08-01', 'YYYY-MM-DD'), to_date('2024-05-31', 'YYYY-MM-DD'));
insert into Procurement (SupplierID, External_PartPart_ID, Unit_Cost, Minimum_Quantity, Offer_Start, Offer_End)
values (12298, 'PN12344A21', 0.65, 200, to_date('2023-07-01', 'YYYY-MM-DD'), null);


--order-line missing
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12945S22', 'ORD001', 5);
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12945S17', 'ORD001', 10);
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12945P17', 'ORD002', 8);
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12946S20', 'ORD002', 12);
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12945S20', 'ORD003', 15);
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12947S22', 'ORD003', 7);
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12947S20', 'ORD004', 10);
insert into Order_Line (Product_ID, Order_ID, quantity) values ('AS12945S48', 'ORD004', 5);
-- Order Lines (usando os novos Order_IDs)
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12945S22', 'ORD009', 50);
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12945S20', 'ORD009', 25);
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12946S22', 'ORD010', 30);
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12947S22', 'ORD010', 30);
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12945S17', 'ORD011', 15);
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12945P17', 'ORD011', 15);
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12945S48', 'ORD012', 40);
insert into Order_Line (Product_ID, Order_ID, quantity)
values ('AS12945G48', 'ORD012', 40);

--reservation missing
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12945S22', 'ORD001', 'PN18544C21', 10);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12946S20', 'ORD002', 'PN18324C54', 5);

-- Reservations (usando os novos Order_IDs e garantindo combinações únicas)
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12945S22', 'ORD009', 'PN18544C21', 100);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12945S22', 'ORD009', 'PN52384R50', 50);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12946S22', 'ORD010', 'PN18324C54', 30);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12947S22', 'ORD010', 'PN12344A21', 90);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12945S17', 'ORD011', 'PN18544C21', 30);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12945P17', 'ORD011', 'PN18324C51', 15);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12945S48', 'ORD012', 'PN18324C54', 40);
insert into Reservation (Product_ID, Order_ID, Part_ID, quantity)
values ('AS12945G48', 'ORD012', 'PN18324C91', 40);