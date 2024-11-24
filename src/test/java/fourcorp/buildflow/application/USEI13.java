package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.ProductionTree;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class USEI13 {
    @Test
    public void testCalculateQuantityOfMaterials() {
        // Criando nós de materiais (produtos)
        ProductionNode material1 = new ProductionNode("M1", "Material1", true);
        material1.setQuantity(100); // Definindo quantidade de Material1 como 100
        ProductionNode material2 = new ProductionNode("M2", "Material2", true);
        material2.setQuantity(200); // Definindo quantidade de Material2 como 200

        // Criando uma operação que usa os materiais
        ProductionNode op1 = new ProductionNode("O1", "Operation1", false);

        // Conectando a operação aos materiais
        Map<ProductionNode, Double> subNodes = new HashMap<>();
        subNodes.put(material1, 10.0); // Operation1 usa 10 unidades de Material1
        subNodes.put(material2, 5.0);  // Operation1 usa 5 unidades de Material2

        // Criando a árvore de produção
        ProductionTree productionTree = new ProductionTree();
        productionTree.addNode(material1);
        productionTree.addNode(material2);
        productionTree.addNode(op1);

        // Adicionando as dependências entre a operação e os materiais
        productionTree.addDependencyBom(op1, material1, 10.0); // Adicionando dependência de Material1
        productionTree.addDependencyBom(op1, material2, 5.0);  // Adicionando dependência de Material2

        // Criando o ProductionTreeSearcher
        ProductionTreeSearcher searcher = new ProductionTreeSearcher();

        // Calculando as quantidades totais dos materiais
        searcher.calculateQuantityOfMaterials();

        // Esperamos que a quantidade total de materiais seja calculada corretamente.
        // A quantidade de Material1 deve ser 100 (a quantidade definida) mais 10 unidades usadas pela operação.
        // A quantidade de Material2 deve ser 200 (a quantidade definida) mais 5 unidades usadas pela operação.

        // Podemos verificar isso observando a saída ou verificando como o método realiza os cálculos.
    }

}
