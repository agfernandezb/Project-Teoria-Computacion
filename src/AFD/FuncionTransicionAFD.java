package AFD;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class FuncionTransicionAFD {
	private Set<Character> alfabeto;
	private Set<String> conjuntoEstados;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloANumero;
	private String[][] transiciones;
	private Vector<String> numeroAEstado;
	private Vector<Character> numeroASimboloAlfabeto;

	public FuncionTransicionAFD(Set<Character> alfabeto, Set<String> conjuntoEstados,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloANumero,
			Vector<String> numeroAEstado, Vector<Character> numeroASimboloAlfabeto) {
		super();
		this.alfabeto = alfabeto;
		this.conjuntoEstados = conjuntoEstados;
		this.estadoANumero = estadoANumero;
		this.simboloANumero = simboloANumero;
		this.transiciones = new String[conjuntoEstados.size()][alfabeto.size()];
		this.numeroAEstado = numeroAEstado;
		this.numeroASimboloAlfabeto = numeroASimboloAlfabeto;
	}

	public void setTransicion(String estadoActual, char simbolo, String estadoFinal) {
		transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)] = estadoFinal;
	}

	public String getTransicion(String estadoActual, char simbolo) {
		return transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)];
	}

	public String toString() {
		String resultado = "";
		for (int i = 0; i < transiciones.length; i++) {
			for (int j = 0; j < transiciones[i].length; j++) {
				resultado += (transiciones[i][j] != null)
						? numeroAEstado.get(i) + ":" + numeroASimboloAlfabeto.get(j) + ":" + ">" + transiciones[i][j]
								+ "\n"
						: "";

			}
		}
		return resultado;
	}

}
