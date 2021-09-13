package TabelaDeSimbolos;

public class Identificador {
	
	protected int tipo;
	protected int offsetFP;
    
    @Override
    public String toString(){
        return "Identificador";
    }
    
    public int obter_tipo() {
    	return tipo;
    }
    
    public int obter_offset() {
    	return offsetFP;
    }
}

