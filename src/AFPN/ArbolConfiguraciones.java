package AFPN;

import java.util.Set;
import java.util.Vector;

public class ArbolConfiguraciones {
	private Nodo_CI raiz;
	private Set<String> estadosAceptacion;

	public ArbolConfiguraciones(String configuracionInicial) {
		this.raiz = new Nodo_CI(configuracionInicial);
	}

	public Vector<String> getProcesamientos() {
		Vector<String> procesamientos = new Vector<>();
		addProcesamiento(raiz, "", procesamientos);
		return procesamientos;
	}

	public Nodo_CI getRaiz() {
		return raiz;
	}

	private void addProcesamiento(Nodo_CI configuracion, String procesamiento, Vector<String> procesamientos) {
		String configuracionInstantanea = configuracion.getConfiguracion();
		if (configuracion.getHijos() == null) {
			String estado = configuracionInstantanea.split(",")[0];
			String cinta = configuracionInstantanea.split(",")[1];
			String pila = configuracionInstantanea.split(",")[2];
			boolean resultado = estadosAceptacion.contains(estado) && cinta.equals("$") && pila.equals("$");
			String resultadoProcesamiento = resultado ? "accepted" : "rejected";
			if (procesamiento.length() > 0)
				procesamientos.add(procesamiento + "->" + "(" + configuracion + ")" + ">>" + resultadoProcesamiento);
			else
				procesamientos.add("(" + configuracionInstantanea + ")" + ">>" + resultadoProcesamiento);

		} else {
			for (Nodo_CI hijo : configuracion.getHijos()) {
				if (procesamiento.length() > 0)
					addProcesamiento(hijo, procesamiento + "->" + "(" + configuracionInstantanea + ")", procesamientos);
				else
					addProcesamiento(hijo, "(" + configuracionInstantanea + ")", procesamientos);
				;
			}
		}
	}
}
