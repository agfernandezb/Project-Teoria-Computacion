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
	private Vector<String> numeroAEstado;
	private Vector<Character> numeroASimboloAlfabeto;
	private Vector<Character> numeroASimboloPila;

	public FuncionTransicionAFPN(Set<String> conjuntoEstados, Set<Character> alfabetoCinta, Set<Character> alfabetoPila,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloAlfabetoANumero,
			HashMap<Character, Integer> simboloPilaANumero, Vector<String> numeroAEstado,
			Vector<Character> numeroASimboloAlfabeto, Vector<Character> numeroASimboloPila) {
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
		this.numeroAEstado = numeroAEstado;
		this.numeroASimboloAlfabeto = numeroASimboloAlfabeto;
		this.numeroASimboloPila = numeroASimboloPila;
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

	public HashMap<Character, Integer> getSimboloAlfabetoANumero() {
		return simboloAlfabetoANumero;
	}

	public HashMap<Character, Integer> getSimboloPilaANumero() {
		return simboloPilaANumero;
	}

	public Vector<Character> getNumeroASimboloAlfabeto() {
		return numeroASimboloAlfabeto;
	}

	public Vector<Character> getNumeroASimboloPila() {
		return numeroASimboloPila;
	}

	public String toString() {
		String resultado = "";
		for (int i = 0; i < transiciones.length; i++) {
			for (int j = 0; j < transiciones[i].length; j++) {
				for (int k = 0; k < transiciones[i][j].length; k++) {
					resultado += (transiciones[i][j][k] != null)
							? numeroAEstado.get(i) + ":" + numeroASimboloAlfabeto.get(j) + ":"
									+ numeroASimboloPila.get(k) + ">" + transiciones[i][j][k] + "\n"
							: "";

				}
			}
		}
		return resultado;
	}

}
