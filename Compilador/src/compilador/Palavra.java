package compilador;

public class Palavra extends Token {
	private String lexeme = "";
	
	public static final Palavra AND = new Palavra ("&&", Constantes.AND);
	public static final Palavra OR = new Palavra ("||", Constantes.OR);
	public static final Palavra EQ = new Palavra ("==", Constantes.EQ);
	public static final Palavra NE = new Palavra ("!=", Constantes.NE);
	public static final Palavra LE = new Palavra ("<=", Constantes.LE);
	public static final Palavra GE = new Palavra (">=", Constantes.GE);
	public static final Palavra LT = new Palavra ("<", Constantes.LT);
	public static final Palavra GT = new Palavra (">", Constantes.GT);
	public static final Palavra CLASS = new Palavra ("class", Constantes.CLASS);
	public static final Palavra INT = new Palavra ("int", Constantes.INT);
	public static final Palavra STRING = new Palavra ("string", Constantes.STRING);
	public static final Palavra FLOAT = new Palavra ("float", Constantes.FLOAT);
	public static final Palavra DO = new Palavra ("do", Constantes.DO);
	public static final Palavra WHILE = new Palavra ("while", Constantes.WHILE);
	public static final Palavra READ = new Palavra ("read", Constantes.READ);
	public static final Palavra WRITE = new Palavra ("write", Constantes.WRITE);
	public static final Palavra INIT = new Palavra ("init", Constantes.INIT);
	public static final Palavra STOP = new Palavra ("stop", Constantes.STOP);
	public static final Palavra IF = new Palavra ("if", Constantes.IF);
	public static final Palavra ELSE = new Palavra ("else", Constantes.ELSE);
	
	
	public Palavra (String s, int tag){
		super (tag);
		lexeme = s;
	}
	
	public String toString(){
		return "" + lexeme;
	}

	public String getLexeme() {
		return lexeme;
	}

	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	

}
