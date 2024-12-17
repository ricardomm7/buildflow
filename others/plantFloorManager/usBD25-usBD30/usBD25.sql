CREATE OR REPLACE FUNCTION Get_Product_Operations(product_id_in IN VARCHAR2)
RETURN SYS_REFCURSOR
IS
  operations_cursor SYS_REFCURSOR;
BEGIN
  OPEN operations_cursor FOR
    WITH RECURSIVE_OPERATION (
      Operation_ID,
      Designation,
      Expected_Time,
      Product_ID,
      Output_Part_ID,
      NextOperation_ID,
      Path
    ) AS (
      -- Base case: Start with the product's operations
      SELECT
        o.Operation_ID,
        o.Designation,
        o.Expected_Time,
        o.Product_ID,
        o.Output_Part_ID,
        o.NextOperation_ID,
        CAST(o.Operation_ID AS VARCHAR2(4000)) AS Path
      FROM Operation o
      WHERE o.Product_ID = product_id_in

      UNION ALL

      -- Recursive case: Include operations of sub-products
      SELECT
        o.Operation_ID,
        o.Designation,
        o.Expected_Time,
        o.Product_ID,
        o.Output_Part_ID,
        o.NextOperation_ID,
        ro.Path || '->' || o.Operation_ID AS Path
      FROM Operation o
      JOIN RECURSIVE_OPERATION ro
      ON o.Product_ID = ro.Output_Part_ID
      WHERE INSTR(ro.Path, o.Operation_ID) = 0 -- Prevent cycles by ensuring the path doesn't revisit an operation
    )
    SELECT
      ro.Operation_ID,
      ro.Designation,
      ro.Expected_Time,
      ro.Product_ID,
      ro.Output_Part_ID,
      i.Part_ID AS Input_Part_ID,
      i.Quantity AS Input_Quantity
    FROM RECURSIVE_OPERATION ro
    LEFT JOIN Operation_Input i ON ro.Operation_ID = i.Operation_ID
    ORDER BY ro.Operation_ID;

  RETURN operations_cursor;
END;


DECLARE
  operations SYS_REFCURSOR;
  operation_id NUMBER;
  designation VARCHAR2(100);
  expected_time NUMBER;
  product_id VARCHAR2(10);
  output_part_id VARCHAR2(10);
  input_part_id VARCHAR2(10);
  input_quantity NUMBER;
BEGIN
  -- Call the function
  operations := Get_Product_Operations('AS12947S22');

  -- Loop through the cursor results
  LOOP
    FETCH operations INTO operation_id, designation, expected_time, product_id, output_part_id, input_part_id, input_quantity;
    EXIT WHEN operations%NOTFOUND;

    -- Print each row
    DBMS_OUTPUT.PUT_LINE(
      'Operation ID: ' || operation_id || ', Designation: ' || designation ||
      ', Time: ' || expected_time || ', Product: ' || product_id ||
      ', Output: ' || output_part_id || ', Input: ' || NVL(input_part_id, 'N/A') ||
      ', Quantity: ' || NVL(input_quantity, 0)
    );
  END LOOP;

  -- Close the cursor
  CLOSE operations;
END;
/
