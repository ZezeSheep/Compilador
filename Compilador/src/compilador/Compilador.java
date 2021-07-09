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
        AnalisadorLexico analizadorLexico = new AnalisadorLexico("teste2.txt");
        Ambiente tabelaSimbolos = new Ambiente(null);
        
        System.out.println(">> Iniciando compilador");
        Token t;
        System.out.println(">>\t Iniciando análise léxica");
        System.out.println(">>\t Fluxo de tokens:");
        while(!analizadorLexico.isEOF){
            t = analizadorLexico.scan();
            if(!analizadorLexico.error && t.getTag() != Constantes.EOF){
                System.out.println(">>\t\t Token: "+t);
                if(analizadorLexico.getPalavras().contains(t)){
                    if(t.getTag() == Constantes.ID)
                        tabelaSimbolos.adicionaSimbolo(t, new Identificador());
                    else 
                        tabelaSimbolos.adicionaSimbolo(t, new PalavraReservada());
                }
            } 
        }
        System.out.println(">>\t Fim do fluxo de tokens");
        System.out.println(">>\t Tabela de símbolos incompleta:");
        tabelaSimbolos.printTable(2);
        System.out.println(">>\t Análise léxica concluída");
        if(analizadorLexico.error){
                System.out.println(">>\t com erro.");
                for(String s: analizadorLexico.getErros()){
                    System.out.println("**\t\t" + s);
                }
        }
        System.out.println(">> Compilação concluída");
    }

}
