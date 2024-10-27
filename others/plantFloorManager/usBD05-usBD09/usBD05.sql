--USBD05

SELECT
    p.Product_ID,
    p.Name AS Product_Name,
    o.Order_ID,
    c.Name AS Customer_Name,
    po.quantity AS Quantity,
    o.DeliveryDate
FROM
    Product p
JOIN
    Production_Line po ON p.Product_ID = po.ProductProduct_ID
JOIN
    "Order" o ON po.OrderOrder_ID = o.Order_ID
JOIN
    Costumer c ON o.CostumerVAT = c.VAT
WHERE
    o.DeliveryDate BETWEEN TO_DATE('2024-10-26', 'YYYY-MM-DD') AND TO_DATE('2025-01-10', 'YYYY-MM-DD') --Substituir pelas datas desejadas
ORDER BY
    o.DeliveryDate;