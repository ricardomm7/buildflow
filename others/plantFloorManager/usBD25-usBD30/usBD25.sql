CREATE OR REPLACE FUNCTION  GetProductOperations(p_Product_ID IN Product.Part_ID%TYPE)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
        WITH recursive_operations(
            operation_id,
            operation_typeid,
            next_operation_id,
            product_id,
            output_part_id,
            root_product_id
        ) AS (
            -- Base case: get direct operations for the product
            SELECT
                o.Operation_ID,
                o.Operation_TypeID,
                o.NextOperation_ID,
                o.Product_ID,
                o.Output_Part_ID,
                o.Product_ID AS root_product_id
            FROM Operation o
            WHERE o.Product_ID = p_Product_ID

            UNION ALL

            -- Recursive case: get operations for subproducts
            SELECT
                o.Operation_ID,
                o.Operation_TypeID,
                o.NextOperation_ID,
                o.Product_ID,
                o.Output_Part_ID,
                ro.root_product_id
            FROM recursive_operations ro
            JOIN Operation_Input oi ON oi.Operation_ID = ro.operation_id
            JOIN Product p ON p.Part_ID = oi.Part_ID
            JOIN Operation o ON o.Product_ID = p.Part_ID
        )
        -- Main query combining all required information
        SELECT
            ro.operation_id,
            ro.product_id,
            ot.Description AS operation_type,
            ot.Expec_Time AS expected_time,
            ro.next_operation_id,
            ro.output_part_id,
            -- Subquery for input parts and quantities
            CURSOR(
                SELECT oi.Part_ID, pt.Description, oi.Quantity
                FROM Operation_Input oi
                JOIN Product_Type pt ON pt.Part_ID = oi.Part_ID
                WHERE oi.Operation_ID = ro.operation_id
            ) AS inputs
        FROM recursive_operations ro
        JOIN Operation_Type ot ON ot.ID = ro.operation_typeid
        ORDER BY ro.operation_id;

    RETURN cur_results;
END GetProductOperations;
/


DECLARE
    v_cursor SYS_REFCURSOR;

    -- Main cursor variables
    v_operation_id Operation.Operation_ID%TYPE;
    v_product_id Product.Part_ID%TYPE;
    v_operation_type Operation_Type.Description%TYPE;
    v_expected_time Operation_Type.Expec_Time%TYPE;
    v_next_operation_id Operation.NextOperation_ID%TYPE;
    v_output_part_id Operation.Output_Part_ID%TYPE;

    -- Nested cursor for inputs
    v_inputs SYS_REFCURSOR;

    -- Input cursor variables
    v_input_part_id Product_Type.Part_ID%TYPE;
    v_input_description Product_Type.Description%TYPE;
    v_input_quantity Operation_Input.Quantity%TYPE;

BEGIN
    -- Get the operations for product AS12945S22
    v_cursor := GetProductOperations('AS12945S22');

    LOOP
        FETCH v_cursor INTO
            v_operation_id,
            v_product_id,
            v_operation_type,
            v_expected_time,
            v_next_operation_id,
            v_output_part_id,
            v_inputs;

        exit WHEN v_cursor%notfound;

        -- Print main operation information
        dbms_output.put_line('----------------------------------------');
        dbms_output.put_line('Operation ID: ' || v_operation_id);
        dbms_output.put_line('Product: ' || v_product_id);
        dbms_output.put_line('Operation Type: ' || v_operation_type);
        dbms_output.put_line('Expected Time: ' || v_expected_time);
        dbms_output.put_line('Next Operation: ' || v_next_operation_id);
        dbms_output.put_line('Output: ' || v_output_part_id);

        -- Print input parts
        dbms_output.put_line('Inputs:');
        loop
            FETCH v_inputs INTO v_input_part_id, v_input_description, v_input_quantity;
            exit WHEN v_inputs%notfound;
            dbms_output.put_line('  - ' || v_input_part_id || ' (' || v_input_description || '): ' || v_input_quantity);
        END loop;
    END loop;

    CLOSE v_cursor;
END;
/