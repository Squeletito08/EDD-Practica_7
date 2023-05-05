package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        protected T elemento;
        /** El padre del vértice. */
        protected Vertice padre;
        /** El izquierdo del vértice. */
        protected Vertice izquierdo;
        /** El derecho del vértice. */
        protected Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        protected Vertice(T elemento) {
            this.elemento = elemento; 
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            return padre != null; 
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            return izquierdo != null; 
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            return derecho != null; 
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            if(!hayPadre())
                throw new NoSuchElementException("No hay padre");
            return this.padre; 

        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            if(!hayIzquierdo())
                throw new NoSuchElementException("No hay izquierdo");
            return this.izquierdo; 
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            if(!hayDerecho())
                throw new NoSuchElementException("No hay derecho"); 
            return this.derecho; 
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura(this); 
        }

        /**
         * Calcula la altura recursivamente como el maximo entre la altura del subarbol 
         * izquierdo y el subarbol derecho más 1, o -1 en caso de que el vertice sea null.
         * @param v el vertice al cual calcular la altura.
         * @return la altura del vertice recibido.
         */
        protected int altura(Vertice v){  
            return (v == null) ? -1 : 1 + max(altura(v.izquierdo),
                                              altura(v.derecho));
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            return profundidad(this);
        }

        /**
         * Metodo auxiliar. Calcula la profundidad de un vertice recursivamente
         * como 1 más la profundidad del padre del vertice, o -1 si el vertice o
         * su padre son null. 
         * @param v vertice al cual calcular la profundidad.
         * @return la profundidad del vertice. 
         */
        protected int profundidad(Vertice v){
            return (v == null || v.padre == null) ? 0 : 1 + profundidad(v.padre);
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            return this.elemento; 
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            
            return equals(this,vertice);
        }

        /**
         * Metodo auxiliar para el metodo equals. 
         * @param actual vertice que mandó a llamar al metodo equals.
         * @param v vertice con el cual se empezará a comparar,
         * @return <code>true</code> si los dos arboles son iguales, 
         *         <code>false</code> en cualquier otro caso.
         */
        protected boolean equals(Vertice actual, Vertice v){
            if(actual == null && v == null)
                return true; 
    
            if(actual == null || v == null)
                return false; 

            return (actual.elemento.equals(v.elemento) && 
            equals(actual.izquierdo,v.izquierdo) && equals(actual.derecho, v.derecho));
        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        @Override public String toString() {
            return elemento.toString(); 
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        for(T elemento: coleccion)
            agrega(elemento);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento); 
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        return (esVacia()) ? -1 : raiz.altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        return elementos; 
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return busca(elemento) != null; 
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        if(esVacia() || elemento == null)
            return null; 

        Cola<VerticeArbolBinario<T>> cola = new Cola<VerticeArbolBinario<T>>(); 
        cola.mete(raiz);

        while(!cola.esVacia()){
            VerticeArbolBinario<T> v = cola.saca(); 

            if(v.get().equals(elemento))
                return v; 
            if(v.hayIzquierdo())
                cola.mete(v.izquierdo());
            if(v.hayDerecho())
                cola.mete(v.derecho());
        }
        return null; 
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        if(esVacia())
            throw new NoSuchElementException("El arbol es vacío");
        return (VerticeArbolBinario<T>)raiz; 
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return raiz == null; 
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        raiz = null; 
        elementos = 0; 
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;

        if(esVacia() && arbol.esVacia())
            return true; 

        return elementos == arbol.getElementos() && 
        raiz.equals(arbol.raiz()); 
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        if(raiz == null)
            return "";
        
        int[] A = new int[raiz.altura()+1];

        return toString(raiz,0,A);  
    }

    /**
     * Metodo auxiliar para convertir un arbol binario a su representanción en cadena.
     * @param v el vertice sobre el cual se trabaja.
     * @param nivel el nivel al que se encuntra el vertice.
     * @param A arreglo para saber cuando dibujar espacios. 
     * @return representación en cadena del arbol del vertice recibido. 
     */
    protected String toString(Vertice v, int nivel, int[] A){
        String s = v.toString() + "\n";
        A[nivel] = 1; 

        if(v.izquierdo != null && v.derecho != null){
            s += dibujaEspacios(nivel, A);
            s += "├─›";
            s += toString(v.izquierdo,nivel+1,A);
            s += dibujaEspacios(nivel,A); 
            s += "└─»";
            A[nivel] = 0; 
            s += toString(v.derecho,nivel+1,A);
        }
        else if(v.izquierdo != null){
            s += dibujaEspacios(nivel, A);
            s += "└─›";
            A[nivel] = 0; 
            s += toString(v.izquierdo,nivel+1,A); 
        }
        else if(v.derecho != null){
            s += dibujaEspacios(nivel, A);
            s += "└─»";
            A[nivel] = 0; 
            s += toString(v.derecho,nivel+1,A);
        }
        return s; 
    }

    /**
     * Metodo auxiliar para saber cuando colocar espacios en la cadena del arbol.
     * @param nivel nivel al que se encunetra un vertice.
     * @param A arreglo para saber cuando dibujar espacios.
     * @return una cadena con los espacios necesarios. 
     */
    protected String dibujaEspacios(int nivel, int[] A){
        String s = "";
        for(int i=0; i <= nivel-1; i++){
            if(A[i] == 1)
                s += "│  ";
            else
                s += "   ";
        }
        return s; 
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }

    /**
     * Metodo auxiliar que calcula el maximo entre 2 números enteros.
     * @param a un entero.
     * @param b un entero.
     * @return el máximo entre a y b.
     */
    protected int max(int a, int b){
        return (a>=b) ? a : b;
    }
}
