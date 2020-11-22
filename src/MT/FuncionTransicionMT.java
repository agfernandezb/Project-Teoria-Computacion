package MT;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class FuncionTransicionMT {
	private Set<String> conjuntoEstados;
	private Set<Character> alfabetoCinta;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloANumero;
	private Transicion[][] transiciones;
	private Vector<String> numeroAEstado;
	private Vector<Character> numeroASimbolo;

	public FuncionTransicionMT(Set<String> conjuntoEstados, Set<Character> alfabetoCinta,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloANumero,
			Vector<String> numeroAEstado, Vector<Character> numeroASimbolo) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.alfabetoCinta = alfabetoCinta;
		this.estadoANumero = estadoANumero;
		this.simboloANumero = simboloANumero;
		this.transiciones = new Transicion[conjuntoEstados.size()][alfabetoCinta.size() + 1];
		this.numeroAEstado = numeroAEstado;
		this.numeroASimbolo = numeroASimbolo;

	}

	public void setTransicion(String estadoActual, char simbolo, String estadoFinal, char simboloCinta,
			char movimiento) {
		if (movimiento == '-' || movimiento == '<' || movimiento == '>')
			transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)] = new Transicion(estadoFinal,
					simboloCinta, movimiento);
		else
			System.out.println("Error en entrada desplazamiento!");
	}

	public Transicion getTransicion(String estadoActual, char simbolo) {
		return transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)];
	}

	public String toString() {
		String resultado = "";
		for (int i = 0; i < transiciones.length; ++i)
			for (int j = 0; j < transiciones[i].length; ++j)
				resultado += (transiciones[i][j] != null)
						? numeroAEstado.get(i) + ":" + numeroASimbolo.get(j) + "?" + transiciones[i][j].toString()
								+ "\n"
						: "";

		return resultado;
	}

	public class Transicion {
		String estado;
		char simbolo;
		char movimiento;

		public Transicion(String estado, char simbolo, char movimiento) {
			super();
			this.estado = estado;
			this.simbolo = simbolo;
			this.movimiento = movimiento;
		}

		public String toString() {
			return estado + ":" + simbolo + ":" + movimiento;
		}
	}
}
