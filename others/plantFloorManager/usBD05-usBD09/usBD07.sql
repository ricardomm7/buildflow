--USBD07

-- Consulta para obter os materiais/componentes e as suas quantidades
SELECT
    po.OrderOrder_ID AS "Order ID",
    po.ProductProduct_ID AS "Product ID",
    prod.Name AS "Product Name",
    c.Component_ID AS "Component ID",
    c.Name AS "Component Name",
    c.Quantity AS "Component Quantity",
    rm.Name AS "Raw Material Name",
    rm.Quantity AS "Raw Material Quantity"
FROM
    Production_Order po
    JOIN BOM b ON po.ProductProduct_ID = b.ProductProduct_ID
    JOIN Product prod ON po.ProductProduct_ID = prod.Product_ID
    JOIN Component c ON b.ProductProduct_ID = c.BOMProductProduct_ID
    LEFT JOIN Raw_Materials rm ON b.ProductProduct_ID = rm.BOMProductProduct_ID
WHERE
    po.OrderOrder_ID = 'O003'; -- Inserir Id da Order desejada