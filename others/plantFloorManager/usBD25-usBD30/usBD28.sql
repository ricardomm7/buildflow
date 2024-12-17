SELECT
    r.Part_ID AS Reserved_Part_ID,
    pt.Description AS Part_Description,
    r.quantity AS Reserved_Quantity,
    p.SupplierID AS Supplier_ID
FROM
    Reservation r
        LEFT JOIN External_Part ep ON r.Part_ID = ep.Part_ID
        LEFT JOIN Product_Type pt ON r.Part_ID = pt.Part_ID
        LEFT JOIN Procurement p ON ep.Part_ID = p.External_PartPart_ID
WHERE
    p.Offer_Start <= TO_DATE('2023-12-12', 'YYYY-MM-DD')
  AND (p.Offer_End IS NULL OR p.Offer_End >= TO_DATE('2023-12-12', 'YYYY-MM-DD'));