/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.Bucket;

import Beans.Tupla.Pagina;
import Beans.Tupla.Tupla;
import java.util.ArrayList;

/**
 *
 * @author Matheus
 */
public class Hash {

    public ArrayList<Bucket> buckets = new ArrayList<>();
    public int hashPrime;
    public double colisoes;
    public double overflow;
    
    
    public Hash(int hashPrime){
        this.hashPrime = hashPrime;
    }
    
    public ArrayList<Bucket> criarBuckets(ArrayList<Pagina> tabela) {

        ArrayList<Bucket> bl = new ArrayList<>();

        int aux;
        ArrayList<Integer> listaux = new ArrayList<>();
        for (Pagina p : tabela) {
            for (Tupla t : p.tuplas) {
                aux = (hashPrime % t.primaryKey);
                listaux.add(aux);
            }
        }

        listaux.forEach((integer) -> {
            bl.add(new Bucket(integer));
        });

        int bucketSize = (int) Math.ceil((double) tabela.size() / (double) bl.size());

        for (Pagina p : tabela) {
            for (Tupla t : p.tuplas) {
                aux = hashPrime % t.primaryKey;
                for (Bucket bucket : bl) {
                    if (bucket.bucketId == aux) {
                        addBucketTupla(new BucketTupla(tabela.indexOf(p), t.primaryKey), bucket, bucketSize);
                        break;
                    }
                }
            }
        }

        return bl;
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
