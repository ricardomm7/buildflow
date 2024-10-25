--USBD08

--Option 1
SELECT DISTINCT o.Operation_ID, o.Designation
FROM Operation o
JOIN Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
JOIN Type_Workstation tw ON otw.Type_WorkstationWorkstationType_ID = tw.WorkstationType_ID
JOIN Workstation w ON tw.WorkstationType_ID = w.Type_WorkstationWorkstationType_ID
ORDER BY o.Operation_ID ASC;

--Option 2
SELECT
    o.Operation_ID,
    o.Designation AS Operation_Designation,
    tw.WorkstationType_ID,
    tw.Designation AS Workstation_Designation,
    w.Workstation_ID,
    w.Name AS Workstation_Name,
    w.Description AS Workstation_Description
FROM
    Operation o
JOIN
    Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
JOIN
    Type_Workstation tw ON otw.Type_WorkstationWorkstationType_ID = tw.WorkstationType_ID
JOIN
    Workstation w ON tw.WorkstationType_ID = w.Type_WorkstationWorkstationType_ID;