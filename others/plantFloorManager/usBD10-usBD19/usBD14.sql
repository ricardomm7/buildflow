--USBD14
SELECT P.Product_ID, P.Name, P.Description
FROM Product P
JOIN Product_Family PF ON P.Product_FamilyFamily_ID = PF.Family_ID
JOIN BOO_Operation BOO ON PF.Family_ID = BOO.Product_FamilyFamily_ID
JOIN Operation_Type_Workstation OTW ON BOO.OperationOperation_ID = OTW.OperationOperation_ID
GROUP BY P.Product_ID, P.Name, P.Description
HAVING COUNT(DISTINCT OTW.Type_WorkstationWorkstationType_ID) =
       (SELECT COUNT(*) FROM Type_Workstation);

-- incerteza no output (sem dados)
