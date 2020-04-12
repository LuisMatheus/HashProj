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
    // 16273
    public int hashPrime = 524203;

    private int overflow = 0;
    public double overflowPercentage;
    public int bucketSize = 0;
    private int bucketCount = 0;
    
    public int bucketId_busca;
    public int paginaId_busca;
    public String resultado_Busca;

    private int colisoes = 0;
    
    public double colisoesPercentage;

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
       
        this.colisoesPercentage = (((double)colisoes / (double)bucketCount) / (double) bucketSize) * 100;

        JOptionPane.showMessageDialog(null, "Database Criado");

    }

    private void addBucketTupla(BucketTupla tupla, Bucket bucket) {
        if (bucket.bucketTuplas.size() <= bucketSize) {
            bucket.bucketTuplas.add(tupla);
        } else {
            if (bucket.overflow == null) {
                bucket.overflow = new Bucket(bucket.bucketId);
            }
            addBucketTupla(tupla, bucket.overflow);
        }
    }

    private void countOverflow() {

        bucketList.stream().filter((bucket) -> (bucket.overflow != null)).forEachOrdered((bucket) -> {
            oveflowCheck(bucket.overflow);
        });
    }

    private void oveflowCheck(Bucket k) {
        overflow += k.bucketTuplas.size();
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
        //TODO
        bucketCount++;
        colisoes += k.bucketTuplas.size() ;
        if(k.overflow != null){
            coliCheck(k.overflow);
        }
    }

    public boolean buscarPalavra(int id) {

        int aux = hashPrime % id;

        for (Bucket bucket : bucketList) {
            if (bucket.bucketId == aux) {

                this.bucketId_busca = bucket.bucketId;

                Bucket auxBucket = bucket;
                while (auxBucket != null) {

                    for (BucketTupla tuplas : auxBucket.bucketTuplas) {
                        if (tuplas.palavraId == id) {

                            this.paginaId_busca = tuplas.paginaId;

                            for (Integer i : pageList.get(tuplas.paginaId).tuplas.keySet()) {
                                
                                if (i == id) {
                                    this.resultado_Busca = pageList.get(tuplas.paginaId).tuplas.get(i);
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
    
    public Bucket getBucket(int bucketId){
        for(Bucket bucket: bucketList){
            if(bucket.bucketId == bucketId){
                return bucket;
            }
        }
        return null;
    }
    
    public Pagina getPagina(int pos){
        return pageList.get(pos);
    }
}
