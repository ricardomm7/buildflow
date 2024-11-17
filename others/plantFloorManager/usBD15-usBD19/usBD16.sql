CREATE OR REPLACE FUNCTION RegisterProduct (
    p_Part_ID   CHAR,       -- Adjust length to match your table definition, e.g., CHAR(10)
    p_Name      VARCHAR2
) RETURN VARCHAR2
AS
    v_message VARCHAR2(200);
    v_part_exists INTEGER;
BEGIN
    -- Check if the Part_ID exists in the Part table
    SELECT COUNT(*)
    INTO v_part_exists
    FROM Part
    WHERE Part_ID = p_Part_ID;

    IF v_part_exists = 0 THEN
        -- Part does not exist, create a new Part
        INSERT INTO Part (Part_ID, Description)
        VALUES (p_Part_ID, P_NAME);

        -- Notify that a new Part was created
        v_message := 'New Part ' || p_Part_ID || ' created. ';
    ELSE
        -- No need to create a new Part
        v_message := '';
    END IF;

    -- Attempt to insert the new Product
    INSERT INTO Product (Part_ID, Name)
    VALUES (p_Part_ID, p_Name);

    -- Append success message for Product registration
    v_message := v_message || 'Product ' || p_Part_ID || ' successfully registered.';
    RETURN v_message;

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        -- Handle case where the primary key constraint is violated (duplicate Part_ID in Product)
        v_message := 'Error: Product Part_ID ' || p_Part_ID || ' already exists.';
        RETURN v_message;
    WHEN OTHERS THEN
        -- Handle any other errors that may occur
        v_message := 'Error registering product: ' || SQLERRM;
        RETURN v_message;
END RegisterProduct;
/
