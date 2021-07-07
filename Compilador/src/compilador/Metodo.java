package compilador;

public class Metodo extends Identificador {
	private Class tipoSaida;
	private Class[] tiposEntrada;
	
	public Metodo(Class[] tiposEntrada, Class tipoSaida) {
		super();
		this.tiposEntrada = tiposEntrada;
		this.tipoSaida = tipoSaida;
	}

}
