package MTN;

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

import MT.FuncionTransicionMT;


public class MTN {
	private Set<String> conjuntoEstados;
	private String estadoInicial;
	private Set<String> estadosAceptacion;
	private Set<Character> alfabetoEntrada;
	private Set<Character> alfabetoCinta;
	private FuncionTransicionMTN delta;
	private cintaMTN cinta;
	public MTN(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
			Set<Character> alfabetoEntrada, Set<Character> alfabetoCinta, FuncionTransicionMTN delta) {
		super();
		this.conjuntoEstados = conjuntoEstados;
		this.estadoInicial = estadoInicial;
		this.estadosAceptacion = estadosAceptacion;
		this.alfabetoEntrada = alfabetoEntrada;
		this.alfabetoCinta = alfabetoCinta;
		this.delta = delta;
		this.cinta = new cintaMTN();
	}

	//Constructor con Nombre de Archivo
	public MTN(String nombreArchivo) {
		Scanner scanner = null;
		int[] headers = new int[6]; // Encabezados como #alphabet, guarda sus posiciones
		File archivo = new File("src/Pruebas/MTN/" + nombreArchivo + ".ntm");
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
		FuncionTransicionMTN delta = null;
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

				delta = new FuncionTransicionMTN(conjuntoEstados, alfabetoCinta, estadoANumero, simboloAlfabetoANumero,
						numeroAEstado, numeroASimbolo);

				for (int j = 0; j < ultima_linea && scanner.hasNext(); j++) {
					String transicion = scanner.nextLine();
					String estadoActual = transicion.split(":")[0];
					char simbolo = transicion.split(":")[1].split("\\?")[0].charAt(0);
					String[] tr = transicion.split("\\?")[1].split(";");
					for(String s: tr) {
						String estadoFinal = s.split(":")[0];
						char simboloCinta = s.split(":")[1].charAt(0);
						char movimiento = s.split(":")[2].charAt(0);
						delta.setTransicion(estadoActual, simbolo, estadoFinal, simboloCinta, movimiento);
					}
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
		this.cinta =  new cintaMTN();
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
	
	//OBJETO CINTA - PARA FACILITAR LAS COSAS
	public class cintaMTN {
		String cinta;
		int apuntador;
				
	cintaMTN(){
		cinta = "!";
		apuntador = 0;
	}
	void ponerCadena(String cadena) {
		cinta = cadena;
		apuntador = 0;
	}
	String leer() {
		String lectura = "";
		if(apuntador < 0) {
			cinta = "!".repeat(apuntador*-1) + cinta;
			apuntador = 0;
		}
		if (apuntador >= cinta.length()) {
			cinta = cinta +  "!".repeat(apuntador - cinta.length()+ 1);
		}	
		lectura += cinta.charAt(apuntador);
		return lectura;
	}
	void escribir(char escritura){
		cinta = cinta.substring(0, apuntador) + escritura + cinta.substring(apuntador + 1);
	}
	void setApuntador(int ap) {
		apuntador = ap;
	}
	int getApuntador() {
		return apuntador;
	}
	void ejecutarMov(char movimiento) {
		if(movimiento == '<')
			apuntador--;
		else if(movimiento == '>')
			apuntador++;
		else if(movimiento == '-')
			apuntador = apuntador;
		else
			System.out.println("Malos movimientos");
	}
	public String toString(String estadoActual) {
		String resultado = "";
		resultado += cinta.substring(0, apuntador)+"("+estadoActual+")"+cinta.substring(apuntador);
		return resultado;
	}
	}
	
	/*public static void main(String[] args) {
		MTN test = new MTN("uno");
		System.out.println(test.toString());
	}*/
}
