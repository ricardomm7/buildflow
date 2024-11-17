CREATE OR REPLACE FUNCTION RegisterProduct (
    p_Part_ID   CHAR,
    p_Name      VARCHAR2,
    p_Family_ID VARCHAR2

) RETURN VARCHAR2
AS
    v_message VARCHAR2(200);
    v_part_exists INTEGER;
    v_family_exists INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO v_family_exists
    FROM Product_Family
    WHERE Family_ID = p_Family_ID;

    IF v_family_exists = 0 THEN
        RETURN 'Erro: Product_Family com ID ' || p_Family_ID || ' não existe.';
    END IF;

    SELECT COUNT(*)
    INTO v_part_exists
    FROM Part
    WHERE Part_ID = p_Part_ID;

    IF v_part_exists = 0 THEN
        INSERT INTO Part (Part_ID, Description)
        VALUES (p_Part_ID, p_Name);

        v_message := 'Novo Part ' || p_Part_ID || ' criado. ';
    ELSE
        v_message := '';
    END IF;

    INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID)
    VALUES (p_Part_ID, p_Name, p_Family_ID);

    v_message := v_message || 'Produto ' || p_Part_ID || ' registrado com sucesso.';
    RETURN v_message;

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        v_message := 'Erro: Produto com Part_ID ' || p_Part_ID || ' já existe.';
        RETURN v_message;
    WHEN OTHERS THEN
        v_message := 'Erro ao registrar o produto: ' || SQLERRM;
        RETURN v_message;
END RegisterProduct;
/
