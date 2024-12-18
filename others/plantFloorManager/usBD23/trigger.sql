create or replace trigger trg_check_execution_time
before insert or update on Operation_Type_Workstation
for each row
declare
    max_exec_time_exceeded exception;
    v_expected_time number;
begin
    -- The complexity is O(1).
    select Expec_Time
    into v_expected_time
    from Operation_Type
    where ID = :new.Operation_TypeID;

    if v_expected_time > :new.Max_Exec_Time then
        raise max_exec_time_exceeded;
    end if;

exception
    when max_exec_time_exceeded then
        RAISE_APPLICATION_ERROR(-20001, 'Expected execution time ' || v_expected_time || ' exceeds maximum execution time ' || :new.Max_Exec_Time || ' for workstation type ' || :new.WorkstationType_ID);
end;
/