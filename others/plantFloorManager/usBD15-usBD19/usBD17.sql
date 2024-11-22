CREATE OR REPLACE FUNCTION REGISTER_ORDER (
    p_order_date    IN DATE,
    p_delivery_date IN DATE,
    p_customer_vat  IN VARCHAR2,
    p_product_id    IN CHAR
) RETURN VARCHAR2 IS
    v_customer_exists NUMBER;
    v_product_exists NUMBER;
    v_line_exists    NUMBER;
    v_new_order_id   NUMBER;
BEGIN
    -- Validate Delivery Date
    IF p_delivery_date < p_order_date THEN
        RETURN 'Error: Delivery date cannot be before the order date.';
    END IF;

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
        RETURN 'Error: Product with Part_ID ' || p_product_id || ' does not exist in the current catalog.';
    END IF;

    -- Generate new Order_ID
    SELECT NVL(MAX(TO_NUMBER(Order_ID)), 0) + 1
    INTO v_new_order_id
    FROM "Order";

    -- Savepoint before inserting order
    SAVEPOINT before_order_insert;

    -- Insert Order
    INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
    VALUES (TO_CHAR(v_new_order_id), p_order_date, p_delivery_date, p_customer_vat);

    -- Validate if Product is already in Production_Line for this Order
    SELECT COUNT(*)
    INTO v_line_exists
    FROM Production_Line
    WHERE Product_ID = p_product_id AND Order_ID = TO_CHAR(v_new_order_id);

    IF v_line_exists > 0 THEN
        -- Rollback to savepoint and return error
        ROLLBACK TO before_order_insert;
        RETURN 'Error: Product with Part_ID ' || p_product_id ||
               ' is already associated with Order_ID ' || TO_CHAR(v_new_order_id) || '.';
    ELSE
        -- Insert Product into Production_Line
        INSERT INTO Production_Line (Product_ID, Order_ID, quantity)
        VALUES (p_product_id, TO_CHAR(v_new_order_id), 1);
    END IF;

    -- Commit transaction to persist changes
    COMMIT;

    -- Return Success Message
    RETURN 'Order ' || TO_CHAR(v_new_order_id) || ' successfully registered.';
EXCEPTION
    WHEN OTHERS THEN
        -- Rollback the entire transaction if any error occurs
        ROLLBACK;
        RETURN 'Error: ' || SQLERRM;
END REGISTER_ORDER;
/


DECLARE
    v_result VARCHAR2(255);
BEGIN
    v_result := REGISTER_ORDER(
        p_order_date    => TO_DATE('2024-11-21', 'YYYY-MM-DD'),
        p_delivery_date => TO_DATE('2024-11-28', 'YYYY-MM-DD'),
        p_customer_vat  => '123456789',
        p_product_id    => 'AS12945S22'
    );
    DBMS_OUTPUT.PUT_LINE(v_result);
END;
/