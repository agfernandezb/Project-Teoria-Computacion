import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import AFD.AFD;
import AFPD.AFPD;

public class main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean menu = true;
		while (menu) {
			System.out.println("Seleccione el módulo que desea observar:");
			System.out.println("1: AFD");
			System.out.println("2: AFPD");
			System.out.println("3: AFPN");
			System.out.println("4: AF2P");
			System.out.println("5: MT");
			System.out.println("Presione cualquier otra tecla para salir del programa");
			String opcionSeleccionada = scanner.next();
			switch (opcionSeleccionada) {
			case "1": {
				boolean menu1 = true;
				while (menu1) {
					System.out.println("Clase AFD");
					System.out.println("Seleccione el autómata finito determinista que desea utilizar:");
					System.out.println(
							"1. Autómata finito determinista que acepta las cadenas con un número par de aes y de bes");
					System.out.println("2. Autómata finito determinista que acepta el lenguaje (a^2i)*(b^3j)*");
					System.out.println("3. Autómata finito determinista que acepta el lenguaje a+b* U ac* U b*ca*");
					System.out.println("4. Ingresar uno propio");
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
						System.out.println("1. Saber si una cadena es aceptada o no");
						System.out.println("2. Procesar una cadena con detalles*");
						System.out.println(
								"3. Procesar una lista de cadenas, viendo el procesamiento de cada una de ellas");
						System.out.println("4. Imprimir el automata en el formato de entrada");
						System.out.println("5. Cambiar el autómata");
						System.out.println("Presione cualquier otra tecla para volver al menú inicial");
						String opcion2 = scanner.next();
						ArrayList<String> bancoString = null;
						boolean hayBancoString = !opcion1.equals("4");
						if (hayBancoString) {
							if (opcion1.equals("3")) {
								bancoString = new ArrayList<>();
								bancoString.add("aaaaaab");
								bancoString.add("cb");
								bancoString.add("aaaabc");
								bancoString.add("acccccc");
								bancoString.add("c");
								bancoString.add("cccc");
								bancoString.add("bbbbc");
								bancoString.add("bcaa");
								bancoString.add("bc");
								bancoString.add("aaaaaaabccccc");
							} else {
								bancoString = new ArrayList<>();
								bancoString.add("aabbabba");
								bancoString.add("abababa");
								bancoString.add("aaabbb");
								bancoString.add("abababab");
								bancoString.add("bbbbbbbb");
								bancoString.add("aaaabaaaa");
								bancoString.add("bbb");
								bancoString.add("aaaabbbbbb");
								bancoString.add("aabbba");
								bancoString.add("bababaaabb");
							}
						}
						switch (opcion2) {
						case "1": {
							boolean usarBanco = true;
							if (hayBancoString) {
								System.out.println(
										"¿Desea escribir la cadena a utilizar? ('si' para una respuesta afirmativa)");
								String res = scanner.next();
								if (res.equalsIgnoreCase("si")) {
									usarBanco = false;
									String cadena = scanner.next();
									System.out.println("Resultado: " + afd.procesarCadena(cadena));
								}
							}
							if (usarBanco) {
								Random random = new Random();
								int indiceAleatorio = random.nextInt(10);
								System.out.println("La cadena seleccionada fue: " + bancoString.get(indiceAleatorio));
								System.out
										.println("Resultado: " + afd.procesarCadena(bancoString.get(indiceAleatorio)));
							}
							continue;
						}
						case "2": {
							boolean usarBanco = true;
							if (hayBancoString) {
								System.out.println(
										"¿Desea escribir la cadena a utilizar? ('Si' para una respuesta afirmativa)");
								String res = scanner.next();
								if (res.equalsIgnoreCase("si")) {
									usarBanco = false;
									String cadena = scanner.next();
									System.out.println("Resultado: " + afd.procesarCadenaConDetalles(cadena));
								}
							}
							if (usarBanco) {
								Random random = new Random();
								int indiceAleatorio = random.nextInt(10);
								System.out.println("La cadena seleccionada fue: " + bancoString.get(indiceAleatorio));
								System.out.println("Resultado: "
										+ afd.procesarCadenaConDetalles(bancoString.get(indiceAleatorio)));
							}
							continue;
						}
						case "3": {
							System.out.println("Escriba un nombre para el archivo donde se guardará el procesamiento.");
							String nombreArchivo = scanner.next();
							System.out.println("");
							Integer num = null;
							while (true) {
								System.out.println("Escriba el numero de cadenas a procesar: ");
								try {
									num = scanner.nextInt();
									if (num > 0)
										break;
								} catch (Exception e) {
								}
							}
							ArrayList<String> listaCadenas = new ArrayList<String>();
							for (int i = 0; i < num; ++i) {
								listaCadenas.add(scanner.next());
							}
							System.out.println("Desea ver los resultados en pantalla? ");
							String res = scanner.next();
							boolean imprimirPantalla = res.equalsIgnoreCase("si");
							afd.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
							continue;
						}
						case "4": {
							System.out.println(afd.toString());
							continue;
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
			case "2": {
				boolean menu1 = true;
				while (menu1) {
					System.out.println("Clase AFPD");
					System.out.println("Seleccione el autómata finito con pila determinista que desea utilizar:");
					System.out.println("1. Autómata finito que acepta el lenguaje a^2n b^n, n >= 0");
					System.out.println("2. Autómata finito que acepta el lenguaje a^m b^n con n > m >=1");
					System.out.println("3. Autómata finito que acepta el lenguaje a^n b^m a^(n+1) con m >= 1, n >= 0");
					System.out.println("4. Ingresar uno propio");
					System.out.println("Presione cualquier otra tecla para volver al menú inicial");
					String opcion1 = scanner.next();
					AFPD afpd = null;
					switch (opcion1) {
					case "1": {
						afpd = new AFPD("a2nbn");
						break;
					}
					case "2": {
						afpd = new AFPD("ambn");
						break;
					}
					case "3": {
						afpd = new AFPD("anbman1");
						break;
					}
					case "4": {
						System.out.println(
								"Ingrese el nombre del archivo con la extensión .dpda en la carpeta Pruebas\\AFPD");
						String nombre = scanner.next();
						afpd = new AFPD(nombre);
						while (afpd.getEstadoInicial() == null) {
							System.out.println(
									"Hubo un error, ingrese el nombre del archivo con la extensión .dpda en la carpeta Pruebas\\AFPD ");
							nombre = scanner.next();
							afpd = new AFPD(nombre);
						}
						break;
					}
					default:
						menu1 = false;
						break;
					}
					boolean menu2 = menu1;
					while (menu2) {
						System.out.println("Seleccione la operación que desea realizar con el AFPD:");
						System.out.println("1. Saber si una cadena es aceptada o no");
						System.out.println("2. Procesar una cadena con detalles*");
						System.out.println(
								"3. Procesar una lista de cadenas, viendo el procesamiento de cada una de ellas");
						System.out.println("4. Imprimir el automata en el formato de entrada");
						System.out.println("5. Cambiar el autómata");
						System.out.println("Presione cualquier otra tecla para volver al menú inicial");
						String opcion2 = scanner.next();
						ArrayList<String> bancoString = null;
						boolean hayBancoString = !opcion1.equals("4");
						if (hayBancoString) {
							bancoString = new ArrayList<>();
							bancoString.add("aaaaaabbb");
							bancoString.add("aaaabb");
							bancoString.add("aaaaaaaaaaaaaaaabbbb");
							bancoString.add("aaaaabbbbbbbbbbb");
							bancoString.add("bbbbbbb");
							bancoString.add("aaa");
							bancoString.add("aaaaabbbbbbbbbb");
							bancoString.add("aaaaaabbbbbb");
							bancoString.add("aabbaaa");
							bancoString.add("bbbbbbb");
							bancoString.add("aaabbbaaa");
							bancoString.add("aaaaabbbbbbbaaaaaa");
							bancoString.add("a");
						}
						switch (opcion2) {
						case "1": {
							boolean usarBanco = true;
							if (hayBancoString) {
								System.out.println(
										"¿Desea escribir la cadena a utilizar? ('si' para una respuesta afirmativa)");
								String res = scanner.next();
								if (res.equalsIgnoreCase("si")) {
									usarBanco = false;
									String cadena = scanner.next();
									System.out.println("Resultado: " + afpd.procesarCadena(cadena));
								}
							}
							if (usarBanco) {
								Random random = new Random();
								int indiceAleatorio = random.nextInt(10);
								System.out.println("La cadena seleccionada fue: " + bancoString.get(indiceAleatorio));
								System.out
										.println("Resultado: " + afpd.procesarCadena(bancoString.get(indiceAleatorio)));
							}
							continue;
						}
						case "2": {
							boolean usarBanco = true;
							if (hayBancoString) {
								System.out.println(
										"¿Desea escribir la cadena a utilizar? ('Si' para una respuesta afirmativa)");
								String res = scanner.next();
								if (res.equalsIgnoreCase("si")) {
									usarBanco = false;
									String cadena = scanner.next();
									System.out.println("Resultado: " + afpd.procesarCadenaConDetalles(cadena));
								}
							}
							if (usarBanco) {
								Random random = new Random();
								int indiceAleatorio = random.nextInt(10);
								System.out.println("La cadena seleccionada fue: " + bancoString.get(indiceAleatorio));
								System.out.println("Resultado: "
										+ afpd.procesarCadenaConDetalles(bancoString.get(indiceAleatorio)));
							}
							continue;
						}
						case "3": {
							System.out.println("Escriba un nombre para el archivo donde se guardará el procesamiento.");
							String nombreArchivo = scanner.next();
							System.out.println("");
							Integer num = null;
							while (true) {
								System.out.println("Escriba el numero de cadenas a procesar: ");
								try {
									num = scanner.nextInt();
									if (num > 0)
										break;
								} catch (Exception e) {
								}
							}
							ArrayList<String> listaCadenas = new ArrayList<String>();
							for (int i = 0; i < num; ++i) {
								listaCadenas.add(scanner.next());
							}
							System.out.println("Desea ver los resultados en pantalla? ");
							String res = scanner.next();
							boolean imprimirPantalla = res.equalsIgnoreCase("si");
							afpd.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
							continue;
						}
						case "4": {
							System.out.println(afpd.toString());
							continue;
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
			/*case "3": {
				boolean menu1 = true;
				while (menu1) {
					System.out.println("Clase AFPN");
					System.out.println("Seleccione el autómata finito con pila no determinista que desea utilizar:");
					System.out.println("1. Autómata finito que acepta los palindromes con alfabeto {a,b}");
					System.out.println("2. Autómata finito que acepta el lenguaje a^mb^n con m > n >= 0");
					System.out.println("3. Autómata finito en el que el número de aes es dos veces el número de bes");
					System.out.println(
							"4. Autómata finito que acepta el lenguaje a^k b^m c^n con k,m >= 1, n >= 0 y n < k + m");
					System.out.println("5. Ingresar uno propio");
					System.out.println("Presione cualquier otra tecla para volver al menú inicial");
					String opcion1 = scanner.next();
					AFPN afpn = null;
					switch (opcion1) {
					case "1": {
						afpn = new AFPN("palindrome");
						break;
					}
					case "2": {
						afpn = new AFPN("ambn");
						break;
					}
					case "3": {
						afpn = new AFPN("a=2b");
						break;
					}
					case "4": {
						afpn = new AFPN("akbmcn");
						break;
					}
					case "5": {
						System.out.println(
								"Ingrese el nombre del archivo con la extensión .dpda en la carpeta Pruebas\\AFPN");
						String nombre = scanner.next();
						afpn = new AFPN(nombre);
						while (afpn.getEstadoInicial() == null) {
							System.out.println(
									"Hubo un error, ingrese el nombre del archivo con la extensión .pda en la carpeta Pruebas\\AFPN ");
							nombre = scanner.next();
							afpn = new AFPN(nombre);
						}
						break;
					}
					default:
						menu1 = false;
						break;
					}
					boolean menu2 = menu1;
					while (menu2) {
						System.out.println("Seleccione la operación que desea realizar con el AFPN:");
						System.out.println("1. Saber si una cadena es aceptada o no");
						System.out.println("2. Procesar una cadena con detalles*");
						System.out.println("3. Computar todos los procesamientos para una cadena");
						System.out.println(
								"4. Procesar una lista de cadenas, viendo el procesamiento de cada una de ellas");
						System.out.println("5. Hallar producto cartesiano con un AFD");
						System.out.println("6. Imprimir el autómata en el formato de entrada");
						System.out.println("7. Cambiar el autómata");
						System.out.println("Presione cualquier otra tecla para volver al menú inicial");
						String opcion2 = scanner.next();
						ArrayList<String> bancoString = null;
						boolean hayBancoString = !opcion1.equals("5");
						if (opcion1.equals("4")) {
							/*bancoString = new ArrayList<>();
							bancoString.add("aaaaaab");
							bancoString.add("cb");
							bancoString.add("aaaabc");
							bancoString.add("acccccc");
							bancoString.add("c");
							bancoString.add("cccc");
							bancoString.add("bbbbc");
							bancoString.add("bcaa");
							bancoString.add("bc");
							bancoString.add("aaaaaaabccccc");
						} else {
							/*bancoString = new ArrayList<>();
							bancoString.add("aabbabba");
							bancoString.add("abababa");
							bancoString.add("aaabbb");
							bancoString.add("abababab");
							bancoString.add("bbbbbbbb");
							bancoString.add("aaaabaaaa");
							bancoString.add("bbb");
							bancoString.add("aaaabbbbbb");
							bancoString.add("aabbba");
							bancoString.add("bababaaabb");
						}
						switch (opcion2) {
						case "1": {
							boolean usarBanco = true;
							if (hayBancoString) {
								System.out.println(
										"¿Desea escribir la cadena a utilizar? ('si' para una respuesta afirmativa)");
								String res = scanner.next();
								if (res.equalsIgnoreCase("si")) {
									usarBanco = false;
									String cadena = scanner.next();
									System.out.println("Resultado: " + afpd.procesarCadena(cadena));
								}
							}
							if (usarBanco) {
								Random random = new Random();
								int indiceAleatorio = random.nextInt(10);
								System.out.println("La cadena seleccionada fue: " + bancoString.get(indiceAleatorio));
								System.out
										.println("Resultado: " + afpd.procesarCadena(bancoString.get(indiceAleatorio)));
							}
							continue;
						}
						case "2": {
							boolean usarBanco = true;
							if (hayBancoString) {
								System.out.println(
										"¿Desea escribir la cadena a utilizar? ('Si' para una respuesta afirmativa)");
								String res = scanner.next();
								if (res.equalsIgnoreCase("si")) {
									usarBanco = false;
									String cadena = scanner.next();
									System.out.println("Resultado: " + afpd.procesarCadenaConDetalles(cadena));
								}
							}
							if (usarBanco) {
								Random random = new Random();
								int indiceAleatorio = random.nextInt(10);
								System.out.println("La cadena seleccionada fue: " + bancoString.get(indiceAleatorio));
								System.out.println("Resultado: "
										+ afpd.procesarCadenaConDetalles(bancoString.get(indiceAleatorio)));
							}
							continue;
						}
						case "3": {
			
						}
						case "4": {
							System.out.println("Escriba un nombre para el archivo donde se guardará el procesamiento.");
							String nombreArchivo = scanner.next();
							System.out.println("");
							Integer num = null;
							while (true) {
								System.out.println("Escriba el numero de cadenas a procesar: ");
								try {
									num = scanner.nextInt();
									if (num > 0)
										break;
								} catch (Exception e) {
								}
							}
							ArrayList<String> listaCadenas = new ArrayList<String>();
							for (int i = 0; i < num; ++i) {
								listaCadenas.add(scanner.next());
							}
							System.out.println("Desea ver los resultados en pantalla? ");
							String res = scanner.next();
							boolean imprimirPantalla = res.equalsIgnoreCase("si");
							afpd.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
							continue;
						}
						case "5": {
			
						}
						case "6": {
							System.out.println(afpd.toString());
							continue;
						}
						case "7": {
							menu2 = false;
							break;
						}
						default:
							menu1 = false;
							menu2 = false;
							break;
						}
					}
				}break;
			}case"4":{
			
			}case"5":{}*/default:
				menu = false;
			}
		}
		scanner.close();


	}
}
