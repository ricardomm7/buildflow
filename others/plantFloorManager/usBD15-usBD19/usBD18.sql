--USBD18
CREATE OR REPLACE FUNCTION DeactivateCustomer(p_CustomerVAT IN Costumer.VAT%TYPE)
    RETURN VARCHAR2
IS
    v_ActiveOrders NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO v_ActiveOrders
    FROM "Order"
    WHERE CostumerVAT = p_CustomerVAT
      AND DeliveryDate > SYSDATE; -- Considera pedidos não entregues

    IF v_ActiveOrders > 0 THEN
        RETURN 'Error: Customer has active orders and cannot be deactivated.';
    END IF;

    DELETE FROM Costumer
    WHERE VAT = p_CustomerVAT;

    RETURN 'Success: Customer deactivated.';
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'Error: Customer not found.';
    WHEN OTHERS THEN
        RETURN 'Error: An unexpected error occurred.';
END;
/

-- FOR TESTINGGG
BEGIN
    DBMS_OUTPUT.PUT_LINE(DeactivateCustomer('123456789')); -- Substituir por um VAT válido.
END;
/

