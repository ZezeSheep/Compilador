/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author rapha
 */
public class AnalisadorLexico {

    private Hashtable palavras;
    private LeitorArquivo leitorArquivo;
    private char caracter;
    public static int linha = 1;
    public boolean isEOF;
    public static boolean error = false;

    public Hashtable getPalavras() {
        return palavras;
    }

    private void reservar(Palavra palavra) {
        palavras.put(palavra.getLexeme(), palavra);
    }

    public AnalisadorLexico(String nomeArquivo) {
        palavras = new Hashtable();
        leitorArquivo = new LeitorArquivo(nomeArquivo);
        caracter = ' ';
        isEOF = false;
        reservar(Palavra.CLASS);
        reservar(Palavra.IF);
        reservar(Palavra.ELSE);
        reservar(Palavra.DO);
        reservar(Palavra.WHILE);
        reservar(Palavra.INIT);
        reservar(Palavra.STOP);
        reservar(Palavra.READ);
        reservar(Palavra.INT);
        reservar(Palavra.STRING);
        reservar(Palavra.FLOAT);
    }

    private boolean isDelimiter(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\b';
    }

    private void lerCaracter() {
        caracter = (char)leitorArquivo.getChar();
    }

    private boolean verificarProximoCaracter(char c) {
        lerCaracter();
        return caracter == c;
    }

    private ConstanteNumerica lerNumero() {
        if (caracter == '0') {
            caracter = ' ';
            return new ConstanteNumerica(0);
        } else {
            double valor = 0;
            do {
                valor = 10 * valor + (caracter - '0');
                lerCaracter();
            } while (Character.isDigit(caracter));
            if (caracter != '.') {
                return new ConstanteNumerica((int) valor); //leu valor inteiro
            } else { //leu valor real
                lerCaracter();
                if (Character.isDigit(caracter)) {
                    int expoente = -1;
                    do {
                        valor = valor + (caracter - '0') * (Math.pow(10, expoente));
                        expoente--;
                        lerCaracter();
                    } while (Character.isDigit(caracter));
                    return new ConstanteNumerica(valor);
                } else {  // um erro de sintaxe do tipo EX: 123.a
                    return null;
                }
            }
        }
    }

    private Palavra lerLiteral() throws LiteralBadFormattedException {
        StringBuffer valorLiteral = new StringBuffer();    
        lerCaracter();
        while (caracter != '"') {
            if(caracter == Constantes.EOF){
                throw new LiteralBadFormattedException("String literal sem fecha aspas, verifique sua string!");//lansar a braba
            }
            if (caracter != '\n') {
                valorLiteral.append(caracter);
            }else{
                System.out.println(">>PLIss help");
                throw new LiteralBadFormattedException("String literal sem fecha aspas, verifique sua string!");//lansar a braba
            }
            lerCaracter();
        }
        caracter = ' ';
        return new Palavra(valorLiteral.toString(), Constantes.LITERAL);
    }

    private Token lerIdentidicador() { //esse metodo precisa ser alterado
        StringBuffer nomeIdentidicador = new StringBuffer();
        do {
            nomeIdentidicador.append(caracter);
            lerCaracter();
        } while (Character.isLetterOrDigit(caracter) || caracter == '_');
        String s = nomeIdentidicador.toString();
        Palavra w = (Palavra) palavras.get(s);
        if (w != null) {
            return w; //palavra j� existe na HashTable
        }
        w = new Palavra(s, Constantes.ID);
        palavras.put(s, w);
        return w;
    }

    public Token scan() {
        //System.out.println(">> Entrou scan...");
        for (;; lerCaracter()) {
            //System.out.println(">>dentro for");
            if (isDelimiter(caracter)) {
                continue;
            } else if (caracter == '\n') {
                linha++;
            } else if (caracter == '/') {
                lerCaracter();
                if (caracter == '/'){
                    while (caracter != '\n') {
                        lerCaracter();
                    }
                    linha++;
                } else if (caracter == '*') {
                    lerCaracter();
                    while (true) {
                        if (caracter == '*') {
                            lerCaracter();
                            if (caracter == '/') {
                                break;
                            }
                            else if(caracter == '*'){
                                continue;
                            }
                        }
                        if (caracter == '\n') {
                            linha++;
                        }
                        lerCaracter();
                    }
                }
                else{
                    return new Token('/');
                }
            } 
            else {
                break;
            }
        }
        
        switch (caracter) {
            case Constantes.EOF:
                isEOF = true;
                break;
            case '&':
                if (verificarProximoCaracter('&')) {
                    return Palavra.AND;
                } else {
                    return new Token('&');
                }
            case '=':
                if (verificarProximoCaracter('=')) {
                    return Palavra.EQ;
                } else {
                    return new Token('=');
                }
            case '!':
                if (verificarProximoCaracter('=')) {
                    return Palavra.NE;
                } else {
                    return new Token('!');
                }
            case '>':
                if (verificarProximoCaracter('=')) {
                    return Palavra.GE;
                } else {
                    return Palavra.GT;
                }
            case '<':
                if (verificarProximoCaracter('=')) {
                    return Palavra.LE;
                } else {
                    return Palavra.LT;
                }
        }

        if (Character.isDigit(caracter)) {
            return lerNumero();
        }
        if (caracter == '"'){
            try{
                return lerLiteral();
            }catch(Exception e){
                System.err.println("erro léxico linha: " + linha);
                System.err.println(e);
                error = true;
            }
        }
        if (Character.isLetter(caracter)) {
            return lerIdentidicador();
        }
        
        Token token = new Token(caracter);
        caracter = ' ';
        return token;
    }
}
