--USBD13
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
        wt.WorkstationType_ID,
        wt.Designation AS WorkstationType_Designation
    FROM
        Operation o
    JOIN
        Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
    JOIN
        Type_Workstation wt ON otw.WorkstationType_ID = wt.WorkstationType_ID
    WHERE
        o.Product_ID = p_Product_ID;

    RETURN cur_results;
END;
/




-- PARA TESTEEEEEEEEEE
DECLARE
    cur SYS_REFCURSOR;
    v_Operation_ID NUMBER;
    v_Operation_Designation VARCHAR2(100);
    v_WorkstationType_ID CHAR(5);
    v_WorkstationType_Designation VARCHAR2(60);
BEGIN
    cur := GetProductOperationsAndWorkstations('PROD001');

    LOOP
        FETCH cur INTO v_Operation_ID, v_Operation_Designation, v_WorkstationType_ID, v_WorkstationType_Designation;
        EXIT WHEN cur%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Operation ID: ' || v_Operation_ID ||
                             ', Operation: ' || v_Operation_Designation ||
                             ', Workstation Type ID: ' || v_WorkstationType_ID ||
                             ', Workstation Type: ' || v_WorkstationType_Designation);
    END LOOP;

    CLOSE cur;
END;
/
