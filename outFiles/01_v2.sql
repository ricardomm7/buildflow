INSERT INTO Clients (IDCliente, Name, VATIN, Addess, ZIP, Town, Country) VALUES
(456.0, 'Carvalho & Carvalho, Lda', 'PT501245987', 'Tv. Augusto Lessa 23', '4200-047', 'Porto', 'Portugal'),
(785.0, 'Tudo para a casa, Lda', 'PT501245488', 'R. Dr. Barros 93', '4465-219', 'São Mamede de Infesta', 'Portugal'),
(657.0, 'Sair de Cena', 'PT501242417', 'EDIFICIO CRISTAL lj18, R. António Correia de Carvalho 88', '4400-023', 'Vila Nova de Gaia', 'Portugal'),
(348.0, 'U Fleku', 'CZ6451237810', 'Křemencova 11', '110 00', 'Nové Město', 'Czechia');

INSERT INTO Products (Code, Name, Description) VALUES
('AS12945T22', 'La Belle 22 5l pot', '5l 22 cm aluminium and teflon non stick pot'),
('AS12945S22', 'Pro 22 5l pot', '5l 22 cm stainless steel pot'),
('AS12945S20', 'Pro 20 3l pot', '3l 20 cm stainless steel pot'),
('AS12945S17', 'Pro 17 2l sauce pan', '2l 17 cm stainless steel souce pan'),
('AS12945S48', 'Pro 17 lid', '17 cm stainless steel lid'),
('AS12945G48', 'Pro Clear 17 lid', '17 cm glass lid');

INSERT INTO Orders (OID, Client, Product, Quantity, DateOrder, DateDelivery) VALUES
(1.0, 785.0, 'AS12945S22', 5.0, 'Sun Sep 15 00:00:00 WEST 2024', 'Mon Sep 23 00:00:00 WEST 2024'),
(1.0, 785.0, 'AS12945S20', 15.0, 'Sun Sep 15 00:00:00 WEST 2024', 'Mon Sep 23 00:00:00 WEST 2024'),
(2.0, 657.0, 'AS12945S22', 10.0, 'Sun Sep 15 00:00:00 WEST 2024', 'Wed Sep 25 00:00:00 WEST 2024'),
(3.0, 348.0, 'AS12945S22', 10.0, 'Sun Sep 15 00:00:00 WEST 2024', 'Wed Sep 25 00:00:00 WEST 2024'),
(3.0, 348.0, 'AS12945S20', 10.0, 'Sun Sep 15 00:00:00 WEST 2024', 'Wed Sep 25 00:00:00 WEST 2024'),
(4.0, 785.0, 'AS12945S22', 4.0, 'Wed Sep 18 00:00:00 WEST 2024', 'Wed Sep 25 00:00:00 WEST 2024'),
(5.0, 657.0, 'AS12945S22', 12.0, 'Wed Sep 18 00:00:00 WEST 2024', 'Wed Sep 25 00:00:00 WEST 2024'),
(6.0, 348.0, 'AS12945S22', 8.0, 'Wed Sep 18 00:00:00 WEST 2024', 'Thu Sep 26 00:00:00 WEST 2024'),
(7.0, 456.0, 'AS12945S22', 7.0, 'Sat Sep 21 00:00:00 WEST 2024', 'Thu Sep 26 00:00:00 WEST 2024');

INSERT INTO Operations (OPID, Description, WorkstationType, WorkstationType, WorkstationType) VALUES
(5647.0, 'Disc cutting', 'A4578', 'A4588', 'A4598'),
(5649.0, 'Initial pot base pressing', 'A4588', 'A4598'),
(5651.0, 'Final pot base pressing', 'A4588', 'A4598'),
(5653.0, 'Pot base finishing', 'C5637'),
(5655.0, 'Lid pressing', 'A4588', 'A4598'),
(5657.0, 'Lid finishing', 'C5637'),
(5659.0, 'Pot handles riveting', 'S3271'),
(5661.0, 'Lid handle screw', 'T3452'),
(5663.0, 'Pot test and packaging', 'K3675'),
(5665.0, 'Handle welding', 'D9123'),
(5667.0, 'Lid polishing', 'Q3547'),
(5669.0, 'Pot base polishing', 'Q3547'),
(5671.0, 'Teflon painting', 'Q5478');

