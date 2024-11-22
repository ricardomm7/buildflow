-- USBD13
CREATE OR REPLACE FUNCTION GetProductOperationsAndWorkstations(
    p_Product_ID IN Product.Part_ID%TYPE
)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
    SELECT
        o.Operation_ID,
        o.Designation AS Operation_Designation,
        wt.WorkstationType_ID
    FROM
        Operation o
    JOIN
        Operation_Type_Workstation otw ON o.Operation_ID = otw.OperationOperation_ID
    JOIN
        Type_Workstation wt ON otw.WorkstationType_ID = wt.WorkstationType_ID
    WHERE
        o.Product_ID = p_Product_ID

    UNION ALL

    -- Operações dos subprodutos encontrados nas Operation Inputs
    SELECT
        subo.Operation_ID,
        subo.Designation AS Operation_Designation,
        wt.WorkstationType_ID
    FROM
        Operation subo
    JOIN
        Operation_Type_Workstation otw ON subo.Operation_ID = otw.OperationOperation_ID
    JOIN
        Type_Workstation wt ON otw.WorkstationType_ID = wt.WorkstationType_ID
    WHERE
        subo.Product_ID IN (
            SELECT DISTINCT
                oi.Part_ID
            FROM
                Operation_Input oi
            WHERE
                EXISTS (
                    SELECT 1
                    FROM Product p
                    WHERE p.Part_ID = oi.Part_ID
                )
                AND oi.Operation_ID IN (
                    SELECT o.Operation_ID
                    FROM Operation o
                    WHERE o.Product_ID = p_Product_ID
                )
        );

    RETURN cur_results;
END;
/


-- USBD12
CREATE OR REPLACE FUNCTION GetProductOperationParts(p_Product_ID IN Part.Part_ID%TYPE)
    RETURN SYS_REFCURSOR
IS
    cur_results SYS_REFCURSOR;
BEGIN
    OPEN cur_results FOR
    -- Partes associadas ao produto principal, excluindo Intermediate Products
    SELECT
        oi.Part_ID,
        SUM(oi.Quantity) AS Quantity
    FROM
        Operation_Input oi
        JOIN Operation o ON oi.Operation_ID = o.Operation_ID
    WHERE
        o.Product_ID = p_Product_ID
        AND NOT EXISTS (
            SELECT 1
            FROM Intermediate_Product ip
            WHERE ip.Part_ID = oi.Part_ID -- Exclui Intermediate Products
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Product p
            WHERE p.Part_ID = oi.Part_ID -- Exclui produtos com operações associadas
        )
    GROUP BY
        oi.Part_ID

    UNION ALL

    -- Partes de subprodutos encontrados nas partes do produto principal
    SELECT
        suboi.Part_ID,
        SUM(suboi.Quantity) AS Quantity
    FROM
        Operation_Input suboi
        JOIN Operation subo ON suboi.Operation_ID = subo.Operation_ID
    WHERE
        subo.Product_ID IN (
            SELECT DISTINCT oi.Part_ID
            FROM
                Operation_Input oi
                JOIN Operation o ON oi.Operation_ID = o.Operation_ID
            WHERE
                o.Product_ID = p_Product_ID
                AND EXISTS (
                    SELECT 1
                    FROM Product p
                    WHERE p.Part_ID = oi.Part_ID -- Apenas subprodutos que são produtos
                )
                AND NOT EXISTS (
                    SELECT 1
                    FROM Intermediate_Product ip
                    WHERE ip.Part_ID = oi.Part_ID -- Exclui Intermediate Products
                )
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Intermediate_Product ip
            WHERE ip.Part_ID = suboi.Part_ID -- Exclui Intermediate Products
        )
    GROUP BY
        suboi.Part_ID

    UNION ALL

    -- Produtos sem operações associadas, mas presentes como partes
    SELECT
        p.Part_ID,
        NULL AS Quantity
    FROM
        Product p
    WHERE
        p.Part_ID IN (
            SELECT oi.Part_ID
            FROM
                Operation_Input oi
                JOIN Operation o ON oi.Operation_ID = o.Operation_ID
            WHERE
                o.Product_ID = p_Product_ID
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Operation o
            WHERE o.Product_ID = p.Part_ID -- Exclui produtos com operações associadas
        )
        AND NOT EXISTS (
            SELECT 1
            FROM Intermediate_Product ip
            WHERE ip.Part_ID = p.Part_ID -- Exclui Intermediate Products
        );

    RETURN cur_results;
END;
/

