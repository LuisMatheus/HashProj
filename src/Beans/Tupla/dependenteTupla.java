/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.Tupla;

/**
 *
 * @author Matheus
 */
public class dependenteTupla extends Tupla{
    int matri_resp;
    String nome = new String();
    
    public dependenteTupla(int matri_resp , String nome){
        this.matri_resp = matri_resp;
        this.nome = nome;
        this.primaryKey = matri_resp;
    }
    
}
