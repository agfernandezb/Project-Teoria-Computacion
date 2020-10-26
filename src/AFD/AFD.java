package AFD;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class AFD {
	private Set<Character> alfabeto;
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private String[][] funcionTransicion;
	private HashMap<String, Integer> estadoANumero;
	private HashMap<Character, Integer> simboloANumero;

	public AFD(Set<Character> alfabeto, Set<String> conjuntoEstados, String estadoInicial,
			Set<String> estadosAceptacion, String[][] funcionTransicion) {
		super();
		this.alfabeto = alfabeto;
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.funcionTransicion = funcionTransicion;
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
		String estadoInicial;
		Set<String> estadosAceptacion = new HashSet<String>();
		String[][] funcionTransicion;
		HashMap<String, Integer> estadoANumero;
		HashMap<Character, Integer> simboloANumero;

		for (int i = 0; i < headers.length; i++) {
			String encabezado = scanner.nextLine();
			int ultima_linea = i != headers.length - 1 ? headers[i + 1] - headers[i] - 1
					: Integer.MAX_VALUE; /*
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

				funcionTransicion = new String[numeroEstado][alfabeto.size()];
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String transicion = scanner.nextLine();
					String estadoActual = transicion.split(":")[0];
					char simbolo = transicion.split(":")[1].split(">")[0].charAt(0);
					String estadoFinal = transicion.split(":")[1].split(">")[1];
					funcionTransicion[estadoANumero.get(estadoActual)][simboloANumero.get(simbolo)] = estadoFinal;
				}
				break;
			}
			}
		}

	}

	public static void main(String[] args) {
		System.out.println(new AFD("uno"));
	}

}
