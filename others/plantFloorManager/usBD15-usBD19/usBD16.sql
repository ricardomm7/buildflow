create or replace function RegisterProduct (
    p_Part_ID   char,
    p_Name      varchar2,
    p_Family_ID varchar2
) return varchar2
as
    v_message       varchar2(200);
    v_part_exists   integer;
    v_family_exists integer;
    v_product_exists integer;
begin
    -- Verificar se a família existe
    select count(*)
    into v_family_exists
    from Product_Family
    where Family_ID = p_Family_ID;

    if v_family_exists = 0 then
        return 'Error: Product_Family with ID ' || p_Family_ID || ' does not exist.';
    end if;

    -- Verificar se o Part já existe
    select count(*)
    into v_part_exists
    from Part
    where Part_ID = p_Part_ID;

    -- Criar Part se não existir
    if v_part_exists = 0 then
        insert into Part (Part_ID, Description)
        values (p_Part_ID, p_Name);

        v_message := 'New Part ' || p_Part_ID || ' created. ';
    else
        v_message := '';
    end if;

    -- Verificar se o Produto já existe
    select count(*)
    into v_product_exists
    from Product
    where Part_ID = p_Part_ID;

    if v_product_exists > 0 then
        return 'Error: Product with Part_ID ' || p_Part_ID || ' already exists.';
    end if;

    -- Inserir o Produto
    insert into Product (Part_ID, Name, Product_FamilyFamily_ID)
    values (p_Part_ID, p_Name, p_Family_ID);

    v_message := v_message || 'Product ' || p_Part_ID || ' registered successfully.';
    return v_message;

exception
    when others then
        return 'Error: Failed to register the product: ' || sqlerrm;
end RegisterProduct;
/
