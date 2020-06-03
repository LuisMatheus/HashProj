/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Beans.Grafo.Graph;
import Beans.Grafo.GraphNode;
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

    public Graph g = new Graph();

    public void carregarArquivos(int qtdTpl) {

        consultas.add("SELECT\\s(\\w+|\\*)\\sFROM\\s(\\w+)");
        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\sFROM\\s(\\w+)");
        consultas.add("SELECT\\s(\\w+|\\*)\\s,\\s(\\w+)\\sFROM\\s(\\w+)\\s,\\s(\\w+)");
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
        int counter = 0;

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

        counter = 0;

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

        counter = 0;

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

        departamento.get(0).tuplas.add(new departamentoTupla(1, "VENDAS", 1));
        departamento.get(0).tuplas.add(new departamentoTupla(2, "SUPORTE", 2));
        departamento.get(0).tuplas.add(new departamentoTupla(3, "DESENVOLVIMENTO", 3));
        departamento.get(0).tuplas.add(new departamentoTupla(4, "RECURSOS HUMANOS", 4));
        departamento.get(0).tuplas.add(new departamentoTupla(5, "ATENDIMENTO AO CLIENTE", 5));
        departamento.get(0).tuplas.add(new departamentoTupla(6, "CONTROLE DE QUALIDADE", 6));
        departamento.get(0).tuplas.add(new departamentoTupla(7, "DIRETORIA", 7));
        departamento.get(0).tuplas.add(new departamentoTupla(8, "RECRUTAMENTO", 8));
        departamento.get(0).tuplas.add(new departamentoTupla(9, "ACESSORIA JURIDICA", 9));
        departamento.get(0).tuplas.add(new departamentoTupla(10, "MARKETING", 10));
        departamento.get(0).tuplas.add(new departamentoTupla(11, "PESQUISA", 11));
        departamento.get(0).tuplas.add(new departamentoTupla(12, "INVESTIMENTO", 12));
        departamento.get(0).tuplas.add(new departamentoTupla(13, "CONTROLADORIA", 13));
        departamento.get(0).tuplas.add(new departamentoTupla(14, "CONTABILIDADE", 14));
        departamento.get(0).tuplas.add(new departamentoTupla(15, "ADMINISTRACAO", 15));
        departamento.get(0).tuplas.add(new departamentoTupla(16, "EXECUTIVO", 16));
        departamento.get(0).tuplas.add(new departamentoTupla(17, "SOCIO", 17));
        departamento.get(0).tuplas.add(new departamentoTupla(18, "LOGISTICA", 18));
        departamento.get(0).tuplas.add(new departamentoTupla(19, "INOVACAO", 19));
        departamento.get(0).tuplas.add(new departamentoTupla(20, "RELACAO COM O CLIENTE", 20));

    }

    public ArrayList<ArrayList<String>> parser(String query) {
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

                for (int i = 1; i < keys.length; i++) {

                    if (i < counterFrom && !",".equals(keys[i])) {
                        SELECT.add(keys[i]);
                    } else if (i > counterFrom && i < counterWhere && !",".equals(keys[i])) {
                        FROM.add(keys[i]);
                    } else if (i > counterWhere && !",".equals(keys[i])) {
                        WHERE.add(keys[i]);
                    }

                }
                ArrayList<ArrayList<String>> res = new ArrayList<>();
                res.add(SELECT);
                res.add(FROM);
                res.add(WHERE);
                return res;

            }

        }
        return null;
    }

    public ArrayList<ArrayList<Tupla>> processadorDeConsulta(Graph graph) {
        ArrayList<GraphNode> FROM = new ArrayList<>();
        GraphNode WHERE = new GraphNode();

        graph.graph.forEach((n) -> {
            FROM.add(n);
        });

        GraphNode JUNCAO = FROM.get(0).arestas;

        WHERE = JUNCAO.arestas;

        ArrayList<Tupla> res = new ArrayList<>();
        ArrayList<ArrayList<Tupla>> proj = new ArrayList<>();

        for (GraphNode n : FROM) {
            if (n.arestas.nome.get(0).contentEquals("Busca Linear")) {
                switch (n.nome.get(0)) {
                    case "empregado":
                        res = buscaLinear(empregado, WHERE.nome.get(0));
                        break;
                    case "departamento":
                        res = buscaLinear(departamento, WHERE.nome.get(0));
                        break;
                    case "dependente":
                        res = buscaLinear(dependente, WHERE.nome.get(0));
                        break;
                    default:
                        System.out.println("ERROR");
                        break;
                }
            } else if (n.arestas.nome.get(0).contentEquals("Busca Binaria")) {
                switch (n.nome.get(0)) {
                    case "empregado":
                        res = buscaBinario(empregadoOrdered, WHERE.nome.get(0));
                        break;
                    case "departamento":
                        res = buscaBinario(departamento, WHERE.nome.get(0));
                        break;
                    case "dependente":
                        res = buscaBinario(dependente, WHERE.nome.get(0));
                        break;
                    default:
                        System.out.println("ERROR");
                        break;
                }
            }

            proj.add(res);
        }

        return proj;
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
    public ArrayList<Tupla> buscaBinario(ArrayList<Pagina> tabela, String condicao) {
        ArrayList<Tupla> res = new ArrayList<>();
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

            res.add(aux.get(mid));

        } catch (NoSuchFieldException | SecurityException | ScriptException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    //nao igualdade
    public ArrayList<Tupla> indexScan(ArrayList<Pagina> tabela, String condicao) {

        return null;
    }

    //hash
    public ArrayList<Tupla> indexSeek(ArrayList<Pagina> pagina, String condicao) {

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
