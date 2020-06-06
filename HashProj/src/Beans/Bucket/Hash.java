/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.Bucket;

import Beans.Tupla.Pagina;
import Beans.Tupla.Tupla;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Matheus
 */
public class Hash {

    public ArrayList<Bucket> buckets = new ArrayList<>();
    ArrayList<Pagina> tabela;
    public int hashPrime;
    public double colisoes;
    public double overflow;

    public Hash(int hashPrime, ArrayList<Pagina> tabela) {
        this.tabela = tabela;
        this.hashPrime = hashPrime;
    }

    public void criarBuckets() {
        
        int aux;
        Set<Integer> listaux = new HashSet<Integer>();
        for (Pagina p : tabela) {
            for (Tupla t : p.tuplas) {
                aux = (hashPrime % t.primaryKey);
                listaux.add(aux);
            }
        }
               
        listaux.forEach((integer) -> {
            buckets.add(new Bucket(integer));
        });
        
        int sizeOfTabela = 0;
        
        for(Pagina p : tabela){
            for(Tupla t : p.tuplas){
                sizeOfTabela++;
            }
        }

        int bucketSize = (int) Math.ceil((double) sizeOfTabela / (double) buckets.size());

        for (Pagina p : tabela) {
            for (Tupla t : p.tuplas) {
                aux = hashPrime % t.primaryKey;
                for (Bucket bucket : buckets) {
                    if (bucket.bucketId == aux) {
                        addBucketTupla(new BucketTupla(tabela.indexOf(p), t.primaryKey), bucket, bucketSize);
                        break;
                    }
                }
            }
        }
    }

    public Tupla buscarIndice(int key) {
        int aux = hashPrime % key;

        for (Bucket b : buckets) {
            Bucket overflowAux = b;
            while (overflowAux != null) {
                if (overflowAux.bucketId == aux) {
                    for (BucketTupla bt : overflowAux.bucketTuplas) {
                        if (bt.tuplaId == key) {
                            for (Tupla t : tabela.get(bt.paginaId).tuplas) {
                                if (t.primaryKey == key) {
                                    return t;
                                }
                            }
                        }
                    }
                }
                overflowAux = overflowAux.overflow;
            }
        }

        return null;
    }

    private void addBucketTupla(BucketTupla tupla, Bucket bucket, int bucketSize) {
        if (bucket.bucketTuplas.size() < bucketSize) {
            bucket.bucketTuplas.add(tupla);
        } else {
            if (bucket.overflow == null) {
                bucket.overflow = new Bucket(bucket.bucketId);
            }
            addBucketTupla(tupla, bucket.overflow, bucketSize);
        }
    }
}
