package MT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class MT {
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoEntrada;
	private Set<Character> alfabetoCinta;
	private FuncionTransicionMT delta;
	private String cinta;

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
		this.cinta = "";
	}

	//Constructor con Nombre de Archivo
	public MT(String nombreArchivo) {
		Scanner scanner = null;
		int[] headers = new int[6]; // Encabezados como #alphabet, guarda sus posiciones
		File archivo = new File("src/Pruebas/MT/" + nombreArchivo + ".tm");

		try {
			scanner = new Scanner(archivo);
			int numberOfHeaders = 0;
			int line = 0; // iterador
			while (scanner.hasNextLine()) {
				if (scanner.nextLine()
						.matches("#tapeAlphabet|#inputAlphabet|#states|#initial|#accepting|#transitions")) {
					headers[numberOfHeaders] = line;
					++numberOfHeaders;
				}
				++line;
			}
			scanner = new Scanner(archivo);
		} catch (FileNotFoundException e) {
			System.out.println("Error en la lectura del archivo, no fue encontrado.");
			return;
		}

		Set<String> conjuntoEstados = new HashSet<String>();
		String estadoInicial = null;
		Set<String> estadosAceptacion = new HashSet<String>();
		Set<Character> alfabetoEntrada = new HashSet<Character>();
		Set<Character> alfabetoCinta = new HashSet<Character>();
		FuncionTransicionMT delta = null;
		HashMap<String, Integer> estadoANumero = null;
		HashMap<Character, Integer> simboloAlfabetoANumero = null;
		Vector<String> numeroAEstado = new Vector<String>();
		Vector<Character> numeroASimbolo = new Vector<Character>();

		for (int i = 0; i < headers.length; i++) {
			String encabezado = scanner.nextLine();
			int ultima_linea = i != headers.length - 1 ? headers[i + 1] - headers[i] - 1
					: Integer.MAX_VALUE; /*
											 * Si esta leyendo el último encabezado, entonces lee hasta el final.																									 */

			switch (encabezado) {
			case "#tapeAlphabet": {
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String linea = scanner.nextLine();
					if (linea.length() == 3) {
						int inicio = linea.charAt(0);
						int fin = linea.charAt(2);
						for (; inicio <= fin; ++inicio) {
							alfabetoCinta.add((char) inicio);
						}

					} else {
						alfabetoCinta.add(linea.charAt(0));
					}
				}
				break;
			}
			case "#inputAlphabet": {
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String linea = scanner.nextLine();
					if (linea.length() == 3) {
						int inicio = linea.charAt(0);
						int fin = linea.charAt(2);
						for (; inicio <= fin; ++inicio) {
							alfabetoEntrada.add((char) inicio);
						}

					} else {
						alfabetoEntrada.add(linea.charAt(0));
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
					numeroAEstado.add(estado);
					estadoANumero.put(estado, numeroEstado);
					++numeroEstado;
				}

				simboloAlfabetoANumero = new HashMap<>();
				simboloAlfabetoANumero.put('!', 0);
				numeroASimbolo.add('!');
				int numeroSimbolo = 1;
				for (Character simbolo : alfabetoCinta) {
					numeroASimbolo.add(simbolo);
					simboloAlfabetoANumero.put(simbolo, numeroSimbolo);
					++numeroSimbolo;
				}

				delta = new FuncionTransicionMT(conjuntoEstados, alfabetoCinta, estadoANumero, simboloAlfabetoANumero,
						numeroAEstado, numeroASimbolo);

				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String transicion = scanner.nextLine();
					String estadoActual = transicion.split(":")[0];
					char simbolo = transicion.split(":")[1].split("\\?")[0].charAt(0);
					String estadoFinal = transicion.split(":")[1].split("\\?")[1];
					char simboloCinta = transicion.split(":")[2].charAt(0);
					char movimiento = transicion.split(":")[3].charAt(0);
					delta.setTransicion(estadoActual, simbolo, estadoFinal, simboloCinta, movimiento);
				}
				break;
			}
			}
		}
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.alfabetoEntrada = alfabetoEntrada;
		this.alfabetoCinta = alfabetoCinta;
		this.delta = delta;
		this.cinta = "";

	}

	private String procesarCadena(String cadena, boolean imprimirProcesamiento) {
		String procesamiento = "Cadena: " + cadena + "\n" + "Procesamiento: \n";
		String estadoActual = estadoInicial;
		cinta = cadena;
		int apuntador = 0;
		while (!estadosAceptacion.contains(estadoActual)) {
			if (apuntador < 0) {
				cinta = "!".repeat(apuntador * -1) + cinta;
				apuntador = 0;
			}
			if (apuntador >= cinta.length()) {
				cinta = cinta + "!".repeat(apuntador - cinta.length() + 1);
			}

			procesamiento += cinta.substring(0, apuntador) + "(" + estadoActual + ")" + cinta.substring(apuntador)
					+ "->";
			//Ejecutar delta
			try {
				char ch = delta.getTransicion(estadoActual, cinta.charAt(apuntador)).simbolo;
				char mov = delta.getTransicion(estadoActual, cinta.charAt(apuntador)).movimiento;
				estadoActual = delta.getTransicion(estadoActual, cinta.charAt(apuntador)).estado;
				//cambiar símbolo en cinta
				cinta = cinta.substring(0, apuntador) + ch + cinta.substring(apuntador + 1);
				//movimiento
				if (mov == '<')
					apuntador--;
				if (mov == '>')
					apuntador++;
				//estado
			} catch (Exception e) {
				procesamiento += "> no";
				if (imprimirProcesamiento)
					System.out.println(procesamiento);
				return "abortada";
			}
		}
		procesamiento += cinta.substring(0, apuntador) + "(" + estadoActual + ")" + cinta.substring(apuntador) + "->";
		procesamiento += "> yes";
		if (imprimirProcesamiento)
			System.out.println(procesamiento);
		return "aceptada";
	}

	public boolean procesarCadena(String cadena) {
		return procesarCadena(cadena, false).equals("aceptada");
	}

	public boolean procesarCadenaConDetalles(String cadena) {
		String aux = procesarCadena(cadena, true);
		return aux.equals("aceptada");
	}

	public String procesarFuncion(String cadena) {
		procesarCadena(cadena, false);
		//System.out.println(cinta);
		return cinta;
	}

	public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {

		PrintStream flujo_salida;
		File archivo = null;
		if (nombreArchivo != null && nombreArchivo.length() > 0)
			archivo = new File("src/Resultados/MT/" + nombreArchivo + ".txt");
		try {
			flujo_salida = new PrintStream(archivo);
		} catch (Exception e) {
			archivo = new File("src/Resultados/MT/" + "procesamientoListaCadenas" + ".txt");
			try {
				flujo_salida = new PrintStream(archivo);
			} catch (FileNotFoundException e1) {
				System.out.println("Error en el procesamiento de cadenas");
				return;
			}
		}

		for (Iterator<String> iterator = listaCadenas.iterator(); iterator.hasNext();) {
			String cadena = (String) iterator.next();
			String configFinal = procesarFuncion(cadena);
			boolean resultado = procesarCadena(cadena);
			String procesamientoConDetalles = cadena + "\t" + configFinal + "\t" + (resultado ? "yes" : "no");
			if (imprimirPantalla)
				System.out.println(procesamientoConDetalles);
			flujo_salida.println(procesamientoConDetalles);

		}
		flujo_salida.flush();
		flujo_salida.close();
	}

	public String toString() {
		String resultado = "";
		resultado += "#states \n";
		resultado += conjuntoEstados.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "")
				.replaceAll(",", "\n") + "\n";
		resultado += "#initial \n";
		resultado += estadoInicial + "\n";
		resultado += "#accepting \n";
		resultado += estadosAceptacion.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "")
				.replaceAll(",", "\n") + "\n";
		resultado += "#inputAlphabet \n";
		resultado += alfabetoEntrada.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "")
				.replaceAll(",", "\n") + "\n";
		resultado += "#tapeAlphabet \n";
		resultado += alfabetoCinta.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "")
				.replaceAll(",", "\n") + "\n";
		resultado += "#transitions \n";
		resultado += delta.toString() + "\n";
		return resultado;
	}
}