--USBD14
CREATE OR REPLACE PROCEDURE PrintProductsUsingAllWorkstationTypes
IS
    totalWorkstationTypes NUMBER;
    CURSOR productCursor IS
        SELECT Part_ID, Name FROM Product;
    v_Product_ID Product.Part_ID%TYPE;
    v_Product_Name Product.Name%TYPE;
    v_UsedWorkstationTypes NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO totalWorkstationTypes
    FROM Type_Workstation;

    OPEN productCursor;

    LOOP
        FETCH productCursor INTO v_Product_ID, v_Product_Name;
        EXIT WHEN productCursor%NOTFOUND;

        SELECT COUNT(DISTINCT otw.WorkstationType_ID)
        INTO v_UsedWorkstationTypes
        FROM (
            SELECT o.Operation_ID
            FROM Operation o
            WHERE o.Product_ID = v_Product_ID

            UNION ALL

            SELECT subo.Operation_ID
            FROM Operation subo
            WHERE subo.Product_ID IN (
                SELECT DISTINCT oi.Part_ID
                FROM Operation_Input oi
                WHERE EXISTS (
                    SELECT 1
                    FROM Product p
                    WHERE p.Part_ID = oi.Part_ID
                )
                AND oi.Operation_ID IN (
                    SELECT o.Operation_ID
                    FROM Operation o
                    WHERE o.Product_ID = v_Product_ID
                )
            )
        ) operations
        JOIN Operation_Type_Workstation otw ON operations.Operation_ID = otw.OperationOperation_ID;

        IF v_UsedWorkstationTypes = totalWorkstationTypes THEN
            DBMS_OUTPUT.PUT_LINE('Product ID: ' || v_Product_ID || ', Name: ' || v_Product_Name);
        END IF;
    END LOOP;

    CLOSE productCursor;
END;
/

-- USBD17
CREATE OR REPLACE FUNCTION REGISTER_ORDER (
    p_order_date    IN DATE,
    p_delivery_date IN DATE,
    p_customer_vat  IN VARCHAR2,
    p_product_id    IN CHAR
) RETURN VARCHAR2 IS
    v_customer_exists NUMBER;
    v_product_exists NUMBER;
    v_line_exists    NUMBER;
    v_new_order_id   NUMBER;
BEGIN
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

    -- Generate new Order_ID
    SELECT NVL(MAX(TO_NUMBER(Order_ID)), 0) + 1
    INTO v_new_order_id
    FROM "Order";

    -- Savepoint before inserting order
    SAVEPOINT before_order_insert;

    -- Insert Order
    INSERT INTO "Order" (Order_ID, OrderDate, DeliveryDate, CostumerVAT)
    VALUES (TO_CHAR(v_new_order_id), p_order_date, p_delivery_date, p_customer_vat);

    -- Validate if Product is already in Production_Line for this Order
    SELECT COUNT(*)
    INTO v_line_exists
    FROM Production_Line
    WHERE Product_ID = p_product_id AND Order_ID = TO_CHAR(v_new_order_id);

    IF v_line_exists > 0 THEN
        -- Rollback to savepoint and return error
        ROLLBACK TO before_order_insert;
        RETURN 'Error: Product with Part_ID ' || p_product_id ||
               ' is already associated with Order_ID ' || TO_CHAR(v_new_order_id) || '.';
    ELSE
        -- Insert Product into Production_Line
        INSERT INTO Production_Line (Product_ID, Order_ID, quantity)
        VALUES (p_product_id, TO_CHAR(v_new_order_id), 1);
    END IF;

    -- Commit transaction to persist changes
    COMMIT;

    -- Return Success Message
    RETURN 'Order ' || TO_CHAR(v_new_order_id) || ' successfully registered.';
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
create or replace function RegisterProduct (
    p_Part_ID   char,
    p_Name      varchar2,
    p_Family_ID varchar2
) return varchar2
as
    v_message       varchar2(200);
    v_part_exists   integer;
    v_family_exists integer;
    v_product_exists integer;
begin
    -- Verificar se a família existe
    select count(*)
    into v_family_exists
    from Product_Family
    where Family_ID = p_Family_ID;

    if v_family_exists = 0 then
        return 'Error: Product_Family with ID ' || p_Family_ID || ' does not exist.';
    end if;

    -- Verificar se o Part já existe
    select count(*)
    into v_part_exists
    from Part
    where Part_ID = p_Part_ID;

    -- Criar Part se não existir
    if v_part_exists = 0 then
        insert into Part (Part_ID, Description)
        values (p_Part_ID, p_Name);

        v_message := 'New Part ' || p_Part_ID || ' created. ';
    else
        v_message := '';
    end if;

    -- Verificar se o Produto já existe
    select count(*)
    into v_product_exists
    from Product
    where Part_ID = p_Part_ID;

    if v_product_exists > 0 then
        return 'Error: Product with Part_ID ' || p_Part_ID || ' already exists.';
    end if;

    -- Inserir o Produto
    insert into Product (Part_ID, Name, Product_FamilyFamily_ID)
    values (p_Part_ID, p_Name, p_Family_ID);

    v_message := v_message || 'Product ' || p_Part_ID || ' registered successfully.';
    return v_message;

exception
    when others then
        return 'Error: Failed to register the product: ' || sqlerrm;
end RegisterProduct;
/

-- USBD19
CREATE OR REPLACE FUNCTION ProductWithMostOperations
RETURN VARCHAR2 IS
  max_product_id VARCHAR2(100);
BEGIN
  SELECT Product_ID
  INTO max_product_id
  FROM (
    SELECT Product_ID, MAX(sequencia) AS maior_sequencia
    FROM (
      SELECT Product_ID,
             Operation_ID AS StartOperation,
             LEVEL AS sequencia
      FROM Operation
      CONNECT BY PRIOR NextOperation_ID = Operation_ID
      START WITH NextOperation_ID IS NOT NULL
    )
    GROUP BY Product_ID
    ORDER BY maior_sequencia DESC
  )
  WHERE ROWNUM = 1;

  RETURN max_product_id;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('Nenhuma sequência encontrada.');
    RETURN NULL;
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('Produto com a maior sequência: ' || ProductWithMostOperations());
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