package AFPN;

import java.util.HashMap;
import java.util.Set;

import AFPD.FuncionTransicionAFPD;

public class FuncionTransicionAFPN extends FuncionTransicionAFPD {

	public FuncionTransicionAFPN(Set<String> conjuntoEstados, Set<Character> alfabetoCinta, Set<Character> alfabetoPila,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloAlfabetoANumero,
			HashMap<Character, Integer> simboloPilaANumero) {
		super(conjuntoEstados, alfabetoCinta, alfabetoPila, estadoANumero, simboloAlfabetoANumero, simboloPilaANumero);
	}
	
	

}
