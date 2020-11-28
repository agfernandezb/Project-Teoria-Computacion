package MTP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

public class MTP {
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoEntrada;
	private Set<Character> alfabetoCinta;
	private FuncionTransicionMTP delta;
	private List<String> cintaPistas;
	//Constructor
		public MTP(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
				Set<Character> alfabetoEntrada, Set<Character> alfabetoCinta, FuncionTransicionMTP delta) {
			super();
			this.conjuntoEstados = conjuntoEstados;
			this.estadoInicial = estadoInicial;
			this.estadosAceptacion = estadosAceptacion;
			this.alfabetoEntrada = alfabetoEntrada;
			this.alfabetoCinta = alfabetoCinta;
			this.delta = delta;
			cintaPistas = new ArrayList<String>();
			cintaPistas.add("!!!!!!!!!!!!!!!!");
		}

		//Constructor con Nombre de Archivo
		public MTP(String nombreArchivo) {
			Scanner scanner = null;
			int[] headers = new int[6]; // Encabezados como #alphabet, guarda sus posiciones
			File archivo = new File("src/Pruebas/MTP/" + nombreArchivo + ".ttm");

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
			FuncionTransicionMTP delta = null;
			HashMap<String, Integer> estadoANumero = null;
			HashMap<String, Integer> simbolosAlfabetoANumero = null;
			Vector<String> numeroAEstado = new Vector<String>();
			Vector<String> numeroASimbolos = new Vector<String>();
			int n_pistas = 1;
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
					
					List<String> auxi = new ArrayList<String>();
					List<String> auxii = new ArrayList<String>();
					for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
						String transicion = scanner.nextLine();
						String estadoActual = transicion.split(":")[0]; //Estado Actual
						auxi.add(estadoActual);
						String simbolos = transicion.split("\\?")[0].substring(estadoActual.length()).replaceAll(":",""); //Símbolos
						auxi.add(simbolos);
						auxii.add(simbolos);
						String estadoFinal = transicion.split("\\?")[1].split(":")[0]; //Estado Final
						auxi.add(estadoFinal);
						String simbolosCinta = transicion.split("\\?")[1].substring(estadoFinal.length()).replaceAll(":","").replaceAll("<","").replaceAll(">","").replaceAll("-",""); //Símbolos a reemplazar en la cinta
						auxi.add(simbolosCinta);
						auxii.add(simbolos);
						char movimiento = transicion.split(":")[transicion.split(":").length-1].charAt(0); //Movimiento
						auxi.add(Character.toString(movimiento));
					}
					
					n_pistas = auxii.get(0).length();
					
					simbolosAlfabetoANumero = new HashMap<>();
					simbolosAlfabetoANumero.put("!".repeat(n_pistas), 0);
					numeroASimbolos.add("!".repeat(n_pistas));
					
					int numeroSimbolo = 1;
					for (String cadena : auxii) {
						numeroASimbolos.add(cadena);
						simbolosAlfabetoANumero.put(cadena, numeroSimbolo);
						++numeroSimbolo;
					}

					delta = new FuncionTransicionMTP(conjuntoEstados, alfabetoCinta, estadoANumero, simbolosAlfabetoANumero,
							numeroAEstado, numeroASimbolos, numeroSimbolo);

					for (int j = 0; j < auxi.size(); j+=5) {
						delta.setTransicion(auxi.get(j), auxi.get(j+1), auxi.get(j+2), auxi.get(j+3), auxi.get(j+4).charAt(0));
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

			this.cintaPistas =  new ArrayList<String>();
			for(int i = 0; i < n_pistas; i++)
				this.cintaPistas.add("!".repeat(n_pistas));
		}

