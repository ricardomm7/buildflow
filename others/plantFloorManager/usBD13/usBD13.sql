--USBD13
CREATE OR REPLACE FUNCTION GetOperationsAndWorkstations (
    p_Product_ID IN Product.Product_ID%TYPE
) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT
        BOO.Product_FamilyFamily_ID AS Family_ID,
        BOO.BOOOperation_Sequence AS Operation_Sequence,
        O.Designation AS Operation_Designation,
        TWS.WorkstationType_ID AS Workstation_Type,
        TW.Designation AS Workstation_Type_Description
    FROM
        Product P
    INNER JOIN
        Product_Family PF ON P.Product_FamilyFamily_ID = PF.Family_ID
    INNER JOIN
        BOO_Operation BOO ON PF.Family_ID = BOO.Product_FamilyFamily_ID
    INNER JOIN
        Operation O ON BOO.OperationOperation_ID = O.Operation_ID
    INNER JOIN
        Operation_Type_Workstation TWS ON O.Operation_ID = TWS.OperationOperation_ID
    INNER JOIN
        Type_Workstation TW ON TWS.Type_WorkstationWorkstationType_ID = TW.WorkstationType_ID
    WHERE
        P.Product_ID = p_Product_ID
    ORDER BY
        BOO.BOOOperation_Sequence;

    RETURN v_cursor;
END;
/



--Para teste
DECLARE
    v_result SYS_REFCURSOR;
    v_Family_ID VARCHAR2(60);
    v_Operation_Sequence NUMBER;
    v_Operation_Designation VARCHAR2(60);
    v_Workstation_Type CHAR(5);
    v_Workstation_Type_Description VARCHAR2(60);
BEGIN
    v_result := GetOperationsAndWorkstations('AS12945S22'); -- Substitua 'P001' pelo ID do produto desejado

    LOOP
        FETCH v_result INTO v_Family_ID, v_Operation_Sequence, v_Operation_Designation, v_Workstation_Type, v_Workstation_Type_Description;
        EXIT WHEN v_result%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Family ID: ' || v_Family_ID ||
                             ', Sequence: ' || v_Operation_Sequence ||
                             ', Operation: ' || v_Operation_Designation ||
                             ', Workstation Type: ' || v_Workstation_Type ||
                             ', Workstation Description: ' || v_Workstation_Type_Description);
    END LOOP;

    CLOSE v_result;
END;
/


-- ESTA FUNÇÃO ESTÁ A RETOENAR SEQUENCIAS REPETIDAS DE FORMA A MOSTRAR TODOS OS WORKSTATION TYPES. MUDAR ISSO. FALTA TAMBÉM ESTA PARTE DOS AC:
-- When a part is a subproduct made at the factory, its list of operations should be included. For each operation, the inputs and outputs should be included.
