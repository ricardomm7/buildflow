CREATE OR REPLACE PROCEDURE GET_PRODUCT_OPERATIONS (
    p_Product_ID IN CHAR,
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
    WITH RECURSIVE_OPERATION (
        Operation_ID, Designation, Expected_Time, Product_ID, Output_Part_ID, NextOperation_ID
    ) AS (
        -- Anchor member
        SELECT
            o.Operation_ID,
            o.Designation,
            o.Expected_Time,
            o.Product_ID,
            o.Output_Part_ID,
            o.NextOperation_ID
        FROM Operation o
        WHERE o.Product_ID = p_Product_ID
        -- Recursive member
        UNION ALL
        SELECT
            o.Operation_ID,
            o.Designation,
            o.Expected_Time,
            o.Product_ID,
            o.Output_Part_ID,
            o.NextOperation_ID
        FROM Operation o
        INNER JOIN RECURSIVE_OPERATION ro
        ON o.Operation_ID = ro.NextOperation_ID
    )
    SELECT
        ro.Operation_ID,
        ro.Designation,
        ro.Expected_Time,
        ro.Output_Part_ID AS Output_Part,
        COALESCE(pi.Part_ID, 'N/A') AS Input_Part,
        COALESCE(pi.Quantity, 0) AS Input_Quantity
    FROM RECURSIVE_OPERATION ro
    LEFT JOIN Operation_Input pi
    ON ro.Operation_ID = pi.Operation_ID
    ORDER BY ro.Operation_ID;
END;
/


DECLARE
    product_operations SYS_REFCURSOR;
    operation_id NUMBER;
    designation VARCHAR2(100);
    expected_time NUMBER;
    output_part CHAR(10);
    input_part CHAR(10);
    input_quantity NUMBER;
BEGIN
    GET_PRODUCT_OPERATIONS('PROD01', product_operations);

    LOOP
        FETCH product_operations INTO operation_id, designation, expected_time, output_part, input_part, input_quantity;
        EXIT WHEN product_operations%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Operation ID: ' || operation_id || ', Designation: ' || designation ||
                             ', Expected Time: ' || expected_time || ', Output Part: ' || output_part ||
                             ', Input Part: ' || input_part || ', Input Quantity: ' || input_quantity);
    END LOOP;

    CLOSE product_operations;
END;
/
