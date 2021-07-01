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
        System.out.println(">> Iniciando compilador");
        analizadorLexico.scan();
        System.out.println(analizadorLexico.getPalavras().toString());
        System.out.println(">> Análise léxixa concluída");
    }

}
