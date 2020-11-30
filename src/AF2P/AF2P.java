package AF2P;

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

public class AF2P {

	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoCinta;
	private Set<Character> alfabetoPila;
	private FuncionTransicionAF2P delta;

	public AF2P(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
			Set<Character> alfabetoCinta, Set<Character> alfabetoPila, FuncionTransicionAF2P delta) {
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.alfabetoCinta = alfabetoCinta;
		this.alfabetoPila = alfabetoPila;
		this.delta = delta;
	}

	public AF2P(String nombre) {
		String estadoInicial = null;
		Scanner scanner = null;
		int[] headers = new int[6]; // Encabezados como #alphabet, guarda sus posiciones
		File archivo = new File("src/Pruebas/AF2P/" + nombre + ".msm");

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
		}

		Set<String> conjuntoEstados = new HashSet<String>();
		Set<String> estadosAceptacion = new HashSet<String>();
		Set<Character> alfabetoCinta = new HashSet<Character>();
		Set<Character> alfabetoPila = new HashSet<Character>();
		FuncionTransicionAF2P delta = null;
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
				delta = new FuncionTransicionAF2P(conjuntoEstados, alfabetoCinta, alfabetoPila, estadoANumero,
						simboloAlfabetoANumero, simboloPilaANumero, numeroASimboloAlfabeto, numeroAEstado,
						numeroASimboloPila);
				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String transicion = scanner.nextLine();
					String estadoActual = transicion.split(">")[0].split(":")[0];
					char simboloCinta = transicion.split(">")[0].split(":")[1].charAt(0);
					char simboloPrimeraPila = transicion.split(">")[0].split(":")[2].charAt(0);
					char simboloSegundaPila = transicion.split(">")[0].split(":")[3].charAt(0);
					String configuracionFinal = transicion.split(">")[1];
					delta.setTransicion(estadoActual, simboloCinta, simboloPrimeraPila, simboloSegundaPila,
							configuracionFinal);
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

		String configuracion = estadoInicial + "," + cadena + "," + "$" + "," + "$";
		ArbolConfiguracionesAF2P arbol = new ArbolConfiguracionesAF2P(configuracion, estadosAceptacion);
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

	private String siguienteConfiguracion(String estado, String cinta, String primeraPila, String segundaPila) {
		return estado + "," + cinta + "," + primeraPila + "," + segundaPila;
	}

