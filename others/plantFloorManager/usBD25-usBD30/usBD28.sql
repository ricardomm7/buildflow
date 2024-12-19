CREATE OR REPLACE PROCEDURE Get_Reserved_Parts (
    Search_Date IN DATE, -- Data de pesquisa fornecida como parâmetro
    Result_Cursor OUT SYS_REFCURSOR -- Cursor para retorno dos resultados
)
IS
BEGIN
OPEN Result_Cursor FOR
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
    p.Offer_Start <= Search_Date
  AND (p.Offer_End IS NULL OR p.Offer_End >= Search_Date)
ORDER BY r.Part_ID; -- Ordena os resultados pelo Part_ID
END;
/




DECLARE
Result SYS_REFCURSOR;
    Reserved_Part_ID VARCHAR2(10);
    Part_Description VARCHAR2(100);
    Reserved_Quantity NUMBER;
    Supplier_ID NUMBER;
BEGIN
    -- Chama o procedimento
    Get_Reserved_Parts(TO_DATE('2023-12-12', 'YYYY-MM-DD'), Result);

    -- Loop para percorrer os resultados do cursor
    LOOP
FETCH Result INTO Reserved_Part_ID, Part_Description, Reserved_Quantity, Supplier_ID;
        EXIT WHEN Result%NOTFOUND;

        -- Exibe os dados em formato legível
        DBMS_OUTPUT.PUT_LINE(
            'Part ID: ' || Reserved_Part_ID ||
            ', Description: ' || Part_Description ||
            ', Quantity: ' || Reserved_Quantity ||
            ', Supplier ID: ' || Supplier_ID
        );
END LOOP;

    -- Fecha o cursor
CLOSE Result;
END;
/