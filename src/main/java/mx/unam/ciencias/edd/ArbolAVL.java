package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura; 
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            String s = elemento.toString(); 
            s += " " + altura;
            s += "/" + balanceVertice();
            return s; 
        }

        /*Metodo auxiliar para calcular el balance de un vertice */
        private int balanceVertice(){
            int alturaSubArbolIzquierdo = -1;
            int alturaSubArbolDerecho = -1;

            if(hayIzquierdo())
                alturaSubArbolIzquierdo = this.izquierdo.altura();
            
            if(hayDerecho())
                alturaSubArbolDerecho = this.derecho.altura();

            return alturaSubArbolIzquierdo - alturaSubArbolDerecho; 
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return (altura == vertice.altura && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalanceaArbolAVL((VerticeAVL)ultimoAgregado);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL vertice = (VerticeAVL)busca(elemento);

        if(vertice == null)
            return; 

        elementos--; 

        if(vertice.izquierdo != null && vertice.derecho != null){
            vertice = (VerticeAVL)intercambiaEliminable(vertice);
            eliminaVertice(vertice);
        }

        eliminaVertice(vertice);
        rebalanceaArbolAVL((VerticeAVL)vertice.padre);
    }

    /**
     * Metodo auxiliar de rebalanceo despues de agregar o eliminar
     * @param vertice el padre del vertice recien agregado o eliminado
     */
    private void rebalanceaArbolAVL(VerticeAVL vertice){

        if(vertice == null)
            return; 
        
        /*Hijo izquierdo del vertice */
        VerticeAVL p = getHijoIzquierdo(vertice);  /*p */

        /*Hijo derecho del vertice */
        VerticeAVL q = getHijoDerecho(vertice); /*q */

        /*Hijo izquierdo q */
        VerticeAVL x;

        /*Hijo derecho de p */
        VerticeAVL y;

        vertice.altura = calculaAltura(vertice);

        int balance = vertice.balanceVertice(); 

        int balance_q = 0;
        int balance_p = 0;
        
        if(q != null)
            balance_q = q.balanceVertice(); 

        if(p != null)
            balance_p = p.balanceVertice();
        

        if(balance == -2){

            /*Hijo izquierdo de q */
            x = getHijoIzquierdo(q); 

            if(balance_q == 1){
                super.giraDerecha(q);
                q.altura = calculaAltura(q);
                x.altura = calculaAltura(x); 
                q = getHijoDerecho(vertice);
                x = getHijoIzquierdo(q); 
            }

            /*Para este punto el balance de q es 0, -1 o -2 */
            super.giraIzquierda(vertice);
            vertice.altura = calculaAltura(vertice);
            q.altura = calculaAltura(q); 
        }

        if(balance == 2){

            /*Hijo derecho de p */
            y = getHijoDerecho(p); 

            if(balance_p == -1){
                super.giraIzquierda(p);
                y.altura = calculaAltura(y);
                p.altura = calculaAltura(p);
                p = getHijoIzquierdo(vertice);
                y = getHijoDerecho(p);
            }

            /*Para este punto el balance de p es 0, 1 o 2 */
            super.giraDerecha(vertice);
            vertice.altura = calculaAltura(vertice);
            p.altura = calculaAltura(p);
        }

        rebalanceaArbolAVL((VerticeAVL)vertice.padre);
    }

    /**
     * Calcula la altura de un vertice de tipo VerticeAVL aprovechando la 
     * propieadad altura. Esto lo hace en tiempo constante.
     * @param vertice el vertice a culcular altura.
     * @return el máximo entre la altura del subArbolizquierdo y del 
     *         subArbolderecho + 1, o -1 en caso de que el vertice sea null.
     */
    protected int calculaAltura(VerticeAVL vertice){

        VerticeAVL hijoIzquierdo = getHijoIzquierdo(vertice);
        VerticeAVL hijoDerecho = getHijoDerecho(vertice);

        int alturaSubArbolIzquierdo = -1; 
        int alturaSubArbolDerecho = -1;

        if(vertice.hayIzquierdo())
            alturaSubArbolIzquierdo = hijoIzquierdo.altura;

        if(vertice.hayDerecho())
            alturaSubArbolDerecho = hijoDerecho.altura; 

        return max(alturaSubArbolIzquierdo,alturaSubArbolDerecho) + 1;
    }

    /**
     * Obtiene el hijo derecho de un VerticeAVL
     * @param vertice un vertice
     * @return un VerticeAVL que será el hijo derecho del vertice, 
     *         o null si el hijo derecho no existe. 
     */
    protected VerticeAVL getHijoDerecho(VerticeAVL vertice){
        return vertice.hayDerecho() ? (VerticeAVL)vertice.derecho : null; 
    }

    /**
     * Obtiene el hijo derecho de un VerticeAVL
     * @param vertice un vertice
     * @return un VerticeAVL que será el hijo izquierdo del vertice, 
     *         o null si el hijo derecho no existe. 
     */
    protected VerticeAVL getHijoIzquierdo(VerticeAVL vertice){
        return (vertice.hayIzquierdo()) ? (VerticeAVL)vertice.izquierdo : null; 
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
