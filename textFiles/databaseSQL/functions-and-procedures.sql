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


-- USBD13
CREATE OR REPLACE FUNCTION GetProductOperationsAndWorkstations(
    p_Product_ID IN Product.Part_ID%TYPE
)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
    -- Main product operations
    SELECT
        o.Operation_ID,
        ot.Description AS Operation_Description,
        wt.WorkstationType_ID
    FROM
        Operation o
    JOIN
        Operation_Type ot ON o.Operation_TypeID = ot.ID
    JOIN
        Operation_Type_Workstation otw ON o.Operation_TypeID = otw.Operation_TypeID
    JOIN
        Type_Workstation wt ON otw.WorkstationType_ID = wt.WorkstationType_ID
    WHERE
        o.Product_ID = p_Product_ID

    UNION ALL

    -- Operations of subproducts found in Operation Inputs
    SELECT
        subo.Operation_ID,
        subot.Description AS Operation_Description,
        wt.WorkstationType_ID
    FROM
        Operation subo
    JOIN
        Operation_Type subot ON subo.Operation_TypeID = subot.ID
    JOIN
        Operation_Type_Workstation otw ON subo.Operation_TypeID = otw.Operation_TypeID
    JOIN
        Type_Workstation wt ON otw.WorkstationType_ID = wt.WorkstationType_ID
    WHERE
        subo.Product_ID IN (
            SELECT DISTINCT
                oi.Part_ID
            FROM
                Operation_Input oi
            JOIN
                Operation o ON oi.Operation_ID = o.Operation_ID
            JOIN
                Product p ON oi.Part_ID = p.Part_ID
            WHERE
                o.Product_ID = p_Product_ID
    );

    RETURN cur_results;
END;
/

--usbd12
create or replace function GetProductOperationParts(p_Product_ID in Product.Part_ID%type)
    return SYS_REFCURSOR
is
    cur_results SYS_REFCURSOR;
begin
    open cur_results for
        with RecursiveParts(part_id, quantity) as (
            select oi.Part_ID, oi.Quantity
            from Operation_Input oi
            join Operation op on oi.Operation_ID = op.Operation_ID
            where op.Product_ID = p_Product_ID

            union all

            select oi.Part_ID, oi.Quantity
            from RecursiveParts rp
            join Product p on rp.part_id = p.Part_ID
            join Operation op on p.Part_ID = op.Product_ID
            join Operation_Input oi on op.Operation_ID = oi.Operation_ID
        )

        select rp.part_id, sum(rp.quantity) as total_quantity
        from RecursiveParts rp
        where not exists (
            select 1 from Product p where p.Part_ID = rp.part_id
        )
        and not exists (
            select 1 from Intermediate_Product ip where ip.Part_ID = rp.part_id
        )
        group by rp.part_id;

    return cur_results;
end GetProductOperationParts;
/

--USBD14
CREATE OR REPLACE PROCEDURE PrintProductsUsingAllWorkstationTypes
IS
    totalWorkstationTypes NUMBER;
    CURSOR productCursor IS
        SELECT Part_ID, Name
        FROM Product;
    v_Product_ID Product.Part_ID%TYPE;
    v_Product_Name Product.Name%TYPE;
    v_UsedWorkstationTypes NUMBER;
