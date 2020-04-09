/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author Matheus
 */
public class Database extends Thread {

    public ArrayList<Bucket> bucketList = new ArrayList<>();
    public ArrayList<Pagina> pageList = new ArrayList<>();
    public HashMap<Integer, String> tabela = new HashMap<>();
    // 524203
    public int hashPrime = 524203;
    
    private int overflow = 0;
    public double overflowPercentage;
    public int bucketSize = 0;
    private int colisoes;
    public int colisoesPercentage;

    public void criarPaginas(double qtdTup) {

        int counter = 1;

        while (counter <= tabela.size()) {

            pageList.add(pageList.size(), new Pagina());

            for (int i = 0; i < qtdTup; i++) {

                pageList.get(pageList.size() - 1).tuplas.put(counter, tabela.get(counter));

                counter++;
            }
        }

        JOptionPane.showMessageDialog(null, "Paginas Criadas" + "\n" + "Criando Buckets");
    }

    public void criarBuckets() {

        int aux;
        ArrayList<Integer> listaux = new ArrayList<>();
        for (Integer i : tabela.keySet()) {
            aux = (hashPrime % i);
            if (!listaux.contains(aux)) {
                listaux.add(aux);
            }
        }

        listaux.forEach((integer) -> {
            bucketList.add(new Bucket(integer));
        });

        bucketSize = (int) Math.ceil((double) tabela.size() / (double) bucketList.size());

        for (Pagina p : pageList) {
            for (Integer i : p.tuplas.keySet()) {
                aux = (hashPrime % i);
                for (Bucket bucket : bucketList) {
                    if (bucket.bucketId == aux) {
                        addBucketTupla(new BucketTupla(pageList.indexOf(p), i), bucket);
                        break;
                    }
                }
            }
        }

        countOverflow();

        this.overflowPercentage = ((double) overflow / (double) tabela.size()) * 100;

        countColisoes();
        
        this.colisoesPercentage = 0;
        
        JOptionPane.showMessageDialog(null, "Database Criado");

    }

    public void addBucketTupla(BucketTupla tupla, Bucket bucket) {
        if (bucket.bucketTuplas.size() <= bucketSize) {
            bucket.bucketTuplas.add(tupla);
        } else {
            if (bucket.overflow == null) {
                bucket.overflow = new Bucket(bucket.bucketId);
            }
            addBucketTupla(tupla, bucket.overflow);
        }
    }

    public void countOverflow() {
        bucketList.stream().filter((bucket) -> (bucket.overflow != null)).forEachOrdered((bucket) -> {
            bucketCheck(bucket.overflow);
        });
    }

    public void bucketCheck(Bucket k) {
        overflow += k.bucketTuplas.size();
        if (k.overflow != null) {
            bucketCheck(k.overflow);
        }
    }
    
    public void countColisoes (){
        
    }

    public boolean buscarPalavra(int id) {

        int aux = hashPrime % id;

        int bucketId;
        int paginaId;

        for (Bucket bucket : bucketList) {
            if (bucket.bucketId == aux) {

                bucketId = bucket.bucketId;

                Bucket auxBucket = bucket;
                while (auxBucket != null) {

                    for (BucketTupla tuplas : auxBucket.bucketTuplas) {
                        if (tuplas.palavraId == id) {

                            paginaId = tuplas.paginaId + 1;

                            for (Integer i : pageList.get(tuplas.paginaId).tuplas.keySet()) {
                                if (i == id) {
                                    JOptionPane.showMessageDialog(null, "Bucket : " + bucketId + "\n" + "Pagina Numero: " + paginaId + "\n" + "Palavra: " + pageList.get(tuplas.paginaId).tuplas.get(i));
                                    return true;
                                }
                            }
                        }
                    }

                    auxBucket = auxBucket.overflow;

                }

            }

        }

        return false;
    }
}
