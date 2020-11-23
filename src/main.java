import java.util.Scanner;

import AFD.AFD;

public class main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean menu = true;
		while (menu) {
			System.out.println("Seleccione el módulo que desea observar:");
			System.out.println("1: AFD");
			System.out.println("2: AFPD");
			System.out.println("3: AFPN");
			System.out.println("4: MT");
			System.out.println("Presione cualquier otra tecla para salir del programa");
			String opcionSeleccionada = scanner.next();
			switch (opcionSeleccionada) {
			case "1": {
				boolean menu1 = true;
				while (menu1) {
					System.out.println("Clase AFD");
					System.out.println("Seleccione el autómata finito determinista que desea utilizar:");
					System.out.println(
							"1.Autómata finito determinista que acepta las cadenas con un número par de aes y de bes");
					System.out.println("2.Autómata finito determinista que acepta el lenguaje (a^2i)*(b^3j)*");
					System.out.println("3.Autómata finito determinista que acepta el lenguaje a+b* U ac* U b*ca*");
					System.out.println("4.Ingresar uno propio");
					System.out.println("Presione cualquier otra tecla para volver al menú inicial");
					String opcion1 = scanner.next();
					AFD afd = null;
					switch (opcion1) {
					case "1": {
						afd = new AFD("parAparB");
						break;
					}
					case "2": {
						afd = new AFD("a2ib3j");
						break;
					}
					case "3": {
						afd = new AFD("afd3");
						break;
					}
					case "4": {
						System.out.println(
								"Ingrese el nombre del archivo con la extensión .dfa en la carpeta Pruebas\\AFD");
						String nombre = scanner.next();
						afd = new AFD(nombre);
						while (afd.getEstadoInicial() == null) {
							System.out.println(
									"Hubo un error, ingrese el nombre del archivo con la extensión .dfa en la carpeta Pruebas\\AFD ");
							nombre = scanner.next();
							afd = new AFD(nombre);
						}
						break;
					}
					default:
						menu1 = false;
						break;
					}
					boolean menu2 = menu1;
					while (menu2) {
						System.out.println("Seleccione la operación que desea realizar con el AFD:");
						System.out.println("1.Saber si una cadena es aceptada o no");
						System.out.println("2.Procesar una cadena con detalles*");
						System.out.println(
								"3.Procesar una lista de cadenas, viendo el procesamiento de cada una de ellas");
						System.out.println("4.Imprimir el automata en el formato de entrada");
						System.out.println("5.Cambiar el autómata");
						System.out.println("Presione cualquier otra tecla para volver al menú inicial");
						String opcion2 = scanner.next();
						switch (opcion2) {
						case "1": {

							break;
						}
						case "2": {

							break;
						}
						case "3": {

							break;
						}
						case "4": {
							System.out.println(afd.toString());
							break;
						}
						case "5": {
							menu2 = false;
							break;
						}
						default:
							menu1 = false;
							menu2 = false;
							break;
						}
					}

				}
				break;
			}
			/*case 2: {
			
			}
			case 3: {
			
			}
			case 4: {
			
			}*/
			default:
				menu = false;
			}
		}
		scanner.close();
		/*
				System.out.println("Clase AFD");
				System.out.println("1.Automata finito determinista que acepta las cadenas con un número par de aes y de bes");
				AFD parAparB = 
				parAparB.procesarCadenaConDetalles();
				parAparB.procesarCadenaConDetalles();
				
				BANCO CADENAS AFD 1
				"aabbabba"
				"abababa"
				"aaabbb"
				"abababab"
				"bbbbbbbb"
				"aaaabaaaa"
		
				System.out.println("2..Automata finito determinista que acepta el lenguaje (a^2i)*(b^3j)*");
				AFD a2ib3j = new AFD("a2ib3j");
				a2ib3j.procesarCadenaConDetalles("bbb");
				a2ib3j.procesarCadenaConDetalles("aaaabbbbbb");
				a2ib3j.procesarCadenaConDetalles("aabbba");
				System.out.println("3..Automata finito determinista que acepta el lenguaje a+b* U ac* U b*ca*");
				AFD afd3 =
				List<String> listaCadenas = new ArrayList<>();
				listaCadenas.add("aaaaaab");
				listaCadenas.add("cb");
				listaCadenas.add("aaaabc");
				listaCadenas.add("acccccc");
				listaCadenas.add("c");
				listaCadenas.add("cccc");
				listaCadenas.add("bbbbc");
				listaCadenas.add("bcaa");
				listaCadenas.add("bc");
				afd3.procesarListaCadenas(listaCadenas, "procesamientoListaAFD3", true);
		*/
		//
		//WORK IN PROGRESS
		//
		//AF2P af2p = new AF2P("uno");
		//System.out.println(af2p.toString());
		//MT -> mismo numero de a que b
		//MT mt = new MT("uno");
		/*mt.procesarCadenaConDetalles("ababababaa");
		System.out.println(mt.procesarFuncion("ababababaa"));
		List<String> prueba = Arrays.asList("aabb", "abaaba", "abababaaa", "aabbaab");
		mt.procesarListaCadenas(prueba, "igualAqueB", true);*/
		//System.out.println(mt.toString());

	}
}
