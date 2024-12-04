-- USBD15
CREATE OR REPLACE FUNCTION RegisterWorkstation(
    var_workstation_id IN VARCHAR2,
    var_name IN VARCHAR2,
    var_description IN VARCHAR2,
    var_Workstation_type IN VARCHAR2
) RETURN VARCHAR2
AS
    result_message VARCHAR2(255);
    workstation_exists INT;
BEGIN
    -- Verifica se a workstation já existe
    SELECT COUNT(*)
    INTO workstation_exists
    FROM Workstation
    WHERE Workstation_ID = var_workstation_id;

    -- Se já existir, retorna erro
    IF workstation_exists > 0 THEN
        result_message := 'Error: Workstation ID already exists.';
    ELSE
        -- Insere uma nova workstation
        INSERT INTO Workstation (Workstation_ID, Name, Description, WorkstationType_ID)
        VALUES (var_workstation_id, var_name, var_description, var_Workstation_type);

        result_message := 'Success: Workstation registered successfully.';
    END IF;

    RETURN result_message;
END;
/



DECLARE
    var_workstation_id VARCHAR2(50) := 'WS001';
    var_name VARCHAR2(255) := 'Main Workstation';
    var_description VARCHAR2(255) := 'Primary workstation for operations';
    var_workstation_type VARCHAR2(50) := 'Type1';
    result_message VARCHAR2(255);
BEGIN
    -- Chama a função RegisterWorkstation
    result_message := RegisterWorkstation(
        var_workstation_id => var_workstation_id,
        var_name => var_name,
        var_description => var_description,
        var_Workstation_type => var_workstation_type
    );

    -- Exibe a mensagem de resultado
    DBMS_OUTPUT.PUT_LINE(result_message);
EXCEPTION
    WHEN OTHERS THEN
        -- Captura e exibe qualquer erro que ocorra
        DBMS_OUTPUT.PUT_LINE('Erro: ' || SQLERRM);
END;
