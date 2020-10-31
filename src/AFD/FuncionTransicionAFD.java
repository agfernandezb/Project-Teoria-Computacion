package AFD;

import java.util.HashMap;
import java.util.Set;

public class FuncionTransicionAFD {
	private Set<Character> alfabeto;
	private Set<String> conjuntoEstados;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloANumero;
	private String[][] transiciones;

	public FuncionTransicionAFD(Set<Character> alfabeto, Set<String> conjuntoEstados,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloANumero) {
		super();
		this.alfabeto = alfabeto;
		this.conjuntoEstados = conjuntoEstados;
		this.estadoANumero = estadoANumero;
		this.simboloANumero = simboloANumero;
		this.transiciones = new String[conjuntoEstados.size()][alfabeto.size()];
	}

	public void setTransicion(String estadoActual, char simbolo, String estadoFinal) {
		transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)] = estadoFinal;
	}

	public String getTransicion(String estadoActual, char simbolo) {
		return transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)];
	}

}
