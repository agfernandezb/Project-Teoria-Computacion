package AFPN;

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

import AFD.AFD;

public class AFPN {

	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoCinta;
	private Set<Character> alfabetoPila;
	private FuncionTransicionAFPN delta;

	public AFPN(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
			Set<Character> alfabetoCinta, Set<Character> alfabetoPila, FuncionTransicionAFPN delta) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.delta = delta;
	}

	public AFPN(String nombre) {
		String estadoInicial = null;
		Scanner scanner = null;
		int[] headers = new int[6]; // Encabezados como #alphabet, guarda sus posiciones
		File archivo = new File("src/Pruebas/AFPN/" + nombre + ".pda");

		try {
			scanner = new Scanner(archivo);
			int numberOfHeaders = 0;
			int line = 0; // iterador
			while (scanner.hasNextLine()) {
				if (scanner.nextLine()
						.matches("#tapeAlphabet|#stackAlphabet|#states|#initial|#accepting|#transitions")) {
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
		Set<String> estadosAceptacion = new HashSet<String>();
		Set<Character> alfabetoCinta = new HashSet<Character>();
		Set<Character> alfabetoPila = new HashSet<Character>();
		FuncionTransicionAFPN delta = null;
		HashMap<String, Integer> estadoANumero = null;
		HashMap<Character, Integer> simboloAlfabetoANumero = null;
		HashMap<Character, Integer> simboloPilaANumero = null;
		Vector<String> numeroAEstado = new Vector<String>();
		Vector<Character> numeroASimboloAlfabeto = new Vector<Character>();
		Vector<Character> numeroASimboloPila = new Vector<Character>();
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
			case "#stackAlphabet": {
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
				int numeroSimboloCinta = 1;
				simboloAlfabetoANumero.put('$', 0);
				numeroASimboloAlfabeto.add('$');
				for (Character simbolo : alfabetoCinta) {
					numeroASimboloAlfabeto.add(simbolo);
					simboloAlfabetoANumero.put(simbolo, numeroSimboloCinta);
					++numeroSimboloCinta;
				}

				simboloPilaANumero = new HashMap<>();
				int numeroSimboloPila = 1;
				simboloPilaANumero.put('$', 0);
				numeroASimboloPila.add('$');
				for (Character simbolo : alfabetoPila) {
					numeroASimboloPila.add(simbolo);
					simboloPilaANumero.put(simbolo, numeroSimboloPila);
					++numeroSimboloPila;
				}
				delta = new FuncionTransicionAFPN(conjuntoEstados, alfabetoCinta, alfabetoPila, estadoANumero,
						simboloAlfabetoANumero, simboloPilaANumero, numeroAEstado, numeroASimboloAlfabeto,
						numeroASimboloPila);
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

	private Vector<String> procesamientosCadena(String cadena) {

		String configuracion = estadoInicial + "," + cadena + "," + "$";
		ArbolConfiguraciones arbol = new ArbolConfiguraciones(configuracion, estadosAceptacion);
		procesarConfiguracionInstantanea(configuracion, arbol.getRaiz());
		return arbol.getProcesamientos();

	}

	private String modificarPila(String pila, String operacion, char parametro) {
		switch (operacion) {
		case "Reemplazo": {
			if (pila.length() == 1 && pila.charAt(0) != '$')
				pila = String.valueOf(parametro);
			else
				pila = String.valueOf(parametro) + pila.substring(1, pila.length());
			break;
		}
		case "Insertar": {
			if (pila.length() == 1 && pila.charAt(0) == '$')
				pila = String.valueOf(parametro);
			else
				pila = String.valueOf(parametro) + pila;
			break;
		}
		case "Remover": {
			if (pila.length() == 1)
				pila = "$";
			else
				pila = pila.substring(1, pila.length());
			break;
		}
		}
		return pila;

	}

	private String siguienteConfiguracion(String estado, String cinta, String pila) {
		return estado + "," + cinta + "," + pila;
	}

	private void procesarConfiguracionInstantanea(String configuracionInstantanea, Nodo_CI nodo) {
		String estadoActual = configuracionInstantanea.split(",")[0];
		String cadena = configuracionInstantanea.split(",")[1];
		String pila = configuracionInstantanea.split(",")[2];
		char simboloPila = pila.charAt(0);

		{//Lambda transicion
			if (simboloPila != '$' && delta.getTransicion(estadoActual, '$', simboloPila) != null) {
				Vector<String> transiciones = delta.getTransicion(estadoActual, '$', simboloPila);
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacion = transicion.split(":")[1].equals("$") ? "Remover" : "Reemplazo";
					String pilaModificada = modificarPila(pila, operacion, transicion.split(":")[1].charAt(0));
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena, pilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}

			}
			if (delta.getTransicion(estadoActual, '$', '$') != null) {
				Vector<String> transiciones = delta.getTransicion(estadoActual, '$', '$');
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacion = transicion.split(":")[1].equals("$") ? "Nada" : "Insertar";
					String pilaModificada = modificarPila(pila, operacion, transicion.split(":")[1].charAt(0));
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena, pilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
		}
		if (!cadena.equals("$")) { //Consume simbolo
			char simboloCinta = cadena.charAt(0);
			if (simboloPila != '$' && delta.getTransicion(estadoActual, simboloCinta, simboloPila) != null) {
				Vector<String> transiciones = delta.getTransicion(estadoActual, simboloCinta, simboloPila);
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacion = transicion.split(":")[1].equals("$") ? "Remover" : "Reemplazo";
					String pilaModificada = modificarPila(pila, operacion, transicion.split(":")[1].charAt(0));
					String cadena_replace = cadena;
					if (cadena_replace.length() == 1)
						cadena_replace = "$";
					else
						cadena_replace = cadena.substring(1);
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena_replace,
							pilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}

			}
			if (delta.getTransicion(estadoActual, simboloCinta, '$') != null) {
				Vector<String> transiciones = delta.getTransicion(estadoActual, simboloCinta, '$');
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacion = transicion.split(":")[1].equals("$") ? "Nada" : "Insertar";
					String pilaModificada = modificarPila(pila, operacion, transicion.split(":")[1].charAt(0));
					String cadena_replace = cadena;
					if (cadena_replace.length() == 1)
						cadena_replace = "$";
					else
						cadena_replace = cadena.substring(1);
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena_replace,
							pilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
		}
	}

	public boolean procesarCadena(String cadena) {
		Vector<String> procesamientos = procesamientosCadena(cadena);
		for (String procesamiento : procesamientos) {
			if (procesamiento.split(">>")[1].equals("accepted"))
				return true;
		}
		return false;
	}

	public boolean procesarCadenaConDetalles(String cadena) {
		Vector<String> procesamientos = procesamientosCadena(cadena);
		System.out.println("Cadena: " + cadena);
		for (String procesamiento : procesamientos) {
			if (procesamiento.split(">>")[1].equals("accepted")) {
				System.out.println("Procesamiento 1: " + procesamiento);
				return true;
			}
		}
		int numeroProcesamiento = 1;
		for (String procesamiento : procesamientos) {
			System.out.println("Procesamiento " + numeroProcesamiento + ": " + procesamiento);
			++numeroProcesamiento;
		}
		return false;
	}

	public int computarTodosLosProcesamientos(String cadena, String nombreArchivo) {
		PrintStream flujo_aceptacion;
		PrintStream flujo_rechazo;
		File dir = new File("src/ProcesamientoCadenas/AFPN");
		dir.mkdirs();
		File archivoAceptadas = null;
		File archivoRechazadas = null;
		if (nombreArchivo != null && nombreArchivo.length() > 0) {
			archivoAceptadas = new File("src/ProcesamientoCadenas/AFPN/" + nombreArchivo + "AceptadasAFPN.txt");
			archivoRechazadas = new File("src/ProcesamientoCadenas/AFPN/" + nombreArchivo + "RechazadasAFPN.txt");
		}
		try {
			flujo_aceptacion = new PrintStream(archivoAceptadas);
			flujo_rechazo = new PrintStream(archivoRechazadas);
		} catch (Exception e) {
			archivoAceptadas = new File("src/ProcesamientoCadenas/AFPN/" + "default" + "AceptadasAFPN.txt");
			archivoRechazadas = new File("src/ProcesamientoCadenas/AFPN/" + "default" + "RechazadasAFPN.txt");
			try {
				flujo_aceptacion = new PrintStream(archivoAceptadas);
				flujo_rechazo = new PrintStream(archivoRechazadas);
			} catch (FileNotFoundException e1) {
				System.out.println("Error en el computo de procesamientos");
				return -1;
			}
		}
		Vector<String> procesamientos = procesamientosCadena(cadena);
		Vector<String> procesamientosAceptados = new Vector<>();
		Vector<String> procesamientosRechazados = new Vector<>();
		int cadenasAceptadas = 0;
		int cadenasRechazadas = 0;
		flujo_aceptacion.println("Cadena: " + cadena);
		flujo_rechazo.println("Cadena: " + cadena);
		for (String procesamiento : procesamientos) {
			if (procesamiento.split(">>")[1].equals("accepted")) {
				cadenasAceptadas++;
				String procesamientoAceptacion = "Procesamiento " + cadenasAceptadas + ": " + procesamiento;
				flujo_aceptacion.println(procesamientoAceptacion);
				procesamientosAceptados.add(procesamientoAceptacion);
			} else {
				cadenasRechazadas++;
				String procesamientoRechazo = "Procesamiento " + cadenasRechazadas + ": " + procesamiento;
				flujo_rechazo.println(procesamientoRechazo);
				procesamientosRechazados.add(procesamientoRechazo);
			}
		}
		System.out.println("Cadena: " + cadena);
		System.out.println("Procesamientos de aceptación: ");
		if (procesamientosAceptados.isEmpty()) {
			System.out.println("No existen procesamientos de aceptación.");
		} else {
			for (String procesamientoAceptacion : procesamientosAceptados) {
				System.out.println(procesamientoAceptacion);
			}
		}
		System.out.println("Procesamientos de rechazo: ");
		if (procesamientosRechazados.isEmpty()) {
			System.out.println("No existen procesamientos rechazados.");
		} else {
			for (String procesamientoRechazo : procesamientosRechazados) {
				System.out.println(procesamientoRechazo);
			}
		}
		flujo_aceptacion.flush();
		flujo_rechazo.flush();
		flujo_aceptacion.close();
		flujo_rechazo.close();
		return procesamientos.size();
	}

	public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {

		PrintStream flujo_salida;
		File archivo = null;
		File dir = new File("src/ProcesamientoCadenas/AFPN");
		dir.mkdirs();
		if (nombreArchivo != null && nombreArchivo.length() > 0)
			archivo = new File("src/ProcesamientoCadenas/AFPN/" + nombreArchivo + ".txt");
		try {
			flujo_salida = new PrintStream(archivo);
		} catch (Exception e) {
			archivo = new File("src/ProcesamientoCadenas/AFPN/" + "procesamientoListaCadenas" + ".txt");
			try {
				flujo_salida = new PrintStream(archivo);
			} catch (FileNotFoundException e1) {
				System.out.println("Error en el procesamiento de cadenas");
				return;
			}
		}

		for (Iterator<String> iterator = listaCadenas.iterator(); iterator.hasNext();) {
			String cadena = (String) iterator.next();
			Vector<String> procesamientos = procesamientosCadena(cadena);
			boolean resultado = false;
			int indexAImprimir = 0;
			int procesamientosAceptados = 0;
			for (int i = 0; i < procesamientos.size(); ++i) {
				if (procesamientos.get(i).split(">>")[1].equals("accepted")) {
					resultado = true;
					indexAImprimir = i;
					++procesamientosAceptados;
				}
			}
			String procesamientoConDetalles = cadena + "\t" + procesamientos.get(indexAImprimir) + "\t"
					+ procesamientos.size() + "\t" + procesamientosAceptados + "\t"
					+ (procesamientos.size() - procesamientosAceptados) + "\t" + (resultado ? "yes" : "no");
			if (imprimirPantalla)
				System.out.println(procesamientoConDetalles);
			flujo_salida.println(procesamientoConDetalles);
		}
		flujo_salida.flush();
		flujo_salida.close();
	}

	private boolean igualdadAlfabetos(Set<Character> primerAlfabeto, Set<Character> segundoAlfabeto) {
		boolean iguales = true;
		for (Character caracter : primerAlfabeto) {
			iguales = iguales && segundoAlfabeto.contains(caracter);
		}
		for (Character caracter : segundoAlfabeto) {
			iguales = iguales && primerAlfabeto.contains(caracter);
		}
		return iguales;
	}

	private Set<String> conjuntoEstadosProductoCartesiano(Set<String> estados_AFPN, Set<String> estados_AFD) {
		Set<String> conjuntoEstados = new HashSet<String>();
		for (String estado_AFPN : estados_AFPN) {
			for (String estado_AFD : estados_AFD) {
				conjuntoEstados.add(generarEstadoProductoCartesiano(estado_AFPN, estado_AFD));
			}
		}
		return conjuntoEstados;
	}

	private String generarEstadoProductoCartesiano(String estado_AFPN, String estado_AFD) {
		return "(" + estado_AFPN + "+" + estado_AFD + ")";
	}

	public AFPN hallarProductoCartesianoConAFD(AFD afd) {

		//Atributos AFPN
		Set<String> conjuntoEstados;
		String estadoInicial;
		Set<String> estadosAceptacion;
		Set<Character> alfabetoCinta;
		Set<Character> alfabetoPila;
		Vector<String> numeroAEstado = new Vector<String>();
		Vector<Character> numeroASimboloAlfabeto = this.delta.getNumeroASimboloAlfabeto();
		Vector<Character> numeroASimboloPila = this.delta.getNumeroASimboloPila();
		FuncionTransicionAFPN delta;

		//Primero verificamos que ambos alfabetos sean iguales
		if (!igualdadAlfabetos(this.alfabetoCinta, afd.getAlfabeto()))
			return null;

		//Como son iguales, podemos proceder

		//Creacion conjunto estados
		conjuntoEstados = conjuntoEstadosProductoCartesiano(this.conjuntoEstados, afd.getConjuntoEstados());

		//Estado Inicial
		estadoInicial = generarEstadoProductoCartesiano(this.estadoInicial, afd.getEstadoInicial());

		//Estados Aceptacion
		estadosAceptacion = conjuntoEstadosProductoCartesiano(this.estadosAceptacion, afd.getEstadosAceptacion());

		//Alfabeto Cinta
		alfabetoCinta = this.alfabetoCinta;

		//Alfabeto Pila
		alfabetoPila = this.alfabetoPila;

		//Funcion Transicion
		int index = 0;
		HashMap<String, Integer> estadoANumero = new HashMap<>();
		for (String estado : conjuntoEstados) {
			numeroAEstado.add(estado);
			estadoANumero.put(estado, index);
			++index;
		}
		delta = new FuncionTransicionAFPN(conjuntoEstados, alfabetoCinta, alfabetoPila, estadoANumero,
				this.delta.getSimboloAlfabetoANumero(), this.delta.getSimboloPilaANumero(), numeroAEstado,
				numeroASimboloAlfabeto, numeroASimboloPila);

		for (String estado_AFPN : this.conjuntoEstados) {
			//Insertar Transiciones Lambda con simbolos de pila diferentes a Lambda
			for (char simboloPila : this.alfabetoPila) {
				char simboloCinta = '$';
				if (this.delta.getTransicion(estado_AFPN, simboloCinta, simboloPila) != null) {
					Vector<String> transiciones = this.delta.getTransicion(estado_AFPN, simboloCinta, simboloPila);
					//Iteramos sobre los estados del AFD
					for (String estado_AFD : afd.getConjuntoEstados()) {
						String estadoActual = generarEstadoProductoCartesiano(estado_AFPN, estado_AFD);
						String transicion = "";
						for (int i = 0; i < transiciones.size(); ++i) {
							String estadoTransicion_AFPN = transiciones.get(i).split(":")[0]; //Estado al cual se mueve el AFPN
							String estadoTransicion_AFD = estado_AFD; //AFD permanece quieto
							String operacionPila = transiciones.get(i).split(":")[1]; //Simbolo pila final
							String estadoFinal = generarEstadoProductoCartesiano(estadoTransicion_AFPN,
									estadoTransicion_AFD);
							transicion += estadoFinal + ":" + operacionPila;
							if (i < transiciones.size() - 1)
								transicion += ";";
						}
						delta.setTransicion(estadoActual, simboloCinta, simboloPila, transicion);
					}
				}
			}
			//Simbolo cinta y pila son Lambda
			if (this.delta.getTransicion(estado_AFPN, '$', '$') != null) {
				Vector<String> transiciones = this.delta.getTransicion(estado_AFPN, '$', '$');
				//Iteramos sobre los estados del AFD
				for (String estado_AFD : afd.getConjuntoEstados()) {
					String estadoActual = generarEstadoProductoCartesiano(estado_AFPN, estado_AFD);
					String transicion = "";
					for (int i = 0; i < transiciones.size(); ++i) {
						String estadoTransicion_AFPN = transiciones.get(i).split(":")[0]; //Estado al cual se mueve el AFPN
						String estadoTransicion_AFD = estado_AFD; //AFD permanece quieto
						String operacionPila = transiciones.get(i).split(":")[1]; //Simbolo pila final
						String estadoFinal = generarEstadoProductoCartesiano(estadoTransicion_AFPN,
								estadoTransicion_AFD);
						transicion += estadoFinal + ":" + operacionPila;
						if (i < transiciones.size() - 1)
							transicion += ";";
					}
					delta.setTransicion(estadoActual, '$', '$', transicion);
				}
			}
			//Iteramos sobre los simbolos de cinta
			for (char simboloCinta : this.alfabetoCinta) {
				//Insertar operaciones de la pila con lambda
				char lambda = '$';
				if (this.delta.getTransicion(estado_AFPN, simboloCinta, lambda) != null) {
					Vector<String> transiciones = this.delta.getTransicion(estado_AFPN, simboloCinta, lambda);
					//Iteramos sobre los estados del AFD
					for (String estado_AFD : afd.getConjuntoEstados()) {
						String estadoActual = generarEstadoProductoCartesiano(estado_AFPN, estado_AFD);
						String transicion = "";
						for (int i = 0; i < transiciones.size(); ++i) {
							String estadoTransicion = transiciones.get(i).split(":")[0]; //Estado al cual se mueve el AFPN
							String estadoTransicion_AFD = afd.getTransicion(estado_AFD, simboloCinta); //Estado al cual se mueve el AFD
							String operacionPila = transiciones.get(i).split(":")[1]; //Simbolo pila final
							String estadoFinal = generarEstadoProductoCartesiano(estadoTransicion,
									estadoTransicion_AFD);
							transicion += estadoFinal + ":" + operacionPila;
							if (i < transiciones.size() - 1)
								transicion += ";";
						}
						delta.setTransicion(estadoActual, simboloCinta, lambda, transicion);
					}
				}
				//Operaciones de pila sin lambda
				for (char simboloPila : this.alfabetoPila) {
					if (this.delta.getTransicion(estado_AFPN, simboloCinta, simboloPila) != null) {
						Vector<String> transiciones = this.delta.getTransicion(estado_AFPN, simboloCinta, simboloPila);
						//Iteramos sobre los estados del AFD
						for (String estado_AFD : afd.getConjuntoEstados()) {
							String estadoActual = generarEstadoProductoCartesiano(estado_AFPN, estado_AFD);
							String transicion = "";
							for (int i = 0; i < transiciones.size(); ++i) {
								String estadoTransicion_AFPN = transiciones.get(i).split(":")[0]; //Estado al cual se mueve el AFPN
								String estadoTransicion_AFD = afd.getTransicion(estado_AFD, simboloCinta); //Estado al cual se mueve el AFD
								String operacionPila = transiciones.get(i).split(":")[1]; //Simbolo pila final
								String estadoFinal = generarEstadoProductoCartesiano(estadoTransicion_AFPN,
										estadoTransicion_AFD);
								transicion += estadoFinal + ":" + operacionPila;
								if (i < transiciones.size() - 1)
									transicion += ";";
							}
							delta.setTransicion(estadoActual, simboloCinta, simboloPila, transicion);
						}
					}
				}
			}
		}
		AFPN result = new AFPN(conjuntoEstados, estadoInicial, estadosAceptacion, alfabetoCinta, alfabetoPila, delta);
		return result;
	}

	public String getEstadoInicial() {
		return estadoInicial;
	}

	@Override
	public String toString() {
		String states = "#states";
		String estados = "";
		for (String estado : conjuntoEstados) {
			estados += estado + '\n';
		}
		String initial = "#initial";
		String accepting = "#accepting";
		String estadoAceptacion = "";
		for (String estado : estadosAceptacion) {
			estadoAceptacion += estado + '\n';
		}
		String tapeAlphabet = "#tapeAlphabet";
		String alfabCinta = "";
		for (char simbolo : this.alfabetoCinta) {
			alfabCinta += String.valueOf(simbolo) + '\n';
		}
		String stackAlphabet = "#stackAlphabet";
		String alfabPila = "";
		for (char simbolo : alfabetoPila) {
			alfabPila += String.valueOf(simbolo) + '\n';
		}
		String transitions = "#transitions";
		return states + '\n' + estados + initial + '\n' + estadoInicial + '\n' + accepting + '\n' + estadoAceptacion
				+ tapeAlphabet + '\n' + alfabCinta + stackAlphabet + '\n' + alfabPila + transitions + '\n'
				+ delta.toString();

	}
}
