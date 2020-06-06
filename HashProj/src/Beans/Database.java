/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Beans.Bucket.Hash;
import Beans.Tupla.Pagina;
import Beans.Tupla.Tupla;
import Beans.Tupla.departamentoTupla;
import Beans.Tupla.dependenteTupla;
import Beans.Tupla.empregadoTupla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

/**
 *
 * @author Matheus
 */
public class Database extends Thread {

    public ArrayList<Pagina> empregado = new ArrayList<>();
    public ArrayList<Pagina> empregadoOrdered = new ArrayList<>();
    public ArrayList<Pagina> dependente = new ArrayList<>();
    public ArrayList<Pagina> departamento = new ArrayList<>();

    public ArrayList<String> consultas = new ArrayList<>();

    public void carregarArquivos(int qtdTpl) {

        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\sFROM\\s(\\w+)");
        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s([<>=])\\s(\\d+)");
        consultas.add("SELECT\\s(\\w+|\\*)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s([<>=])\\s(\\d+)");
        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\s,\\s(\\w+)\\s,\\s(\\w+)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s([<>=])\\s(\\d+)");

        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s([<>=])\\s(\\d+)");
        consultas.add("SELECT\\s(\\w+|\\*)\\sFROM\\s(\\w+)\\s,\\s(\\w+)\\sWHERE\\s(\\w+)\\s=\\s(\\w+)");
        consultas.add("SELECT\\s(\\w+|\\*)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s=\\s(\\w+)");

        ArrayList<String> arquivo = new ArrayList<>();
        File file = new File("names.txt");
        FileReader fr;
        BufferedReader br;

        try {

            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line;
            while ((line = br.readLine()) != null) {
                arquivo.add(line);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        Random r = new Random();
        int counter = 1;

        //ORDERED
        while (counter < arquivo.size()) {

            empregadoOrdered.add(empregadoOrdered.size(), new Pagina());

            for (int i = 0; i < qtdTpl; i++) {

                try {
                    empregadoOrdered.get(empregadoOrdered.size() - 1).tuplas.add(new empregadoTupla(counter, arquivo.get(counter), ((double) r.nextInt(1000000)) / 100, r.nextInt(20) + 1));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace(System.out);
                } finally {
                    counter++;
                }

            }
        }

        counter = 1;

        ArrayList<Integer> numMatri = new ArrayList<>();
        IntStream.range(1, arquivo.size() + 1).forEach(numMatri::add);
        Collections.shuffle(numMatri);

        while (counter < arquivo.size()) {

            empregado.add(empregado.size(), new Pagina());

            for (int i = 0; i < qtdTpl; i++) {

                try {
                    empregado.get(empregado.size() - 1).tuplas.add(new empregadoTupla(numMatri.get(counter), arquivo.get(counter), ((double) r.nextInt(1000000)) / 100, r.nextInt(20) + 1));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace(System.out);
                } finally {
                    counter++;
                }

            }
        }

        counter = 1;

        while (counter < 3000) {

            dependente.add(dependente.size(), new Pagina());

            for (int j = 0; j < qtdTpl; j++) {
                try {
                    int i = r.nextInt(10000) + 1;
                    dependente.get(dependente.size() - 1).tuplas.add(new dependenteTupla(i, arquivo.get(r.nextInt(10000) + 1), i));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace(System.out);
                } finally {
                    counter++;
                }
            }

        }

        departamento.add(new Pagina());

        departamento.get(0).tuplas.add(new departamentoTupla(1, "VENDAS"));
        departamento.get(0).tuplas.add(new departamentoTupla(2, "SUPORTE"));
        departamento.get(0).tuplas.add(new departamentoTupla(3, "DESENVOLVIMENTO"));
        departamento.get(0).tuplas.add(new departamentoTupla(4, "RECURSOS HUMANOS"));
        departamento.get(0).tuplas.add(new departamentoTupla(5, "ATENDIMENTO AO CLIENTE"));
        departamento.get(0).tuplas.add(new departamentoTupla(6, "CONTROLE DE QUALIDADE"));
        departamento.get(0).tuplas.add(new departamentoTupla(7, "DIRETORIA"));
        departamento.get(0).tuplas.add(new departamentoTupla(8, "RECRUTAMENTO"));
        departamento.get(0).tuplas.add(new departamentoTupla(9, "ACESSORIA JURIDICA"));
        departamento.get(0).tuplas.add(new departamentoTupla(10, "MARKETING"));
        departamento.get(0).tuplas.add(new departamentoTupla(11, "PESQUISA"));
        departamento.get(0).tuplas.add(new departamentoTupla(12, "INVESTIMENTO"));
        departamento.get(0).tuplas.add(new departamentoTupla(13, "CONTROLADORIA"));
        departamento.get(0).tuplas.add(new departamentoTupla(14, "CONTABILIDADE"));
        departamento.get(0).tuplas.add(new departamentoTupla(15, "ADMINISTRACAO"));
        departamento.get(0).tuplas.add(new departamentoTupla(16, "EXECUTIVO"));
        departamento.get(0).tuplas.add(new departamentoTupla(17, "SOCIO"));
        departamento.get(0).tuplas.add(new departamentoTupla(18, "LOGISTICA"));
        departamento.get(0).tuplas.add(new departamentoTupla(19, "INOVACAO"));
        departamento.get(0).tuplas.add(new departamentoTupla(20, "RELACAO COM O CLIENTE"));

    }

    public Graph parser(String query, int id) {
        ArrayList<String> SELECT = new ArrayList<>();
        ArrayList<String> FROM = new ArrayList<>();
        ArrayList<String> WHERE = new ArrayList<>();

        Pattern p;
        Matcher m;
        for (String string : consultas) {
            p = Pattern.compile(string);
            m = p.matcher(query);

            if (m.matches()) {

                String[] keys = query.split("\\s");

                int counterFrom = keys.length;
                int counterWhere = keys.length;

                for (int i = 1; i < keys.length; i++) {
                    if (keys[i].equals("FROM")) {
                        counterFrom = i;
                    } else if (keys[i].equals("WHERE")) {
                        counterWhere = i;
                    }
                }

                int auxWhere = 0;
                String auxWhereString = "";
                for (int i = 1; i < keys.length; i++) {

                    if (i < counterFrom && !",".equals(keys[i])) {
                        SELECT.add(keys[i]);
                    } else if (i > counterFrom && i < counterWhere && !",".equals(keys[i])) {
                        FROM.add(keys[i]);
                    } else if (i > counterWhere && !",".equals(keys[i])) {
                        if (auxWhere < 3) {
                            auxWhereString = auxWhereString + keys[i] + " ";
                            auxWhere++;
                        }
                        if (auxWhere == 3) {
                            auxWhereString = auxWhereString.substring(0, auxWhereString.length() - 1);
                            WHERE.add(auxWhereString);
                            auxWhere = 0;
                        }

                    }

                }
                Graph g = new DefaultGraph("default");
                g.setAutoCreate(true);

                for (String s : FROM) {
                    g.addNode(s).addAttribute("tabela", s);
                }

                for (String s : WHERE) {
                    g.addNode(s).addAttribute("condicao", s);
                }

                for (String s : SELECT) {
                    g.addNode(s).addAttribute("projecao", s);
                }

                String aux = "";
                switch (id) {
                    case 0:
                        aux = "busca linear";
                        g.addNode(aux).addAttribute("operador", aux);
                        break;
                    case 1:
                        aux = "busca binaria";
                        g.addNode(aux).addAttribute("operador", aux);
                        break;
                    case 2:
                        aux = "index seek";
                        g.addNode(aux).addAttribute("operador", aux);
                        break;
                    default:
                        break;
                }

                for (Node n : g.getEachNode()) {
                    if (n.getAttribute("tabela") != null) {
                        g.addEdge("operacao: " + n.getId(), n, g.getNode(aux), true);
                    } else if (n.getAttribute("condicao") != null) {
                        g.addEdge("corte horizontal: " + n.getId(), g.getNode(aux), n, true);
                        for (Node node : g.getEachNode()) {
                            if (node.getAttribute("projecao") != null) {
                                g.addEdge("corte vertical: " + node.getId(), n, node, true);
                            }
                        }
                    }
                }

                for (Node node : g) {
                    node.addAttribute("ui.label", node.getId());
                }
                return g;
            }

        }
        return null;
    }

    public ArrayList<ArrayList<Tupla>> processadorDeConsulta(Graph graph) {

        ArrayList<ArrayList<Tupla>> resultado = new ArrayList<>();

        ArrayList<Node> FROM = new ArrayList<>();
        Node WHERE = null;
        Node OPERADOR = null;

        for (Node node : graph) {
            if (node.getAttribute("tabela") != null) {
                FROM.add(node);
            } else if (node.getAttribute("operador") != null) {
                OPERADOR = node;
            } else if (node.getAttribute("condicao") != null) {
                WHERE = node;
            }
        }
        switch (OPERADOR.getAttribute("operador").toString()) {
            case "busca linear":
                if (WHERE == null) {
                    switch (FROM.get(0).getAttribute("tabela").toString()) {
                        case "empregado":
                            resultado.add(buscaLinear(empregado));
                            break;
                        case "dependente":
                            resultado.add(buscaLinear(dependente));
                            break;
                        case "departamento":
                            resultado.add(buscaLinear(departamento));
                            break;
                    }

                } else {
                    switch (FROM.get(0).getAttribute("tabela").toString()) {
                        case "empregado":
                            resultado.add(buscaLinear(empregado, WHERE.getAttribute("condicao").toString()));
                            break;
                        case "dependente":
                            resultado.add(buscaLinear(dependente, WHERE.getAttribute("condicao").toString()));
                            break;
                        case "departamento":
                            resultado.add(buscaLinear(departamento, WHERE.getAttribute("condicao").toString()));
                            break;
                    }

                }
                break;
            case "busca binaria":
                ArrayList<Tupla> temp = new ArrayList<>();

                switch (FROM.get(0).getAttribute("tabela").toString()) {
                    case "empregado":
                        temp.add(buscaBinario(empregadoOrdered, WHERE.getAttribute("condicao").toString()));
                        resultado.add(temp);
                        break;
                    case "dependente":
                        temp.add(buscaBinario(dependente, WHERE.getAttribute("condicao").toString()));
                        resultado.add(temp);
                        break;
                    case "departamento":
                        temp.add(buscaBinario(departamento, WHERE.getAttribute("condicao").toString()));
                        resultado.add(temp);
                        break;
                }

                break;
            case "index seek":
                ArrayList<Tupla> temp1 = new ArrayList<>();

                switch (FROM.get(0).getAttribute("tabela").toString()) {
                    case "empregado":
                        temp1.add(indexSeek(empregadoOrdered, WHERE.getAttribute("condicao").toString(),8192));
                        resultado.add(temp1);
                        break;
                    case "dependente":
                        temp1.add(indexSeek(dependente, WHERE.getAttribute("condicao").toString(),2048));
                        resultado.add(temp1);
                        break;
                    case "departamento":
                        temp1.add(indexSeek(departamento, WHERE.getAttribute("condicao").toString(),16));
                        resultado.add(temp1);
                        break;
                }
                break;
            default:
                break;
        }
        return resultado;
    }

    public ArrayList<Tupla> buscaLinear(ArrayList<Pagina> tabela) {
        ArrayList<Tupla> res = new ArrayList<>();

        for (Pagina p : tabela) {
            for (Tupla t : p.tuplas) {
                res.add(t);
            }
        }
        return res;
    }

    public ArrayList<Tupla> buscaLinear(ArrayList<Pagina> tabela, String condicao) {
        ArrayList<Tupla> res = new ArrayList<>();

        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Field f;
        try {
            String[] atrib = (condicao.split("\\s"));
            if (atrib[1].equals("=")) {
                atrib[1] = "==";
            }
            for (Pagina p : tabela) {
                for (Tupla t : p.tuplas) {
                    f = t.getClass().getDeclaredField(atrib[0]);
                    f.setAccessible(true);
                    if ((boolean) engine.eval(f.get(t) + atrib[1] + atrib[2])) {
                        res.add(t);
                    }
                }
            }

        } catch (ScriptException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    //ordenada
    public Tupla buscaBinario(ArrayList<Pagina> tabela, String condicao) {
        ArrayList<Tupla> aux = buscaLinear(tabela);
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Field f;

        String[] atrib = condicao.split("\\s");

        try {
            f = aux.get(0).getClass().getDeclaredField(atrib[0]);
            f.setAccessible(true);
            int low = 0;
            int high = aux.size() - 1;
            int mid = (low + high) / 2;

            while (low <= high && !(boolean) engine.eval(f.get(aux.get(mid)) + " == " + atrib[2])) {
                if ((Integer) f.get(aux.get(mid)) < Double.parseDouble(atrib[2])) {
                    low = mid;
                    mid = (low + high) / 2;
                } else if ((Integer) f.get(aux.get(mid)) > Double.parseDouble(atrib[2])) {
                    high = mid;
                    mid = (low + high) / 2;
                }
            }

            return aux.get(mid);

        } catch (NoSuchFieldException | SecurityException | ScriptException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    //hash
    public Tupla indexSeek(ArrayList<Pagina> pagina, String condicao, int hashPrime) {
        Hash h = new Hash(hashPrime, pagina);
        h.criarBuckets();
        String[] keys = condicao.split("\\s");
        return h.buscarIndice(Integer.parseInt(keys[2]));
    }

    //nao igualdade
    public ArrayList<Tupla> indexScan(ArrayList<Pagina> tabela, String condicao) {

        return null;
    }

    public ArrayList<Tupla> nestedLoopJoin(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao) {

        return null;
    }

    //hash
    public ArrayList<Tupla> nestedLoopJoinHash(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao) {

        return null;
    }

    public ArrayList<Tupla> mergeJoin(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao) {

        return null;
    }

    public ArrayList<Tupla> hashJoin(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao) {

        return null;
    }

    /*
    private void countOverflow() {

        for (Bucket bucket : bucketList) {
            if (bucket.overflow != null) {
                oveflowCheck(bucket.overflow);
            }
        }

    }

    private void oveflowCheck(Bucket k) {
        overflowCount += k.bucketTuplas.size();
        if (k.overflow != null) {
            oveflowCheck(k.overflow);
        }
    }

    public void countColisoes() {
        bucketList.forEach((bucket) -> {
            coliCheck(bucket);
        });
    }

    private void coliCheck(Bucket k) {
        bucketCount++;
        if (k.overflow != null) {
            coliCheck(k.overflow);
        }
    }
     */
}
