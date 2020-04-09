/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.util.ArrayList;

/**
 *
 * @author Matheus
 */
public class Bucket {
    public int bucketId;
    public ArrayList<BucketTupla> bucketTuplas = new ArrayList<>();
    public Bucket overflow;
    
    public Bucket(int bucket){
        this.bucketId = bucket;
    }
        
}
