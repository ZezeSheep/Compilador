/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author rapha
 */
public class AnalisadorLexico {
    
    private List _delimiters = new ArrayList();

    public AnalisadorLexico() {
        _delimiters.add(" ");
        _delimiters.add("   ");
        _delimiters.add(',');
        _delimiters.add(';');
        _delimiters.add('+');
        _delimiters.add('-');
        _delimiters.add('*');
        _delimiters.add('/');
        _delimiters.add('%');
        _delimiters.add('>');
        _delimiters.add('<');
        _delimiters.add('=');
        _delimiters.add('!');
        _delimiters.add('&');
        _delimiters.add('|');
        _delimiters.add('(');
        _delimiters.add(')');
        _delimiters.add('{');
        _delimiters.add('}');
        _delimiters.add('\"');
        _delimiters.add('\'');
    }
    
    public static void start(){
        LeitorArquivo leitorArquivo;
        Scanner scanner = new Scanner(System.in);

        //System.out.println(">> Informe o nome do arquivo a ser compilado: ");
        //String arq = scanner.next();
        String arq = "teste1.txt";
        try {
            leitorArquivo = new LeitorArquivo(arq);
            int caracter = leitorArquivo.getChar();
            String token = "";
            while (caracter != -1) {
                //System.out.println((char)caracter);
                if(isDelimiter((char)caracter)){
                   System.out.println("Token identificado: "+token);
                   token = "";
                } else {
                   token = token + (char)caracter;
                }
                caracter = leitorArquivo.getChar();
            }
            System.out.println(">> Arquivo lido com sucesso!");
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo " + arq + " Não encontrado");
            System.exit(400);
        }
    }
    
    private static boolean isDelimiter(char c){
        //return _delimiters.contains(c);
        return c == ' ' || c == ',' || c == ';' || c == '(' || c == ')'
                || c == '=' || c == '<' || c == '>' || c == '!' || c == '/'
                || c == '\"';
    }
}
