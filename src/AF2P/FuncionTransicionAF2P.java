package AF2P;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class FuncionTransicionAF2P {
	private Set<String> conjuntoEstados;
	private Set<Character> alfabetoCinta;
	private Set<Character> alfabetoPila;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloAlfabetoANumero;
	private HashMap<Character, Integer> simboloPilaANumero;
	private String[][][][] transiciones; // Cuadruple, primero el estado, luego el simboloCinta y los dos simbolos de pila.
	private Vector<Character> numeroASimboloAlfabeto;
	private Vector<String> numeroAEstado;
	private Vector<Character> numeroASimboloPila;

	public FuncionTransicionAF2P(Set<String> conjuntoEstados, Set<Character> alfabetoCinta, Set<Character> alfabetoPila,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloAlfabetoANumero,
			HashMap<Character, Integer> simboloPilaANumero, Vector<Character> numeroASimboloAlfabeto,
			Vector<String> numeroAEstado, Vector<Character> numeroASimboloPila) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.estadoANumero = estadoANumero;
		this.simboloAlfabetoANumero = simboloAlfabetoANumero;
		this.simboloPilaANumero = simboloPilaANumero;
		this.transiciones = new String[conjuntoEstados.size()][alfabetoCinta.size() + 1][alfabetoPila.size()
				+ 1][alfabetoPila.size() + 1]; /* Se suma
												1 pues se utiliza Lambda tanto en la cinta como en la pila */
		this.numeroAEstado = numeroAEstado;
		this.numeroASimboloAlfabeto = numeroASimboloAlfabeto;
		this.numeroASimboloPila = numeroASimboloPila;
	}

	public void setTransicion(String estadoActual, char simboloCinta, char simboloPrimeraPila, char simboloSegundaPila,
			String configuracionFinal) {
		transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero.get(simboloCinta)][simboloPilaANumero
				.get(simboloPrimeraPila)][simboloPilaANumero.get(simboloSegundaPila)] = configuracionFinal;
	}

	public Vector<String> getTransicion(String estadoActual, char simboloCinta, char simboloPrimeraPila,
			char simboloSegundaPila) {

		String transicion = transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
				.get(simboloCinta)][simboloPilaANumero.get(simboloPrimeraPila)][simboloPilaANumero
						.get(simboloSegundaPila)];
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

	public String getTransicionString(String estadoActual, char simboloCinta, char simboloPrimeraPila,
			char simboloSegundaPila) {
		String transicion = transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
				.get(simboloCinta)][simboloPilaANumero.get(simboloPrimeraPila)][simboloPilaANumero
						.get(simboloSegundaPila)];
		return transicion;
	}

	public HashMap<Character, Integer> getSimboloAlfabetoANumero() {
		return simboloAlfabetoANumero;
	}

	public HashMap<Character, Integer> getSimboloPilaANumero() {
		return simboloPilaANumero;
	}

	@Override
	public String toString() {
		String resultado = "";
		for (int i = 0; i < transiciones.length; i++) {
			for (int j = 0; j < transiciones[i].length; j++) {
				for (int k = 0; k < transiciones[i][j].length; k++) {
					for (int l = 0; l < transiciones[i][j][k].length; l++) {
						resultado += (transiciones[i][j][k][l] != null)
								? numeroAEstado.get(i) + ":" + numeroASimboloAlfabeto.get(j) + ":"
										+ numeroASimboloPila.get(k) + ":" + numeroASimboloPila.get(l) + ">"
										+ transiciones[i][j][k][l] + "\n"
								: "";
					}

				}
			}
		}
		return resultado;
	}

}
