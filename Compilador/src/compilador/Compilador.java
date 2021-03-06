/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import AnalisadorLexico.AnalisadorLexico;
import AnalisadorSintatico.AnalisadorSintatico;
import TabelaDeSimbolos.Ambiente;

/**
 *
 * @author Raphael Assis
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AnalisadorLexico analisadorLexico = new AnalisadorLexico(args[0]);
        Ambiente tabelaSimbolos = new Ambiente(null);
        AnalisadorSintatico analisadorSintatico = new AnalisadorSintatico(analisadorLexico, args[0]);
        analisadorSintatico.startAnalysis();        
        System.out.println(">> Compilacao concluida");
    }

}
