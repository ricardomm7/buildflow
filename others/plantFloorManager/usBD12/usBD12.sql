CREATE OR REPLACE FUNCTION GetProductOperationParts(p_Product_ID IN Product.Part_ID%TYPE)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
    SELECT 
        oi.Part_ID,
        oi.Quantity
    FROM 
        Operation o
    JOIN 
        Operation_Input oi ON o.Operation_ID = oi.Operation_ID
    JOIN 
        Part p ON oi.Part_ID = p.Part_ID
    WHERE 
        o.Product_ID = p_Product_ID

    UNION ALL

    SELECT 
        oo.Part_ID,
        oo.Quantity
    FROM 
        Operation o
    JOIN 
        Operation_Output oo ON o.Operation_ID = oo.Operation_ID
    JOIN 
        Part p ON oo.Part_ID = p.Part_ID
    WHERE 
        o.Product_ID = p_Product_ID;

    RETURN cur_results;
END;
/


DECLARE
    v_cursor SYS_REFCURSOR;
    v_part_id Part.Part_ID%TYPE;
    v_quantity NUMBER;
BEGIN
    -- Chamar a função passando o ID do produto
    v_cursor := GetProductOperationParts('PROD001');

    LOOP
        FETCH v_cursor INTO v_part_id, v_quantity;
        EXIT WHEN v_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Part ID: ' || v_part_id || 
                             ', Quantity: ' || v_quantity);
    END LOOP;

    CLOSE v_cursor;
END;
/
