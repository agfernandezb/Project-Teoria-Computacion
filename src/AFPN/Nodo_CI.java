package AFPN;

import java.util.Vector;

public class Nodo_CI {
	 
	    private String configuracion; 
	    private Vector<Nodo_CI> hijos;
	  
	    public Nodo_CI(String configuracion) 
	    { 
	        this.configuracion = configuracion;
	        hijos = null;
	    } 
	    public String getConfiguracion() {
			return configuracion;
		}
	    
		public Vector<Nodo_CI> getHijos() {
			return hijos;
		}
		public void insertarHijo (String configuracion) 
	    { 
	        if(hijos == null)
	        {
	        	hijos = new Vector<Nodo_CI>();
	        }
	        hijos.add(new Nodo_CI(configuracion));
	    }
	    
}
