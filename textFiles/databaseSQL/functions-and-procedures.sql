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

        IF v_UsedWorkstationTypes = totalWorkstationTypes THEN
            DBMS_OUTPUT.PUT_LINE('Product ID: ' || v_Product_ID || ', Name: ' || v_Product_Name);
        END IF;
    END LOOP;

    CLOSE productCursor;
END;
/

-- USBD17
CREATE OR REPLACE FUNCTION REGISTER_ORDER (
    p_order_id      IN VARCHAR2,
    p_order_date    IN DATE,
    p_delivery_date IN DATE,
    p_customer_vat  IN VARCHAR2,
    p_product_id    IN CHAR
) RETURN VARCHAR2 IS
    v_customer_exists NUMBER;
    v_product_exists NUMBER;
    v_order_exists   NUMBER;
    v_line_exists    NUMBER;
BEGIN
    -- Validate Customer
    SELECT COUNT(*)
    INTO v_customer_exists
    FROM Costumer
    WHERE VAT = p_customer_vat;

    IF v_customer_exists = 0 THEN
        RETURN 'Error: Customer with VAT ' || p_customer_vat || ' does not exist.';
    END IF;

    -- Validate Product
    SELECT COUNT(*)
    INTO v_product_exists
    FROM Product
    WHERE Part_ID = p_product_id;

    IF v_product_exists = 0 THEN
        RETURN 'Error: Product with Part_ID ' || p_product_id || ' does not exist in the current lineup.';
    END IF;

    -- Check if Order ID already exists
    SELECT COUNT(*)
    INTO v_order_exists
    FROM "Order"
    WHERE Order_ID = p_order_id;

    IF v_order_exists > 0 THEN
        RETURN 'Error: Order with Order_ID ' || p_order_id || ' already exists.';
    END IF;

    -- Insert Order
    INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
    VALUES (p_order_id, p_order_date, p_delivery_date, p_customer_vat);

    -- Check if the Product is already in the Production_Line for this Order
    SELECT COUNT(*)
    INTO v_line_exists
    FROM Production_Line
    WHERE Product_ID = p_product_id AND Order_ID = p_order_id;

    IF v_line_exists > 0 THEN
        RETURN 'Error: Product with Part_ID ' || p_product_id ||
               ' is already associated with Order_ID ' || p_order_id || '.';
    ELSE
        -- Insert Product into Production_Line
        INSERT INTO Production_Line (Product_ID, Order_ID, quantity)
        VALUES (p_product_id, p_order_id, 1);
    END IF;

    -- Return Success Message
    RETURN 'Order ' || p_order_id || ' successfully registered.';
END REGISTER_ORDER;
/


-- USBD18
CREATE OR REPLACE FUNCTION DeactivateCustomer(p_CustomerVAT IN Costumer.VAT%TYPE)
    RETURN VARCHAR2
IS
    v_ActiveOrders NUMBER := 0;
    v_TotalOrders NUMBER := 0;
    v_CustomerExists NUMBER := 0;
BEGIN
    SELECT COUNT(*)
    INTO v_CustomerExists
    FROM Costumer
    WHERE VAT = p_CustomerVAT;

    IF v_CustomerExists = 0 THEN
        RETURN 'Error: Customer not found.';
    END IF;

    SELECT COUNT(*)
    INTO v_ActiveOrders
    FROM "Order"
    WHERE CostumerVAT = p_CustomerVAT
      AND DeliveryDate > SYSDATE;

    IF v_ActiveOrders > 0 THEN
        RETURN 'Error: Customer has active orders and cannot be deactivated.';
    END IF;

    SELECT COUNT(*)
    INTO v_TotalOrders
    FROM "Order"
    WHERE CostumerVAT = p_CustomerVAT;

    IF v_TotalOrders > 0 THEN
        RETURN 'Error: Customer cannot be deactivated due to existing orders.';
    END IF;

    DELETE FROM Costumer
    WHERE VAT = p_CustomerVAT;

    RETURN 'Success: Customer deactivated.';
END;
/
