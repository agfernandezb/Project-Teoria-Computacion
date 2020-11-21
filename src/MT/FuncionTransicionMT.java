package MT;

import java.util.HashMap;
import java.util.Set;

public class FuncionTransicionMT {
	private Set<String> conjuntoEstados;
	private Set<Character> alfabetoCinta;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloANumero;
	private Transicion [][] transiciones;
	
	
	public FuncionTransicionMT(Set<String> conjuntoEstados, Set<Character> alfabetoCinta,
			HashMap<String, Integer> estadoANumero, HashMap<Character, Integer> simboloANumero) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.alfabetoCinta = alfabetoCinta;
		this.estadoANumero = estadoANumero;
		this.simboloANumero = simboloANumero;
		this.transiciones = new Transicion [conjuntoEstados.size()][alfabetoCinta.size()+1];
		
	}

	public void setTransicion(String estadoActual, char simbolo, String estadoFinal, char simboloCinta, char movimiento) {
		if(movimiento=='-' || movimiento=='<' || movimiento=='>')
			transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)] = new Transicion(estadoFinal, simboloCinta, movimiento);
		else
			System.out.println("Error en entrada desplazamiento!");
	}

	public Transicion getTransicion(String estadoActual, char simbolo) {
		return transiciones[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)];
	}
	
	public String toString() {
		String resultado = "";
        for (Transicion[] row : transiciones) 
            for (Transicion x : row) 
                resultado += (x!= null) ? x.toString()+"\n" : ""; 
        
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
