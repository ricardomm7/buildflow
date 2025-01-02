CREATE OR REPLACE FUNCTION WorkstationsNotUsed
RETURN SYS_REFCURSOR IS
  result_cursor SYS_REFCURSOR;
BEGIN

  OPEN result_cursor FOR
    SELECT tw.WorkstationType_ID, tw.Designation
    FROM Type_Workstation tw
    WHERE NOT EXISTS (
      SELECT 1
      FROM Operation_Type_Workstation otw
      WHERE otw.WorkstationType_ID = tw.WorkstationType_ID
      AND otw.Operation_TypeID IN (

        SELECT DISTINCT o.Operation_TypeID
        FROM Operation o
        CONNECT BY PRIOR o.NextOperation_ID = o.Operation_ID
        START WITH o.NextOperation_ID IS NOT NULL
      )
    );


  RETURN result_cursor;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RETURN NULL;
  WHEN OTHERS THEN
    RETURN NULL;
END;
/

DECLARE
  v_cursor SYS_REFCURSOR;
  v_WorkstationType_ID Type_Workstation.WorkstationType_ID%TYPE;
  v_Designation Type_Workstation.Designation%TYPE;
BEGIN

  v_cursor := WorkstationsNotUsed;


  LOOP
    FETCH v_cursor INTO v_WorkstationType_ID, v_Designation;
    EXIT WHEN v_cursor%NOTFOUND;
    DBMS_OUTPUT.PUT_LINE('WorkstationType_ID: ' || v_WorkstationType_ID || ', Designation: ' || v_Designation);
  END LOOP;


  CLOSE v_cursor;
END;
/


