package TabelaDeSimbolos;

public class Identificador {
	
	protected int tipo;	
    
    @Override
    public String toString(){
        return "Identificador";
    }
    
    public int obter_tipo() {
    	return tipo;
    }
}

