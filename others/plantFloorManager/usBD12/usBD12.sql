-- USBD12
CREATE OR REPLACE FUNCTION GetProductOperationParts(p_Product_ID IN Part.Part_ID%TYPE)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
    -- Partes associadas ao produto principal (com soma de quantidades)
    SELECT
        oi.Part_ID,
        SUM(oi.Quantity) AS Quantity
    FROM
        Operation_Input oi
        JOIN Operation o ON oi.Operation_ID = o.Operation_ID
    WHERE
        o.Product_ID = p_Product_ID
        AND oi.Part_ID NOT IN (SELECT Part_ID FROM Product)
    GROUP BY
        oi.Part_ID

    UNION ALL

    -- Partes de subprodutos encontrados nas partes do produto principal (com soma de quantidades)
    SELECT
        suboi.Part_ID,
        SUM(suboi.Quantity) AS Quantity
    FROM
        Operation_Input suboi
        JOIN Operation subo ON suboi.Operation_ID = subo.Operation_ID
    WHERE
        subo.Product_ID IN (
            SELECT oi.Part_ID
            FROM
                Operation_Input oi
                JOIN Operation o ON oi.Operation_ID = o.Operation_ID
            WHERE
                o.Product_ID = p_Product_ID
                AND oi.Part_ID IN (SELECT Part_ID FROM Product)
        )
    GROUP BY
        suboi.Part_ID;

    RETURN cur_results;
END;
/


-- PARA TESTAR
DECLARE
    v_cursor SYS_REFCURSOR;
    v_part_id Part.Part_ID%TYPE;
    v_quantity NUMBER;
BEGIN
    -- Chamar a função passando o ID do produto
    v_cursor := GetProductOperationParts('AS12945S22');

    LOOP
        FETCH v_cursor INTO v_part_id, v_quantity;
        EXIT WHEN v_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Part ID: ' || v_part_id ||
                             ', Quantity: ' || v_quantity);
    END LOOP;

    CLOSE v_cursor;
END;
/
