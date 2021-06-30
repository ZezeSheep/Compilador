package compilador;

public class ConstanteNumerica<T> extends Token {
	
	private final T value;
	
	public ConstanteNumerica(T value){
		super(Constantes.NUM);
		this.value = value;
	}
	
	public String toString(){
		return "" + value;
	}

	public T getValue() {
		return value;
	}

}