INSERT INTO WorkstationTypes (WTID, Name) VALUES
('A4578', '600t cold forging stamping press'),
('A4588', '600t cold forging precision stamping press'),
('A4598', '1000t cold forging precision stamping press'),
('S3271', 'Handle rivet'),
('K3675', 'Packaging'),
('K3676', 'Packaging for large itens'),
('C5637', 'Border trimming'),
('D9123', 'Spot welding'),
('Q5478', 'Teflon application station'),
('Q3547', 'Stainless steel polishing'),
('T3452', 'Assembly T1'),
('G9273', 'Circular glass cutting'),
('G9274', 'Glass trimming');

INSERT INTO Workstations (WSID, WTID, Name, Description) VALUES
(9875.0, 'A4578', 'Press 01', '220-630t cold forging press'),
(9886.0, 'A4578', 'Press 02', '220-630t cold forging press'),
(9847.0, 'A4588', 'Press 03', '220-630t precision cold forging press'),
(9855.0, 'A4588', 'Press 04', '160-1000t precison cold forging press'),
(8541.0, 'S3271', 'Rivet 02', 'Rivet station'),
(8543.0, 'S3271', 'Rivet 03', 'Rivet station'),
(6814.0, 'K3675', 'Packaging 01', 'Packaging station'),
(6815.0, 'K3675', 'Packaging 02', 'Packaging station'),
(6816.0, 'K3675', 'Packaging 03', 'Packaging station'),
(6821.0, 'K3675', 'Packaging 04', 'Packaging station'),
(6822.0, 'K3676', 'Packaging 05', 'Packaging station'),
(8167.0, 'D9123', 'Welding 01', 'Spot welding staion'),
(8170.0, 'D9123', 'Welding 02', 'Spot welding staion'),
(8171.0, 'D9123', 'Welding 03', 'Spot welding staion'),
(7235.0, 'T3452', 'Assembly 01', 'Product assembly station'),
(7236.0, 'T3452', 'Assembly 02', 'Product assembly station'),
(7238.0, 'T3452', 'Assembly 03', 'Product assembly station'),
(5124.0, 'C5637', 'Trimming 01', 'Metal trimming station'),
(4123.0, 'Q3547', 'Polishing 01', 'Metal polishing station'),
(4124.0, 'Q3547', 'Polishing 02', 'Metal polishing station'),
(4125.0, 'Q3547', 'Polishing 03', 'Metal polishing station');

INSERT INTO BOM (ProductID, PartNumber, Description, Quantity) VALUES
('AS12945S22', 'PN12344A21', 'Screw M6 35 mm', 1.0),
('AS12945S22', 'PN52384R50', '300x300 mm 5mm stainless steel sheet', 1.0),
('AS12945S22', 'PN52384R10', '300x300 mm 1mm stainless steel sheet', 1.0),
('AS12945S22', 'PN18544A21', 'Rivet 6 mm', 4.0),
('AS12945S22', 'PN18544C21', 'Stainless steel handle model U6', 2.0),
('AS12945S22', 'PN18324C54', 'Stainless steel handle model R12', 1.0),
('AS12945S20', 'PN12344A21', 'Screw M6 35 mm', 1.0),
('AS12945S20', 'PN52384R50', '300x300 mm 5mm stainless steel sheet', 1.0),
('AS12945S20', 'PN52384R10', '300x300 mm 1mm stainless steel sheet', 1.0),
('AS12945S20', 'PN18544A21', 'Rivet 6 mm', 4.0),
('AS12945S20', 'PN18544C21', 'Stainless steel handle model U6', 2.0),
('AS12945S20', 'PN18324C51', 'Stainless steel handle model R11', 1.0);

INSERT INTO BOO (ProductID, OPID, OPNumber) VALUES
('AS12945S22', 5647.0, 1.0),
('AS12945S22', 5647.0, 2.0),
('AS12945S22', 5649.0, 3.0),
('AS12945S22', 5651.0, 4.0),
('AS12945S22', 5653.0, 5.0),
('AS12945S22', 5659.0, 6.0),
('AS12945S22', 5669.0, 7.0),
('AS12945S22', 5655.0, 8.0),
('AS12945S22', 5657.0, 9.0),
('AS12945S22', 5661.0, 10.0),
('AS12945S22', 5667.0, 11.0),
('AS12945S22', 5663.0, 12.0);

