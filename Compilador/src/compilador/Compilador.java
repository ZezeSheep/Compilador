/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Raphael Assis
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AnalisadorLexico analizadorLexico = new AnalisadorLexico("teste6.txt");
        Ambiente tabelaSimbolos = new Ambiente(null);
        
        System.out.println(">> Iniciando compilador");
        Token t;
        while(!analizadorLexico.isEOF){
            t = analizadorLexico.scan();
            if(!analizadorLexico.error && t.getTag() != Constantes.EOF){
                System.out.println(">>Token: "+t);
                if(analizadorLexico.getPalavras().contains(t)){
                    if(t.getTag() == Constantes.ID)
                        tabelaSimbolos.adicionaSimbolo(t, new Identificador());
                    else 
                        tabelaSimbolos.adicionaSimbolo(t, new PalavraReservada());
                }
            } 
        }
        
        //System.out.println(analizadorLexico.getPalavras().toString());
        tabelaSimbolos.printTable();
        System.out.println(">> Análise léxica concluída");
        if(analizadorLexico.error){
                System.out.println("com erro.");
        }
    }

}
