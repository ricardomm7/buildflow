create or replace trigger trg_prevent_circular_boo
before insert or update on Operation_Input
for each row
declare
    circular_reference exception;
    parent_product_id char(10);
begin
    -- The complexity is O(1).
    select Product_ID
    into parent_product_id
    from Operation
    where Operation_ID = :new.Operation_ID;

    if :new.Part_ID = parent_product_id then
        raise circular_reference;
    end if;

exception
    when circular_reference then
        RAISE_APPLICATION_ERROR(-20002, 'Circular reference detected in ' || parent_product_id || ': A product cannot be used as an input in its own Bill of Operations (BOO).');
end;
/