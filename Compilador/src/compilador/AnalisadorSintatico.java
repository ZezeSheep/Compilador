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
    
    private Token tok;
    
    public AnalisadorSintatico(AnalisadorLexico lexer) {
        this.lexer = lexer;
    }
    
    private void advance(){
        tok = lexer.scan();
    }
    
    private void eat(Token t){
        if(t.getTag() == this.tok.getTag()) advance();
        else error();
    }
    
    private void error(){
        System.out.println("to be implemented");
    }
    
    /*
    program -> class id program_prime
    program_prime -> body | decl_list body
    */
    
    private void program(){
        switch(tok.getTag()){
            case Constantes.CLASS: eat(new Token(Constantes.CLASS));
                                   eat(new Token(Constantes.ID));
                                   program_prime();
                                   break;

            default: error(); break;
        }
    }
    
    private void program_prime(){
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
    
    private void decl_list(){
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
    
    private void decl_list_prime(){
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
    
    private void decl(){
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
    
    private void ident_list(){
        switch(tok.getTag()){
            case Constantes.ID: eat(new Token(Constantes.ID));
                                ident_list_prime();
                                break;

            default: error(); break;
        }
    }
    
    private void ident_list_prime(){
        switch(tok.getTag()){
            case Constantes.ID: eat(new Token(','));
                                eat(new Token(Constantes.ID));
                                ident_list_prime();
                                break;

            default: break;
        }
    }
    
    private void type(){
        switch(tok.getTag()){
            case Constantes.INT: eat(new Token(Constantes.INT)); break;
            case Constantes.STRING: eat(new Token(Constantes.STRING)); break;
            case Constantes.FLOAT: eat(new Token(Constantes.FLOAT)); break;

            default: error(); break;
        }
    }
    
    private void body(){
        switch(tok.getTag()){
            case Constantes.INIT: eat(new Token(Constantes.INIT));
                                  stmt_list();
                                  eat(new Token(Constantes.STOP));
                                simple_expr();
                                break;

            default: error(); break;
        }
    }
    
    /*
    stmt_list -> stmt ; stmt_list_prime
    stmt_list_prime ->  stmt ; stmt_list_prime | lambda
    */
    
    private void stmt_list(){
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
    
    private void stmt_list_prime(){
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
    
    private void stmt(){
        switch(tok.getTag()){
            case Constantes.ID: assign_stmt(); break;
            case Constantes.IF: if_stmt(); break;
            case Constantes.DO: do_stmt(); break;
            case Constantes.READ: read_stmt(); break;
            case Constantes.WRITE: write_stmt(); break;
                
            default: error(); break;
        }
    }
    
    private void assign_stmt(){
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
    
    private void if_stmt(){
        switch(tok.getTag()){
            case Constantes.IF: eat(new Token(Constantes.IF));
                                eat(new Token('('));
                                condition();
                                eat(new Token(')'));
                                eat(new Token('{'));
                                stmt_list();
                                eat(new Token('}'));
                                break;

            default: error(); break;
        }
    }
    
    private void if_stmt_prime(){
        switch(tok.getTag()){
            case Constantes.ELSE: eat(new Token(Constantes.ELSE));
                                  eat(new Token('{'));
                                  stmt_list();
                                  eat(new Token('}'));
                                  break;

            default: break;
        }
    }
    
    private void condition(){
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
    
    private void do_stmt(){
        switch(tok.getTag()){
            case Constantes.DO: eat(new Token(Constantes.DO));
                                  eat(new Token('{'));
                                  stmt_list();
                                  eat(new Token('}'));
                                  break;

            default: error(); break;
        }
    }
    
    private void do_sulfix(){
        switch(tok.getTag()){
            case Constantes.WHILE: eat(new Token(Constantes.WHILE));
                                  eat(new Token('('));
                                  condition();
                                  eat(new Token(')'));
                                  break;

            default: error(); break;
        }
    }
    
    private void read_stmt(){
        switch(tok.getTag()){
            case Constantes.READ: eat(new Token(Constantes.READ));
                                  eat(new Token('('));
                                  eat(new Token(Constantes.ID));
                                  eat(new Token(')'));
                                  break;

            default: error(); break;
        }
    }

    private void write_stmt(){
        switch(tok.getTag()){
            case Constantes.WRITE: eat(new Token(Constantes.WRITE));
                                   eat(new Token('('));
                                   writable();
                                   eat(new Token(')'));
                                   break;

            default: error(); break;
        }
    }
    
    private void writable(){
        switch(tok.getTag()){
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
    expression_prime -> relop simple_expr | lambda
    */
    
    private void expression(){
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
    
    private void expression_prime(){
        switch(tok.getTag()){
            case Constantes.GT: 
            case Constantes.GE:
            case Constantes.LT:
            case Constantes.LE:
            case Constantes.NE:
            case Constantes.EQ: relop();
                                simple_expr();
                                break;
            default: break;
        }
    }
    
     /*
    simple_expr -> term simple_expr_prime
    simple_expr_prime -> addop term simple_expr_prime | lambda
    
    */
    
    private void simple_expr(){
        switch(tok.getTag()){
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
   
    private void simple_expr_prime(){
        switch(tok.getTag()){
            case '+':
            case '-':
            case Constantes.OR: addop();
                                term();
                                simple_expr_prime();
                                break;
            
            default: break;
        }
    }
    
    /*
    term -> factor-a | term mulop factor-a
            
    term -> factor-a term_prime  
    
    term_prime -> mulop factor-a term_prime | lambda
    */
    private void term(){
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': 
            case '!': 
            case '-': factor_a();
                      term_prime();
                      break;
                    
            
            default: error(); break;
        }
    }
    
    private void term_prime(){
        switch(tok.getTag()){
            case '*': 
            case '/': 
            case Constantes.AND: mulop();
                                 factor_a();
                                 term_prime();
                                 break;

            default: break;
        }
    }
    
    private void factor_a(){
        switch(tok.getTag()){
            case Constantes.ID: 
            case Constantes.NUM: 
            case '(': factor();
                      break;
            case '!': eat(new Token('!'));
                      factor();
            case '-': eat(new Token('-'));
                      factor();
            
            default: error(); break;
        }
    }
    
    private void factor(){
        switch(tok.getTag()){
            case Constantes.ID: eat(new Token(Constantes.ID)); break;
            case Constantes.NUM: eat(new Token(Constantes.NUM)); break;
            case '(': eat(new Token('('));
                      expression(); 
                      eat(new Token(')'));
                      break;
            
            default: error(); break;
        }
    }
    
    private void relop(){
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
    
    private void addop(){
        switch(tok.getTag()){
            case '+': eat(new Token('+')); break;
            case '-': eat(new Token('-')); break;
            case Constantes.OR: eat(new Token(Constantes.OR)); break;
            
            default: error(); break;
        }
    }
    
    private void mulop(){
        switch(tok.getTag()){
            case '*': eat(new Token('*')); break;
            case '/': eat(new Token('/')); break;
            case Constantes.AND: eat(new Token(Constantes.AND)); break;
            
            default: error(); break;
        }
    }
}
