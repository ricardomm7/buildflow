
SELECT
    ep.Part_ID AS Material_Component_ID,
    ep.Minimum_Stock AS Reserved_Quantity,
    p.SupplierID AS Supplier_ID
FROM
    External_Part ep
        LEFT JOIN Procurement p ON ep.Part_ID = p.External_PartPart_ID
WHERE
    ep.Part_ID IN (
        SELECT Part_ID
        FROM Operation_Input
    );