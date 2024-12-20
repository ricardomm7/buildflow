CREATE OR REPLACE PROCEDURE consume_material(
    p_part_id IN CHAR,
    p_quantity IN NUMBER,
    p_success OUT BOOLEAN,
    p_message OUT VARCHAR2
) IS
    v_current_stock NUMBER := 0;
    v_total_reserved NUMBER := 0;
BEGIN
    p_success := FALSE; -- Inicializa o sucesso como falso

    -- Valida os parâmetros de entrada
    IF p_quantity <= 0 THEN
        p_message := 'Quantity to consume must be greater than 0.';
        RETURN;
    END IF;

    IF p_part_id IS NULL OR TRIM(p_part_id) = '' THEN
        p_message := 'Part ID cannot be null or empty.';
        RETURN;
    END IF;

    -- Obtém o stock atual e total reservado em uma única consulta
    BEGIN
        SELECT ep.Minimum_Stock, SUM(r.quantity)
        INTO v_current_stock, v_total_reserved
        FROM External_Part ep
        LEFT JOIN Reservation r ON r.Part_ID = ep.Part_ID
        WHERE ep.Part_ID = TRIM(p_part_id)
        GROUP BY ep.Part_ID, ep.Minimum_Stock;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            p_message := 'Part not found: ' || TRIM(p_part_id);
            RETURN;
    END;

    -- Trata o caso de SUM retornando NULL (nenhuma reserva encontrada)
    IF v_total_reserved IS NULL THEN
        v_total_reserved := 0;
    END IF;

    -- Verifica se o consumo é possível
    IF p_quantity > v_current_stock THEN
        p_message := 'Cannot consume material: requested quantity exceeds current stock. ' ||
                     'Current stock: ' || v_current_stock ||
                     ', Requested: ' || p_quantity;
        RETURN;
    ELSIF (v_current_stock - p_quantity) < v_total_reserved THEN
        p_message := 'Cannot consume material: would fall below reserved quantity. ' ||
                     'Current stock: ' || v_current_stock ||
                     ', Reserved: ' || v_total_reserved ||
                     ', Requested: ' || p_quantity;
        RETURN;
    END IF;

    -- Atualiza o estoque
    UPDATE External_Part
    SET Minimum_Stock = Minimum_Stock - p_quantity
    WHERE Part_ID = TRIM(p_part_id);

    COMMIT; -- Confirma a transação

    p_success := TRUE;
    p_message := 'Material consumed successfully.';
EXCEPTION
    WHEN OTHERS THEN
        p_message := 'Error: ' || SQLERRM;
        ROLLBACK; -- Reverte a transação em caso de erro
END;
/

-- Teste 1: Válido, consome menos que o stock e reservado
DECLARE
    v_success BOOLEAN;
    v_message VARCHAR2(200);
BEGIN
    consume_material('PN18544C21', 5, v_success, v_message);
    IF v_success THEN
        DBMS_OUTPUT.PUT_LINE('Success: ' || v_message);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Failed: ' || v_message);
    END IF;
END;
/

-- Teste 2: Inválido, consome maior que o reservado
DECLARE
    v_success BOOLEAN;
    v_message VARCHAR2(200);
BEGIN
    consume_material('PN18544C21', 85, v_success, v_message);
    IF v_success THEN
        DBMS_OUTPUT.PUT_LINE('Success: ' || v_message);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Failed: ' || v_message);
    END IF;
END;
/

-- Teste 3: Inválido, consome maior que o stock
DECLARE
    v_success BOOLEAN;
    v_message VARCHAR2(200);
BEGIN
    consume_material('PN18324C54', 155, v_success, v_message);
    IF v_success THEN
        DBMS_OUTPUT.PUT_LINE('Success: ' || v_message);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Failed: ' || v_message);
    END IF;
END;
/