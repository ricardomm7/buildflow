SELECT DISTINCT tw.WorkstationType_ID, tw.Designation
FROM "Order" o
         Left JOIN Production_Order po ON o.Order_ID = po.OrderOrder_ID
         Left JOIN Product p ON po.ProductProduct_ID = p.Product_ID
         Left JOIN BOO_Operation b ON p.Product_FamilyFamily_ID = b.Product_FamilyFamily_ID
         Left JOIN Operation op ON b.OperationOperation_ID = op.Operation_ID
         Left JOIN Type_Workstation tw ON op.Type_WorkstationWorkstationType_ID = tw.WorkstationType_ID
WHERE o.Order_ID = '1';
