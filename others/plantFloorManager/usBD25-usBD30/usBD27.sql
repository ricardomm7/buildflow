CREATE OR REPLACE PROCEDURE Reserve_Order_Components (
    p_order_id IN "Order".Order_ID%TYPE,
    p_product_id IN Product.Part_ID%TYPE
) IS
    CURSOR c_components IS
        SELECT oi.Part_ID, oi.Quantity * ol.quantity AS Required_Quantity
        FROM Operation_Input oi
        JOIN Operation op ON oi.Operation_ID = op.Operation_ID
        JOIN Order_Line ol ON ol.Product_ID = op.Product_ID
        WHERE ol.Order_ID = p_order_id AND op.Product_ID = p_product_id;

    v_part_id External_Part.Part_ID%TYPE;
    v_required_qty NUMBER;
    v_available_qty NUMBER;
    v_total_qty_needed NUMBER := 0;
    v_can_fulfill BOOLEAN := TRUE;

BEGIN
    -- Verifica disponibilidade de cada componente
    FOR comp IN c_components LOOP
        SELECT ep.Minimum_Stock INTO v_available_qty
        FROM External_Part ep
        WHERE ep.Part_ID = comp.Part_ID;

        -- Se faltar stock para qualquer componente, não pode cumprir o pedido
        IF v_available_qty < comp.Required_Quantity THEN
            v_can_fulfill := FALSE;
            EXIT; -- Saia do loop, não é necessário verificar mais componentes
        END IF;
    END LOOP;

    -- Se não puder cumprir o pedido, encerra com uma exceção
    IF NOT v_can_fulfill THEN
        RAISE_APPLICATION_ERROR(-20001, 'Pedido não pode ser cumprido: estoque insuficiente.');
    END IF;

    -- Realiza as reservas, uma vez que todos os componentes estão disponíveis
    FOR comp IN c_components LOOP
        INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
        VALUES (p_product_id, p_order_id, comp.Part_ID, comp.Required_Quantity);
    END LOOP;

    -- Confirma a transação
    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Reserva criada com sucesso para o pedido: ' || p_order_id);

EXCEPTION
    WHEN OTHERS THEN
        -- Em caso de erro, reverte as alterações
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Erro ao processar reserva: ' || SQLERRM);
        RAISE;
END;
/


BEGIN
    Reserve_Order_Components('ORD001', 'PROD01');
END;
/

select * from Reservation
