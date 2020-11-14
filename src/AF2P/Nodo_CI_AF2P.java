package AF2P;

import java.util.Vector;

public class Nodo_CI_AF2P {

	private String configuracion;
	private Vector<Nodo_CI_AF2P> hijos;

	public Nodo_CI_AF2P(String configuracion) {
		this.configuracion = configuracion;
		hijos = null;
	}

	public String getConfiguracion() {
		return configuracion;
	}

	public Vector<Nodo_CI_AF2P> getHijos() {
		return hijos;
	}

	public void insertarHijo(String configuracion) {
		if (hijos == null) {
			hijos = new Vector<Nodo_CI_AF2P>();
		}
		hijos.add(new Nodo_CI_AF2P(configuracion));
	}

}
