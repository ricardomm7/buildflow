CREATE OR REPLACE PROCEDURE Reserve_Order_Components(p_order_id IN "Order".Order_ID%TYPE)
IS
    CURSOR c_order_products IS
        SELECT ol.Product_ID, ol.Quantity AS Order_Quantity
        FROM Order_Line ol
        WHERE ol.Order_ID = p_order_id;

    cur_components SYS_REFCURSOR;
    v_part_id External_Part.Part_ID%TYPE;
    v_required_qty NUMBER;
    v_available_qty NUMBER;
    v_can_fulfill BOOLEAN := TRUE;

BEGIN
    -- Chamar a USBD26 para verificar se o pedido pode ser cumprido
    v_can_fulfill := CheckOrderStockAvailability(p_order_id);

    IF NOT v_can_fulfill THEN
        RAISE_APPLICATION_ERROR(-20001, 'Order cannot be fulfilled: insufficient stock.');
    END IF;

    -- Iterar pelos produtos da ordem
    FOR prod IN c_order_products LOOP
        -- Obter componentes do produto usando GetProductOperationParts
        cur_components := GetProductOperationParts(prod.Product_ID);

        LOOP
            FETCH cur_components INTO v_part_id, v_required_qty;
            EXIT WHEN cur_components%NOTFOUND;

            v_required_qty := v_required_qty * prod.Order_Quantity;

            INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity) VALUES (prod.Product_ID, p_order_id, v_part_id, v_required_qty);
        END LOOP;

        CLOSE cur_components;
    END LOOP;

    -- Confirmar transação
    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Reservation successfully created for the order: ' || p_order_id);

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error processing reservation: ' || SQLERRM);
        RAISE;
END;
/


BEGIN
    Reserve_Order_Components('ORD002');
END;
/