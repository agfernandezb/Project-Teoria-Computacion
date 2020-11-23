import java.util.Scanner;

public class main {
	public static void main(String[] args) {

		//Algun menu que le indique al usuario cual clase desea ver...
		//puede seleccionar algunos lenguajes que estan ahi y verlos, y puede dar cadenas...
		Scanner scanner = new Scanner(System.in);
		boolean menu = true;
		while (menu) {
			System.out.println("Seleccione el módulo que desea observar:");
			System.out.println("1: AFD");
			/*System.out.println("Seleccione el módulo que desea observar:");
			System.out.println("Seleccione el módulo que desea observar:");
			System.out.println("Seleccione el módulo que desea observar:");*/
			int opcionSeleccionada = scanner.nextInt();
			switch (opcionSeleccionada) {
			case 1: {
				System.out.println("Clase AFD");
				System.out.println("Seleccione el automada finito determinista que desea utilizar:");
				System.out.println(
						"1.Automata finito determinista que acepta las cadenas con un número par de aes y de bes");
				System.out.println("2.Automata finito determinista que acepta el lenguaje (a^2i)*(b^3j)*");
				System.out.println("3.Automata finito determinista que acepta el lenguaje a+b* U ac* U b*ca*");
				System.out.println("4.Ingresar uno propio");
				//switch
			}
			default:
				menu = false;
			}
		}
		/*
				System.out.println("Clase AFD");
				System.out.println("1.Automata finito determinista que acepta las cadenas con un número par de aes y de bes");
				AFD parAparB = new AFD("parAparB");
				parAparB.procesarCadenaConDetalles("aabbabba");
				parAparB.procesarCadenaConDetalles("abababa");
		
				System.out.println("2..Automata finito determinista que acepta el lenguaje (a^2i)*(b^3j)*");
				AFD a2ib3j = new AFD("a2ib3j");
				a2ib3j.procesarCadenaConDetalles("bbb");
				a2ib3j.procesarCadenaConDetalles("aaaabbbbbb");
				a2ib3j.procesarCadenaConDetalles("aabbba");
				System.out.println("3..Automata finito determinista que acepta el lenguaje a+b* U ac* U b*ca*");
				AFD afd3 = new AFD("afd3");
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
		// TODO Auto-generated method stub
		//MT -> mismo numero de a que b
		//MT mt = new MT("uno");
		/*mt.procesarCadenaConDetalles("ababababaa");
		System.out.println(mt.procesarFuncion("ababababaa"));
		List<String> prueba = Arrays.asList("aabb", "abaaba", "abababaaa", "aabbaab");
		mt.procesarListaCadenas(prueba, "igualAqueB", true);*/
		//System.out.println(mt.toString());

	}
}