BEGIN
    -- Get total number of workstation types
    SELECT COUNT(*)
    INTO totalWorkstationTypes
    FROM Type_Workstation;

    OPEN productCursor;

    LOOP
        FETCH productCursor INTO v_Product_ID, v_Product_Name;
        EXIT WHEN productCursor%NOTFOUND;

        -- Count distinct workstation types used by the product and its subproducts
        SELECT COUNT(DISTINCT otw.WorkstationType_ID)
        INTO v_UsedWorkstationTypes
        FROM (
            -- Main product operations
            SELECT o.Operation_TypeID
            FROM Operation o
            WHERE o.Product_ID = v_Product_ID

            UNION ALL

            -- Subproduct operations
            SELECT subo.Operation_TypeID
            FROM Operation subo
            JOIN Operation o ON o.Product_ID = v_Product_ID
            JOIN Operation_Input oi ON oi.Operation_ID = o.Operation_ID
            JOIN Product p ON p.Part_ID = oi.Part_ID
            WHERE subo.Product_ID = oi.Part_ID
        ) operations
        JOIN Operation_Type_Workstation otw ON operations.Operation_TypeID = otw.Operation_TypeID;

        -- Print products that use all workstation types
        IF v_UsedWorkstationTypes = totalWorkstationTypes THEN
            DBMS_OUTPUT.PUT_LINE('Product ID: ' || v_Product_ID || ', Name: ' || v_Product_Name);
        END IF;
    END LOOP;

    CLOSE productCursor;
EXCEPTION
    WHEN OTHERS THEN
        IF productCursor%ISOPEN THEN
            CLOSE productCursor;
        END IF;
        RAISE;
END;
/

-- USBD17
CREATE OR REPLACE FUNCTION REGISTER_ORDER (
    p_order_date    IN DATE,
    p_delivery_date IN DATE,
    p_customer_vat  IN VARCHAR2,
    p_product_id    IN CHAR,
    p_quantity      IN NUMBER DEFAULT 1
) RETURN VARCHAR2 IS
    v_customer_exists NUMBER;
    v_product_exists NUMBER;
    v_line_exists    NUMBER;
    v_new_order_id   VARCHAR2(255);
BEGIN
    -- Validate quantity
    IF p_quantity <= 0 THEN
        RETURN 'Error: Quantity must be greater than 0.';
    END IF;

    -- Validate Delivery Date
    IF p_delivery_date < p_order_date THEN
        RETURN 'Error: Delivery date cannot be before the order date.';
    END IF;

    -- Validate Customer
    SELECT COUNT(*)
    INTO v_customer_exists
    FROM Costumer
    WHERE VAT = p_customer_vat;

    IF v_customer_exists = 0 THEN
        RETURN 'Error: Customer with VAT ' || p_customer_vat || ' does not exist.';
    END IF;

    -- Validate Product
    SELECT COUNT(*)
    INTO v_product_exists
    FROM Product
    WHERE Part_ID = p_product_id;

    IF v_product_exists = 0 THEN
        RETURN 'Error: Product with Part_ID ' || p_product_id || ' does not exist in the current catalog.';
    END IF;

    -- Generate new Order_ID (using a different approach since Order_ID is VARCHAR2)
    SELECT TO_CHAR(NVL(MAX(TO_NUMBER(REGEXP_REPLACE(Order_ID, '[^0-9]', ''))), 0) + 1)
    INTO v_new_order_id
    FROM "Order";

    -- Savepoint before inserting order
    SAVEPOINT before_order_insert;

    -- Insert Order
    INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
    VALUES (v_new_order_id, p_order_date, p_delivery_date, p_customer_vat);

    -- Validate if Product is already in Order_Line for this Order
    SELECT COUNT(*)
    INTO v_line_exists
    FROM Order_Line
    WHERE Product_ID = p_product_id AND Order_ID = v_new_order_id;

    IF v_line_exists > 0 THEN
        -- Rollback to savepoint and return error
        ROLLBACK TO before_order_insert;
        RETURN 'Error: Product with Part_ID ' || p_product_id ||
               ' is already associated with Order_ID ' || v_new_order_id || '.';
    ELSE
        -- Insert Product into Order_Line
        INSERT INTO Order_Line (Product_ID, Order_ID, quantity)
        VALUES (p_product_id, v_new_order_id, p_quantity);

        -- Create initial reservations for the product
        INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity)
        SELECT
            p_product_id,
            v_new_order_id,
            ep.Part_ID,
            p_quantity
        FROM External_Part ep
        WHERE EXISTS (
            SELECT 1
            FROM Operation_Input oi
            JOIN Operation o ON oi.Operation_ID = o.Operation_ID
            WHERE o.Product_ID = p_product_id
            AND oi.Part_ID = ep.Part_ID
        );
    END IF;

    -- Commit transaction to persist changes
    COMMIT;

    -- Return Success Message
    RETURN 'Order ' || v_new_order_id || ' successfully registered.';
