package fourcorp.buildflow.application;

import fourcorp.buildflow.domain.Activity;
import fourcorp.buildflow.repository.ActivitiesGraph;
import fourcorp.buildflow.repository.Repositories;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class DisplayGraph {

    private ActivitiesGraph graph;

    public DisplayGraph() {
        this.graph = Repositories.getInstance().getActivitiesGraph();
    }

    public void generateDotFile(String dotFilePath) {
        StringBuilder dotContent = new StringBuilder();
        dotContent.append("digraph PERT_CPM {\n");

        // Configurações importantes para evitar corte
        dotContent.append("  size=\"20,20\";\n");  // Aumentar o tamanho total da página
        dotContent.append("  margin=0.5;\n");     // Margem pequena
        dotContent.append("  rankdir=TB;\n");
        dotContent.append("  compound=true;\n");
        dotContent.append("  splines=ortho;\n");
        dotContent.append("  nodesep=1.0;\n");
        dotContent.append("  ranksep=1.5;\n");

        // Configurações de fonte e estilo global
        dotContent.append("  node [fontname=\"Arial Bold\" fontsize=18];\n");
        dotContent.append("  edge [fontname=\"Arial\" fontsize=14];\n");

        // Adicionar nós (atividades)
        for (Activity activity : graph.getGraph().vertices()) {
            // Determinar a forma e cor do nó
            String shape = activity.getEarlyStart() == activity.getLateStart() ? "doubleoctagon" : "ellipse";
            String fillColor = activity.getEarlyStart() == activity.getLateStart() ? "lightcoral" : "lightblue";

            // Criar um label mais informativo
            String label = String.format("%s", activity.getId());

            // Adicionar nó com mais detalhes
            dotContent.append(String.format(
                    "  \"%s\" [\n" +
                            "    shape=%s,\n" +
                            "    style=\"filled,rounded\",\n" +
                            "    fillcolor=%s,\n" +
                            "    width=2,\n" +
                            "    height=1,\n" +
                            "    label=\"%s\",\n" +
                            "    fontsize=18\n" +
                            "  ];\n",
                    activity.getId(), shape, fillColor, label
            ));
        }

        // Adicionar arestas (dependências) com pesos e informações
        for (Activity fromActivity : graph.getGraph().vertices()) {
            // Obter as atividades adjacentes
            Collection<Activity> adjacencies = graph.getGraph().adjVertices(fromActivity);
            for (Activity toActivity : adjacencies) {
                // Definir o peso como a duração da atividade anterior
                int edgeWeight = fromActivity.getDuration();

                dotContent.append(String.format(
                        "  \"%s\" -> \"%s\" [\n" +
                                "    style=solid,\n" +
                                "    color=gray,\n" +
                                "    penwidth=2,\n" +
                                "    label=\"%d " + fromActivity.getDurationUnit() + "\",\n" +  // Adicionar duração na aresta
                                "    weight=%d\n" +           // Peso para layout e algoritmos de grafo
                                "  ];\n",
                        fromActivity.getId(),
                        toActivity.getId(),
                        edgeWeight,
                        edgeWeight
                ));
            }
        }

        dotContent.append("}\n");

        // Escrever o conteúdo no arquivo DOT
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dotFilePath))) {
            writer.write(dotContent.toString());
            System.out.println("DOT file generated: " + dotFilePath);
        } catch (IOException e) {
            System.err.println("Error writing DOT file: " + e.getMessage());
        }
    }


    public void generateSVG(String dotFilePath, String svgFilePath) {
        try {
            // Opções para garantir todo o conteúdo seja visível
            String command = String.format(
                    "dot -Tsvg -Gpage=\"20,20\" -Gmargin=0.5 -Gsize=\"20,20\" %s -o %s",
                    dotFilePath, svgFilePath
            );
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("SVG file generated: " + svgFilePath);
            } else {
                System.err.println("Graphviz failed to generate SVG. Exit code: " + exitCode);
            }
        } catch (Exception e) {
            System.err.println("Error generating SVG file: " + e.getMessage());
        }
    }

    public void setGraph(ActivitiesGraph graph) {
        this.graph = graph;
    }
}