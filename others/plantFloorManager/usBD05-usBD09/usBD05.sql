--USBD05
SELECT
    p.Product_ID,
    p.Name AS Product_Name,
    o.Order_ID,
    o.OrderDate AS Order_Date,
    o.DeliveryDate AS Delivery_Date,
    c.Name AS Customer_Name,
    c.Address AS Customer_Address
FROM
    Production_Order po
JOIN
    Product p ON po.ProductProduct_ID = p.Product_ID
               AND po.ProductType_ProductDesignation = p.Type_ProductDesignation
               AND po.ProductProduct_FamilyFamily_ID = p.Product_FamilyFamily_ID
JOIN
    "Order" o ON po.OrderOrder_ID = o.Order_ID
JOIN
    Costumer c ON o.CostumerNIF = c.NIF
WHERE
    o.DeliveryDate >= SYSDATE
ORDER BY
    o.DeliveryDate;
