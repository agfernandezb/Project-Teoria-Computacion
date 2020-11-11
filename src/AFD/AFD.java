package AFD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AFD {
	private Set<Character> alfabeto;
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private FuncionTransicionAFD delta;

	public AFD(Set<Character> alfabeto, Set<String> conjuntoEstados, String estadoInicial,
			Set<String> estadosAceptacion, FuncionTransicionAFD delta) {
		super();
		this.alfabeto = alfabeto;
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.delta = delta;
	}

	public AFD(String nombre) {
		Scanner scanner = null;
		int[] headers = new int[5]; // Encabezados como #alphabet, guarda sus posiciones
		File archivo = new File("src/Pruebas/AFD/" + nombre + ".dfa");

		try {
			scanner = new Scanner(archivo);
			int numberOfHeaders = 0;
			int line = 0;
			while (scanner.hasNextLine()) {
				if (scanner.nextLine().matches("#alphabet|#states|#initial|#accepting|#transitions")) {
					headers[numberOfHeaders] = line;
					++numberOfHeaders;
				}
				++line;
			}
			scanner = new Scanner(archivo);
		} catch (FileNotFoundException e) {
			System.out.println("Error en la lectura del archivo, no fue encontrado.");
		}

		Set<Character> alfabeto = new HashSet<Character>();
		Set<String> conjuntoEstados = new HashSet<String>();
		String estadoInicial = null;
		Set<String> estadosAceptacion = new HashSet<String>();
		FuncionTransicionAFD delta = null;
		HashMap<String, Integer> estadoANumero = null;
		HashMap<Character, Integer> simboloANumero = null;

		for (int i = 0; i < headers.length; i++) {
			String encabezado = scanner.nextLine();
			int ultima_linea = i != headers.length - 1 ? headers[i + 1] - headers[i] - 1 : Integer.MAX_VALUE; /*
																												 * Si esta leyendo el último encabezado, entonces lee hasta el final.
																												 */

			switch (encabezado) {
			case "#alphabet": {
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String linea = scanner.nextLine();
					if (linea.length() == 3) {
						int inicio = linea.charAt(0);
						int fin = linea.charAt(2);
						for (; inicio <= fin; ++inicio) {
							alfabeto.add((char) inicio);
						}

					} else {
						alfabeto.add(linea.charAt(0));
					}
				}
				break;
			}
			case "#states": {
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					conjuntoEstados.add(scanner.nextLine());
				}
				break;

			}
			case "#initial": {
				estadoInicial = scanner.nextLine();
				break;
			}
			case "#accepting": {

				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					estadosAceptacion.add(scanner.nextLine());
				}
				break;
			}
			case "#transitions": {
				estadoANumero = new HashMap<>();
				int numeroEstado = 0;
				for (String estado : conjuntoEstados) {
					estadoANumero.put(estado, numeroEstado);
					++numeroEstado;
				}

				simboloANumero = new HashMap<>();
				int numeroSimbolo = 0;
				for (Character simbolo : alfabeto) {
					simboloANumero.put(simbolo, numeroSimbolo);
					++numeroSimbolo;
				}

				delta = new FuncionTransicionAFD(alfabeto, conjuntoEstados, estadoANumero, simboloANumero);

				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String transicion = scanner.nextLine();
					String estadoActual = transicion.split(":")[0];
					char simbolo = transicion.split(":")[1].split(">")[0].charAt(0);
					String estadoFinal = transicion.split(":")[1].split(">")[1];
					delta.setTransicion(estadoActual, simbolo, estadoFinal);
				}
				break;
			}
			}
		}

		this.alfabeto = alfabeto;
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.delta = delta;

	}

	public String procesarCadena(String cadena, boolean retornarProcesamiento) {
		String estadoActual = estadoInicial;
		String procesamiento = "";

		if (cadena.equals("$")) {
			procesamiento += "(" + estadoInicial + ",$,$" + ")>>";
			boolean resultado = estadosAceptacion.contains(estadoInicial);
			String resultadoProcesamiento = resultado ? "accepted" : "rejected";
			if (retornarProcesamiento)
				return procesamiento + resultadoProcesamiento;
			return resultadoProcesamiento;
		}

		for (int i = 0; i < cadena.length(); ++i) {
			procesamiento += "(" + estadoActual + "," + cadena.substring(i) + ")->";
			estadoActual = delta.getTransicion(estadoActual, cadena.charAt(i));
		}
		procesamiento += "(" + estadoActual + "," + "$)>>";
		boolean resultado = estadosAceptacion.contains(estadoActual);
		String resultadoProcesamiento = resultado ? "accepted" : "rejected";

		if (retornarProcesamiento)
			return procesamiento + resultadoProcesamiento;
		return resultadoProcesamiento;
	}

	public boolean procesarCadena(String cadena) {
		return procesarCadena(cadena, false).equals("accepted");
	}

	public boolean procesarCadenaConDetalles(String cadena) {
		String procesamiento = procesarCadena(cadena, true);
		System.out.println(procesamiento);
		return procesamiento.split(">>")[1].equals("accepted");
	}

	public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {

		PrintStream flujo_salida;
		File archivo = null;
		if (nombreArchivo != null && nombreArchivo.length() > 0)
			archivo = new File("src/ProcesamientoCadenas/AFD/" + nombreArchivo + ".txt");
		try {
			flujo_salida = new PrintStream(archivo);
		} catch (Exception e) {
			archivo = new File("src/ProcesamientoCadenas/AFD/" + "procesamientoListaCadenas" + ".txt");
			try {
				flujo_salida = new PrintStream(archivo);
			} catch (FileNotFoundException e1) {
				System.out.println("Error en el procesamiento de cadenas");
				return;
			}
		}

		for (Iterator<String> iterator = listaCadenas.iterator(); iterator.hasNext();) {
			String cadena = (String) iterator.next();
			String estadoActual = estadoInicial;
			String procesamiento = "";
			if (!cadena.equals("$")) {
				for (int i = 0; i < cadena.length(); ++i) {
					procesamiento += "(" + estadoActual + "," + cadena.substring(i) + ")->";
					estadoActual = delta.getTransicion(estadoActual, cadena.charAt(i));

				}
			}
			procesamiento += "(" + estadoActual + "," + "$)>>";
			boolean resultado = estadosAceptacion.contains(estadoActual);
			String resultadoProcesamiento = resultado ? "accepted" : "rejected";
			String procesamientoConDetalles = cadena + "\t" + procesamiento + "\t" + (resultado ? "yes" : "no");
			if (imprimirPantalla)
				System.out.println(procesamientoConDetalles);
			flujo_salida.println(procesamientoConDetalles);
		}
		flujo_salida.flush();
		flujo_salida.close();
	}

	public String getTransicion(String estado, char simbolo) {
		return delta.getTransicion(estado, simbolo);
	}

	public Set<Character> getAlfabeto() {
		return alfabeto;
	}

	public Set<String> getConjuntoEstados() {
		return conjuntoEstados;
	}

	public String getEstadoInicial() {
		return estadoInicial;
	}

	public Set<String> getEstadosAceptacion() {
		return estadosAceptacion;
	}

}
