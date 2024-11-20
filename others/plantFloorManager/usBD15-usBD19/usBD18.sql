--USBD18
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

-- FOR TESTINGGG
BEGIN
    DBMS_OUTPUT.PUT_LINE(DeactivateCustomer('PT501242417')); -- Substituir por um VAT válido.
END;
/