EXCEPTION
    WHEN OTHERS THEN
        -- Rollback the entire transaction if any error occurs
        ROLLBACK;
        RETURN 'Error: ' || SQLERRM;
END REGISTER_ORDER;
/


-- USBD18
CREATE OR REPLACE FUNCTION DeactivateCustomer(p_CustomerVAT IN Costumer.VAT%TYPE)
    RETURN VARCHAR2
IS
    v_ActiveOrders NUMBER := 0;
    v_TotalOrders NUMBER := 0;
    v_CustomerExists NUMBER := 0;
BEGIN
    SELECT COUNT(*)
    INTO v_CustomerExists
    FROM Costumer
    WHERE VAT = p_CustomerVAT;

    IF v_CustomerExists = 0 THEN
        RETURN 'Error: Customer not found.';
    END IF;

    SELECT COUNT(*)
    INTO v_ActiveOrders
    FROM "Order"
    WHERE CostumerVAT = p_CustomerVAT
      AND DeliveryDate > SYSDATE;

    IF v_ActiveOrders > 0 THEN
        RETURN 'Error: Customer has active orders and cannot be deactivated.';
    END IF;

    SELECT COUNT(*)
    INTO v_TotalOrders
    FROM "Order"
    WHERE CostumerVAT = p_CustomerVAT;

    IF v_TotalOrders > 0 THEN
        RETURN 'Error: Customer cannot be deactivated due to existing orders.';
    END IF;

    DELETE FROM Costumer
    WHERE VAT = p_CustomerVAT;

    RETURN 'Success: Customer deactivated.';
END;
/

--USBD16
CREATE OR REPLACE FUNCTION RegisterProduct (
    p_Part_ID   CHAR,
    p_Name      VARCHAR2,
    p_Family_ID VARCHAR2
) RETURN VARCHAR2
AS
    v_message        VARCHAR2(200);
    v_family_exists  INTEGER;
    v_part_exists    INTEGER;
    v_product_exists INTEGER;
BEGIN
    -- Check if the family exists
    SELECT COUNT(*)
    INTO v_family_exists
    FROM Product_Family
    WHERE Family_ID = p_Family_ID;

    IF v_family_exists = 0 THEN
        RETURN 'Error: Product_Family with ID ' || p_Family_ID || ' does not exist.';
    END IF;

    -- Check if the Part_ID already exists in Product_Type
    SELECT COUNT(*)
    INTO v_part_exists
    FROM Product_Type
    WHERE Part_ID = p_Part_ID;

    -- Create Product_Type if it doesn't exist
    IF v_part_exists = 0 THEN
        INSERT INTO Product_Type (Part_ID, Description)
        VALUES (p_Part_ID, p_Name);
        v_message := 'New Product_Type ' || p_Part_ID || ' created. ';
    ELSE
        v_message := '';
    END IF;

    -- Check if the Product already exists
    SELECT COUNT(*)
    INTO v_product_exists
    FROM Product
    WHERE Part_ID = p_Part_ID;

    IF v_product_exists > 0 THEN
        RETURN 'Error: Product with Part_ID ' || p_Part_ID || ' already exists.';
    END IF;

    -- Insert the Product
    INSERT INTO Product (Part_ID, Name, Product_FamilyFamily_ID)
    VALUES (p_Part_ID, p_Name, p_Family_ID);

    v_message := v_message || 'Product ' || p_Part_ID || ' registered successfully.';

    RETURN v_message;

EXCEPTION
    WHEN OTHERS THEN
        -- Rollback if any error occurs
        ROLLBACK;
        RETURN 'Error: Failed to register the product: ' || SQLERRM;
END RegisterProduct;
/

