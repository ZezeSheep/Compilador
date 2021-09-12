/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalisadorSintatico;

import AnalisadorLexico.AnalisadorLexico;
import Exception.ErroSemanticoException;
import Exception.ErroSintaticoException;
import TabelaDeSimbolos.Ambiente;
import TabelaDeSimbolos.Variavel;
import Tokens.ConstanteNumerica;
import Tokens.Palavra;
import Tokens.Token;
import compilador.Constantes;

/**
 *
 * @author Meu Computador
 */
public class AnalisadorSintatico {
    
    private AnalisadorLexico lexer;
    private Ambiente tabelaSimbolosAtual;
    private int linhaErroSemantico;
    private GeradorCodigo geradorCodigo;
    private int numeroVariaveis;
    private boolean isDivisao;
    private int currentLabelIndexIF;
    private int currentLabelIndexDO;
    
    private Token tok;
    
    public AnalisadorSintatico(AnalisadorLexico lexer, String nomeArquivoEntrada) {
        this.lexer = lexer;
        tabelaSimbolosAtual = new Ambiente(null);
        linhaErroSemantico = -1;
        String nomeArquivoExterno = "C:\\Users\\josec\\Desktop\\vm\\" + nomeArquivoEntrada.replace(".txt", ".o");
        geradorCodigo = new GeradorCodigo(nomeArquivoExterno);
        geradorCodigo.escreverStringEmArquivo("START");
        numeroVariaveis = 0;
        currentLabelIndexIF = 0;
        currentLabelIndexDO = 0;
    }
    
    private void printLabel(String nomeLabel, int index) {
    	geradorCodigo.escreverStringEmArquivo(nomeLabel+index+":");
    }
    
    private void advance(){
        tok = lexer.scan();
    }
    
    private void eat(Token t) throws ErroSintaticoException{
        if(t.getTag() == this.tok.getTag()) advance();
        else error();
    }
    
    private void error() throws ErroSintaticoException{
        throw new ErroSintaticoException("Erro na linha : " + lexer.linha);
    }
    
    private void setarLinhaErroSemantico(int linhaAtual){
        if(linhaErroSemantico==-1)
        	linhaErroSemantico = linhaAtual;
    }
    
    /*
    program -> class id program_prime
    program_prime -> body | decl_list body
    */
    
    public void startAnalysis(){
    	advance();
    	try {
	        switch(tok.getTag()){
	            case Constantes.CLASS: int tipoPrograma = program();
	            					   if(tipoPrograma == Constantes.ERRO) {
	            						   throw new ErroSemanticoException("Erro semantico na linha: " +linhaErroSemantico+"\n");
	            					   }
	                                   eat(new Token(Constantes.EOF));
	                                   geradorCodigo.escreverStringEmArquivo("STOP");
	                                   geradorCodigo.concluirEscrita();
	                                   break;
	
	            default: error(); break;
	        }
	        System.out.println("Analise sintatica concluida com sucesso, 0 erros encontrados.");
        }catch(ErroSintaticoException e) {
        	e.printStackTrace();
        	System.out.println("Compilacao concluida com erro");
        	geradorCodigo.cancelarArquivo();
        } catch (ErroSemanticoException e) {
			e.printStackTrace();
			System.out.println("Compilacao concluida com erro");
			geradorCodigo.cancelarArquivo();
		}
    }
    
    private int program() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.CLASS: eat(new Token(Constantes.CLASS));
                                   eat(new Token(Constantes.ID));
                                   return program_prime();

