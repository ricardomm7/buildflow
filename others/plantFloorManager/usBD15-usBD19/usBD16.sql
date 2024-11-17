create or replace FUNCTION RegisterProduct (
    p_Part_ID   char,
    p_Name      varchar2
) return varchar2
as
    v_message varchar2(100);
begin
    -- Attempt to insert the new product
    insert into Product (Part_ID, Name)
    values (p_Part_ID, p_Name);

    -- If successful, return a success message
    v_message := 'Product ' || p_Part_ID || ' successfully registered.';
    return v_message;

exception
    when DUP_VAL_ON_INDEX then
        -- Handle case where the primary key constraint is violated (duplicate Part_ID)
        v_message := 'Error: Product Part_ID ' || p_Part_ID || ' already exists.';
        return v_message;
    when others then
        -- Handle any other errors that may occur
        v_message := 'Error registering product: ' || sqlerrm;
        return v_message;
end RegisterProduct;
/


