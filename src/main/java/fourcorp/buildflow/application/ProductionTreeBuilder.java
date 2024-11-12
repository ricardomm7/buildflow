package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.CriticalPathHandler;
import fourcorp.buildflow.domain.ProductionNode;
import fourcorp.buildflow.repository.Repositories;

import java.util.List;
import java.util.Map;

public class ProductionTreeBuilder {
    private final Repositories repository = Repositories.getInstance();
    public Map<String, ProductionNode> nodesMap = repository.getProductionTree().getNodesMap();
    public List<ProductionNode> rootNodes = repository.getProductionTree().rootNodes;  // Lista para múltiplas raízes


    // Método de busca eficiente que pode pesquisar tanto por nome quanto por ID
    public String searchNodeByNameOrId(String identifier) {
        ProductionNode node = nodesMap.get(identifier);  // Primeiro tenta buscar pelo ID

        if (node == null) {
            // Se não encontrar pelo ID, tenta buscar pelo nome
            node = findNodeByName(identifier);
        }

        if (node == null) {
            return "Nó não encontrado";
        }

        // Se encontrou o nó, monta a resposta
        StringBuilder result = new StringBuilder("Detalhes do Nó:\n");
        result.append("ID: ").append(node.getId()).append("\n");
        result.append("Nome: ").append(node.getName()).append("\n");
        result.append("Tipo: ").append(node.isMaterial() ? "Material" : "Operação").append("\n");

        if (node.isMaterial()) {
            result.append("Quantidade: ").append(node.getQuantity()).append("\n");
            result.append("Operação Pai: ").append(node.getParent() != null ? node.getParent().getName() : "Nenhuma").append("\n");
        }
        return result.toString();
    }

    // Método auxiliar para encontrar um nó pelo nome (caso não encontre pelo ID)
    private ProductionNode findNodeByName(String name) {
        for (ProductionNode node : nodesMap.values()) {
            if (node.getName().equalsIgnoreCase(name)) {
                return node;
            }
        }
        return null;  // Retorna null se não encontrar o nó pelo nome
    }


    // Método para exibir a árvore de produção (suportando múltiplas raízes)
    public void displayProductionTrees() {
        for (ProductionNode root : rootNodes) {
            displayProductionTree(root, "");
            System.out.println();  // Separador entre diferentes árvores
        }
    }

    private void displayProductionTree(ProductionNode node, String indent) {
        System.out.println(indent + "- " + node.getName());
        for (ProductionNode child : node.getChildren()) {
            displayProductionTree(child, indent + "  ");
        }
    }

    // Método para atualizar a quantidade de um material
    public void updateMaterialQuantity(String materialId, int newQuantity) {
        ProductionNode materialNode = nodesMap.get(materialId);
        if (materialNode != null && materialNode.isMaterial()) {
            materialNode.setQuantity(newQuantity);
            System.out.println("Quantidade do material " + materialNode.getName() + " foi atualizada para " + newQuantity);

            // Propagar a atualização para os filhos (cascata)
            updateMaterialQuantityCascade(materialNode.getParent(), materialNode);
        } else {
            System.out.println("Material não encontrado!");
        }
    }

    // Propaga a alteração de quantidade para os materiais dependentes (se necessário)
    private void updateMaterialQuantityCascade(ProductionNode parent, ProductionNode updatedMaterial) {
        if (parent != null) {
            for (ProductionNode child : parent.getChildren()) {
                if (child.isMaterial()) {
                    // Aqui podemos ter alguma lógica de dependência, por exemplo, atualizar a quantidade no material dependente
                    // Se houver dependências, você pode chamar updateMaterialQuantity de novo.
                }
            }
        }
    }

    // (Exemplo) Atualiza a quantidade de materiais, percorrendo a árvore
    public void updateAllMaterialsQuantity() {
        for (ProductionNode node : nodesMap.values()) {
            if (node.isMaterial()) {
                System.out.println("Atualizando material " + node.getName() + " com quantidade " + node.getQuantity());
            }
        }
    }

    public void addQualityChecksToQueue(QualityCheckHandler handler) {
        for (ProductionNode node : nodesMap.values()) {
            if (!node.isMaterial()) { // Considerando que queremos adicionar apenas nós de operação
                handler.addQualityCheck(node);
            }
        }
    }

    // Método recursivo para calcular o custo total de produção
    public double calculateTotalCost(ProductionNode node) {
        if (node == null) {
            return 0.0;
        }

        double totalCost;
        if (node.isMaterial()) {
            // Multiplicação de custo por quantidade para materiais
            totalCost = node.getCost() * node.getQuantity();
        } else {
            // Custo de operação
            totalCost = node.getCost();
        }

        // Calcular recursivamente os custos dos filhos
        for (ProductionNode child : node.getChildren()) {
            totalCost += calculateTotalCost(child);
        }

        return totalCost;
    }


    public void displayTotalCosts() {
        double totalCombinedCost = 0;

        System.out.println("\nCusto total estimado de produção por raiz:");
        for (ProductionNode root : rootNodes) {
            double totalCostForRoot = calculateTotalCost(root);
            System.out.println("Raiz: " + root.getName() + " | Custo Total: " + totalCostForRoot);
            totalCombinedCost += totalCostForRoot;
        }

        System.out.println("\nCusto total combinado de todas as raízes: " + totalCombinedCost);
    }


    // Método para Identificar o caminho crítico
    public void identifyCriticalPath() {
        CriticalPathHandler criticalPathHandler = new CriticalPathHandler();
        for (ProductionNode node : nodesMap.values()) { // Suponha que este método retorne todas as operações
            criticalPathHandler.addOperationToCriticalPath(node);
        }
        criticalPathHandler.displayCriticalPath();
    }


}