	private void procesarConfiguracionInstantanea(String configuracionInstantanea, Nodo_CI_AF2P nodo) {
		String estadoActual = configuracionInstantanea.split(",")[0];
		String cadena = configuracionInstantanea.split(",")[1];
		String primeraPila = configuracionInstantanea.split(",")[2];
		String segundaPila = configuracionInstantanea.split(",")[3];
		char simboloPrimeraPila = primeraPila.charAt(0);
		char simboloSegundaPila = segundaPila.charAt(0);
		//En total son 8 casos, dependiendo de los simbolos de cada una de las pilas
		{//Lambda transicion
			if (simboloPrimeraPila != '$' && simboloSegundaPila != '$'
					&& delta.getTransicion(estadoActual, '$', simboloPrimeraPila, simboloSegundaPila) != null) { //Ambas pilas no estan vacias
				Vector<String> transiciones = delta.getTransicion(estadoActual, '$', simboloPrimeraPila,
						simboloSegundaPila);
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Remover" : "Reemplazo";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Remover" : "Reemplazo";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
			if (simboloSegundaPila != '$' && delta.getTransicion(estadoActual, '$', '$', simboloSegundaPila) != null) { //En la primera pila utilizamos Lambda, en la segunda no
				Vector<String> transiciones = delta.getTransicion(estadoActual, '$', '$', simboloSegundaPila);
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Nada" : "Insertar";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Remover" : "Reemplazo";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
			if (simboloPrimeraPila != '$' && delta.getTransicion(estadoActual, '$', simboloPrimeraPila, '$') != null) { //En la segunda pila utilizamos lambda, en la primera no
				Vector<String> transiciones = delta.getTransicion(estadoActual, '$', simboloPrimeraPila, '$');
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Remover" : "Reemplazo";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Nada" : "Insertar";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
			if (delta.getTransicion(estadoActual, '$', '$', '$') != null) { //En ambas pilas utilizamos a Lambda
				Vector<String> transiciones = delta.getTransicion(estadoActual, '$', '$', '$');
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Nada" : "Insertar";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Nada" : "Insertar";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
		}
		if (!cadena.equals("$")) { //Consume simbolo
			char simboloCinta = cadena.charAt(0);
			if (simboloPrimeraPila != '$' && simboloSegundaPila != '$' && delta.getTransicion(estadoActual,
					simboloCinta, simboloPrimeraPila, simboloSegundaPila) != null) { //Ambas pilas no estan vacias, se consume simbolo
				Vector<String> transiciones = delta.getTransicion(estadoActual, simboloCinta, simboloPrimeraPila,
						simboloSegundaPila);
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Remover" : "Reemplazo";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Remover" : "Reemplazo";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String cadena_replace = cadena;
					if (cadena_replace.length() == 1)
						cadena_replace = "$";
					else
						cadena_replace = cadena.substring(1);
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena_replace,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
			if (simboloSegundaPila != '$'
					&& delta.getTransicion(estadoActual, simboloCinta, '$', simboloSegundaPila) != null) { //En la primera pila utilizamos Lambda, en la segunda no
				Vector<String> transiciones = delta.getTransicion(estadoActual, simboloCinta, '$', simboloSegundaPila);
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Nada" : "Insertar";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Remover" : "Reemplazo";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String cadena_replace = cadena;
					if (cadena_replace.length() == 1)
						cadena_replace = "$";
					else
						cadena_replace = cadena.substring(1);
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena_replace,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
			if (simboloPrimeraPila != '$'
					&& delta.getTransicion(estadoActual, simboloCinta, simboloPrimeraPila, '$') != null) { //En la segunda pila utilizamos lambda, en la primera no
				Vector<String> transiciones = delta.getTransicion(estadoActual, simboloCinta, simboloPrimeraPila, '$');
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Remover" : "Reemplazo";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Nada" : "Insertar";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String cadena_replace = cadena;
					if (cadena_replace.length() == 1)
						cadena_replace = "$";
					else
						cadena_replace = cadena.substring(1);
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena_replace,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
					procesarConfiguracionInstantanea(configuracionSiguiente, nodoSiguiente);
				}
			}
			if (delta.getTransicion(estadoActual, simboloCinta, '$', '$') != null) { //En ambas pilas utilizamos a Lambda
				Vector<String> transiciones = delta.getTransicion(estadoActual, simboloCinta, '$', '$');
				for (String transicion : transiciones) {
					String siguienteEstado = transicion.split(":")[0];
					String operacionPrimeraPila = transicion.split(":")[1].equals("$") ? "Nada" : "Insertar";
					String operacionSegundaPila = transicion.split(":")[2].equals("$") ? "Nada" : "Insertar";
					String primeraPilaModificada = modificarPila(primeraPila, operacionPrimeraPila,
							transicion.split(":")[1].charAt(0));
					String segundaPilaModificada = modificarPila(segundaPila, operacionSegundaPila,
							transicion.split(":")[2].charAt(0));
					String cadena_replace = cadena;
					if (cadena_replace.length() == 1)
						cadena_replace = "$";
					else
						cadena_replace = cadena.substring(1);
					String configuracionSiguiente = siguienteConfiguracion(siguienteEstado, cadena_replace,
							primeraPilaModificada, segundaPilaModificada);
					nodo.insertarHijo(configuracionSiguiente);
					Nodo_CI_AF2P nodoSiguiente = nodo.getHijos().get(nodo.getHijos().size() - 1);
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
		File dir = new File("src/ProcesamientoCadenas/AF2P");
		dir.mkdirs();
		File archivoAceptadas = null;
		File archivoRechazadas = null;
		if (nombreArchivo != null && nombreArchivo.length() > 0) {
			archivoAceptadas = new File("src/ProcesamientoCadenas/AF2P/" + nombreArchivo + "AceptadasAF2P.txt");
			archivoRechazadas = new File("src/ProcesamientoCadenas/AF2P/" + nombreArchivo + "RechazadasAF2P.txt");
		}
		try {
			flujo_aceptacion = new PrintStream(archivoAceptadas);
			flujo_rechazo = new PrintStream(archivoRechazadas);
		} catch (Exception e) {
			archivoAceptadas = new File("src/ProcesamientoCadenas/AF2P/" + "default" + "AceptadasAF2P.txt");
			archivoRechazadas = new File("src/ProcesamientoCadenas/AF2P/" + "default" + "RechazadasAF2P.txt");
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
				String procesamientoAceptacion = "Procesamiento" + cadenasAceptadas + ": " + procesamiento;
				flujo_aceptacion.println(procesamientoAceptacion);
				procesamientosAceptados.add(procesamientoAceptacion);
			} else {
				cadenasRechazadas++;
				String procesamientoRechazo = "Procesamiento" + cadenasRechazadas + ": " + procesamiento;
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
		File dir = new File("src/ProcesamientoCadenas/AF2P");
		dir.mkdirs();
		if (nombreArchivo != null && nombreArchivo.length() > 0)
			archivo = new File("src/ProcesamientoCadenas/AF2P/" + nombreArchivo + ".txt");
		try {
			flujo_salida = new PrintStream(archivo);
		} catch (Exception e) {
			archivo = new File("src/ProcesamientoCadenas/AF2P/" + "procesamientoListaCadenas" + ".txt");
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

	public String getEstadoInicial() {
		return estadoInicial;
	}

	@Override
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
		resultado += "#tapeAlphabet \n";
		resultado += alfabetoCinta.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "")
				.replaceAll(",", "\n") + "\n";
		resultado += "#stackAlphabet \n";
		resultado += alfabetoPila.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "")
				.replaceAll(",", "\n") + "\n";
		resultado += "#transitions \n";
		resultado += delta.toString();
		return resultado;

	}

}