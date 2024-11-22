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




