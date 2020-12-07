package MTN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class FuncionTransicionMTN {
	private Set<String> conjuntoEstados;
	private Set<Character> alfabetoCinta;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloAlfabetoANumero;
	private arrTransicion [][] transiciones;
	private Vector<String> numeroAEstado;
	private Vector<Character> numeroASimboloAlfabeto;
	
	public FuncionTransicionMTN(Set<String> conjuntoEstados, Set<Character> alfabetoCinta,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloAlfabetoANumero, 
			Vector<String> numeroAEstado, Vector<Character> numeroASimboloAlfabeto) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.alfabetoCinta = alfabetoCinta;
		this.estadoANumero = estadoANumero;
		this.simboloAlfabetoANumero = simboloAlfabetoANumero;
		this.transiciones = new arrTransicion[conjuntoEstados.size()][alfabetoCinta.size() + 1];
		/* Se suma 1 pues se utiliza ! en la cinta */
		this.numeroAEstado = numeroAEstado;
		this.numeroASimboloAlfabeto = numeroASimboloAlfabeto;
	}

	public void setTransicion(String estadoActual, char simbolo, String estadoFinal, char simboloCinta, char movimiento) {
		if(transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero.get(simbolo)] == null)
			transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero.get(simbolo)] = new arrTransicion();
		
		Transicion t = new Transicion(estadoFinal, simboloCinta, movimiento);
		transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero.get(simbolo)].addTransicion(t);
	}

	public ArrayList<Transicion> getTransicion(String estadoActual, char simboloCinta) {

		arrTransicion transicion = transiciones[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
				.get(simboloCinta)];
		if (transicion == null)
			return null;
		
		return transicion.getTransiciones();
		
	}

	public HashMap<Character, Integer> getSimboloAlfabetoANumero() {
		return simboloAlfabetoANumero;
	}

	public Vector<Character> getNumeroASimboloAlfabeto() {
		return numeroASimboloAlfabeto;
	}

	public String toString() {
		String resultado = "";
		for (int i = 0; i < transiciones.length; i++) {
			for (int j = 0; j < transiciones[i].length; j++) {
					resultado += (transiciones[i][j] != null) ? numeroAEstado.get(i) + ":" + numeroASimboloAlfabeto.get(j) + "?" + transiciones[i][j].toString() + "\n" : "";
			}
		}
		return resultado;
	}
	
	//	TRANSICIONES
	public class arrTransicion {
		ArrayList<Transicion> lista;
		
		public arrTransicion() {
			lista = new ArrayList<Transicion>();
		}
		
		public void addTransicion(Transicion t) {
			lista.add(t);
		}
		
		public ArrayList<Transicion> getTransiciones() {
			return lista;
		}
		
		public String toString() {
			String res = "";
			for(int i = 0; i < lista.size(); i++ )
				res += (i < lista.size()-1) ? lista.get(i).toString() + ";" : lista.get(i).toString();
			return res;
		}
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
