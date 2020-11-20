package MT;

import java.util.Set;

public class MT {
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoEntrada;
	private Set<Character> alfabetoCinta;
	private FuncionTransicionMT delta;
	//Constructor
	public MT(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
			Set<Character> alfabetoEntrada, Set<Character> alfabetoCinta, FuncionTransicionMT delta) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.alfabetoEntrada = alfabetoEntrada;
		this.alfabetoCinta = alfabetoCinta;
		this.delta = delta;
	}
	public MT(String nombreArchivo) {
		
	}
}
