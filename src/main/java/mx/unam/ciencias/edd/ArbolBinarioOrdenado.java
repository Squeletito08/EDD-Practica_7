package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            pila = new Pila<Vertice>(); 
            agregaRamaIzquierda(raiz);
        }

        /* Metodo axiliar para agregar los vertices de una rama 
         * del arbol a una pila. 
         */
        protected void agregaRamaIzquierda(Vertice actual){
            if(actual == null)
                return; 
            pila.mete(actual);
            agregaRamaIzquierda(actual.izquierdo);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia(); 
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
           Vertice vertice = pila.saca(); 
           if(vertice.hayDerecho())
               agregaRamaIzquierda(vertice.derecho);
           return vertice.elemento; 
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException("No se puede agregar un elemento nulo");

        Vertice nuevo = nuevoVertice(elemento); 
        ultimoAgregado = nuevo; 
        elementos++;
        if(raiz == null){
            raiz = nuevo; 
            return;
        }
        agrega(raiz,nuevo); 
    }

    /**
     * Metodo auxiliar recursivo que agrega un elemento al árbol.
     * @param actual vertice sobre el cual se compara
     * @param nuevo nuevo vertice a agregar
     */
    protected void agrega(Vertice actual, Vertice nuevo){
 
        if(nuevo.elemento.compareTo(actual.elemento) <= 0){
            if(actual.izquierdo == null){
                actual.izquierdo = nuevo; 
                nuevo.padre = actual; 
                return;
            }else{
                agrega(actual.izquierdo,nuevo);
            }
        }
        if(nuevo.elemento.compareTo(actual.elemento) > 0){
            if(actual.derecho == null){
                actual.derecho = nuevo; 
                nuevo.padre = actual; 
                return;
            }
            else{
                agrega(actual.derecho,nuevo); 
            }
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice aEliminar = vertice(busca(elemento));

        if(aEliminar == null)
            return; 

        elementos--; 

        if(aEliminar.izquierdo != null && aEliminar.derecho != null){
            aEliminar = intercambiaEliminable(aEliminar);
            eliminaVertice(aEliminar);
            return; 
        }

        eliminaVertice(aEliminar);
    }

    /**
     * Encunetra en un vertice que sea el máximo en el subArbol izquierdo del vertice.
     * Por definición este máximo solo tiene un hijo izquierdo, y todos los elementos del
     * subArbol izquierdo son menores o iguales que el maximo, y a su vez es menor o igual 
     * a todos los elementos del subArbol derecho del vertice. 
     * @param vertice el vertice sobre el cual se trabaja
     * @return el máximo en el subArbol izquierdo del vertice recibido
     */
    protected Vertice maximoSubArbol(Vertice vertice){
        if(vertice.derecho == null)
            return vertice; 
        return maximoSubArbol(vertice.derecho); 
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice maximoSubArbol = maximoSubArbol(vertice.izquierdo); 
        vertice.elemento = maximoSubArbol.elemento; 
        return maximoSubArbol; 
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {

        if(vertice == null)
            return; 

        if(vertice == raiz){
            if(vertice.hayIzquierdo()){
                raiz = vertice.izquierdo; 
                raiz.padre = null; 
            }
            else if(vertice.hayDerecho()){
                raiz = vertice.derecho;
                raiz.padre = null;
            }
            else{
                raiz = null; 
            }
            return;
        }

        Vertice padre = vertice.padre; 

        if(padre.izquierdo == vertice){
            if(vertice.hayIzquierdo()){
                padre.izquierdo = vertice.izquierdo; 
                vertice.izquierdo.padre = padre;
            }
            else if(vertice.hayDerecho()){
                padre.izquierdo = vertice.derecho;
                vertice.derecho.padre = padre; 
            }
            else{
                padre.izquierdo = null; 
            }
            return;
        }

        if(padre.derecho == vertice){
            if(vertice.hayIzquierdo()){
                padre.derecho = vertice.izquierdo; 
                vertice.izquierdo.padre = padre;
            }
            else if(vertice.hayDerecho()){
                padre.derecho = vertice.derecho;
                vertice.derecho.padre = padre; 
            }
            else{
                padre.derecho = null; 
            }
            return; 
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        return busca(raiz,elemento);
    }

    protected VerticeArbolBinario<T> busca(VerticeArbolBinario<T> actual, T elemento){
        if(actual == null || elemento.compareTo(actual.get()) == 0)
            return actual; 

        if(elemento.compareTo(actual.get()) < 0 && actual.hayIzquierdo())
            return busca(actual.izquierdo(),elemento);
        
        if(elemento.compareTo(actual.get()) > 0 && actual.hayDerecho())
            return busca(actual.derecho(),elemento);
        
        return null; 
    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {

        if(esVacia() || vertice == null)
            return;
        
        if(!vertice.hayIzquierdo())
            return; 

        Vertice actual = vertice(vertice);

        Vertice padreOriginal = actual.padre; 
        Vertice izquierdoOriginal = actual.izquierdo; 
        Vertice derechoIzquierdoOriginal = izquierdoOriginal.derecho; 

        actual.padre = izquierdoOriginal; 
        actual.padre.derecho = actual; 
        actual.padre.padre = padreOriginal; 

        //para saber si sobre el que se gira era izquierdo o derecho
        if(padreOriginal != null){
            if(padreOriginal.derecho == actual)
                padreOriginal.derecho = actual.padre; 
            else
                padreOriginal.izquierdo = actual.padre;         
        }
        else{
            raiz = actual.padre; 
        }

        actual.izquierdo = derechoIzquierdoOriginal;

        if(actual.izquierdo != null)
            actual.izquierdo.padre = actual; 
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        
        if(esVacia() || vertice == null)
            return;
        
        if(!vertice.hayDerecho())
            return; 

        Vertice actual = vertice(vertice);

        Vertice padreOriginal = actual.padre; 
        Vertice derechoOriginal = actual.derecho; 
        Vertice izquierdoDerechoOriginal = derechoOriginal.izquierdo; 
        
        actual.padre = derechoOriginal; 
        actual.padre.izquierdo = actual; 
        actual.padre.padre = padreOriginal; 

        //para saber si sobre el que se gira era izquierdo o derecho
        if(padreOriginal != null){
            if(padreOriginal.derecho == actual)
                padreOriginal.derecho = actual.padre; 
            else
                padreOriginal.izquierdo = actual.padre;   
        }
        else{
            raiz = actual.padre; 
        }
      
        actual.derecho = izquierdoDerechoOriginal;
        
        if(actual.derecho != null)
            actual.derecho.padre = actual; 
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPreOrder(raiz,accion);
    }

    /**
     * Metodo auxiliar recursivo para implementar dfsPreOrder.
     * @param actual vertice sobre el cual se empieza la acción.
     * @param accion la acción a realizar en cada elemento del árbol. 
     */
    protected void dfsPreOrder(Vertice actual, AccionVerticeArbolBinario<T> accion){
        if(actual == null)
            return; 
        accion.actua(actual);
        dfsPreOrder(actual.izquierdo,accion);
        dfsPreOrder(actual.derecho,accion);
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        dfsInOrder(raiz,accion);
    }

    /**
     * Metodo recursivo auxiliar para implementar dfsInOrder.
     * @param actual vertice sobre el cual se empieza la acción.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    protected void dfsInOrder(Vertice actual, AccionVerticeArbolBinario<T> accion){
        if(actual == null)
            return; 
        dfsInOrder(actual.izquierdo,accion);
        accion.actua(actual);
        dfsInOrder(actual.derecho,accion);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(raiz,accion);
    }

    /**
     * Metodo recursivo auxiliar para implemenar dfsPostOrder.
     * @param actual vertice sobre el cual se empieza la acción.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    protected void dfsPostOrder(Vertice actual, AccionVerticeArbolBinario<T> accion){
        if(actual == null)
            return; 
        dfsPostOrder(actual.izquierdo,accion);
        dfsPostOrder(actual.derecho,accion);
        accion.actua(actual);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
