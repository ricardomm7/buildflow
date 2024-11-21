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

---Para Testarrrrrrrr
DECLARE
    v_result VARCHAR2(255);
BEGIN
    v_result := REGISTER_ORDER(
        p_order_id      => 'ORD00159',
        p_order_date    => SYSDATE,
        p_delivery_date => SYSDATE + 7,
        p_customer_vat  => 'PT501242417',
        p_product_id    => 'AS12945S22'
    );
    DBMS_OUTPUT.PUT_LINE(v_result);
END;
/

SELECT * from "Order"



