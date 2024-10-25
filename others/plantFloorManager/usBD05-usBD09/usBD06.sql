CREATE VIEW View_WorkstationType_For_Order AS
SELECT DISTINCT tw.WorkstationType_ID, tw.Designation
FROM "Order" o
         JOIN Production_Order po ON o.Order_ID = po.OrderOrder_ID
         JOIN Product p ON po.ProductProduct_ID = p.Product_ID
         JOIN BOO_Operation b ON p.Product_FamilyFamily_ID = b.Product_FamilyFamily_ID
         JOIN Operation op ON b.OperationOperation_ID = op.Operation_ID
         JOIN Operation_Type_Workstation otw ON op.Operation_ID = otw.OperationOperation_ID
         JOIN Type_Workstation tw ON otw.Type_WorkstationWorkstationType_ID = tw.WorkstationType_ID
WHERE o.Order_ID = '1';


SELECT * FROM View_WorkstationType_For_Order;