--USBD14
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

        -- Conta as WorkstationTypes únicas usadas nas operações do produto
        SELECT COUNT(DISTINCT otw.WorkstationType_ID)
        INTO v_UsedWorkstationTypes
        FROM Operation o
        JOIN Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
        WHERE o.Product_ID = v_Product_ID;

        -- Compara o número de Workstation Types usados com o total existente
        IF v_UsedWorkstationTypes = totalWorkstationTypes THEN
            DBMS_OUTPUT.PUT_LINE('Product ID: ' || v_Product_ID || ', Name: ' || v_Product_Name);
        END IF;
    END LOOP;

    CLOSE productCursor;
END;
/



-- PARA TESTAR
BEGIN
    PrintProductsUsingAllWorkstationTypes;
END;
/
