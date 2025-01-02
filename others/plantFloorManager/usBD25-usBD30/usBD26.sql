--usbd26
CREATE OR REPLACE FUNCTION CheckOrderStockAvailability(p_OrderID IN VARCHAR2) RETURN BOOLEAN IS
    v_ProductID Order_Line.Product_ID%TYPE;
    v_QuantityRequired NUMBER;
    v_PartID External_Part.Part_ID%TYPE;
    v_QuantityAvailable NUMBER;
    v_Parts SYS_REFCURSOR;

    v_StockAvailable BOOLEAN := TRUE;
BEGIN
    FOR product IN (
        SELECT Product_ID, Quantity
        FROM Order_Line
        WHERE Order_ID = p_OrderID
    ) LOOP
        v_ProductID := product.Product_ID;

        v_Parts:=GetProductOperationParts(v_ProductID);

        LOOP
            FETCH v_Parts INTO v_PartID, v_QuantityRequired;
            EXIT WHEN v_Parts%NOTFOUND;

            SELECT Stock
            INTO v_QuantityAvailable
            FROM External_Part
            WHERE Part_ID = v_PartID;

            IF v_QuantityAvailable < (v_QuantityRequired * product.Quantity) THEN
                v_StockAvailable := FALSE;
                EXIT;
            END IF;
        END LOOP;

        CLOSE v_Parts;

        IF NOT v_StockAvailable THEN
            EXIT;
        END IF;
    END LOOP;

    RETURN v_StockAvailable;
END CheckOrderStockAvailability;
/


--sucesso
DECLARE
    v_OrderID VARCHAR2(255) := 'ORD001';
    v_Result BOOLEAN;
BEGIN
    v_Result := CheckOrderStockAvailability(v_OrderID);

    IF v_Result THEN
        DBMS_OUTPUT.PUT_LINE('Sufficient stock for order: ' || v_OrderID);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Insufficient stock for the given order: ' || v_OrderID);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/


--falha
DECLARE
    v_OrderID VARCHAR2(255) := 'ORD003';
    v_Result BOOLEAN;
BEGIN
    v_Result := CheckOrderStockAvailability(v_OrderID);

    IF v_Result THEN
        DBMS_OUTPUT.PUT_LINE('Sufficient stock for order: ' || v_OrderID);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Insufficient stock for the given order: ' || v_OrderID);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

