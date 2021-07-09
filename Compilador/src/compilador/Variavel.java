package compilador;

public class Variavel extends Identificador {
	
	private Class tipo;
	private Object valor;
	
	public Variavel(Object variavel){
		super();
		this.valor = variavel;
		this.tipo = variavel.getClass();
	}

}
