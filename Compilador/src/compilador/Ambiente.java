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
	
	public Boolean adicionaSimbolo(String nomeIdentificador, Identificador identificador) {
        if(!this.simbolos.containsKey(nomeIdentificador)) {
        	simbolos.put(nomeIdentificador, identificador);
        	return true;
        }
        else
        	return false; // restricao unicidade
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
			return null; // restricao de existencia
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
      
      public Integer obter_tipo(Palavra entrada) {
    	  Identificador identificador = getIdentificador(entrada.getLexeme());
    	  if(identificador == null)
    		  return null;
    	  else
    		  return getIdentificador(entrada.getLexeme()).obter_tipo();
      }
     
        
	

}
