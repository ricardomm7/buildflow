-- USBD12
CREATE OR REPLACE FUNCTION GetProductOperationParts(p_Product_ID IN Part.Part_ID%TYPE)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
    -- Partes associadas ao produto principal, excluindo Intermediate Products
    SELECT
        oi.Part_ID,
        SUM(oi.Quantity) AS Quantity
    FROM
        Operation_Input oi
        JOIN Operation o ON oi.Operation_ID = o.Operation_ID
    WHERE
        o.Product_ID = p_Product_ID
        AND NOT EXISTS (
            SELECT 1
            FROM Intermediate_Product ip
            WHERE ip.Part_ID = oi.Part_ID -- Exclui Intermediate Products
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Product p
            WHERE p.Part_ID = oi.Part_ID -- Exclui produtos com operações associadas
        )
    GROUP BY
        oi.Part_ID

    UNION ALL

    -- Partes de subprodutos encontrados nas partes do produto principal
    SELECT
        suboi.Part_ID,
        SUM(suboi.Quantity) AS Quantity
    FROM
        Operation_Input suboi
        JOIN Operation subo ON suboi.Operation_ID = subo.Operation_ID
    WHERE
        subo.Product_ID IN (
            SELECT DISTINCT oi.Part_ID
            FROM
                Operation_Input oi
                JOIN Operation o ON oi.Operation_ID = o.Operation_ID
            WHERE
                o.Product_ID = p_Product_ID
                AND EXISTS (
                    SELECT 1
                    FROM Product p
                    WHERE p.Part_ID = oi.Part_ID -- Apenas subprodutos que são produtos
                )
                AND NOT EXISTS (
                    SELECT 1
                    FROM Intermediate_Product ip
                    WHERE ip.Part_ID = oi.Part_ID -- Exclui Intermediate Products
                )
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Intermediate_Product ip
            WHERE ip.Part_ID = suboi.Part_ID -- Exclui Intermediate Products
        )
    GROUP BY
        suboi.Part_ID

    UNION ALL

    -- Produtos sem operações associadas, mas presentes como partes
    SELECT
        p.Part_ID,
        NULL AS Quantity
    FROM
        Product p
    WHERE
        p.Part_ID IN (
            SELECT oi.Part_ID
            FROM
                Operation_Input oi
                JOIN Operation o ON oi.Operation_ID = o.Operation_ID
            WHERE
                o.Product_ID = p_Product_ID
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Operation o
            WHERE o.Product_ID = p.Part_ID -- Exclui produtos com operações associadas
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Intermediate_Product ip
            WHERE ip.Part_ID = p.Part_ID -- Exclui Intermediate Products
        );

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
