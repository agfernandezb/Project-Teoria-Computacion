package AF2P;

import java.util.Set;
import java.util.Vector;

public class ArbolConfiguracionesAF2P {
	private Nodo_CI_AF2P raiz;
	private Set<String> estadosAceptacion;

	public ArbolConfiguracionesAF2P(String configuracionInicial, Set<String> estadosAceptacion) {
		this.raiz = new Nodo_CI_AF2P(configuracionInicial);
		this.estadosAceptacion = estadosAceptacion;
	}

	public Vector<String> getProcesamientos() {
		Vector<String> procesamientos = new Vector<>();
		addProcesamiento(raiz, "", procesamientos);
		return procesamientos;
	}

	public Nodo_CI_AF2P getRaiz() {
		return raiz;
	}

	private void addProcesamiento(Nodo_CI_AF2P configuracion, String procesamiento, Vector<String> procesamientos) {
		String configuracionInstantanea = configuracion.getConfiguracion();
		String estado = configuracionInstantanea.split(",")[0];
		String cinta = configuracionInstantanea.split(",")[1];
		String caracterPrimeraPila = configuracionInstantanea.split(",")[2];
		String caracterSegundaPila = configuracionInstantanea.split(",")[3];
		if (cinta.equals("$") && caracterPrimeraPila.equals("$") && caracterSegundaPila.equals("$")
				&& configuracion.getHijos() != null) { //El procesamiento no terminó y hay Lambda transiciones
			boolean resultado = estadosAceptacion.contains(estado); //Como ambas pilas estan vacias, basta con verificar esa condición
			String resultadoProcesamiento = resultado ? "accepted" : "rejected";
			if (procesamiento.length() > 0)
				procesamientos.add(
						procesamiento + "->" + "(" + configuracionInstantanea + ")" + ">>" + resultadoProcesamiento);
			else
				procesamientos.add("(" + configuracionInstantanea + ")" + ">>" + resultadoProcesamiento);
		}
		if (configuracion.getHijos() == null) { //El procesamiento terminó
			boolean resultado = estadosAceptacion.contains(estado) && cinta.equals("$")
					&& caracterPrimeraPila.equals("$") && caracterSegundaPila.equals("$");
			String resultadoProcesamiento = resultado ? "accepted" : "rejected";
			if (procesamiento.length() > 0)
				procesamientos.add(
						procesamiento + "->" + "(" + configuracionInstantanea + ")" + ">>" + resultadoProcesamiento);
			else
				procesamientos.add("(" + configuracionInstantanea + ")" + ">>" + resultadoProcesamiento);

		} else {
			for (Nodo_CI_AF2P hijo : configuracion.getHijos()) {
				if (procesamiento.length() > 0)
					addProcesamiento(hijo, procesamiento + "->" + "(" + configuracionInstantanea + ")", procesamientos);
				else
					addProcesamiento(hijo, "(" + configuracionInstantanea + ")", procesamientos);

			}
		}
	}
}
