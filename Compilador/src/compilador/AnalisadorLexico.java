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


	private void reservar(Palavra palavra) {
		palavras.put(palavra.getLexeme(), palavra);
	}

	public AnalisadorLexico(String nomeArquivo) {
		palavras = new Hashtable();   
		leitorArquivo = new LeitorArquivo(nomeArquivo);		
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

	private boolean isDelimiter(char c){
		//return _delimiters.contains(c);
		return c == ' ' || c == '\t' || c == '\r' || c == '\b';
	}

	private void lerCaracter() {
		caracter = leitorArquivo.getChar();
	}
	
	private boolean verificarProximoCaracter(char c) {
		lerCaracter();
		return caracter == c;
	}

	public Token scan() {

		for(;;lerCaracter()) {
			if(isDelimiter(caracter)) {
				continue;
			}
			else if(caracter == '\n')
				linha++;
			else
				break;
		}

		switch(caracter){
			case '&':
				if(verificarProximoCaracter('&'))
					return Palavra.AND;
				else
					return new Token('&');
			case '=':
				if(verificarProximoCaracter('='))
					return Palavra.EQ;
				else
					return new Token('=');
			case '!':
				if(verificarProximoCaracter('='))
					return Palavra.NE;
				else
					return new Token('!');
			case '>':
				if(verificarProximoCaracter('='))
					return Palavra.GE;
				else
					return Palavra.GT;
			case '<':
				if(verificarProximoCaracter('='))
					return Palavra.LE;
				else
					return Palavra.LT;
		}


	}
}
