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
public class empregadoTupla extends Tupla {
    int matri;
    String nome = new String();
    double salario;
    int lotacao;

    public empregadoTupla(int matri, String nome , double salario , int lotacao){
        this.matri = matri;
        this.nome = nome;
        this.salario = salario;
        this.lotacao = lotacao;
        this.primaryKey = matri;
    }
   
}
