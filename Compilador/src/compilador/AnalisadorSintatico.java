/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author Meu Computador
 */
public class AnalisadorSintatico {
    
    private AnalisadorLexico lexer;
    private Ambiente tabelaSimbolosAtual;
    
    private Token tok;
    
    public AnalisadorSintatico(AnalisadorLexico lexer) {
        this.lexer = lexer;
        tabelaSimbolosAtual = new Ambiente(null);
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
    
    /*
    program -> class id program_prime
    program_prime -> body | decl_list body
    */
    
    public void startAnalysis(){
    	advance();
    	try {
	        switch(tok.getTag()){
	            case Constantes.CLASS: program();	
	                                   eat(new Token(Constantes.EOF));
	                                   break;
	
	            default: error(); break;
	        }
	        System.out.println("Analise sintatica concluida com sucesso, 0 erros encontrados.");
        }catch(ErroSintaticoException e) {
        	e.printStackTrace();
        	System.out.println("Compilacao concluida com erro");
        }
    }
    
    private void program() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.CLASS: eat(new Token(Constantes.CLASS));
                                   eat(new Token(Constantes.ID));
                                   program_prime();
                                   break;

            default: error(); break;
        }
    }
    
    private void program_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: decl_list();
                                   body();
                                   break;
                                   
            case Constantes.INIT: body(); break;            

            default: error(); break;
        }
    }
    
    /*
    decl_list -> decl ; decl_list_prime
    decl_list_prime ->  decl ; decl_list_prime | lambda
    */
    
    private void decl_list() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: decl();
                                   eat(new Token(';'));
                                   decl_list_prime();
                                   break;
                
            default: error(); break;
        }
    }
    
    private void decl_list_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: decl();
                                   eat(new Token(';'));
                                   decl_list_prime();
                                   break;
                
            default: break;
        }
    }
    
    private void decl() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT:
            case Constantes.STRING: 
            case Constantes.FLOAT: type();
                                   ident_list();
                                   break;

            default: error(); break;
        }
    }
    
   /*
    ident_list -> id ident_list_prime
    ident_list_prime -> , id ident_list_prime | lambda
    */
    
    private void ident_list() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: eat(new Token(Constantes.ID));
                                ident_list_prime();
                                break;

            default: error(); break;
        }
    }
    
    private void ident_list_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case ',': eat(new Token(','));
                      eat(new Token(Constantes.ID));
                      ident_list_prime();
                      break;

            default: break;
        }
    }
    
    private void type() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INT: eat(new Token(Constantes.INT)); break;
            case Constantes.STRING: eat(new Token(Constantes.STRING)); break;
            case Constantes.FLOAT: eat(new Token(Constantes.FLOAT)); break;

            default: error(); break;
        }
    }
    
    private void body() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.INIT: eat(new Token(Constantes.INIT));
                                  stmt_list();
                                  eat(new Token(Constantes.STOP));
                                  break;

            default: error(); break;
        }
    }
    
    /*
    stmt_list -> stmt ; stmt_list_prime
    stmt_list_prime ->  stmt ; stmt_list_prime | lambda
    */
    
    private void stmt_list() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.IF: 
            case Constantes.DO: 
            case Constantes.READ: 
            case Constantes.WRITE: stmt();
                                   eat(new Token(';'));
                                   stmt_list_prime();                                   
                                   break;
                
            default: error(); break;
        }
    }
    
    private void stmt_list_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.IF: 
            case Constantes.DO: 
            case Constantes.READ: 
            case Constantes.WRITE: stmt();
                                   eat(new Token(';'));
                                   stmt_list_prime();
                                   break;
                
            default: break;
        }
    }
    
    private void stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
        	
            case Constantes.ID: assign_stmt(); break;
            case Constantes.IF: if_stmt(); break;
            case Constantes.DO: do_stmt(); break;
            case Constantes.READ: read_stmt(); break;
            case Constantes.WRITE: write_stmt(); break;
                
            default: error(); break;
        }
    }
    
    private void assign_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: eat(new Token(Constantes.ID));
                                eat(new Token('='));
                                simple_expr();
                                break;

            default: error(); break;
        }
    }
    
    /*
    if_stmt -> if "(" condition ")" "{" stmt_list "}" if_stmt_prime
    if_stmt_prime -> else "{" stmt_list "}"  | lambda
    */
    
    private void if_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.IF: eat(new Token(Constantes.IF));
                                eat(new Token('('));
                                
                                condition();
                                eat(new Token(')'));
                                eat(new Token('{'));
                                stmt_list();
                                eat(new Token('}'));
                                if_stmt_prime();
                                break;

            default: error(); break;
        }
    }
    
    private void if_stmt_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ELSE: eat(new Token(Constantes.ELSE));
                                  eat(new Token('{'));
                                  stmt_list();
                                  eat(new Token('}'));
                                  break;

            default: break;
        }
    }
    
    private void condition() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': expression();
                      break;

            default: error(); break;
        }
    }
    
    private void do_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.DO:   eat(new Token(Constantes.DO));
                                  eat(new Token('{'));                                  
                                  stmt_list();                                  
                                  eat(new Token('}'));
                                  do_sulfix();
                                  break;

            default: error(); break;
        }
    }
    
    private void do_sulfix() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.WHILE: eat(new Token(Constantes.WHILE));
                                  eat(new Token('('));
                                  condition();
                                  eat(new Token(')'));
                                  break;

            default: error(); break;
        }
    }
    
    private void read_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.READ: eat(new Token(Constantes.READ));
                                  eat(new Token('('));
                                  eat(new Token(Constantes.ID));
                                  eat(new Token(')'));
                                  break;

            default: error(); break;
        }
    }

    private void write_stmt() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.WRITE: eat(new Token(Constantes.WRITE));
                                   eat(new Token('('));
                                   writable();
                                   eat(new Token(')'));
                                   break;

            default: error(); break;
        }
    }
    
    private void writable() throws ErroSintaticoException{
        switch(tok.getTag()){
        	case Constantes.LITERAL:
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': simple_expr();
                      break;

            default: error(); break;
        }
    }
    
    /*
    expression -> simple_expr expression_prime
    expression_prime -> relop simple_expr expression_prime | lambda
    */
    
    private void expression() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': simple_expr();
                      expression_prime();
                      break;

            default: error(); break;
        }
    }
    
    private void expression_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.GT: 
            case Constantes.GE:
            case Constantes.LT:
            case Constantes.LE:
            case Constantes.NE:
            case Constantes.EQ: relop();
                                simple_expr();
                                expression_prime();
                                break;
            default: break;
        }
    }
    
     /*
    simple_expr -> term simple_expr_prime
    simple_expr_prime -> addop term simple_expr_prime | lambda
    
    */
    
    private void simple_expr() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.LITERAL:
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': term();
                      simple_expr_prime();
                      break;

            default: error(); break;
        }
    }
   
    private int simple_expr_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case '+':
            case '-':
            case Constantes.OR: 
            					Token aux = tok;
            					addop();
            					int tipoTerm = term();
            					int tipoSimpleExprPrime = simple_expr_prime();
            					switch(aux.getTag()) {
            						case '+':
            							if(tipoTerm == Constantes.ERRO || tipoSimpleExprPrime == Constantes.ERRO)
            								return Constantes.ERRO;
            							else if(tipoTerm == Constantes.STRING || tipoSimpleExprPrime == Constantes.STRING)
            								return Constantes.STRING;
            							else if(tipoTerm == Constantes.FLOAT || tipoSimpleExprPrime == Constantes.FLOAT)
            								return Constantes.FLOAT;
            							else
            								return Constantes.INT;
            						case '-':
            							if(tipoTerm == Constantes.ERRO || tipoSimpleExprPrime == Constantes.ERRO)
            								return Constantes.ERRO;
            							else if(tipoTerm == Constantes.STRING || tipoSimpleExprPrime == Constantes.STRING)
            								return Constantes.ERRO;
            							else if(tipoTerm == Constantes.FLOAT || tipoSimpleExprPrime == Constantes.FLOAT)
            								return Constantes.FLOAT;
            							else
            								return Constantes.INT;
            						case Constantes.OR:
            							if(tipoTerm == Constantes.ERRO || tipoSimpleExprPrime == Constantes.ERRO)
            								return Constantes.ERRO;
            							else if(tipoTerm == Constantes.STRING || tipoSimpleExprPrime == Constantes.STRING)
            								return Constantes.ERRO;
            							else if(tipoTerm == Constantes.FLOAT || tipoSimpleExprPrime == Constantes.FLOAT)
            								return Constantes.INT;
            							else
            								return Constantes.INT;
            					}
                                break;
            
            default: break;
        }
		return 0;
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
                      int tipoTermPrime = term_prime();
                      if(tipoTermPrime == Constantes.ERRO)
                    	  return Constantes.ERRO;
                      else if (tipoTermPrime == Constantes.VAZIO)
                    	  return tipoFactorA;
                      else if(tipoFactorA == Constantes.FLOAT || tipoTermPrime == Constantes.FLOAT)
                    	  return Constantes.FLOAT;
                      else
                    	  return Constantes.INT;                    
            
            default: error(); break;
        }
		return 0;
    }
    
    private int term_prime() throws ErroSintaticoException{
        switch(tok.getTag()){
            case '*': 
            case '/': 
            case Constantes.AND: 
            	mulop();
            	int tipoFactorA = factor_a();
            	int tipoTermPrime = term_prime();
            	if(tipoFactorA == Constantes.STRING || tipoTermPrime == Constantes.STRING)
            		return Constantes.ERRO;
            	else if (tipoTermPrime == Constantes.VAZIO)
            		return tipoFactorA;
            	else if(tipoFactorA == Constantes.FLOAT || tipoTermPrime == Constantes.FLOAT)
            		return Constantes.FLOAT;
            	else
            		return Constantes.INT;


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
        		eat(new Token(Constantes.LITERAL));
        		return Constantes.STRING;
            case Constantes.ID:
            	Token aux = tok; 
            	eat(new Token(Constantes.ID));            	
            	return tabelaSimbolosAtual.obter_tipo((Palavra) aux);
            case Constantes.NUM: eat(new Token(Constantes.NUM)); break;
            case '(': eat(new Token('('));
                      int tipoExpression = expression(); 
                      eat(new Token(')'));
                      return tipoExpression;
            
            default: error(); break;
        }
    }
    
    private void relop() throws ErroSintaticoException{
        switch(tok.getTag()){
            case Constantes.GT: eat(new Token(Constantes.GT)); break;
            case Constantes.GE: eat(new Token(Constantes.GE)); break;
            case Constantes.LT: eat(new Token(Constantes.LT)); break;
            case Constantes.LE: eat(new Token(Constantes.LE)); break;
            case Constantes.NE: eat(new Token(Constantes.NE)); break;
            case Constantes.EQ: eat(new Token(Constantes.EQ)); break;

            default: error(); break;
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
