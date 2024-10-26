
--Get Operations and Workstation Types from the BOO for a Given Product--

SELECT
    o.Operation_ID,
    o.Designation AS Operation_Name,
    tw.WorkstationType_ID,
    tw.Designation AS Workstation_Type
FROM
    Product p
JOIN
    Product_Family pf ON p.Product_FamilyFamily_ID = pf.Family_ID
JOIN
    BOO_Operation bo ON pf.Family_ID = bo.Product_FamilyFamily_ID
JOIN
    Operation o ON bo.OperationOperation_ID = o.Operation_ID
JOIN
    Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
JOIN
    Type_Workstation tw ON otw.Type_WorkstationWorkstationType_ID = tw.WorkstationType_ID
WHERE
    p.Product_ID = 'AS12945S22'  -- example of a product ID--
ORDER BY
    bo.BOOOperation_Sequence;


--Get Operations and Workstation Types from the BOO for a Given Product--
SELECT
    p.Product_ID,
    bo.BOOOperation_Sequence AS Sequence,
    o.Operation_ID,
    o.Designation AS Operation_Name,
    ws.Workstation_ID,
    ws.Name AS Workstation_Name,
    tw.Designation AS Workstation_Type
FROM
    Product p
JOIN
    Product_Family pf ON p.Product_FamilyFamily_ID = pf.Family_ID
JOIN
    BOO_Operation bo ON pf.Family_ID = bo.Product_FamilyFamily_ID
JOIN
    Operation o ON bo.OperationOperation_ID = o.Operation_ID
JOIN
    Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
JOIN
    Type_Workstation tw ON otw.Type_WorkstationWorkstationType_ID = tw.WorkstationType_ID
JOIN
    Workstation ws ON tw.WorkstationType_ID = ws.Type_WorkstationWorkstationType_ID
ORDER BY
    p.Product_ID, bo.BOOOperation_Sequence;

--Fetch Operations Sequence and Workstation Type for a Given Product--
SELECT
    bo.BOOOperation_Sequence AS Sequence,
    o.Operation_ID,
    o.Designation AS Operation_Name,
    tw.WorkstationType_ID,
    tw.Designation AS Workstation_Type
FROM
    Product p
JOIN
    Product_Family pf ON p.Product_FamilyFamily_ID = pf.Family_ID
JOIN
    BOO_Operation bo ON pf.Family_ID = bo.Product_FamilyFamily_ID
JOIN
    Operation o ON bo.OperationOperation_ID = o.Operation_ID
JOIN
    Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
JOIN
    Type_Workstation tw ON otw.Type_WorkstationWorkstationType_ID = tw.WorkstationType_ID
WHERE
    p.Product_ID = 'AS12945S22' /* Example with a product ID*/
ORDER BY
    bo.BOOOperation_Sequence;

---Design a report to display the BOO, operations sequence, and workstation types
SELECT b.BOOOperation_Sequence, o.Designation,tw.Designation
    FROM BOO_Operation b, Operation o, Operation_Type_Workstation otw, Type_Workstation tw
where
	b.OperationOperation_ID = o.Operation_ID
and o.Operation_ID = otw.OPERATIONOPERATION_ID
and otw.TYPE_WORKSTATIONWORKSTATIONTYPE_ID = tw.WorkstationType_ID
