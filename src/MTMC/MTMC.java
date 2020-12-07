package MTMC;

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

public class MTMC {
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoEntrada;
	private Set<Character> alfabetoCinta;
	private FuncionTransicionMTMC delta;
	private cintaMTMC cinta;
	//Constructor
		public MTMC(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
				Set<Character> alfabetoEntrada, Set<Character> alfabetoCinta, FuncionTransicionMTMC delta) {
			super();
			this.conjuntoEstados = conjuntoEstados;
			this.estadoInicial = estadoInicial;
			this.estadosAceptacion = estadosAceptacion;
			this.alfabetoEntrada = alfabetoEntrada;
			this.alfabetoCinta = alfabetoCinta;
			this.delta = delta;
			cinta = new cintaMTMC(3);
		}

		//Constructor con Nombre de Archivo
		public MTMC(String nombreArchivo) {
			Scanner scanner = null;
			int[] headers = new int[6]; // Encabezados como #alphabet, guarda sus posiciones
			File archivo = new File("src/Pruebas/MTMC/" + nombreArchivo + ".mttm");

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
			FuncionTransicionMTMC delta = null;
			HashMap<String, Integer> estadoANumero = null;
			HashMap<String, Integer> simbolosAlfabetoANumero = null;
			Vector<String> numeroAEstado = new Vector<String>();
			Vector<String> numeroASimbolos = new Vector<String>();
			
			int n_pistas = 1;
			for (int i = 0; i < headers.length; i++) {
				String encabezado = scanner.nextLine();
				int ultima_linea = i != headers.length - 1 ? headers[i + 1] - headers[i] - 1
						: Integer.MAX_VALUE; /*
												 * Si esta leyendo el último encabezado, entonces lee hasta el final. */

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
						String alfYmov = transicion.split("\\?")[1].substring(estadoFinal.length()).replaceAll(":","").replaceAll(";",""); //Símbolos a reemplazar en la cinta
						String simbolosCinta = "";
						String movimientos = "";
						for(Character k: alfYmov.toCharArray()) {
							if(k=='<' || k=='>' || k=='-')
								movimientos += k;
							else
								simbolosCinta += k;
						}
						auxi.add(simbolosCinta);
						auxi.add(movimientos);
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

					delta = new FuncionTransicionMTMC(conjuntoEstados, estadoANumero, simbolosAlfabetoANumero,
							numeroAEstado, numeroASimbolos, numeroSimbolo);

					for (int j = 0; j < auxi.size(); j+=5) {
						delta.setTransicion(auxi.get(j), auxi.get(j+1), auxi.get(j+2), auxi.get(j+3), auxi.get(j+4));
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
			this.cinta = new cintaMTMC(n_pistas);
		}

		private boolean procesarCadena(String cadena, boolean imprimirProcesamiento) {
			String procesamiento = "Cadena: " + cadena + "\n" + "Procesamiento: \n";
			String estadoActual = estadoInicial;
			cinta.ponerCadena(cadena);
			long t= System.currentTimeMillis();
			long limit = t+13000; //13 SEG DE TIEMPO LÍMITE 
			
			while (!estadosAceptacion.contains(estadoActual)) {
				//
				String lecturaCinta = cinta.leer();
				procesamiento += "(" + estadoActual + "," + cinta.toString() + ")" + "->";
				//Ejecutar delta
				try {
					String escrituraCinta = delta.getTransicion(estadoActual, lecturaCinta).simbolo;
					String mov = delta.getTransicion(estadoActual, lecturaCinta).movimiento;
					estadoActual = delta.getTransicion(estadoActual, lecturaCinta).estado;
					//cambiar símbolo en cinta
					cinta.escribir(escrituraCinta);
					//movimiento
					cinta.ejecutarMov(mov);
				} catch (Exception e) {
					procesamiento += "> no";
					if (imprimirProcesamiento)
						System.out.println(procesamiento);
					return false;
				}
				// SALE POR TIEMPO
				if(System.currentTimeMillis() > limit) {
					procesamiento += "> TIME-STAMPED";
					if (imprimirProcesamiento)
						System.out.println(procesamiento);
					return false;
				}
			}
			//Procesamiento
			procesamiento += "(" + estadoActual + "," + cinta.toString() + ")" + "->";
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
			String aux = "("+ cinta.toString().replaceAll("\\*", "") + ")";
			return aux;
		}

		public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {

			PrintStream flujo_salida;
			File archivo = null;
			File dir = new File("src/ProcesamientoCadenas/MTMC");
			dir.mkdirs();
			if (nombreArchivo != null && nombreArchivo.length() > 0)
				archivo = new File("src/ProcesamientoCadenas/MTMC/" + nombreArchivo + ".txt");
			try {
				flujo_salida = new PrintStream(archivo);
			} catch (Exception e) {
				archivo = new File("src/ProcesamientoCadenas/MTMC/" + "procesamientoListaCadenasMTMC" + ".txt");
				try {
					flujo_salida = new PrintStream(archivo);
				} catch (FileNotFoundException e1) {
					System.out.println("Error en el procesamiento de cadenas");
					return;
				}
			}
			Object[] fila = new String[] {"CADENA", "ÚLTIMA CONF. INSTANTÁNEA", "RESULTADO"};
			if (imprimirPantalla)
				System.out.format("%15s%60s%15s\n", fila);
			flujo_salida.format("%15s%60s%15s\n", fila);
			for (Iterator<String> iterator = listaCadenas.iterator(); iterator.hasNext();) {
				String cadena = (String) iterator.next();
				String configFinal = procesarFuncion(cadena);
				boolean resultado = procesarCadena(cadena);
				fila = new String[] {cadena, configFinal, (resultado ? "yes" : "no")};
				if (imprimirPantalla)
					System.out.format("%15s%60s%15s\n", fila);
				flujo_salida.format("%15s%60s%15s\n", fila);

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
		public String getAlfabeto() {
			return alfabetoEntrada.toString();
		}
		//OBJETO CINTA - PARA FACILITAR LAS COSAS
		class cintaMTMC {
			List<String> cintas;
			int[] apuntador;
			
			cintaMTMC(int n_cintas){
				cintas = new ArrayList<String>();
				apuntador = new int[n_cintas];
				for(int k = 0; k < n_cintas; k++)
					cintas.add("");
			}
			void ponerCadena(String cadena) {
				cintas.set(0, cadena);
				for(int i=1; i < cintas.size(); i++)
					cintas.set(i, "!");
				for(int i=0; i < apuntador.length; i++)
					apuntador[i]=0;
				
			}
			String leer() {
				String lectura = "";
				for(int i = 0; i < apuntador.length; i++) {
					if(apuntador[i] < 0) {
						cintas.set(i, "!".repeat(apuntador[i]*-1) + cintas.get(i));
						apuntador[i] = 0;
					}
					if (apuntador[i] >= cintas.get(i).length()) {
						cintas.set(i, cintas.get(i) + "!".repeat(apuntador[i]-cintas.get(i).length()+1));
					}	
					lectura += cintas.get(i).charAt(apuntador[i]);
				}
				return lectura;
			}
			void escribir(String escritura){
				for(int i = 0; i < cintas.size(); i++)
					cintas.set(i, cintas.get(i).substring(0, apuntador[i]) + escritura.charAt(i) + cintas.get(i).substring(apuntador[i] + 1));
			}
			void setApuntador(int[] ap) {
				apuntador = ap;
			}
			int[] getApuntador() {
				return apuntador;
			}
			int n_pistas() {
				return apuntador.length;
			}
			void ejecutarMov(String movimientos) {
				if(movimientos.length() != apuntador.length) {
					System.out.println("Movimientos Inválidos");
					return;
				}
				for(int i=0; i < movimientos.length(); i++) {
					if(movimientos.charAt(i)=='<')
						apuntador[i]--;
					else if(movimientos.charAt(i)=='>')
						apuntador[i]++;
					else if(movimientos.charAt(i)=='-')
						apuntador[i] = apuntador[i];
					else
						System.out.println("Malos movimientos");
				}
			}
			public String toString() {
				String resultado = "";
				for(int i = 0; i < cintas.size(); i++) {
					resultado += cintas.get(i).substring(0, apuntador[i])+"*"+cintas.get(i).substring(apuntador[i]);
					resultado += (i<cintas.size()-1) ? ", " : "";
				}
				return resultado;
			}
		}		
		/*public static void main(String[] args) {
			MTMC test = new MTMC("AigualBigualC");
			System.out.println(test.procesarCadena("abccaca"));
			test.procesarCadenaConDetalles("abcaabcbc");
			List<String> cadenas = new ArrayList<String>();
			cadenas.add("!!!!"); cadenas.add("aaababaccc"); cadenas.add("cccaaabbb");
			test.procesarListaCadenas(cadenas, "AigualBigualC", true);
			System.out.println(test.toString());
		}*/
}
