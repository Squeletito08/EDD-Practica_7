package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            cola = new Cola<Vertice>(); 
            if(raiz != null)
                cola.mete(raiz); 
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia(); 
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            
            if(cola.esVacia())
                throw new NoSuchElementException();  
            
            Vertice vertice = cola.saca(); 
            if(vertice.hayIzquierdo())
                cola.mete(vertice.izquierdo); 
            if(vertice.hayDerecho())
                cola.mete(vertice.derecho); 
            return vertice.elemento; 
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException("No se pueden agregar elementos nulos");

        Vertice nuevo = nuevoVertice(elemento); 
        elementos++; 

        if(raiz == null){
            raiz = nuevo;
            return; 
        }

        Cola<Vertice> cola = new Cola<Vertice>(); 
        cola.mete(raiz); 
        while(!cola.esVacia()){
            Vertice v = cola.saca();

            if(v.hayIzquierdo()){
                cola.mete(v.izquierdo);
            }
            else{
                nuevo.padre = v;
                v.izquierdo = nuevo;
                return;
            }

            if(v.hayDerecho()){
                cola.mete(v.derecho);
            }
            else{
                nuevo.padre = v;
                v.derecho = nuevo;
                return;
            }
        }
    }


    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice verticeBuscado = vertice(busca(elemento));

        if(verticeBuscado == null)
            return; 

        elementos--; 

        if(elementos == 0){
            raiz = null;
            return; 
        }
            
        Vertice ultimoVertice = null; 

        Cola<Vertice> cola = new Cola<Vertice>(); 
        cola.mete(raiz); 
        while(!cola.esVacia()){
            ultimoVertice = cola.saca();
            if(ultimoVertice.hayIzquierdo())
                cola.mete(ultimoVertice.izquierdo);
            if(ultimoVertice.hayDerecho())
                cola.mete(ultimoVertice.derecho);
        }

        verticeBuscado.elemento = ultimoVertice.elemento; 

        if(ultimoVertice.padre.izquierdo == ultimoVertice)
            ultimoVertice.padre.izquierdo = null; 

        if(ultimoVertice.padre.derecho == ultimoVertice)
            ultimoVertice.padre.derecho = null;          
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if(esVacia())
            return -1;
        return log2(elementos); 
    }

    protected int log2(int n){
        int contador = 0; 
        while (n >> 1 > 0){
            n = n >> 1; 
            contador++;
        }
        return contador;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if(esVacia())
            return; 

        Cola<VerticeArbolBinario<T>> cola = new Cola<VerticeArbolBinario<T>>(); 
        cola.mete(raiz); 
        while(!cola.esVacia()){
            VerticeArbolBinario<T> v = cola.saca(); 
            accion.actua(v); 
            if(v.hayIzquierdo())
                cola.mete(v.izquierdo());
            if(v.hayDerecho())
                cola.mete(v.derecho());
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
