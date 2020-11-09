package AFPD;

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

public class AFPD {

	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoCinta;
	private Set<Character> alfabetoPila;
	private FuncionTransicionAFPD delta;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloAlfabetoANumero;
	private HashMap<Character, Integer> simboloPilaANumero;

	public AFPD(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
			Set<Character> alfabetoCinta, Set<Character> alfabetoPila, FuncionTransicionAFPD delta) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.delta = delta;
	}

	public AFPD(String nombre) {// Se supone que primero se procesa el alfabeto de cinta luego el de pila
		Scanner scanner = null;
		int[] headers = new int[6]; // Encabezados como #alphabet, guarda sus posiciones
		File archivo = new File("src/Pruebas/AFPD/" + nombre + ".dpda");

		try {
			scanner = new Scanner(archivo);
			int numberOfHeaders = 0;
			int line = 0; // iterador
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

		Set<String> conjuntoEstados = new HashSet<String>();
		String estadoInicial = null;
		Set<String> estadosAceptacion = new HashSet<String>();
		Set<Character> alfabetoCinta = new HashSet<Character>();
		Set<Character> alfabetoPila = new HashSet<Character>();
		FuncionTransicionAFPD delta = null;
		boolean procesoAlfabetoCinta = false; // AYUDA EN LA LECTURA PUESTO QUE AMBOS ENCABEZADOS SE LLAMAN ALFABETO.
		HashMap<String, Integer> estadoANumero = null;
		HashMap<Character, Integer> simboloAlfabetoANumero = null;
		HashMap<Character, Integer> simboloPilaANumero = null;

		for (int i = 0; i < headers.length; i++) {
			String encabezado = scanner.nextLine();
			int ultima_linea = i != headers.length - 1 ? headers[i + 1] - headers[i] - 1
					: Integer.MAX_VALUE; /*
											 * Si esta leyendo el último encabezado, entonces lee hasta el final.																									 */

			switch (encabezado) {
			case "#alphabet": {
				if (!procesoAlfabetoCinta) {
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
					procesoAlfabetoCinta = true;
				} else {
					for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
						String linea = scanner.nextLine();
						if (linea.length() == 3) {
							int inicio = linea.charAt(0);
							int fin = linea.charAt(2);
							for (; inicio <= fin; ++inicio) {
								alfabetoPila.add((char) inicio);
							}

						} else {
							alfabetoPila.add(linea.charAt(0));
						}
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

				simboloAlfabetoANumero = new HashMap<>();
				int numeroSimboloCinta = 1;
				simboloAlfabetoANumero.put('$', 0);
				for (Character simbolo : alfabetoCinta) {
					simboloAlfabetoANumero.put(simbolo, numeroSimboloCinta);
					++numeroSimboloCinta;
				}

				simboloPilaANumero = new HashMap<>();
				int numeroSimboloPila = 1;
				simboloPilaANumero.put('$', 0);
				for (Character simbolo : alfabetoPila) {
					simboloPilaANumero.put(simbolo, numeroSimboloPila);
					++numeroSimboloPila;
				}
				delta = new FuncionTransicionAFPD(conjuntoEstados, alfabetoCinta, alfabetoPila, estadoANumero,
						simboloAlfabetoANumero, simboloPilaANumero);
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String transicion = scanner.nextLine();
					String estadoActual = transicion.split(">")[0].split(":")[0];
					char simboloCinta = transicion.split(">")[0].split(":")[1].charAt(0);
					char simboloPila = transicion.split(">")[0].split(":")[2].charAt(0);
					String configuracionFinal = transicion.split(">")[1];
					delta.setTransicion(estadoActual, simboloCinta, simboloPila, configuracionFinal);
				}
				break;
			}
			}
		}

		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.delta = delta;

	}

	public void modificarPila(Vector<Character> pila, String operacion, char parametro) {
		switch (operacion) {
		case "Reemplazo": {
			pila.set(pila.size() - 1, parametro);
			break;
		}
		case "Insertar": {
			pila.add(parametro);
			break;
		}
		case "Remover": {
			pila.remove(pila.size() - 1);
			break;
		}
		}
	}

	private String procesarCadena(String cadena, boolean retornarProcesamiento) {
		String estadoActual = estadoInicial;
		char pilaSiguiente;
		Vector<Character> pila = new Vector<>();
		char topePila;
		String procesamiento = "";
		String pilaString = "";
		if (cadena.equals("$")) {
			procesamiento += "(" + estadoInicial + ",$,$" + ")>>";
			boolean resultado = estadosAceptacion.contains(estadoInicial);
			String resultadoProcesamiento = resultado ? "accepted" : "rejected";
			if (retornarProcesamiento)
				return procesamiento + resultadoProcesamiento;
			return resultadoProcesamiento;
		}
		for (int i = 0; i < cadena.length(); ++i) {
			if (pila.isEmpty())
				pilaString = "$";
			else {
				pilaString = "";
				for (int j = pila.size() - 1; j >= 0; --j) {
					pilaString += pila.get(j);
				}
			}
			procesamiento += "(" + estadoActual + "," + cadena.substring(i) + "," + pilaString + ")";
			String configuracionSiguiente = null;
			char simboloCinta = cadena.charAt(i);
			if (pila.isEmpty())
				topePila = '$';
			else
				topePila = pila.get(pila.size() - 1);
			configuracionSiguiente = delta.getTransicion(estadoActual, simboloCinta, topePila); // Tomamos la
																								// transicion con el
																								// tope de
																								// pila y el simbolo
																								// actual
			if (topePila != '$' && configuracionSiguiente != null) { // Como topePila no es Lambda, solo se hace
																		// reemplazo o eliminar
				estadoActual = configuracionSiguiente.split(":")[0];
				pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
				if (pilaSiguiente == '$') {// Remover tope
					modificarPila(pila, "Remover", '$');
				} else if (pilaSiguiente != '$') {// Reemplazar tope
					modificarPila(pila, "Reemplazo", pilaSiguiente);
				}
			} else {
				configuracionSiguiente = delta.getTransicion(estadoActual, simboloCinta, '$'); // Tomamos como simbolo
																								// de pila a Lambda
				if (configuracionSiguiente != null) { // Consumimos unicamente un simbolo de cinta, y agregamos algo o
														// no hacemos nada en la pila
					estadoActual = configuracionSiguiente.split(":")[0];
					pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
					if (pilaSiguiente != '$') {// Agregar tope
						modificarPila(pila, "Insertar", pilaSiguiente);
					}
				} else { // La unica posibilidad es que el simbolo de cinta sea Lambda
					if (topePila != '$' && delta.getTransicion(estadoActual, '$', topePila) != null) { // Tope de pila
																										// no es
																										// lambda.
						configuracionSiguiente = delta.getTransicion(estadoActual, '$', topePila);
						estadoActual = configuracionSiguiente.split(":")[0];
						pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
						if (pilaSiguiente == '$') { // Remover tope
							modificarPila(pila, "Remover", '$');
						} else if (pilaSiguiente != '$') {// Reemplazar tope
							modificarPila(pila, "Reemplazo", pilaSiguiente);
						}

					} else if (delta.getTransicion(estadoActual, '$', '$') != null) { // Tope de pila y simbolo de
																						// cinta
																						// son ambos lambda.
						configuracionSiguiente = delta.getTransicion(estadoActual, '$', '$');
						estadoActual = configuracionSiguiente.split(":")[0];
						pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
						if (pilaSiguiente != '$') {// Agregar algo a la pila
							modificarPila(pila, "Insertar", pilaSiguiente);
						}
					} else {// Ningun caso hizo funciono, se aborta el procesamiento de la cadena.
						procesamiento += ">>rejected";
						if (retornarProcesamiento)
							return procesamiento;
						return "rejected";
					}
					--i; // Como el simbolo fue Lambda, no se puede mover
				}
			}
			procesamiento += "->";
		}
		// Se pudo procesar completamente la cadena
		if (pila.isEmpty())
			pilaString = "$";
		else {
			pilaString = "";
			for (int i = pila.size() - 1; i >= 0; --i) {
				pilaString += pila.get(i);
			}
		}
		procesamiento += "(" + estadoActual + ",$," + pilaString + ")>>";
		boolean resultado = estadosAceptacion.contains(estadoActual) && pila.isEmpty();
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
			archivo = new File("src/ProcesamientoCadenas/AFPD/" + nombreArchivo + ".txt");
		try {
			flujo_salida = new PrintStream(archivo);
		} catch (Exception e) {
			archivo = new File("src/ProcesamientoCadenas/AFPD/" + "procesamientoListaCadenas" + ".txt");
			try {
				flujo_salida = new PrintStream(archivo);
			} catch (FileNotFoundException e1) {
				System.out.println("Error en el procesamiento de cadenas");
				return;
			}
		}

		for (Iterator<String> iterator = listaCadenas.iterator(); iterator.hasNext();) {
			String cadena = (String) iterator.next();
			String procesamiento = procesarCadena(cadena, true);
			boolean resultado = procesamiento.split(">>")[1].equals("accepted");
			String procesamientoConDetalles = cadena + "\t" + procesamiento + "\t" + (resultado ? "yes" : "no");
			if (imprimirPantalla)
				System.out.println(procesamientoConDetalles);
			flujo_salida.println(procesamientoConDetalles);

		}
		flujo_salida.flush();
		flujo_salida.close();
	}

}
