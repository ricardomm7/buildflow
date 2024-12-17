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

        // Configurações globais do grafo
        dotContent.append("  rankdir=LR;\n"); // Layout horizontal
        dotContent.append("  compound=true;\n");
        dotContent.append("  splines=true;\n");
        dotContent.append("  nodesep=1.5;\n"); // Espaçamento entre nós
        dotContent.append("  ranksep=1.5;\n"); // Espaçamento entre camadas

        // Estilo geral dos nós e arestas
        dotContent.append("  node [fontname=\"Arial\" fontsize=14 style=\"rounded,filled\"];\n");
        dotContent.append("  edge [fontname=\"Arial\" fontsize=12 color=gray50 penwidth=2.0];\n");

        // Adicionar nós (atividades) com estilos e cores diferenciados
        for (Activity activity : graph.getGraph().vertices()) {
            // Formas e cores para distinguir atividades críticas
            String shape = activity.getEarlyStart() == activity.getLateStart() ? "hexagon" : "ellipse";
            String fillColor = activity.getEarlyStart() == activity.getLateStart() ? "lightcoral" : "lightblue";
            String fontColor = activity.getEarlyStart() == activity.getLateStart() ? "black" : "darkblue";

            // Identificar o nó apenas pelo ID
            String label = activity.getId();

            // Adicionar nó ao DOT
            dotContent.append(String.format(
                "  \"%s\" [\n" +
                    "    shape=%s,\n" +
                    "    width=2.5,\n" +
                    "    height=1.2,\n" +
                    "    fillcolor=\"%s\",\n" +
                    "    fontcolor=\"%s\",\n" +
                    "    label=\"%s\"\n" +
                    "  ];\n",
                activity.getId(),
                shape,
                fillColor,
                fontColor,
                label
            ));
        }

        // Adicionar arestas (dependências)
        for (Activity fromActivity : graph.getGraph().vertices()) {
            Collection<Activity> adjacencies = graph.getGraph().adjVertices(fromActivity);
            for (Activity toActivity : adjacencies) {
                // Duração como peso da aresta
                int edgeWeight = fromActivity.getDuration();

                // Adicionar a aresta com a duração como label
                dotContent.append(String.format(
                    "  \"%s\" -> \"%s\" [\n" +
                        "    label=\"%d %s\",\n" +
                        "    color=\"gray\",\n" +
                        "    fontsize=12\n" +
                        "  ];\n",
                    fromActivity.getId(),
                    toActivity.getId(),
                    edgeWeight,
                    fromActivity.getDurationUnit()
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
            // Gerar o SVG utilizando `dot`
            String command = String.format(
                "dot -Tsvg %s -o %s",
                dotFilePath,
                svgFilePath
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
