package AFPD;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import AFD.AFD;

public class AFPD {
	private Set<Character> alfabetoCinta;
	private Set<Character> alfabetoPila;
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private String[][][] funcionTransicion; // Triple, donde 0 representa lambda y pues debe haber un string el cual se
											// puede tomar
											// de hecho solo 1
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloAlfabetoANumero;
	private HashMap<Character, Integer> simboloPilaANumero;

	public AFPD(Set<Character> alfabetoCinta, Set<Character> alfabetoPila, Set<String> conjuntoEstados,
			String estadoInicial, Set<String> estadosAceptacion, String[][][] funcionTransicion) {
		super();
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.funcionTransicion = funcionTransicion;
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

		Set<Character> alfabetoCinta = new HashSet<Character>();
		Set<Character> alfabetoPila = new HashSet<Character>();
		Set<String> conjuntoEstados = new HashSet<String>();
		String estadoInicial = null;
		Set<String> estadosAceptacion = new HashSet<String>();
		String[][][] funcionTransicion = null;
		HashMap<String, Integer> estadoANumero = null;
		HashMap<Character, Integer> simboloAlfabetoANumero = null;
		HashMap<Character, Integer> simboloPilaANumero = null;
		boolean procesoAlfabetoCinta = false;

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

				funcionTransicion = new String[numeroEstado][numeroSimboloCinta][numeroSimboloPila];
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String transicion = scanner.nextLine();
					String estadoActual = transicion.split(">")[0].split(":")[0];
					char simboloCinta = transicion.split(">")[0].split(":")[1].charAt(0);
					char simboloPila = transicion.split(">")[0].split(":")[2].charAt(0);
					String configuracionFinal = transicion.split(">")[1];
					funcionTransicion[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
							.get(simboloCinta)][simboloPilaANumero.get(simboloPila)] = configuracionFinal;
				}
				break;
			}
			}
		}

		this.estadoANumero = estadoANumero;
		this.simboloAlfabetoANumero = simboloAlfabetoANumero;
		this.simboloPilaANumero = simboloPilaANumero;
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.funcionTransicion = funcionTransicion;

	}

	public boolean procesarCadena(String cadena) {
		String estadoActual = estadoInicial;
		char pilaSiguiente;
		String estadoSiguiente;
		Vector<Character> pila = new Vector<>();
		char topePila;
		if (cadena.equals("$"))
			return estadosAceptacion.contains(estadoActual);
		for (int i = 0; i < cadena.length(); ++i) {
			String configuracionSiguiente = null;
			if (pila.isEmpty())
				topePila = '$';
			else
				topePila = pila.get(pila.size() - 1);
			configuracionSiguiente = funcionTransicion[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
					.get(cadena.charAt(i))][simboloPilaANumero.get(topePila)];
			if (topePila != '$' && configuracionSiguiente != null) {
				estadoSiguiente = configuracionSiguiente.split(":")[0];
				pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
				if (pilaSiguiente == '$') {// Remover tope
					pila.remove(pila.size() - 1);
				} else if (pilaSiguiente != '$') {// Reemplazar tope
					pila.set(pila.size() - 1, pilaSiguiente);
				}
			} else {
				configuracionSiguiente = funcionTransicion[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
						.get(cadena.charAt(i))][simboloPilaANumero.get('$')];
				if (configuracionSiguiente == null) {
					return false;
				}
				estadoSiguiente = configuracionSiguiente.split(":")[0];
				pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
				if (pilaSiguiente != '$') {// Agregar tope
					pila.add(pilaSiguiente);
				}
			}
		}
		return estadosAceptacion.contains(estadoActual) && pila.isEmpty();
	}

	public boolean procesarCadenaConDetalles(String cadena) {
		String estadoActual = estadoInicial;
		char pilaSiguiente;
		String estadoSiguiente;
		Vector<Character> pila = new Vector<>();
		char topePila;
		String procesamiento = "";
		String pilaString = "";
		// Que pasa si no se termina el procesamiento, e ir añadiendo a resultado
		if (!cadena.equals("$")) {
			for (int i = 0; i < cadena.length(); ++i) {
				String configuracionSiguiente = null;
				if (pila.isEmpty())
					topePila = '$';
				else
					topePila = pila.get(pila.size() - 1);
				configuracionSiguiente = funcionTransicion[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
						.get(cadena.charAt(i))][simboloPilaANumero.get(topePila)];
				if (topePila != '$' && configuracionSiguiente != null) {
					estadoSiguiente = configuracionSiguiente.split(":")[0];
					pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
					if (pilaSiguiente == '$') {// Remover tope
						pila.remove(pila.size() - 1);
					} else if (pilaSiguiente != '$') {// Reemplazar tope
						pila.set(pila.size() - 1, pilaSiguiente);
					}
				} else {
					configuracionSiguiente = funcionTransicion[estadoANumero.get(estadoActual)][simboloAlfabetoANumero
							.get(cadena.charAt(i))][simboloPilaANumero.get('$')];
					if (configuracionSiguiente == null) {
						return false;
					}
					estadoSiguiente = configuracionSiguiente.split(":")[0];
					pilaSiguiente = configuracionSiguiente.split(":")[1].charAt(0);
					if (pilaSiguiente != '$') {// Agregar tope
						pila.add(pilaSiguiente);
					}
				}
			}

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
		System.out.println(procesamiento + resultadoProcesamiento);
		return resultado;
		/*
		String estadoActual = estadoInicial;
		String procesamiento = "";
		if (!cadena.equals("$")) {
			for (int i = 0; i < cadena.length(); ++i) {
				procesamiento += "(" + estadoActual + "," + cadena.substring(i) + ")->";
				estadoActual = funcionTransicion[estadoANumero.get(estadoActual)][simboloANumero.get(cadena.charAt(i))];
		
			}
		}
		
		boolean resultado = estadosAceptacion.contains(estadoActual);
		String resultadoProcesamiento = resultado ? "accepted" : "rejected";
		System.out.println(procesamiento + resultadoProcesamiento);
		return resultado;
		*/
	}
	/*
	public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {
	
		PrintStream flujo_salida;
		File archivo = null;
		if (nombreArchivo != null && nombreArchivo.length() > 0)
			archivo = new File("src/ProcesamientoCadenas/AFD/" + nombreArchivo + ".dfa");
		try {
			flujo_salida = new PrintStream(archivo);
		} catch (Exception e) {
			archivo = new File("src/ProcesamientoCadenas/AFD/" + "procesamientoListaCadenas" + ".dfa");
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
			for (int i = 0; i < cadena.length(); ++i) {
				procesamiento += "(" + estadoActual + "," + cadena.substring(i) + ")->";
				estadoActual = funcionTransicion[estadoANumero.get(estadoActual)][simboloANumero.get(cadena.charAt(i))];
	
			}
			procesamiento += "(" + estadoActual + "," + "$)>>";
			boolean resultado = estadosAceptacion.contains(estadoActual);
			String resultadoProcesamiento = resultado ? "accepted" : "rejected";
			if (imprimirPantalla)
				System.out.println(procesamiento + resultadoProcesamiento);
			flujo_salida.print(cadena + "\t");
			flujo_salida.print(procesamiento + resultadoProcesamiento + "\t");
			flujo_salida.println(resultado ? "yes" : "no");
	
		}
		flujo_salida.flush();
		flujo_salida.close();
	}*/

	public static void main(String[] args) {
		AFPD afpd = new AFPD("uno");
		System.out.println(afpd.procesarCadena("aaabbba"));
		System.out.println(afpd.procesarCadena("aaabbb"));
		System.out.println(afpd.procesarCadena("bbbaaa"));
		System.out.println(afpd.procesarCadena("aaabb"));
		System.out.println(afpd.procesarCadena("abb"));
		System.out.println(afpd.procesarCadena("$"));

		AFD afd = new AFD("uno");
		List<String> lista = new ArrayList<>();
		lista.add("$");
		lista.add("ACCCC");
		lista.add("BBBBB");
		lista.add("CCCCACCC");
		lista.add("ACCCCAAABBBBAA");
		afd.procesarListaCadenas(lista, "xd", true);
	}

}
