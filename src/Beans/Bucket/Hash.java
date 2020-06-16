/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.Bucket;

import Beans.Tupla.Pagina;
import Beans.Tupla.Tupla;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matheus
 */
public class Hash {

    public ArrayList<Bucket> buckets = new ArrayList<>();
    public ArrayList<Pagina> tabela;
    public int hashPrime;
    public double colisoes;
    public double overflow;

    public Hash(int hashPrime, ArrayList<Pagina> tabela) {
        this.tabela = tabela;
        this.hashPrime = hashPrime;
    }

    public void criarBuckets(Field f) {
        int aux;
        Set<Integer> listaux = new HashSet<>();
        for (Pagina p : tabela) {
            for (Tupla t : p.tuplas) {
                try {
                    aux = (hashPrime % f.getInt(t));
                    listaux.add(aux);
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        listaux.forEach((integer) -> {
            buckets.add(new Bucket(integer));
        });

        int sizeOfTabela = 0;

        for(Pagina p : tabela){
            sizeOfTabela += p.tuplas.size();
        }

        int bucketSize = (int) Math.ceil((double) sizeOfTabela / (double) buckets.size());
        
        for (Pagina p : tabela) {
            for (Tupla t : p.tuplas) {
                try {
                    aux = hashPrime % (Integer) f.get(t);
                    for (Bucket bucket : buckets) {
                        if (bucket.bucketId == aux) {
                            addBucketTupla(new BucketTupla(tabela.indexOf(p), (Integer) f.get(t)), bucket, bucketSize);
                            break;
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void criarBuckets() {

        int aux;
        Set<Integer> listaux = new HashSet<>();
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

        sizeOfTabela = tabela.stream().map((p) -> p.tuplas.size()).reduce(sizeOfTabela, Integer::sum);

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

    public Tupla buscarTupla(int key) {
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

    public Tupla buscarTupla(int key, Field f) {
        int aux = hashPrime % key;

        for (Bucket b : buckets) {
            Bucket overflowAux = b;
            while (overflowAux != null) {
                if (overflowAux.bucketId == aux) {
                    for (BucketTupla bt : overflowAux.bucketTuplas) {
                        if (bt.tuplaId == key) {
                            for (Tupla t : tabela.get(bt.paginaId).tuplas) {

                                try {
                                    if (f.getInt(t) == key) {
                                        return t;
                                    }
                                } catch (IllegalArgumentException | IllegalAccessException ex) {
                                    Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
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

    public int findBucketID(int key) {
        return hashPrime % key;
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

    private void countOverflow(ArrayList<Bucket> buckets) {

        for (Bucket bucket : buckets) {
            if (bucket.overflow != null) {
                oveflowCheck(bucket.overflow);
            }
        }

    }

    private void oveflowCheck(Bucket k) {
        overflow += k.bucketTuplas.size();
        if (k.overflow != null) {
            oveflowCheck(k.overflow);
        }
    }

    public void countColisoes(ArrayList<Bucket> buckets) {
        buckets.forEach((bucket) -> {
            coliCheck(bucket);
        });
    }

    private void coliCheck(Bucket k) {
        colisoes++;
        if (k.overflow != null) {
            coliCheck(k.overflow);
        }
    }
}
