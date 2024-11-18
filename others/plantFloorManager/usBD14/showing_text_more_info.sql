CREATE OR REPLACE PROCEDURE PrintProductsUsingAllWorkstationTypes
IS
    totalWorkstationTypes NUMBER;
    CURSOR productCursor IS
        SELECT Part_ID, Name FROM Product;
    v_Product_ID Product.Part_ID%TYPE;
    v_Product_Name Product.Name%TYPE;
    v_UsedWorkstationTypes NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO totalWorkstationTypes
    FROM Type_Workstation;

    OPEN productCursor;

    LOOP
        FETCH productCursor INTO v_Product_ID, v_Product_Name;
        EXIT WHEN productCursor%NOTFOUND;

        SELECT COUNT(DISTINCT otw.WorkstationType_ID)
        INTO v_UsedWorkstationTypes
        FROM (
            SELECT o.Operation_ID
            FROM Operation o
            WHERE o.Product_ID = v_Product_ID

            UNION ALL

            SELECT subo.Operation_ID
            FROM Operation subo
            WHERE subo.Product_ID IN (
                SELECT DISTINCT oi.Part_ID
                FROM Operation_Input oi
                WHERE EXISTS (
                    SELECT 1
                    FROM Product p
                    WHERE p.Part_ID = oi.Part_ID
                )
                AND oi.Operation_ID IN (
                    SELECT o.Operation_ID
                    FROM Operation o
                    WHERE o.Product_ID = v_Product_ID
                )
            )
        ) operations
        JOIN Operation_Type_Workstation otw ON operations.Operation_ID = otw.OperationOperation_ID;

        -- Saída intermediária para depuração
        DBMS_OUTPUT.PUT_LINE('Checking Product ID: ' || v_Product_ID ||
                             ', Name: ' || v_Product_Name ||
                             ', Used Workstation Types: ' || v_UsedWorkstationTypes ||
                             ', Total Required: ' || totalWorkstationTypes);

        IF v_UsedWorkstationTypes = totalWorkstationTypes THEN
            DBMS_OUTPUT.PUT_LINE('Product ID: ' || v_Product_ID || ', Name: ' || v_Product_Name);
        END IF;
    END LOOP;

    CLOSE productCursor;
END;
/

BEGIN
    PrintProductsUsingAllWorkstationTypes;
END;
/