		private boolean procesarCadena(String cadena, boolean imprimirProcesamiento) {
			String procesamiento = "Cadena: " + cadena + "\n" + "Procesamiento: \n";
			String estadoActual = estadoInicial;
			
			cintaPistas.set(0, cadena);
			for(int i = 1; i < cintaPistas.size(); i++)
				cintaPistas.set(i, "!".repeat(cadena.length()));
			
			int apuntador = 0;
			while (!estadosAceptacion.contains(estadoActual)) {
				if (apuntador < 0) {
					for(int i = 0; i < cintaPistas.size(); i++)
						cintaPistas.set(i, "!".repeat(apuntador * -1) + cintaPistas.get(i));
					apuntador = 0;
				}
				if (apuntador >= cintaPistas.get(0).length()) {
					for(int i = 0; i < cintaPistas.size(); i++)
						cintaPistas.set(i, cintaPistas.get(i) + "!".repeat(apuntador - cintaPistas.get(0).length() + 1));
				}
				
				//Procesamiento
				for(int i = 0; i < apuntador; i++) {
					procesamiento += "(";
					for(int j = 0; j < cintaPistas.size(); j++) {
						procesamiento += cintaPistas.get(j).charAt(i);
						procesamiento += (j < cintaPistas.size()-1) ? "," : "";
					}
					procesamiento += ")";
				}
				procesamiento += "(" + estadoActual + ")";
				for(int i = apuntador; i < cintaPistas.get(0).length(); i++) {
					procesamiento += "(";
					for(int j = 0; j < cintaPistas.size(); j++) {
						procesamiento += cintaPistas.get(j).charAt(i);
						procesamiento += (j < cintaPistas.size()-1) ? "," : "";
					}
					procesamiento += ")";
				}
				procesamiento += "->";
				
				//Ejecutar delta
				try {
					String lecturaCinta = "";
					for(int j = 0; j < cintaPistas.size(); j++)
						lecturaCinta += cintaPistas.get(j).charAt(apuntador);
					
					String escrituraCinta = delta.getTransicion(estadoActual, lecturaCinta).simbolo;
					char mov = delta.getTransicion(estadoActual, lecturaCinta).movimiento;
					estadoActual = delta.getTransicion(estadoActual, lecturaCinta).estado;
					//cambiar símbolo en cinta
					//cinta = cinta.substring(0, apuntador) + ch + cinta.substring(apuntador + 1);
					for(int j = 0; j < cintaPistas.size(); j++) {
						cintaPistas.set(j, cintaPistas.get(j).substring(0, apuntador) + escrituraCinta.charAt(j) + cintaPistas.get(j).substring(apuntador + 1));
					}
					//movimiento
					if (mov == '<')
						apuntador--;
					if (mov == '>')
						apuntador++;
				} catch (Exception e) {
					procesamiento += "> no";
					if (imprimirProcesamiento)
						System.out.println(procesamiento);
					return false;
				}
			}
			//Procesamiento
			for(int i = 0; i < apuntador; i++) {
				procesamiento += "(";
				for(int j = 0; j < cintaPistas.size(); j++) {
					procesamiento += cintaPistas.get(j).charAt(i);
					procesamiento += (j < cintaPistas.size()-1) ? "," : "";
				}
				procesamiento += ")";
			}
			procesamiento += "(" + estadoActual + ")";
			for(int i = apuntador; i < cintaPistas.get(0).length(); i++) {
				procesamiento += "(";
				for(int j = 0; j < cintaPistas.size(); j++) {
					procesamiento += cintaPistas.get(j).charAt(i);
					procesamiento += (j < cintaPistas.size()-1) ? "," : "";
				}
				procesamiento += ")";
			}
			procesamiento += "->";
			procesamiento += "> yes";
			
			if (imprimirProcesamiento)
				System.out.println(procesamiento);
			return true;
		}

		public boolean procesarCadena(String cadena) {
			return procesarCadena(cadena, false);
		}

		public boolean procesarCadenaConDetalles(String cadena) {
			return procesarCadena(cadena, true);
		}
		public String procesarFuncion(String cadena) {
			procesarCadena(cadena, false);
			String aux = "";
			for(int i = 0; i < cintaPistas.get(0).length(); i++) {
				aux += "(";
				for(int j = 0; j < cintaPistas.size(); j++) {
					aux += cintaPistas.get(j).charAt(i);
					aux += (j < cintaPistas.size()-1) ? "," : "";
				}
				aux += ")";
			}
			return aux;
		}

		public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {

			PrintStream flujo_salida;
			File archivo = null;
			if (nombreArchivo != null && nombreArchivo.length() > 0)
				archivo = new File("src/ProcesamientoCadenas/MTP/" + nombreArchivo + ".txt");
			try {
				flujo_salida = new PrintStream(archivo);
			} catch (Exception e) {
				archivo = new File("src/ProcesamientoCadenas/MTP/" + "procesamientoListaCadenasMTP" + ".txt");
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
			resultado += delta.toString();
			return resultado;
		}
		
		/*public static void main(String[] args) {
			MTP test = new MTP("a^nb^nc^n");
			System.out.println(test.procesarCadena("aabbcc"));
			test.procesarCadenaConDetalles("abcc");
			List<String> cadenas = new ArrayList<String>();
			cadenas.add("!!!!"); cadenas.add("aaabcbc"); cadenas.add("aaabbccc");
			test.procesarListaCadenas(cadenas, "pruebaMTP", true);
			System.out.println(test.toString());
		}*/
	}


