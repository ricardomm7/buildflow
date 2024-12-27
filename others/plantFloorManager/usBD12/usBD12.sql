create or replace function GetProductOperationParts(p_Product_ID in Product.Part_ID%type)
    return SYS_REFCURSOR
is
    cur_results SYS_REFCURSOR;
begin
    -- Abre o cursor que acumula todos os resultados, incluindo chamadas recursivas
    open cur_results for
        with RecursiveParts(part_id, quantity) as (
            -- Partes iniciais associadas ao produto com suas quantidades
            select oi.Part_ID, oi.Quantity
            from Operation_Input oi
            join Operation op on oi.Operation_ID = op.Operation_ID
            where op.Product_ID = p_Product_ID

            union all

            -- Chamada recursiva: verifica se part_id é um produto e busca as partes relacionadas
            select oi.Part_ID, oi.Quantity
            from RecursiveParts rp
            join Product p on rp.part_id = p.Part_ID
            join Operation op on p.Part_ID = op.Product_ID
            join Operation_Input oi on op.Operation_ID = oi.Operation_ID
        )
        -- Seleciona todas as partes, somando quantidades e excluindo produtos/IntermediateProducts
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


begin
    declare
        cur_results SYS_REFCURSOR;
        v_part_id Product.Part_ID%type;
        v_quantity number;
    begin
        -- Chama a função para o Product_ID desejado
        cur_results := GetProductOperationParts('AS12945S22');

        -- Itera pelos resultados retornados pelo cursor
        loop
            fetch cur_results into v_part_id, v_quantity;
            exit when cur_results%notfound;
            -- Imprime os Part_IDs retornados e suas quantidades
            dbms_output.put_line('Part_ID: ' || v_part_id || ', Quantity: ' || v_quantity);
        end loop;

        -- Fecha o cursor após o uso
        close cur_results;
    end;
end;
/

