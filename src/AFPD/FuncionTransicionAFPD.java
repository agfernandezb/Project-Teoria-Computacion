package AFPD;

import java.util.HashMap;
import java.util.Set;

public class FuncionTransicionAFPD {
	private Set<String> conjuntoEstados;
	private Set<Character> alfabetoCinta;
	private Set<Character> alfabetoPila;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloAlfabetoANumero;
	private HashMap<Character, Integer> simboloPilaANumero;
	private String[][][] transiciones; // Triple, donde 0 representa lambda y pues debe haber un string el cual se
										// puede tomar
										// de hecho solo 1

	public FuncionTransicionAFPD(Set<String> conjuntoEstados, Set<Character> alfabetoCinta, Set<Character> alfabetoPila,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloAlfabetoANumero,
			HashMap<Character, Integer> simboloPilaANumero) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.estadoANumero = estadoANumero;
		this.simboloAlfabetoANumero = simboloAlfabetoANumero;
		this.simboloPilaANumero = simboloPilaANumero;
		this.transiciones = new String[conjuntoEstados.size()][alfabetoCinta.size() + 1][alfabetoPila.size()
				+ 1]; /* Se suma
						1 pues se utiliza Lambda tanto en la cinta como en la pila */
	}

	public void setTransicion(String estadoActual, char simboloCinta, char simboloPila, String configuracionFinal) {
		transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero.get(simboloCinta)][simboloPilaANumero
				.get(simboloPila)] = configuracionFinal;
	}

	public String getTransicion(String estadoActual, char simboloCinta, char simboloPila, String configuracionFinal) {
		return transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
				.get(simboloCinta)][simboloPilaANumero.get(simboloPila)];
	}

}
