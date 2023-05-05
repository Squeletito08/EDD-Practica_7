package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator(); 
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext(); 
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().elemento; 
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        private T elemento;
        /* El color del vértice. */
        private Color color;
        /* La lista de vecinos del vértice. */
        private Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento; 
            color = Color.NINGUNO; 
            vecinos = new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento; 
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud(); 
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color; 
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos; 
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas; 
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null || contiene(elemento))
            throw new IllegalArgumentException("El vertice ya está en la gráfica");
        
        Vertice vertice = new Vertice(elemento);
        vertices.agregaFinal(vertice);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {

        if(!(contiene(a) && contiene(b)))
            throw new NoSuchElementException("Los vertices a conectar no están en la grafica");

        if(a == b)
            throw new IllegalArgumentException("No se puede conectar un vertice consigo mismo");

        if(sonVecinos(a, b))
            throw new IllegalArgumentException("No se pueden conectar 2 vertices ya conectados");

        Vertice verticeA = (Vertice)vertice(a);
        Vertice verticeB = (Vertice)vertice(b);

        verticeA.vecinos.agregaFinal(verticeB);
        verticeB.vecinos.agregaFinal(verticeA);

        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        
        if(!(contiene(a) && contiene(b)))
            throw new NoSuchElementException("Los vertices a desconectar no están en la grafica");

        if(!sonVecinos(a, b))
            throw new IllegalArgumentException("No se pueden desconectar 2 vertices ya desconectados");

        Vertice verticeA = (Vertice)vertice(a);
        Vertice verticeB = (Vertice)vertice(b);

        verticeA.vecinos.elimina(verticeB);
        verticeB.vecinos.elimina(verticeA);

        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice vertice: vertices){
            if(vertice.elemento == elemento)
                return true; 
        }
        return false; 
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        if(elemento == null)
            throw new NoSuchElementException("El elemento a eliminar no puede ser nulo");

        Vertice vertice = (Vertice)vertice(elemento);
        vertices.elimina(vertice);

        for(Vertice u: vertice.vecinos){
            u.vecinos.elimina(vertice);
            aristas--; 
        }

    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice verticeA = (Vertice)vertice(a);
        Vertice verticeB = (Vertice)vertice(b);

        if(!(contiene(a) && contiene(b)))
            throw new IllegalArgumentException("Los vertices no están en la gráfica");

        if(verticeA.vecinos.contiene(verticeB) && verticeB.vecinos.contiene(verticeA))
            return true; 

        return false; 
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice verticeABuscar = null; 

        for(Vertice vertice: vertices){
            if(vertice.elemento.equals(elemento)){
                verticeABuscar = vertice;
                break;
            }    
        }

        if(verticeABuscar == null)
            throw new NoSuchElementException("El elemento no está en la grafica");

        return (VerticeGrafica<T>)verticeABuscar;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if(vertice == null || (vertice.getClass() != Vertice.class))
            throw new IllegalArgumentException("El vertice no es instancia de vertice");
        
        Vertice v = (Vertice)vertice; 
        v.color = color; 
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        bfs(vertices.getPrimero().elemento, v -> {});
        for(Vertice vertice: vertices){
            if(vertice.color == Color.ROJO)
                return false; 
        }
        return true; 
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice vertice: vertices)
            accion.actua(vertice);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice vertice = (Vertice)vertice(elemento);
        Cola<Vertice> cola = new Cola<Vertice>(); 

        recorreGrafica(vertice, cola, accion);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Vertice vertice = (Vertice)vertice(elemento);
        Pila<Vertice> pila = new Pila<Vertice>(); 

        recorreGrafica(vertice, pila, accion);
    }

    /**
     * Metodo auxiliar para implementar BFS o DFS
     * Recorre la grafica con una estructucura de datos, pintando cada 
     * vertice en el recorrido para saber si este ya fue visitado o no.
     * @param vertice el vertice con el que se comienza el recorrido.
     * @param estructura una instancia de MeteSaca<T> (una pila o cola).
     * @param accion la accion a realizar para cada vertice de la gráfica.
     */
    private void recorreGrafica(Vertice vertice, MeteSaca<Vertice> estructura,
                 AccionVerticeGrafica<T> accion){
        for(Vertice v: vertices)
            v.color = Color.ROJO; 
        
        vertice.color = Color.NINGUNO; 
        estructura.mete(vertice);

        while(!estructura.esVacia()){
            vertice = estructura.saca();
            accion.actua(vertice);
            for(Vertice u: vertice.vecinos){
                if(u.color == Color.ROJO){
                    u.color = Color.NINGUNO; 
                    estructura.mete(u);
                }
            }
        }
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia(); 
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0; 
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String s = "{";
    
        for(Vertice vertice: vertices)
            s += vertice.elemento.toString() + ", ";
        
        s += "}, {";

        for(Vertice u: vertices){
            for(Vertice w: u.vecinos){
                if(s.contains("(" + w.elemento.toString()))
                    continue;
                s += "(" + u.elemento.toString() + ", " + w.elemento.toString() + "), ";
            }
        }

        s += "}";
        return s; 
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        
        return (getElementos() == grafica.getElementos() &&
               aristas == grafica.getAristas() &&
               verificaElementosIguales(grafica) &&
               verificaAristasIguales(grafica));
    }

    /**
     * Nos dice si las graficas comparadas contienen los mismos 
     * elementos, no necesariamente en el mismo orden.
     * @param grafica una gráfica para comparar elementos.
     * @return <code>true</code> si los elmentos de las 2 graficas son iguales,
     *         <code>false</code> en otro caso. 
     */
    private boolean verificaElementosIguales(Grafica<T> grafica){
        
        for(Vertice vertice: vertices){
            try{
                VerticeGrafica<T> u = grafica.vertice(vertice.elemento);
            }
            catch(NoSuchElementException e){
                return false; 
            }
            /* 
            if(u.getGrado() != vertice.getGrado())
                return false; 
            */
        }
        return true; 
    }

    private boolean verificaAristasIguales(Grafica<T> grafica){
        VerticeGrafica<T> u; 
        for(Vertice vertice: vertices){
            for(Vertice vecino: vertice.vecinos){
                if(!grafica.sonVecinos(vertice.elemento, vecino.elemento))
                    return false;
            }
        }
        return true; 
    }


    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
