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
public class departamentoTupla extends Tupla {

    int cod_dep;
    String nome = new String();
    
    public departamentoTupla(int cod_dep, String nome) {
        this.cod_dep = cod_dep;
        this.nome = nome;
        this.primaryKey = cod_dep;
    }

}
