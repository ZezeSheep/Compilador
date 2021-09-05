package compilador;

public class Metodo extends Identificador {
	private int tipoSaida;
	private int[] tiposEntrada;
	
	public Metodo(int[] tiposEntrada, int tipoSaida) {
		super();
		super.tipo = tipoSaida;
		this.tiposEntrada = tiposEntrada;
		this.tipoSaida = tipoSaida;
	}

	public int getTipoSaida() {
		return tipoSaida;
	}

	public void setTipoSaida(int tipoSaida) {
		this.tipoSaida = tipoSaida;
	}

	public int[] getTiposEntrada() {
		return tiposEntrada;
	}

	public void setTiposEntrada(int[] tiposEntrada) {
		this.tiposEntrada = tiposEntrada;
	}
	
	

}
