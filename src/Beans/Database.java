/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Beans.Bucket.Bucket;
import Beans.Bucket.BucketTupla;
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
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import javafx.util.Pair;
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

    public ArrayList<String> arquivo = new ArrayList<>();
    public ArrayList<Pagina> empregado = new ArrayList<>();
    public ArrayList<Pagina> empregado_Matri_Ordered = new ArrayList<>();
    public ArrayList<Pagina> empregado_lotacao_Ordered = new ArrayList<>();
    public ArrayList<Pagina> dependente = new ArrayList<>();
    public ArrayList<Pagina> dependente_MatriResp_Ordered = new ArrayList<>();
    public ArrayList<Pagina> departamento = new ArrayList<>();

    public ArrayList<String> consultas = new ArrayList<>();

    public Database() {
        //table scan e busca Binaria
        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s([<>=])\\s(\\d+)");
        //index seek
        consultas.add("SELECT\\s(\\w+|\\*)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s([<>=])\\s(\\d+)");
        //index scan
        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\s,\\s(\\w+)\\sFROM\\s(\\w+)\\sWHERE\\s(\\w+)\\s([<>=])\\s(\\d+)");
        //nested loop join
        consultas.add("SELECT\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\sFROM\\s(\\w+\\s\\w)\\s,\\s(\\w+\\s\\w)\\sWHERE\\s(\\w.\\w+)\\s=\\s(\\w.\\w+)\\sAND\\s(\\w.\\w+)\\s([><=])\\s(\\d+)");
        //nested Hash
        consultas.add("SELECT\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\sFROM\\s(\\w+\\s\\w)\\s,\\s(\\w+\\s\\w)\\sWHERE\\s(\\w.\\w+)\\s=\\s(\\w.\\w+)\\sAND\\s(\\w.\\w+)\\s([><=])\\s(\\d+)");
        //merge Join
        consultas.add("SELECT\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\sFROM\\s(\\w+\\s\\w)\\s,\\s(\\w+\\s\\w)\\sWHERE\\s(\\w.\\w+)\\s=\\s(\\w.\\w+)");
        //hash Join
        consultas.add("SELECT\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\s,\\s(\\w.\\w+)\\sFROM\\s(\\w+\\s\\w)\\s,\\s(\\w+\\s\\w)\\sWHERE\\s(\\w.\\w+)\\s=\\s(\\w.\\w+)");

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

        Collections.shuffle(arquivo);
    }

    public void carregarArquivos(int qtdTpl) {

        Random r = new Random();
        int counter = 0;

        //ORDERED
        while (counter < arquivo.size()) {

            empregado_Matri_Ordered.add(empregado_Matri_Ordered.size(), new Pagina());

            for (int i = 0; i < qtdTpl; i++) {

                try {
                    empregado_Matri_Ordered.get(empregado_Matri_Ordered.size() - 1).tuplas.add(new empregadoTupla(counter, arquivo.get(counter), ((double) r.nextInt(1000000)) / 100, r.nextInt(20) + 1));
                } catch (IndexOutOfBoundsException e) {
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
                } finally {
                    counter++;
                }

            }
        }

        counter = 1;

        ArrayList<Integer> numMatri1 = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            numMatri1.add(r.nextInt(20) + 1);
        }
        Collections.sort(numMatri1);

        while (counter < arquivo.size()) {

            empregado_lotacao_Ordered.add(empregado_lotacao_Ordered.size(), new Pagina());

            for (int i = 0; i < qtdTpl; i++) {

                try {
                    empregado_lotacao_Ordered.get(empregado_lotacao_Ordered.size() - 1).tuplas.add(new empregadoTupla(numMatri.get(counter - 1), arquivo.get(counter), ((double) r.nextInt(1000000)) / 100, numMatri1.get(counter - 1)));
                } catch (IndexOutOfBoundsException e) {
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
                    dependente.get(dependente.size() - 1).tuplas.add(new dependenteTupla(r.nextInt(10000) + 1, arquivo.get(counter)));
                } catch (IndexOutOfBoundsException e) {
                } finally {
                    counter++;
                }
            }

        }

        //ORDERED
        counter = 1;

        numMatri.clear();
        for (int i = 1; i <= 3000; i++) {
            numMatri.add(r.nextInt(10000) + 1);
        }
        Collections.sort(numMatri);

        while (counter < 3000) {

            dependente_MatriResp_Ordered.add(dependente_MatriResp_Ordered.size(), new Pagina());

            for (int i = 0; i < qtdTpl; i++) {

                try {
                    dependente_MatriResp_Ordered.get(dependente_MatriResp_Ordered.size() - 1).tuplas.add(new dependenteTupla(numMatri.get(counter - 1), arquivo.get(counter - 1)));
                } catch (IndexOutOfBoundsException e) {
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

                String[] where = query.split("WHERE");
                String[] from = where[0].split("FROM");
                String[] select = from[0].split("SELECT");

                select = select[1].split(",");
                for (String s : select) {
                    SELECT.add(s.substring(1, s.length() - 1));
                }

                from = from[1].split(",");
                for (String s : from) {
                    FROM.add(s.substring(1, s.length() - 1));
                }

                where = where[1].split("AND");
                for (String s : where) {
                    WHERE.add(s.substring(1, s.length()));
                }

                Graph g = new DefaultGraph("default");
                String auxString = "";

                for (String s : FROM) {
                    g.addNode(s).addAttribute("tabela", s);
                }

                Iterator<String> whereIt = WHERE.iterator();
                while (whereIt.hasNext()) {
                    auxString = auxString + "," + whereIt.next();
                }
                auxString = auxString.substring(1, auxString.length());
                g.addNode(auxString).addAttribute("condicao", auxString);

                auxString = "";
                Iterator<String> selectIt = SELECT.iterator();
                while (selectIt.hasNext()) {
                    auxString = auxString + "," + selectIt.next();
                }
                auxString = auxString.substring(1, auxString.length());
                g.addNode(auxString).addAttribute("projecao", auxString);

                switch (id) {
                    case 0:
                        auxString = "busca linear";
                        g.addNode(auxString).addAttribute("operador", auxString);
                        break;
                    case 1:
                        auxString = "busca binaria";
                        g.addNode(auxString).addAttribute("operador", auxString);
                        break;
                    case 2:
                        auxString = "index seek";
                        g.addNode(auxString).addAttribute("operador", auxString);
                        break;
                    case 3:
                        auxString = "index scan";
                        g.addNode(auxString).addAttribute("operador", auxString);
                        break;
                    case 4:
                        auxString = "nested loop join,busca linear";
                        g.addNode(auxString).addAttribute("operador", auxString);

                        break;
                    case 5:
                        auxString = "nested loop join hash,index scan";
                        g.addNode(auxString).addAttribute("operador", auxString);
                        break;
                    case 6:
                        auxString = "merge join";
                        g.addNode(auxString).addAttribute("operador", auxString);
                        break;
                    case 7:
                        auxString = "hash join";
                        g.addNode(auxString).addAttribute("operador", auxString);
                        break;
                    default:
                        break;
                }

                for (Node n : g.getEachNode()) {
                    if (n.getAttribute("tabela") != null) {
                        g.addEdge("operacao: " + n.getId(), n, g.getNode(auxString), true);
                    } else if (n.getAttribute("condicao") != null) {
                        g.addEdge("corte horizontal: " + n.getId(), g.getNode(auxString), n, true);
                        for (Node node : g.getEachNode()) {
                            if (node.getAttribute("projecao") != null) {
                                g.addEdge("corte vertical: " + node.getId(), n, node, true);
                            }
                        }
                        break;
                    }
                }

                g.addAttribute("ui.quality");
                g.addAttribute("ui.stylesheet", "graph { padding: 40px; } node { text-alignment: at-right; shape: diamond; text-size: 15px; text-background-mode: plain; text-background-color: #EB2; text-color: #222; }");
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

        ArrayList<Tupla> temp = new ArrayList<>();
        ArrayList<Pair> pair;
        ArrayList<Tupla> temp1 = new ArrayList<>();
        ArrayList<Tupla> temp2 = new ArrayList<>();

        String aux = OPERADOR.getAttribute("operador");
        String[] operadores = aux.split(",");

        switch (operadores[0]) {
            case "busca linear":

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

                break;
            case "busca binaria":

                switch (FROM.get(0).getAttribute("tabela").toString()) {
                    case "empregado":
                        temp.add(buscaBinario(empregado_Matri_Ordered, WHERE.getAttribute("condicao").toString()));
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

                switch (FROM.get(0).getAttribute("tabela").toString()) {
                    case "empregado":
                        resultado.add(indexSeek(empregado, WHERE.getAttribute("condicao").toString(), 8192));
                        break;
                    case "dependente":
                        resultado.add(indexSeek(dependente, WHERE.getAttribute("condicao").toString(), 2048));
                        break;
                    case "departamento":
                        resultado.add(indexSeek(departamento, WHERE.getAttribute("condicao").toString(), 16));
                        break;
                }
                break;
            case "index scan":

                switch (FROM.get(0).getAttribute("tabela").toString()) {
                    case "empregado":
                        resultado.add(indexScan(empregado, WHERE.getAttribute("condicao").toString(), 8192));
                        break;
                    case "dependente":
                        resultado.add(indexScan(dependente, WHERE.getAttribute("condicao").toString(), 2048));
                        break;
                    case "departamento":
                        resultado.add(indexScan(departamento, WHERE.getAttribute("condicao").toString(), 16));
                        break;
                }

                break;

            case "nested loop join": //+ busca linear
                aux = FROM.get(0).getAttribute("tabela").toString();
                aux = aux.substring(0, aux.length() - 2);
                switch (aux) {
                    case "dependente":
                        //dependente e empregado
                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");
                        pair = nestedLoopJoin(empregado, dependente, operadores[0]);

                        operadores[1] = operadores[1].substring(2, operadores[1].length());
                        temp = buscaLinear(empregado, operadores[1]);

                        for (Pair p : pair) {
                            for (Tupla t : temp) {
                                if (p.getValue() == t) {
                                    temp1.add((Tupla) p.getKey());
                                    temp2.add((Tupla) p.getValue());
                                }
                            }
                        }
                        break;
                    case "departamento":
                        //departamento e empregado

                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");
                        pair = nestedLoopJoin(empregado, departamento, operadores[0]);

                        operadores[1] = operadores[1].substring(2, operadores[1].length());
                        temp = buscaLinear(empregado, operadores[1]);

                        for (Pair p : pair) {
                            for (Tupla t : temp) {
                                if ((Tupla) p.getKey() == t) {
                                    temp1.add((Tupla) p.getKey());
                                    temp2.add((Tupla) p.getValue());
                                }
                            }
                        }

                        resultado.add(temp1);
                        resultado.add(temp2);
                        break;

                }

                break;
            case "nested loop join hash": // + index scan
                aux = FROM.get(0).getAttribute("tabela").toString();
                aux = aux.substring(0, aux.length() - 2);
                switch (aux) {
                    case "dependente":
                        //dependente e empregado
                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");

                        pair = nestedLoopJoinHash(empregado, dependente, operadores[0], 8192);

                        operadores[1] = operadores[1].substring(2, operadores[1].length());

                        temp = indexScan(empregado, operadores[1], 8192);

                        for (Pair p : pair) {
                            for (Tupla t : temp) {
                                if ((Tupla) p.getKey() == t) {
                                    temp1.add((Tupla) p.getKey());
                                    temp2.add((Tupla) p.getValue());
                                }
                            }
                        }

                        resultado.add(temp1);
                        resultado.add(temp2);

                        break;

                    case "departamento":
                        //departamento e empregado
                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");

                        pair = nestedLoopJoinHash(dependente, departamento, operadores[0], 8192);

                        operadores[1] = operadores[1].substring(2, operadores[1].length());

                        temp = indexScan(empregado, operadores[1], 8192);

                        for (Pair p : pair) {
                            for (Tupla t : temp) {
                                if ((Tupla) p.getKey() == t) {
                                    temp1.add((Tupla) p.getKey());
                                    temp2.add((Tupla) p.getValue());
                                }
                            }
                        }

                        resultado.add(temp1);
                        resultado.add(temp2);
                        break;
                }
                break;

            case "merge join":
                aux = FROM.get(0).getAttribute("tabela").toString();
                aux = aux.substring(0, aux.length() - 2);

                switch (aux) {

                    case "dependente":
                        //dependente e empregado
                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");
                        pair = mergeJoin(empregado_Matri_Ordered, dependente_MatriResp_Ordered, operadores[0]);
                        return convertPairToArrayList(pair);
                    case "departamento":
                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");
                        pair = mergeJoin(empregado_lotacao_Ordered, departamento, operadores[0]);
                        return convertPairToArrayList(pair);
                }

            case "hash join":
                aux = FROM.get(0).getAttribute("tabela").toString();
                aux = aux.substring(0, aux.length() - 2);
                switch (aux) {
                    case "dependente":
                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");
                        pair = hashJoin(dependente, empregado, operadores[0], 2048, 8192);
                        return convertPairToArrayList(pair);
                    case "departamento":
                        aux = WHERE.getAttribute("condicao");
                        operadores = aux.split(",");
                        pair = hashJoin(empregado, departamento, operadores[0], 8192, 16);
                        return convertPairToArrayList(pair);
                }
                break;

            default:
                System.out.println("ERROR ON GRAPH");
                break;
        }
        return resultado;
    }

    private ArrayList<ArrayList<Tupla>> convertPairToArrayList(ArrayList<Pair> pair) {
        ArrayList<Tupla> chave = new ArrayList<>();
        ArrayList<Tupla> valor = new ArrayList<>();
        for (Pair p : pair) {
            chave.add((Tupla) p.getKey());
            valor.add((Tupla) p.getValue());
        }
        ArrayList<ArrayList<Tupla>> res = new ArrayList<>();
        res.add(chave);
        res.add(valor);
        return res;
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

    //ordenada pela condicao de igualdade
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
    public ArrayList<Tupla> indexSeek(ArrayList<Pagina> pagina, String condicao, int hashPrime) {
        ArrayList<Tupla> res = new ArrayList<>();
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        ArrayList<Tupla> aux1 = buscaLinear(pagina);

        try {
            Hash h = new Hash(hashPrime, pagina);
            String[] atrib = condicao.split("\\s");

            Field f = aux1.get(0).getClass().getDeclaredField(atrib[0]);
            f.setAccessible(true);
            h.criarBuckets(f);

            for (Tupla t : aux1) {
                if ((boolean) engine.eval(f.get(t) + "==" + atrib[2])) {
                    res.add(h.buscarTupla((int) f.get(t), f));
                    return res;
                }
            }

        } catch (SecurityException | ScriptException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    //nao igualdade
    public ArrayList<Tupla> indexScan(ArrayList<Pagina> tabela, String condicao, int HashPrime) {
        ArrayList<Tupla> res = new ArrayList<>();
        try {
            ArrayList<Tupla> aux = buscaLinear(tabela);
            String[] atrib = (condicao.split("\\s"));
            if (atrib[1].equals("=")) {
                atrib[1] = "==";
            }   
            Field f = aux.get(0).getClass().getDeclaredField(atrib[0]);

            Hash h = new Hash(HashPrime, tabela);
            h.criarBuckets(f);

            h.findBucketID(HashPrime);
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");

            for (Tupla t : aux) {
                if ((boolean) engine.eval(f.getInt(t) + atrib[1] + atrib[2])) {
                    Bucket auxOverflow = h.buckets.get(h.findBucketID(f.getInt(t)));
                    while (auxOverflow != null) {
                        for (BucketTupla bt : auxOverflow.bucketTuplas) {
                            if ((boolean) engine.eval(bt.tuplaId + atrib[1] + atrib[2])) {
                                res.add(h.buscarTupla(bt.tuplaId, f));
                            }
                        }
                        auxOverflow = auxOverflow.overflow;
                    }
                }
            }

        } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException | ScriptException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public ArrayList<Pair> nestedLoopJoin(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao) {
        try {
            ArrayList<Tupla> aux1 = buscaLinear(pag1);
            ArrayList<Tupla> aux2 = buscaLinear(pag2);
            
            ArrayList<Pair> res = new ArrayList<>();

            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            
            Field f[] = new Field[2];

            String[] atrib = condicao.split("\\s");

            atrib[0] = atrib[0].substring(2, atrib[0].length());

            if (atrib[1].equals("=")) {
                atrib[1] = "==";
            }

            atrib[2] = atrib[2].substring(2, atrib[2].length());

            f[0] = aux1.get(0).getClass().getDeclaredField(atrib[0]);
            f[0].setAccessible(true);
            f[1] = aux2.get(0).getClass().getDeclaredField(atrib[2]);
            f[1].setAccessible(true);

            if (aux1.size() < aux2.size()) {
                for (Tupla t1 : aux1) {
                    for (Tupla t2 : aux2) {

                        if ((boolean) engine.eval(f[0].get(t1) + atrib[1] + f[1].get(t2))) {
                            res.add(new Pair(t1, t2));
                        }

                    }
                }
            } else {
                for (Tupla t2 : aux2) {
                    for (Tupla t1 : aux1) {

                        if ((boolean) engine.eval(f[0].get(t1) + atrib[1] + f[1].get(t2))) {

                            res.add(new Pair(t1, t2));
                        }

                    }
                }
            }

            return res;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ScriptException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //hash
    public ArrayList<Pair> nestedLoopJoinHash(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao, int hashPrime) {
        try {
            ArrayList<Pair> res = new ArrayList<>();

            ArrayList<Tupla> aux1 = buscaLinear(pag1);

            String[] atrib = condicao.split("\\s");

            atrib[0] = atrib[0].substring(2, atrib[0].length());

            atrib[2] = atrib[2].substring(2, atrib[2].length());

            Field f = aux1.get(0).getClass().getDeclaredField(atrib[0]);
            f.setAccessible(true);

            ArrayList<Tupla> teste;

            for (Tupla t : aux1) {
                System.out.println(atrib[2] + " = " + (Integer) f.get(t));
                teste = indexSeek(pag2, atrib[2] + " = " + (Integer) f.get(t), hashPrime);
                if (teste != null) {
                    teste.forEach((tupla) -> {
                        res.add(new Pair(t, tupla));
                    });
                    teste.clear();
                }

            }
            return res;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Pair> mergeJoin(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao) {
        try {
            //ordenadas pelo atributo de juncao;
            //tabela mais externa nao pode haver repeticoes(empregado matri == dependente resp_matri)

            ArrayList<Tupla> TabelaInterna = buscaLinear(pag1);
            ArrayList<Tupla> TabelaExterna = buscaLinear(pag2);

            ArrayList<Pair> res = new ArrayList<>();

            ScriptEngineManager factory = new ScriptEngineManager();

            ScriptEngine engine = factory.getEngineByName("JavaScript");
            Field f[] = new Field[2];

            String[] atrib = condicao.split("\\s");

            atrib[0] = atrib[0].substring(2, atrib[0].length());

            if (atrib[1].equals("=")) {
                atrib[1] = "==";
            }

            atrib[2] = atrib[2].substring(2, atrib[2].length());

            f[0] = TabelaInterna.get(0).getClass().getDeclaredField(atrib[0]);
            f[0].setAccessible(true);
            f[1] = TabelaExterna.get(0).getClass().getDeclaredField(atrib[2]);
            f[1].setAccessible(true);

            Tupla t = TabelaInterna.get(0);

            for (Tupla t1 : TabelaExterna) {
                while ((Integer) f[1].get(t1) >= (Integer) f[0].get(t) && TabelaInterna.indexOf(t) < TabelaInterna.size()) {
                    if ((boolean) engine.eval(f[0].get(t) + atrib[1] + f[1].get(t1))) {
                        res.add(new Pair(t, t1));
                    }
                    if (TabelaInterna.indexOf(t) < TabelaInterna.size() - 1) {
                        t = TabelaInterna.get(TabelaInterna.indexOf(t) + 1);
                    } else {
                        break;
                    }
                }
                t = TabelaInterna.get(0);
            }

            return res;

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ScriptException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ArrayList<Pair> hashJoin(ArrayList<Pagina> pag1, ArrayList<Pagina> pag2, String condicao, int hashPrime1, int hashPrime2) {
        ArrayList<Pair> res = new ArrayList<>();

        Hash h1 = new Hash(hashPrime1, pag1);
        Hash h2 = new Hash(hashPrime2, pag2);

        String[] atrib = condicao.split("\\s");

        atrib[0] = atrib[0].substring(2, atrib[0].length());

        atrib[2] = atrib[2].substring(2, atrib[2].length());
        try {
            Field f = pag1.get(0).tuplas.get(0).getClass().getDeclaredField(atrib[0]);
            f.setAccessible(true);
            h1.criarBuckets(f);

            Field f1 = pag2.get(0).tuplas.get(0).getClass().getDeclaredField(atrib[2]);
            f1.setAccessible(true);
            h2.criarBuckets(f1);

            Tupla teste;
            Bucket overflowAux;
            for (Bucket b : h1.buckets) {
                overflowAux = b;
                while (overflowAux != null) {
                    for (BucketTupla bt : overflowAux.bucketTuplas) {
                        teste = h2.buscarTupla(bt.tuplaId, f1);
                        if (teste != null) {
                            res.add(new Pair(teste, h1.buscarTupla(bt.tuplaId, f)));
                        }
                    }
                    overflowAux = overflowAux.overflow;
                }

            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

}
