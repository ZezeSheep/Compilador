package compilador;

public class ConstanteNumerica extends Token {
	
	private final int value;
	
	public ConstanteNumerica(int value){
		super(Constantes.NUM);
		this.value = value;
	}
	
	public String toString(){
		return "" + value;
	}

	public int getValue() {
		return value;
	}

}
