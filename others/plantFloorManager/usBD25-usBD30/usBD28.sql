CREATE OR REPLACE PROCEDURE Get_All_Reserved_Parts (
    Result_Cursor OUT SYS_REFCURSOR
)
IS
    v_Count NUMBER;
BEGIN
    -- Verifica se há registros na tabela de reservas
    SELECT COUNT(*)
    INTO v_Count
    FROM Reservation r
         LEFT JOIN External_Part ep ON r.Part_ID = ep.Part_ID
         LEFT JOIN Product_Type pt ON r.Part_ID = pt.Part_ID
         LEFT JOIN Procurement p ON ep.Part_ID = p.External_PartPart_ID;


    IF v_Count = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Nenhum material ou componente reservado foi encontrado.');
    END IF;

    -- Abre o cursor para os resultados
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
    ORDER BY r.Part_ID; -- Ordena os resultados pelo Part_ID
EXCEPTION
    -- Trata erros inesperados
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20002, 'Ocorreu um erro inesperado: ' || SQLERRM);
END;
/

DECLARE
    Result SYS_REFCURSOR;
    Reserved_Part_ID CHAR(10);
    Part_Description VARCHAR2(100);
    Reserved_Quantity NUMBER;
    Supplier_ID NUMBER;
BEGIN
    BEGIN
        Get_All_Reserved_Parts(Result);

        -- Loop para percorrer os resultados do cursor
        LOOP
            FETCH Result INTO Reserved_Part_ID, Part_Description, Reserved_Quantity, Supplier_ID;
            EXIT WHEN Result%NOTFOUND;

            DBMS_OUTPUT.PUT_LINE(
                'Reserved Part ID: ' || Reserved_Part_ID ||
                ', Description: ' || Part_Description ||
                ', Quantity: ' || Reserved_Quantity ||
                ', Supplier ID: ' || Supplier_ID
            );
        END LOOP;

        CLOSE Result;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Erro Inesperado: ' || SQLERRM);
    END;
END;
/