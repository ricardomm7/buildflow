--USBD12
CREATE OR REPLACE FUNCTION GetProductParts(p_Product_ID IN CHAR)
RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT pp.PartPart_ID, pp.Quantity, p.Description
    FROM Product_Part pp
    JOIN Part p ON pp.PartPart_ID = p.Part_ID
    WHERE pp.ProductProduct_ID = p_Product_ID;
    RETURN v_cursor;
END GetProductParts;
/


--For testing
DECLARE
    v_parts SYS_REFCURSOR;
    v_part_id Part.Part_ID%TYPE;
    v_part_desc Part.Description%TYPE;
    v_part_quant Product_Part.Quantity%TYPE;
BEGIN
    v_parts := GetProductParts('AS12945S22');
    LOOP
        FETCH v_parts INTO v_part_id, v_part_quant, v_part_desc;
        EXIT WHEN v_parts%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE('Part ID: ' || v_part_id || ', Description: ' || v_part_desc || ', Quantity: ' || v_part_quant);
    END LOOP;
    CLOSE v_parts;
END;
/