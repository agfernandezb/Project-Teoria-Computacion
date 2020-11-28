package MTP;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;


public class FuncionTransicionMTP {
	private Set<String> conjuntoEstados;
	private Set<Character> alfabetoCinta;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<String, Integer> simboloANumero;
	private Transicion[][] transiciones;
	private Vector<String> numeroAEstado;
	private Vector<String> numeroASimbolo;

	public FuncionTransicionMTP(Set<String> conjuntoEstados, Set<Character> alfabetoCinta,
			HashMap<String, Integer> estadoANumero, HashMap<String, Integer> simboloANumero,
			Vector<String> numeroAEstado, Vector<String> numeroASimbolo, int n_sim) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.alfabetoCinta = alfabetoCinta;
		this.estadoANumero = estadoANumero;
		this.simboloANumero = simboloANumero;
		this.transiciones = new Transicion[conjuntoEstados.size()][n_sim];
		this.numeroAEstado = numeroAEstado;
		this.numeroASimbolo = numeroASimbolo;

	}

	public void setTransicion(String estadoActual, String simbolos, String estadoFinal, String simbolosCinta,
			char movimiento) {
		
		if (movimiento == '-' || movimiento == '<' || movimiento == '>')
			transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolos)] = new Transicion(estadoFinal,
					simbolosCinta, movimiento);
		else
			System.out.println("Error en entrada desplazamiento!");
	}

	public Transicion getTransicion(String estadoActual, String simbolos) {
		return transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolos)];
	}

	public String toString() {
		String resultado = "";
		for (int i = 0; i < transiciones.length; ++i)
			for (int j = 0; j < transiciones[i].length; ++j) {
				String simbolos = "";
				for(int k = 0; k < numeroASimbolo.get(j).length(); k++) {
					simbolos += numeroASimbolo.get(j).charAt(k);
					simbolos += (k < numeroASimbolo.get(j).length()-1) ? ":" : "";
				}
				resultado += (transiciones[i][j] != null)
						? numeroAEstado.get(i) + ":" + simbolos + "?" + transiciones[i][j].toString()
								+ "\n"
						: "";
			}
		return resultado;
	}

	public class Transicion {
		String estado;
		String simbolo;
		char movimiento;

		public Transicion(String estado, String simbolo, char movimiento) {
			super();
			this.estado = estado;
			this.simbolo = simbolo;
			this.movimiento = movimiento;
		}

		public String toString() {
			String cadena = "";
			for(int k = 0; k < simbolo.length(); k++) {
				cadena += simbolo.charAt(k);
				cadena += (k < simbolo.length()-1) ? ":" : "";
			}
			
			return estado + ":" + cadena + ":" + movimiento;
		}
	}
}
