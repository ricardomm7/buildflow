-- USBD13
CREATE OR REPLACE FUNCTION GetProductOperationsAndWorkstations(
    p_Product_ID IN Product.Part_ID%TYPE
)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
    SELECT
        o.Operation_ID,
        o.Designation AS Operation_Designation,
        wt.WorkstationType_ID
    FROM
        Operation o
    JOIN
        Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
    JOIN
        Type_Workstation wt ON otw.WorkstationType_ID = wt.WorkstationType_ID
    WHERE
        o.Product_ID = p_Product_ID

    UNION ALL

    -- Operações dos subprodutos encontrados nas Operation Inputs
    SELECT
        subo.Operation_ID,
        subo.Designation AS Operation_Designation,
        wt.WorkstationType_ID
    FROM
        Operation subo
    JOIN
        Operation_Type_Workstation otw ON subo.Operation_ID = otw.OperationOperation_ID
    JOIN
        Type_Workstation wt ON otw.WorkstationType_ID = wt.WorkstationType_ID
    WHERE
        subo.Product_ID IN (
            SELECT DISTINCT
                oi.Part_ID
            FROM
                Operation_Input oi
            WHERE
                EXISTS (
                    SELECT 1
                    FROM Product p
                    WHERE p.Part_ID = oi.Part_ID
                )
                AND oi.Operation_ID IN (
                    SELECT o.Operation_ID
                    FROM Operation o
                    WHERE o.Product_ID = p_Product_ID
                )
        );

    RETURN cur_results;
END;
/



-- PARA TESTE
DECLARE
    cur SYS_REFCURSOR;
    v_Operation_ID NUMBER;
    v_Operation_Designation VARCHAR2(100);
    v_WorkstationType_ID CHAR(5);
BEGIN
    cur := GetProductOperationsAndWorkstations('PROD001');

    LOOP
        FETCH cur INTO v_Operation_ID, v_Operation_Designation, v_WorkstationType_ID;
        EXIT WHEN cur%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Operation ID: ' || v_Operation_ID ||
                             ', Operation: ' || v_Operation_Designation ||
                             ', Workstation Type ID: ' || v_WorkstationType_ID);
    END LOOP;

    CLOSE cur;
END;
/
