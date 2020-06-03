package Beans.Grafo;

import java.util.ArrayList;
import javax.swing.JTextArea;

public class Graph {

    public ArrayList<GraphNode> graph = new ArrayList<>();

    public void drawGraph(JTextArea j) {
        j.setText("");
        GraphNode SELECT = new GraphNode();
        ArrayList<GraphNode> FROM = new ArrayList<>();
        GraphNode WHERE = new GraphNode();

        graph.forEach((n) -> {
            FROM.add(n);
        });

        GraphNode JUNCAO = FROM.get(0).arestas;

        WHERE = JUNCAO.arestas;

        SELECT = WHERE.arestas;

        String texto = "\t";

        for (String s : SELECT.nome) {
            texto = texto + s + " ";
        }
        texto = texto
                + "\n\t" + SELECT.tipo
                + "\n\t     ^"
                + "\n\t     |"
                + "\n";

        if (!WHERE.nome.isEmpty()) {
            for (String s : WHERE.nome) {
                texto = texto + "\t" + s + " ";
            }
            texto = texto
                    + "\n\t" + WHERE.tipo
                    + "\n\t     ^"
                    + "\n\t     |"
                    + "\n";
        }

        texto = texto + "\n\t" + JUNCAO.nome.get(0)
                + "\n\t" + JUNCAO.tipo
                + "\n\t     ^"
                + "\n\t     |"
                + "\n";

        if (FROM.size() == 1) {
            texto = texto + "\t" + FROM.get(0).nome.get(0);
        } else {
            for (GraphNode n : FROM) {
                texto = texto + "         " + n.nome.get(0);
            }
        }

        j.setText(texto);

    }
}