-- USBD19
CREATE OR REPLACE FUNCTION ProductWithMostOperations
RETURN VARCHAR2 IS
    max_product_id Product.Part_ID%TYPE;
    max_product_name Product.Name%TYPE;
    operation_count NUMBER;
BEGIN
    SELECT p.Part_ID, p.Name, operation_count
    INTO max_product_id, max_product_name, operation_count
    FROM (
        SELECT
            o.Product_ID,
            COUNT(DISTINCT Operation_ID) as operation_count
        FROM Operation o
        GROUP BY o.Product_ID
        ORDER BY operation_count DESC
    ) seq
    JOIN Product p ON p.Part_ID = seq.Product_ID
    WHERE ROWNUM = 1;

    RETURN 'Product ID: ' || max_product_id ||
           ', Name: ' || max_product_name ||
           ', Number of Operations: ' || operation_count;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'No products with operations found.';
    WHEN OTHERS THEN
        RETURN 'Error: ' || SQLERRM;
END;
/

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

--usbd30
CREATE OR REPLACE PROCEDURE consume_material(
    p_part_id IN CHAR,
    p_quantity IN NUMBER,
    p_success OUT BOOLEAN,
    p_message OUT VARCHAR2
) IS
    v_current_stock NUMBER := 0;
    v_minimum_stock NUMBER := 0;
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

    -- Obtém o estoque atual, estoque mínimo e total reservado em uma única consulta
    BEGIN
        SELECT ep.Stock, ep.Minimum_Stock, NVL(SUM(r.quantity), 0)
        INTO v_current_stock, v_minimum_stock, v_total_reserved
        FROM External_Part ep
        LEFT JOIN Reservation r ON r.Part_ID = ep.Part_ID
        WHERE ep.Part_ID = TRIM(p_part_id)
        GROUP BY ep.Stock, ep.Minimum_Stock;
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
    IF (v_current_stock - p_quantity) < v_minimum_stock THEN
        p_message := 'Cannot consume material: would fall below minimum stock. ' ||
                     'Current stock: ' || v_current_stock ||
                     ', Minimum stock: ' || v_minimum_stock ||
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
    SET Stock = Stock - p_quantity
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



--usbd27
CREATE OR REPLACE PROCEDURE Reserve_Order_Components(p_order_id IN "Order".Order_ID%TYPE)
IS
    CURSOR c_order_products IS
        SELECT ol.Product_ID, ol.Quantity AS Order_Quantity
        FROM Order_Line ol
        WHERE ol.Order_ID = p_order_id;

    cur_components SYS_REFCURSOR;
    v_part_id External_Part.Part_ID%TYPE;
    v_required_qty NUMBER;
    v_available_qty NUMBER;
    v_can_fulfill BOOLEAN := TRUE;

BEGIN
    -- Chamar a USBD26 para verificar se o pedido pode ser cumprido
    --USBD26_CheckOrderFulfillment(p_order_id, v_can_fulfill);

    IF NOT v_can_fulfill THEN
        RAISE_APPLICATION_ERROR(-20001, 'Order cannot be fulfilled: insufficient stock.');
    END IF;

    -- Iterar pelos produtos da ordem
    FOR prod IN c_order_products LOOP
        -- Obter componentes do produto usando GetProductOperationParts
        cur_components := GetProductOperationParts(prod.Product_ID);

        LOOP
            FETCH cur_components INTO v_part_id, v_required_qty;
            EXIT WHEN cur_components%NOTFOUND;

            v_required_qty := v_required_qty * prod.Order_Quantity;

            INSERT INTO Reservation (Product_ID, Order_ID, Part_ID, quantity) VALUES (prod.Product_ID, p_order_id, v_part_id, v_required_qty);
        END LOOP;

        CLOSE cur_components;
    END LOOP;

    -- Confirmar transação
    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Reservation successfully created for the order: ' || p_order_id);

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error processing reservation: ' || SQLERRM);
        RAISE;
END;
/
