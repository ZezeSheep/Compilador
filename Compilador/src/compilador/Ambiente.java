package compilador;

import java.util.HashMap;
import java.util.Map;

public class Ambiente {
	
	private Map<Token, Identificador> simbolos;
	private Ambiente ambienteAnterior;
	
	public Ambiente(Ambiente ambienteAnterior) {
		simbolos = new HashMap<>();
		this.ambienteAnterior = ambienteAnterior;
	}
	
	public void adicionaSimbolo(Token token, Identificador identificador) {
            if(!this.simbolos.containsKey(token))
		simbolos.put(token, identificador);
	}
	
	public Boolean exists(Token token) {
		Ambiente ambiente;
		for(ambiente = this; ambiente!=null; ambiente=ambiente.ambienteAnterior)
			if(ambiente.simbolos.containsKey(token))

				return true;
		return false;
	}
        
	public Identificador getIdentificador(Token token) {
		if(exists(token)) {
			Ambiente ambiente;
			for(ambiente = this; ambiente!=null; ambiente=ambiente.ambienteAnterior)
				if(ambiente.simbolos.containsKey(token))
					return ambiente.simbolos.get(token);
			return null;
		}
		else
			return null;
	}
        public void printTable(){
            Ambiente ambiente;
            for(ambiente = this; ambiente!=null; ambiente=ambiente.ambienteAnterior)
                System.out.println(ambiente.simbolos);
        }
	

}
