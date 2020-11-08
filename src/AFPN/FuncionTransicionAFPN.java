package AFPN;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class FuncionTransicionAFPN {
	private Set<String> conjuntoEstados;
	private Set<Character> alfabetoCinta;
	private Set<Character> alfabetoPila;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloAlfabetoANumero;
	private HashMap<Character, Integer> simboloPilaANumero;
	private String[][][] transiciones; // Triple, donde 0 representa lambda y pues debe haber un string el cual se
										// puede tomar
										// de hecho solo 1

	public FuncionTransicionAFPN(Set<String> conjuntoEstados, Set<Character> alfabetoCinta, Set<Character> alfabetoPila,
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

	public Vector<String> getTransicion(String estadoActual, char simboloCinta, char simboloPila) {

		String transicion = transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
				.get(simboloCinta)][simboloPilaANumero.get(simboloPila)];
		if (transicion == null)
			return null;
		else {
			Vector<String> posiblesTransiciones = new Vector<>();
			String[] transiciones = transicion.split(";");
			for (int i = 0; i < transiciones.length; ++i) {
				posiblesTransiciones.add(transiciones[i]);
			}
			return posiblesTransiciones;
		}
	}

}
