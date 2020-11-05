package AFPN;

import java.util.List;
import java.util.Set;

import AFPD.AFPD;
import AFPD.FuncionTransicionAFPD;

public class AFPN extends AFPD{

	public AFPN(Set<String> conjuntoEstados, String estadoInicial, Set<String> estadosAceptacion,
			Set<Character> alfabetoCinta, Set<Character> alfabetoPila, FuncionTransicionAFPD delta) {
		super(conjuntoEstados, estadoInicial, estadosAceptacion, alfabetoCinta, alfabetoPila, delta);
	}
	
	public AFPN(String nombre) {
		super(nombre);
	}

	//Override...
	@Override
	public String procesarCadena(String cadena, boolean retornarProcesamiento) {
		// TODO Auto-generated method stub
		return super.procesarCadena(cadena, retornarProcesamiento);
	}

	@Override
	public boolean procesarCadena(String cadena) {
		// TODO Auto-generated method stub
		return super.procesarCadena(cadena);
	}

	@Override
	public boolean procesarCadenaConDetalles(String cadena) {
		// TODO Auto-generated method stub
		return super.procesarCadenaConDetalles(cadena);
	}

	@Override
	public void procesarListaCadenas(List<String> listaCadenas, String nombreArchivo, boolean imprimirPantalla) {
		// TODO Auto-generated method stub
		super.procesarListaCadenas(listaCadenas, nombreArchivo, imprimirPantalla);
	}

		
}
