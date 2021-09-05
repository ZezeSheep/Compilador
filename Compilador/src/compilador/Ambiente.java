package compilador;

import java.util.HashMap;
import java.util.Map;

public class Ambiente {
	
	private Map<String, Identificador> simbolos;
	private Ambiente ambienteAnterior;
	
	public Ambiente(Ambiente ambienteAnterior) {
		simbolos = new HashMap<>();
		this.ambienteAnterior = ambienteAnterior;
	}
	
	public void adicionaSimbolo(String nomeIdentificador, Identificador identificador) {
            if(!this.simbolos.containsKey(nomeIdentificador))
		simbolos.put(nomeIdentificador, identificador);
	}
	
	public Boolean exists(String entrada) {
		Ambiente ambiente;
		for(ambiente = this; ambiente!=null; ambiente=ambiente.ambienteAnterior)
			if(ambiente.simbolos.containsKey(entrada))
				return true;
		return false;
	}
        
	public Identificador getIdentificador(String entrada) {
		if(exists(entrada)) {
			Ambiente ambiente;
			for(ambiente = this; ambiente!=null; ambiente=ambiente.ambienteAnterior)
				if(ambiente.simbolos.containsKey(entrada))
					return ambiente.simbolos.get(entrada);
			return null;
		}
		else
			return null;
	}
      public void printTable(int tabs){
            Ambiente ambiente;
            for(ambiente = this; ambiente!=null; ambiente=ambiente.ambienteAnterior)
                for(Map.Entry<String, Identificador> entry : ambiente.simbolos.entrySet()){
                    for(int i = tabs;i>0;i--)
                        System.out.print("\t");
                    System.out.println(entry.getKey()+" : "+entry.getValue());
                }               
            
        }
      
      public int obter_tipo(Palavra entrada) {
    	  return getIdentificador(entrada.getLexeme()).obter_tipo();
      }
     
        
	

}
