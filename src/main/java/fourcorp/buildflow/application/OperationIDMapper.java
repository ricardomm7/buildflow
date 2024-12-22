package fourcorp.buildflow.application;

import java.util.HashMap;
import java.util.Map;

public class OperationIDMapper {
    private final Map<String, Integer> operationMap;
    private final Map<Integer, String> reverseMap;

    public OperationIDMapper() {
        this.operationMap = new HashMap<>();
        this.reverseMap = new HashMap<>();
    }

    public int mapOperationId(String originalId) {
        // Se já existe um mapeamento, retorna-o
        if (operationMap.containsKey(originalId)) {
            return operationMap.get(originalId);
        }

        // Extrai o número do ID original
        int originalNumber = extractNumber(originalId);

        // Tenta manter o último dígito se possível
        int preferredMapping = getPreferredMapping(originalNumber);

        // Registra o mapeamento
        operationMap.put(originalId, preferredMapping);
        reverseMap.put(preferredMapping, originalId);

        return preferredMapping;
    }

    private int getPreferredMapping(int originalNumber) {
        int preferredMapping = originalNumber % 10;

        // Se o número preferido já está em uso ou é maior que 31,
        // tenta encontrar uma alternativa baseada em algumas regras
        if (reverseMap.containsKey(preferredMapping)) {
            // Tenta usar o resto da divisão por 32
            preferredMapping = originalNumber % 32;

            // Se ainda estiver ocupado, encontra o próximo número disponível
            while (reverseMap.containsKey(preferredMapping)) {
                preferredMapping = (preferredMapping + 1) % 32;
            }
        }
        return preferredMapping;
    }

    private int extractNumber(String operationId) {
        // Remove caracteres não numéricos e converte para inteiro
        return Integer.parseInt(operationId.replaceAll("[^0-9]", ""));
    }

    public void printMapping() {
        System.out.println("\nOperation ID Mapping:");
        System.out.println("Original -> Mapped");
        System.out.println("-------------------");

        // Ordena as entradas pelo ‘ID’ original para uma exibição mais organizada
        operationMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("%s -> %d%n", entry.getKey(), entry.getValue());
                });
    }

    /*
    O algoritmo de mapeamento funciona assim:

    Primeiro tenta preservar o último dígito do ID original se ele for ≤ 31 e estiver disponível
    Se não for possível, usa o resto da divisão do número original por 32 (para garantir que fica entre 0 e 31)
    Se ainda houver conflito, procura o próximo número disponível no intervalo 0-31

    Por exemplo:

    Operation_123 -> extrai 123 -> último dígito é 3 -> se 3 estiver disponível, usa 3
    Operation_164 -> extrai 164 -> 164 % 32 = 4 -> se 4 estiver disponível, usa 4
    Se houver conflito, encontra o próximo número disponível
     */

}