            default: error(); break;
        }
		return 0;
    }
    
    private int program_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: int tipoDecList = decl_list();
            						if(tipoDecList == Constantes.VAZIO) {
            							geradorCodigo.escreverStringEmArquivo("PUSHN "+numeroVariaveis);
            							return body();
            						}
            						else {
            							setarLinhaErroSemantico(lexer.linha);
            							return Constantes.ERRO;
            							}
                                   
                                   
            case Constantes.INIT: return body();            

            default: error(); break;
        }
		return 0;
    }
    
    /*
    decl_list -> decl ; decl_list_prime
    decl_list_prime ->  decl ; decl_list_prime | lambda
    */
    
    private int decl_list() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: int tipodecl = decl();
            						eat(new Token(';'));
            						int tipoDeclListPrime = decl_list_prime();
            						if(tipoDeclListPrime == Constantes.VAZIO && tipodecl == Constantes.VAZIO)
            							return Constantes.VAZIO;
            						else {
            							setarLinhaErroSemantico(lexer.linha);
            							return Constantes.ERRO;
            							}
                
            default: error(); break;
        }
		return 0;
    }
    
    private int decl_list_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: int tipodecl = decl();
                                   eat(new Token(';'));
                                   int tipoDeclListPrime = decl_list_prime();
                                   if(tipoDeclListPrime == Constantes.VAZIO && tipodecl == Constantes.VAZIO)
                                	   return Constantes.VAZIO;
                                   else {
                                	   setarLinhaErroSemantico(lexer.linha);
                                	   return Constantes.ERRO;                                   
                                   }
                
            default: return Constantes.VAZIO;
        }
    }
    
    private int decl() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: int tipoType = type();
                                   ident_list(tipoType);
                                   return Constantes.VAZIO;

            default: error(); return Constantes.VAZIO;
        }
    }
    
   /*
    ident_list -> id ident_list_prime
    ident_list_prime -> , id ident_list_prime | lambda
    */
    
    private int ident_list(int tipoType) throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: Token aux = tok;
            					eat(new Token(Constantes.ID));
            					Palavra auxPalavra = (Palavra) aux;
                                Variavel variavel = new Variavel(tipoType);
                                Boolean isSymbolAdded = tabelaSimbolosAtual.adicionaSimbolo(auxPalavra.getLexeme(), variavel);
                                int tipoiDentList = ident_list_prime(tipoType);
                                if(tipoiDentList == Constantes.VAZIO && isSymbolAdded) {
                                	numeroVariaveis++;                               
                                	return Constantes.VAZIO;
                                }
                                else {
                                	setarLinhaErroSemantico(lexer.linha);
                                	return Constantes.ERRO;
                                }

            default: error(); return Constantes.VAZIO;
        }
    }
    
    private int ident_list_prime(int tipoType) throws ErroSintaticoException{
        switch(tok.getTag()){
            case ',': eat(new Token(','));
            		  Token aux = tok;
                      eat(new Token(Constantes.ID));
                      Palavra auxPalavra = (Palavra) aux;
                      Variavel variavel = new Variavel(tipoType);
                      Boolean isSymbolAdded = tabelaSimbolosAtual.adicionaSimbolo(auxPalavra.getLexeme(), variavel);
                      int tipoiDentList = ident_list_prime(tipoType);
                      if(tipoiDentList == Constantes.VAZIO && isSymbolAdded) {
                    	  numeroVariaveis++;
                    	  return Constantes.VAZIO;
                      }
                      else {
                    	  setarLinhaErroSemantico(lexer.linha);
                    	  return Constantes.ERRO;
                      }

            default: return Constantes.VAZIO;
        }
    }
    
    private int type() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT: eat(new Token(Constantes.INT)); return Constantes.INT;
            case Constantes.STRING: eat(new Token(Constantes.STRING)); return Constantes.STRING;
            case Constantes.FLOAT: eat(new Token(Constantes.FLOAT)); return Constantes.FLOAT;

            default: error(); break;
        }
		return 0;
    }
    
    private int body() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INIT: eat(new Token(Constantes.INIT));
                                  int tipoStmtList = stmt_list();
                                  eat(new Token(Constantes.STOP));
                                  return tipoStmtList;

            default: error(); break;
        }
		return 0;
    }
    
    /*
    stmt_list -> stmt ; stmt_list_prime
    stmt_list_prime ->  stmt ; stmt_list_prime | lambda
    */
    
    private int stmt_list() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.IF: 
            case Constantes.DO: 
            case Constantes.READ: 
            case Constantes.WRITE: int tipoStmt = stmt();
            					   eat(new Token(';'));
            					   int tipoStmtListPrime = stmt_list_prime();
            					   if(tipoStmt == Constantes.VAZIO && tipoStmtListPrime == Constantes.VAZIO)
            						   return Constantes.VAZIO;
            					   else {
            						   setarLinhaErroSemantico(lexer.linha);
            						   return Constantes.ERRO;
            					   }
                
            default: error(); break;
        }
		return 0;
    }
    
    private int stmt_list_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.IF: 
            case Constantes.DO: 
            case Constantes.READ: 
            case Constantes.WRITE: int tipoStmt = stmt();
                                   eat(new Token(';'));
                                   int tipoStmtListPrime = stmt_list_prime();
                                   if(tipoStmt == Constantes.VAZIO && tipoStmtListPrime == Constantes.VAZIO)
                                	   return Constantes.VAZIO;
                                   else {
                                	   setarLinhaErroSemantico(lexer.linha);
                                	   return Constantes.ERRO;
                                   }
                
            default: return Constantes.VAZIO;
        }
    }
    
    private int stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
        	
            case Constantes.ID: return assign_stmt();
            case Constantes.IF: return if_stmt();
            case Constantes.DO: return do_stmt();
            case Constantes.READ: return read_stmt();
            case Constantes.WRITE: return write_stmt();
                
            default: error(); break;
        }
		return 0;
    }
    
    private int assign_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: Token aux = tok;
            					eat(new Token(Constantes.ID));
                                eat(new Token('='));
                                int tipoSimpleExpr = simple_expr();                                
                                if(tipoSimpleExpr == tabelaSimbolosAtual.obter_tipo((Palavra)aux)) {
                                	int offsetSimbolo = tabelaSimbolosAtual.obter_offset((Palavra)aux);
                                	geradorCodigo.escreverStringEmArquivo("STOREL "+offsetSimbolo);
                                	return Constantes.VAZIO;
                                }
                                else {
                                	setarLinhaErroSemantico(lexer.linha);
                                	return Constantes.ERRO;
                                }

            default: error(); break;
        }
		return 0;
    }
    
    /*
    if_stmt -> if "(" condition ")" "{" stmt_list "}" if_stmt_prime
    if_stmt_prime -> else "{" stmt_list "}"  | lambda
    */
    
    private int if_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.IF: int indexIfAtual = currentLabelIndexIF;
            					currentLabelIndexIF++;
            					eat(new Token(Constantes.IF));
                                eat(new Token('('));
                                int tipoCondition = condition();
                                geradorCodigo.escreverStringEmArquivo("JZ ELSE"+indexIfAtual);
                                eat(new Token(')'));
                                eat(new Token('{'));
                                
                                int tipoStmtList = stmt_list();
                                eat(new Token('}'));
                                int tipoIfStmtPrime = if_stmt_prime(indexIfAtual);
                                printLabel("FINALIFELSE", indexIfAtual);
                                if(tipoCondition != Constantes.INT) {
                                	setarLinhaErroSemantico(lexer.linha);
                                	return Constantes.ERRO;
                                }
                                else if(tipoStmtList!=Constantes.ERRO) {
                                	setarLinhaErroSemantico(lexer.linha);
                                	return tipoIfStmtPrime;
                                }
                                else {
                                	setarLinhaErroSemantico(lexer.linha);
                                	return Constantes.ERRO;
                                }

            default: error(); break;
        }
		return 0;
    }
    
    private int if_stmt_prime(int indexIfAtual) throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ELSE: geradorCodigo.escreverStringEmArquivo("JUMP FINALIFELSE"+indexIfAtual);
            					  printLabel("ELSE", indexIfAtual);
            					  eat(new Token(Constantes.ELSE));
                                  eat(new Token('{'));
                                  int tipoStmtList = stmt_list(); 
                                  eat(new Token('}'));
                                  return tipoStmtList;

            default: 
            	printLabel("ELSE", indexIfAtual);
            	return Constantes.VAZIO;
        }
    }
    
    private int condition() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': return expression();

            default: error(); break;
        }
		return 0;
    }
    
    private int do_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.DO:   int indexDOatual = currentLabelIndexDO;
            					  currentLabelIndexDO++;
            					  eat(new Token(Constantes.DO));
                                  eat(new Token('{'));
                                  printLabel("DO", indexDOatual);
                                  int tipoStmtList = stmt_list();                                  
                                  eat(new Token('}'));
                                  int tipoDoSufix = do_sulfix(indexDOatual);
                                  if(tipoDoSufix == Constantes.VAZIO)
                                	  return tipoStmtList;
                                  else {
                                	  setarLinhaErroSemantico(lexer.linha);
                                	  return Constantes.ERRO;
                                  }

            default: error(); break;
        }
		return 0;
    }
    
    private int do_sulfix(int indexDOatual) throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.WHILE: eat(new Token(Constantes.WHILE));
                                  eat(new Token('('));
                                  int tipoCondition = condition();
                                  geradorCodigo.escreverStringEmArquivo("NOT");
                                  geradorCodigo.escreverStringEmArquivo("JZ DO"+indexDOatual);
                                  eat(new Token(')'));
                                  if(tipoCondition == Constantes.INT)
                                	  return Constantes.VAZIO;
                                  else {
                                	  setarLinhaErroSemantico(lexer.linha);
                                	  return Constantes.ERRO;
                                  }

            default: error(); break;
        }
		return 0;
    }
    
    private int read_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.READ: eat(new Token(Constantes.READ));
                                  eat(new Token('('));
                                  Palavra aux = (Palavra) tok;
                                  Integer offsetID = tabelaSimbolosAtual.obter_offset(aux);
                                  if(offsetID == null) {
                                	  eat(new Token(Constantes.ID));
                                      eat(new Token(')'));
                                	setarLinhaErroSemantico(lexer.linha);
                                  	return Constantes.ERRO;
                                  }
                                  Integer tipoID = tabelaSimbolosAtual.obter_tipo(aux);
                                  geradorCodigo.escreverStringEmArquivo("READ");
                                  if(tipoID == Constantes.INT) {
                                	  geradorCodigo.escreverStringEmArquivo("ATOI");
                                  }
                                  else if(tipoID == Constantes.FLOAT) {
                                	  geradorCodigo.escreverStringEmArquivo("ATOF");
                                  }
                                  geradorCodigo.escreverStringEmArquivo("STOREL "+offsetID);
                                  eat(new Token(Constantes.ID));
                                  eat(new Token(')'));
                                  return Constantes.VAZIO;

            default: error(); break;
        }
		return 0;
    }

    private int write_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.WRITE: eat(new Token(Constantes.WRITE));
                                   eat(new Token('('));
                                   int tipoWritable = writable();
                                   eat(new Token(')'));
                                   return tipoWritable;

            default: error(); break;
        }
		return 0;
    }
    
    private int writable() throws ErroSintaticoException{
        switch(tok.getTag()){
        	case Constantes.LITERAL:
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': int tipoSimpleExpr = simple_expr();
            		  if(tipoSimpleExpr != Constantes.ERRO) {
            			  if(tipoSimpleExpr == Constantes.STRING) {
            				  geradorCodigo.escreverStringEmArquivo("WRITES");            				  
            			  }
            			  else if(tipoSimpleExpr == Constantes.FLOAT) {
            				  geradorCodigo.escreverStringEmArquivo("WRITEF");
            			  }
            			  else {
            				  geradorCodigo.escreverStringEmArquivo("WRITEI");
            			  }
            			  return Constantes.VAZIO;
            		  }
            		  else {
            			  setarLinhaErroSemantico(lexer.linha);
            			  return Constantes.ERRO;
            		  }

            default: error(); break;
        }
		return 0;
    }
    
    /*
    expression -> simple_expr expression_prime
    expression_prime -> relop simple_expr expression_prime | lambda
    */
    
    private int expression() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': int tipoSimpleExpr = simple_expr();
            	int tipoExprPrime = expression_prime();
            	if(tipoExprPrime == Constantes.VAZIO) {
            		return tipoSimpleExpr;
            	}
            	else if(tipoSimpleExpr == Constantes.INT && tipoExprPrime== Constantes.INT)
            		return Constantes.INT;
            	else {
            		setarLinhaErroSemantico(lexer.linha);
            		return Constantes.ERRO;
            	}

            default: error(); break;
        }
		return 0;
    }
    
    private int expression_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.GT: 
            case Constantes.GE:
            case Constantes.LT:
            case Constantes.LE:
            case Constantes.NE:
            case Constantes.EQ: int tipoRelop = tok.getTag(); 
            					relop();
                                int tipoSimpleExpr = simple_expr();
                                if(tipoRelop == Constantes.EQ) {
                                	if(tipoSimpleExpr == Constantes.INT) {
                                		geradorCodigo.escreverStringEmArquivo("EQUAL");
                                	}
                                	else {
                                		geradorCodigo.escreverStringEmArquivo("EQUAL");
                                	}
                                }
                                else if(tipoRelop == Constantes.NE) {
                                	if(tipoSimpleExpr == Constantes.INT) {
                                		geradorCodigo.escreverStringEmArquivo("EQUAL");
                                		geradorCodigo.escreverStringEmArquivo("NOT");
                                	}
                                	else {
                                		geradorCodigo.escreverStringEmArquivo("EQUAL");
                                		geradorCodigo.escreverStringEmArquivo("NOT");
                                	}
                                }
                                else if(tipoRelop == Constantes.LE) {
                                	if(tipoSimpleExpr == Constantes.INT) {
                                		geradorCodigo.escreverStringEmArquivo("INFEQ");
                                	}
                                	else {
                                		geradorCodigo.escreverStringEmArquivo("FINFEQ");
                                	}
                                }
                                else if(tipoRelop == Constantes.LT) {
                                	if(tipoSimpleExpr == Constantes.INT) {
                                		geradorCodigo.escreverStringEmArquivo("INF");
                                	}
                                	else {
                                		geradorCodigo.escreverStringEmArquivo("FINF");
                                	}
                                }
                                else if(tipoRelop == Constantes.GE) {
                                	if(tipoSimpleExpr == Constantes.INT) {
                                		geradorCodigo.escreverStringEmArquivo("SUPEQ");
                                	}
                                	else {
                                		geradorCodigo.escreverStringEmArquivo("FSUPEQ");
                                	}
                                }
                                else {
                                	if(tipoSimpleExpr == Constantes.INT) {
                                		geradorCodigo.escreverStringEmArquivo("SUP");
                                	}
                                	else {
                                		geradorCodigo.escreverStringEmArquivo("FSUP");
                                	}
                                }
                                int tipoExprPrime = expression_prime();
                                if(tipoExprPrime == Constantes.VAZIO) {
                            		return tipoSimpleExpr;
                            	}
                            	else if(tipoSimpleExpr == Constantes.INT && tipoExprPrime== Constantes.INT)
                            		return Constantes.INT;
                            	else {
                            		setarLinhaErroSemantico(lexer.linha);
                            		return Constantes.ERRO;
                            	}
                                
            default: return Constantes.VAZIO;
        }
    }
    
     /*
    simple_expr -> term simple_expr_prime
    simple_expr_prime -> addop term simple_expr_prime | lambda
    
    */
    
    private int simple_expr() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.LITERAL:
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': int tipoTerm = term();
                      int tipoSimpleExprPrime = simple_expr_prime(tipoTerm);
                      if(tipoTerm == Constantes.ERRO || tipoSimpleExprPrime == Constantes.ERRO) {
                    	  setarLinhaErroSemantico(lexer.linha);
                    	  return Constantes.ERRO;
                      }
						else if(tipoSimpleExprPrime == Constantes.VAZIO) {
							
							return tipoTerm;
						}
						else if(tipoTerm!=tipoSimpleExprPrime) {
		                	  setarLinhaErroSemantico(lexer.linha);
		                	  return Constantes.ERRO;
		                 }
		            	else
		            		return tipoTerm;

            default: error(); break;
        }
		return 0;
    }
   // simple_expr_prime -> addop term simple_expr_prime | lambda
    private int simple_expr_prime(int tipoTermo) throws ErroSintaticoException{
        switch(tok.getTag()){
            case '+':
            case '-':
            case Constantes.OR: 
            					Token aux = tok;
            					addop();
            					int tipoTerm = term();
            					int tipoSimpleExprPrime = simple_expr_prime(tipoTerm);
            					switch(aux.getTag()) {
            						case '+':
            							if(tipoTerm == Constantes.ERRO || tipoSimpleExprPrime == Constantes.ERRO) {
            								setarLinhaErroSemantico(lexer.linha);
            								return Constantes.ERRO;
            							}
            							else if(tipoSimpleExprPrime == Constantes.VAZIO) {
            								if(tipoTerm == Constantes.STRING) {
            									geradorCodigo.escreverStringEmArquivo("CONCAT");
            								}
            								else if(tipoTerm == Constantes.FLOAT){
            									geradorCodigo.escreverStringEmArquivo("FADD");
            								}
            								else {
            									geradorCodigo.escreverStringEmArquivo("ADD");
            								}
            								return tipoTerm;
            							}
            							else if(tipoTerm!=tipoSimpleExprPrime) {
            			                	  setarLinhaErroSemantico(lexer.linha);
            			                	  return Constantes.ERRO;
            			                 }
            			            	else {
            			            		if(tipoTerm == Constantes.STRING) {
            									geradorCodigo.escreverStringEmArquivo("CONCAT");
            								}
            								else if(tipoTerm == Constantes.FLOAT){
            									geradorCodigo.escreverStringEmArquivo("FADD");
            								}
            								else {
            									geradorCodigo.escreverStringEmArquivo("ADD");
            								}
            			            		return tipoTerm;            							
            			            	}
            						case '-':
            							if(tipoTerm == Constantes.ERRO || tipoSimpleExprPrime == Constantes.ERRO) {
            								setarLinhaErroSemantico(lexer.linha);
            								return Constantes.ERRO;
            							}
            							else if(tipoTerm == Constantes.STRING || tipoSimpleExprPrime == Constantes.STRING) {
            								setarLinhaErroSemantico(lexer.linha);
            								return Constantes.ERRO;
            							}
            							else if(tipoSimpleExprPrime == Constantes.VAZIO) {
            								if(tipoTerm == Constantes.FLOAT){
            									geradorCodigo.escreverStringEmArquivo("FSUB");
            								}
            								else {            								
            									geradorCodigo.escreverStringEmArquivo("SUB");
            								}
            								return tipoTerm;
            							}
            							else if(tipoTerm!=tipoSimpleExprPrime) {
            			                	  setarLinhaErroSemantico(lexer.linha);
            			                	  return Constantes.ERRO;
            			                 }
            			            	else {
            			            		if(tipoTerm == Constantes.FLOAT){
            									geradorCodigo.escreverStringEmArquivo("FSUB");
            								}
            								else {
            									geradorCodigo.escreverStringEmArquivo("SUB");
            								}
            			            		return tipoTerm;
            			            	}
            						case Constantes.OR:
            							if(tipoTerm == Constantes.ERRO || tipoSimpleExprPrime == Constantes.ERRO) {
            								setarLinhaErroSemantico(lexer.linha);
            								return Constantes.ERRO;
            							}
            							else if(tipoTerm == Constantes.STRING || tipoSimpleExprPrime == Constantes.STRING) {
            								setarLinhaErroSemantico(lexer.linha);
            								return Constantes.ERRO;
            							}
            							else if(tipoSimpleExprPrime == Constantes.VAZIO) {
            								geradorCodigo.escreverStringEmArquivo("ADD");
            								return tipoTerm;
            							}
            							else if(tipoTerm!=tipoSimpleExprPrime) {
            			                	  setarLinhaErroSemantico(lexer.linha);
            			                	  return Constantes.ERRO;
            			                 }
            			            	else {
            			            		geradorCodigo.escreverStringEmArquivo("ADD");
            			            		return tipoTerm;
            			            	}
            					}
                                
            
            default: return Constantes.VAZIO;
        }
    }
    
    /*
    term -> factor-a | term mulop factor-a
            
    term -> factor-a term_prime  
    
    term_prime -> mulop factor-a term_prime | lambda
    */
    private int term() throws ErroSintaticoException{
        switch(tok.getTag()){
        	case Constantes.LITERAL:
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': int tipoFactorA = factor_a();
                      int tipoTermPrime = term_prime(tipoFactorA);
                      if(tipoTermPrime == Constantes.ERRO) {
                    	  setarLinhaErroSemantico(lexer.linha);
                    	  return Constantes.ERRO;
                      }
                      else if (tipoTermPrime == Constantes.VAZIO)
                    	  return tipoFactorA;
                      else if(tipoFactorA == Constantes.STRING || tipoTermPrime == Constantes.STRING) {
                    	  setarLinhaErroSemantico(lexer.linha);
                    	  return Constantes.ERRO;
                      }
                      else if(tipoFactorA!=tipoTermPrime) {
                    	  setarLinhaErroSemantico(lexer.linha);
                    	  return Constantes.ERRO;
                      }
                      else if(isDivisao)
                    	  return Constantes.FLOAT;
                      else
                    	  return tipoFactorA;                 
            
            default: error(); break;
        }
		return 0;
    }
    
    private int term_prime(int tipoFactorAAnteriror) throws ErroSintaticoException{
    	int tipoTermPrime;
    	int tipoFactorA;
        switch(tok.getTag()){
            case '*':
            	mulop();
            	tipoFactorA = factor_a();
            	tipoTermPrime = term_prime(tipoFactorA);
            	isDivisao = false;
            	if(tipoFactorA == Constantes.STRING || tipoTermPrime == Constantes.STRING)
            	{
            		setarLinhaErroSemantico(lexer.linha);
            		return Constantes.ERRO;
            	}
            	else if (tipoTermPrime == Constantes.VAZIO) {
            		if(tipoFactorA == Constantes.FLOAT){
						geradorCodigo.escreverStringEmArquivo("FMUL");
					}
					else {
						geradorCodigo.escreverStringEmArquivo("MUL");
					}
            		return tipoFactorA;
            	}
            	else if(tipoFactorA!=tipoTermPrime) {
              	  setarLinhaErroSemantico(lexer.linha);
              	  return Constantes.ERRO;
                }
            	else {
            		if(tipoFactorA == Constantes.FLOAT){
						geradorCodigo.escreverStringEmArquivo("FMUL");
					}
					else {
						geradorCodigo.escreverStringEmArquivo("MUL");
					}
            		return tipoFactorA;
            	}
            case '/':
            	if(tipoFactorAAnteriror == Constantes.INT) {            		
            		geradorCodigo.escreverStringEmArquivo("ITOF");
            	}
            	mulop();
            	tipoFactorA = factor_a();
            	tipoTermPrime = term_prime(tipoFactorA);
            	isDivisao = true;
            	if(tipoFactorA == Constantes.STRING || tipoTermPrime == Constantes.STRING)
            	{
            		setarLinhaErroSemantico(lexer.linha);
            		return Constantes.ERRO;
            	}
            	else if (tipoTermPrime == Constantes.VAZIO) {
            		if(tipoFactorA == Constantes.FLOAT){
						geradorCodigo.escreverStringEmArquivo("FDIV");
					}
					else {
						geradorCodigo.escreverStringEmArquivo("ITOF");
						geradorCodigo.escreverStringEmArquivo("FDIV");
					}
            		return tipoFactorA;
            	}
            	else if(tipoFactorA!=tipoTermPrime) {
                	  setarLinhaErroSemantico(lexer.linha);
                	  return Constantes.ERRO;
                 }
            	else {
            		if(tipoFactorA == Constantes.FLOAT){
						geradorCodigo.escreverStringEmArquivo("FDIV");
					}
					else {
						geradorCodigo.escreverStringEmArquivo("ITOF");
						geradorCodigo.escreverStringEmArquivo("FDIV");
					}
            		return tipoFactorA;
            	}
            case Constantes.AND: 
            	mulop();
            	tipoFactorA = factor_a();
            	tipoTermPrime = term_prime(tipoFactorA);
            	isDivisao = false;
            	if(tipoFactorA == Constantes.STRING || tipoTermPrime == Constantes.STRING)
            	{
            		setarLinhaErroSemantico(lexer.linha);
            		return Constantes.ERRO;
            	}
            	else if (tipoTermPrime == Constantes.VAZIO) {
            		geradorCodigo.escreverStringEmArquivo("MUL");
            		return tipoFactorA;
            	}
            	else if(tipoFactorA!=tipoTermPrime) {
              	  setarLinhaErroSemantico(lexer.linha);
              	  return Constantes.ERRO;
               }
            	else {
            		geradorCodigo.escreverStringEmArquivo("MUL");
            		return tipoFactorA;
            	}


            default: return Constantes.VAZIO;
        }
    }
    
    private int factor_a() throws ErroSintaticoException{
        switch(tok.getTag()){
        	case Constantes.LITERAL:
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': return factor();
            case '!': eat(new Token('!'));
                      return factor();
            case '-': eat(new Token('-'));
                      return factor();
            
            default: error(); break;
        }
		return 0;
    }
    
    private int factor() throws ErroSintaticoException{
        switch(tok.getTag()){
        	case Constantes.LITERAL:
        		geradorCodigo.escreverStringEmArquivo("PUSHS \""+((Palavra)tok).getLexeme()+"\"");
        		eat(new Token(Constantes.LITERAL));
        		return Constantes.STRING;
            case Constantes.ID:
            	Token aux = tok; 
            	eat(new Token(Constantes.ID));
            	Integer tipoIdentificador = tabelaSimbolosAtual.obter_tipo((Palavra) aux);
            	Integer offsetVariavel = tabelaSimbolosAtual.obter_offset((Palavra) aux);
            	geradorCodigo.escreverStringEmArquivo("PUSHL "+offsetVariavel);
            	if(tipoIdentificador == null) {
            		setarLinhaErroSemantico(lexer.linha);
            		return Constantes.ERRO;
            	}
            	else
            		return tipoIdentificador;
            case Constantes.NUM:
            	ConstanteNumerica aux1 = ((ConstanteNumerica)tok);
            	eat(new Token(Constantes.NUM)); 
            	if(aux1.getValue().getClass().getSimpleName().equals("Integer")) {
            		geradorCodigo.escreverStringEmArquivo("PUSHI "+ aux1.getValue());
            		return Constantes.INT;
            	}
            	else {
            		geradorCodigo.escreverStringEmArquivo("PUSHF "+ aux1.getValue());
            		return Constantes.FLOAT;
            	}
            case '(': eat(new Token('('));
                      int tipoExpression = expression(); 
                      eat(new Token(')'));
                      return tipoExpression;
            
            default: error(); break;
        }
		return 0;
    }
    
    private void relop() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.GT: eat(new Token(Constantes.GT)); break;
            case Constantes.GE: eat(new Token(Constantes.GE)); break;
            case Constantes.LT: eat(new Token(Constantes.LT)); break;
            case Constantes.LE: eat(new Token(Constantes.LE)); break;
            case Constantes.NE: eat(new Token(Constantes.NE)); break;
            case Constantes.EQ: eat(new Token(Constantes.EQ)); break;


        }
    }
    
    private void addop() throws ErroSintaticoException{
        switch(tok.getTag()){
            case '+': eat(new Token('+')); break;
            case '-': eat(new Token('-')); break;
            case Constantes.OR: eat(new Token(Constantes.OR)); break;
            
            default: error(); break;
        }
    }
    
    private void mulop() throws ErroSintaticoException{
        switch(tok.getTag()){
            case '*': eat(new Token('*')); break;
            case '/': eat(new Token('/')); break;
            case Constantes.AND: eat(new Token(Constantes.AND)); break;
            
            default: error(); break;
        }
    }
}
