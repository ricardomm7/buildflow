-- USBD07

SELECT
    po.OrderOrder_ID AS Order_ID,
    pp.PartPart_ID AS Part_ID,
    p.Description AS Part_Description,
    SUM(po.quantity * pp.Quantity) AS Total_Quantity_Required
FROM
    Production_Line po
JOIN
    Product_Part pp ON po.ProductProduct_ID = pp.ProductProduct_ID
JOIN
    Part p ON pp.PartPart_ID = p.Part_ID
WHERE
    po.OrderOrder_ID = '1'  -- Substituir pelo ID da Order desejada
GROUP BY
    po.OrderOrder_ID, pp.PartPart_ID, p.Description
ORDER BY
    pp.PartPart_ID;
