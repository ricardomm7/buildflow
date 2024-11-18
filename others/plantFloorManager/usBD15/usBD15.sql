
CREATE FUNCTION RegisterWorkstation(
    var_workstation_id VARCHAR(255)
    var_name VARCHAR(50),
    var_description VARCHAR(255),
    var_type_workstationWorkstation_type VARCHAR(100)
)
RETURNS VARCHAR(255)

BEGIN
    DECLARE result_message VARCHAR(255);
    DECLARE workstation_exists INT;

    -- verifica se a workstation existe na tabela
    SELECT COUNT(*)
    INTO workstation_exists
    FROM Type_Workstation
    WHERE Workstation_ID = workstation_id;

    -- se já exister retorna error
    IF EXISTS (SELECT 1 FROM Workstation WHERE Workstation_ID = var_workstation_id) THEN
        SET result_message = 'Error: Workstation id already exists.';
    ELSE
        -- insere a workstation se se houver algum problema retorna um erro
        BEGIN
            INSERT INTO Workstation (Workstation_ID,Name, Description, Type_WorkstationWorkstationType_ID)
            VALUES (var_workstation_id,var_name,var_description,var_type_workstationWorkstation_type);
            SET result_message = 'Success: Workstation registered successfully.';
        EXCEPTION
            WHEN OTHERS THEN
                SET result_message = 'Error: Could not register workstation due to an unknown error.';
        END;
    END IF;
    RETURN result_message;

END RegisterWorkstation;

