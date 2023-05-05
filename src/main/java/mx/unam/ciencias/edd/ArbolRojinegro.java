package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO; 
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            if(color == Color.ROJO)
                return "R{" + elemento.toString() + "}";
            return "N{" + elemento.toString() + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento); 
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        if(vertice == null || !(vertice instanceof ArbolRojinegro.VerticeRojinegro))
            throw new ClassCastException("El vertice recibido es invalido");
        VerticeRojinegro v = (VerticeRojinegro) vertice; 
        return v.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento); 
        VerticeRojinegro v = (VerticeRojinegro) ultimoAgregado; 
        v.color = Color.ROJO; 
        rebalanceoAgrega(v);
    }

    /**
     * Algoritmo de rebalanceo después de agreagar un vertice a un ÁrbolRojiNegro.
     * @param v último vertice agregado. Este vertice SIEMPRE es rojo. 
     */
    private void rebalanceoAgrega(VerticeRojinegro v){

        if(v.padre == null){
            v.color = Color.NEGRO; 
            return; 
        }

        if(!esRojo(getPadre(v)))
            return; 

        VerticeRojinegro tio = getTio(v);
        VerticeRojinegro padre = getPadre(v); 
        VerticeRojinegro abuelo = getAbuelo(v);

        if(tio != null && esRojo(tio)){
            tio.color = padre.color = Color.NEGRO; 
            abuelo.color = Color.ROJO; 
            rebalanceoAgrega(abuelo);
            return; 
        }

        if( (!esIzquierdo(padre) && esIzquierdo(v)) || (esIzquierdo(padre) && !esIzquierdo(v))){
            if(esIzquierdo(padre))
                super.giraIzquierda(padre);
            else 
                super.giraDerecha(padre);

            VerticeRojinegro aux = v; 
            v = (VerticeRojinegro)padre; 
            padre = aux;
        }

        padre.color = Color.NEGRO; 
        abuelo.color = Color.ROJO; 

        if(esIzquierdo(v))
            super.giraDerecha(abuelo);  
        else 
            super.giraIzquierda(abuelo);

    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro aEliminar = (VerticeRojinegro)vertice(busca(elemento));

        if(aEliminar == null)
            return; 

        elementos--; 

        if(aEliminar.izquierdo != null && aEliminar.derecho != null){
            aEliminar = (VerticeRojinegro)intercambiaEliminable(aEliminar);
        }

        VerticeRojinegro fantasma = null;
        VerticeRojinegro hijo = null; 

        if(aEliminar.izquierdo == null && aEliminar.derecho == null){
            fantasma = (VerticeRojinegro)nuevoVertice(null);
            fantasma.color = Color.NEGRO; 
            fantasma.padre = aEliminar; 
            aEliminar.izquierdo = fantasma; 
            hijo = fantasma; 
        }
        else{
            if(aEliminar.izquierdo != null)
                hijo = (VerticeRojinegro) aEliminar.izquierdo;
            else
                hijo = (VerticeRojinegro) aEliminar.derecho; 
        }


        eliminaVertice(aEliminar);
        
        if(hijo.color == Color.ROJO || aEliminar.color == Color.ROJO)
            hijo.color = Color.NEGRO; 
        else
            rebalanceoElimina(hijo);

        if(fantasma != null)
            eliminaVertice(fantasma);
        
    }

    /**
     * Algoritmo de rebalanceo despuúes de eliminar un vertice en un ÁrbolRojiNegro.
     * @param vertice el hijo del vertice eliminado. Este vertice SIEMPRE es negro. 
     */
    private void rebalanceoElimina(VerticeRojinegro vertice){
        
        if(vertice.padre == null)
            return; 

        VerticeRojinegro padre = getPadre(vertice);
        VerticeRojinegro hermano = getHermano(vertice);

        if(esRojo(hermano)){
            padre.color = Color.ROJO;
            hermano.color = Color.NEGRO; 
            
            if(esIzquierdo(vertice))
                super.giraIzquierda(padre);
            else
                super.giraDerecha(padre);

            padre = getPadre(vertice);
            hermano = getHermano(vertice); 
        }

        VerticeRojinegro sobrinoIzq = (VerticeRojinegro)hermano.izquierdo; 
        VerticeRojinegro sobrinoDer = (VerticeRojinegro)hermano.derecho; 

        if( !esRojo(padre) && !esRojo(hermano) && !esRojo(sobrinoDer) && !esRojo(sobrinoIzq)){
            hermano.color = Color.ROJO; 
            rebalanceoElimina(padre);
            return; 
        }
        
        if( esRojo(padre) && !esRojo(hermano) && !esRojo(sobrinoDer) && !esRojo(sobrinoIzq)){
            hermano.color = Color.ROJO; 
            padre.color = Color.NEGRO; 
            return; 
        }

        if( (esIzquierdo(vertice) && esRojo(sobrinoIzq) && !esRojo(sobrinoDer)) ||
            !esIzquierdo(vertice) && !esRojo(sobrinoIzq) && esRojo(sobrinoDer)){

                hermano.color = Color.ROJO; 

                if(esRojo(sobrinoIzq))
                    sobrinoIzq.color = Color.NEGRO; 
                
                if(esRojo(sobrinoDer))
                    sobrinoDer.color = Color.NEGRO; 

                if(esIzquierdo(vertice))
                    super.giraDerecha(hermano);
                else    
                    super.giraIzquierda(hermano);

                hermano = getHermano(vertice);
                sobrinoIzq = (VerticeRojinegro)hermano.izquierdo; 
                sobrinoDer = (VerticeRojinegro)hermano.derecho; 
            }

        hermano.color = padre.color; 
        padre.color = Color.NEGRO; 

        if(esIzquierdo(vertice)){
            sobrinoDer.color = Color.NEGRO; 
            super.giraIzquierda(padre);
        }
        else {
            sobrinoIzq.color = Color.NEGRO; 
            super.giraDerecha(padre);
        }

    }

    /**
     * Obtiene el padre de un vertice
     * @param v un vertice
     * @return Un VerticeRojinegro que será el padre del vertice v
     *         null si el vertice recibido es null o el padre no existe.
     */
    private VerticeRojinegro getPadre(VerticeRojinegro v){
        if(v == null)
            return null; 
        return (VerticeRojinegro) v.padre; 
    }

    /**
     * Obtiene el abuelo de un vertice (padre del padre).
     * @param v un vertice
     * @return  Un VerticeRojinegro que será el padre del padre del vertice v
     *          null si el vertice recibido es null, si el padre de v es o 
     *          si el abuelo no existe. 
     */
    private VerticeRojinegro getAbuelo(VerticeRojinegro v){
        VerticeRojinegro padre = getPadre(v); 
        return getPadre(padre);
    }

    /**
     * Obtiene el tío de un vertice (hermano del padre de v).
     * @param v un vertice. 
     * @return Un VerticeRojinegro que será el hijo del abuelo de v que no es el padre de v,
     *         o null si el vertice es null o si su tío no existe. 
     */
    private VerticeRojinegro getTio(VerticeRojinegro v){
        VerticeRojinegro padre = getPadre(v); 
        VerticeRojinegro abuelo = getAbuelo(v);
        if(abuelo == null)
            return null; 
        if(abuelo.izquierdo == padre)
            return (VerticeRojinegro) abuelo.derecho; 
        return (VerticeRojinegro) abuelo.izquierdo; 

    }

    /**
     * Obtiene el hermano de un vertice (hijo del padre que no es v).
     * @param v un vertice
     * @return Un VerticeRojiNegro que será el hermano de v, o null si 
     *         v es null, si el padre de v es null o si el hermano no existe. 
     */
    private VerticeRojinegro getHermano(VerticeRojinegro v){
        VerticeRojinegro padre = getPadre(v);
        if(padre == null)
            return null; 
        if(padre.izquierdo == v)
            return (VerticeRojinegro)padre.derecho; 
        return (VerticeRojinegro)padre.izquierdo; 
    }

    /**
     * Nos dice si un vertice es izquierdo o no.
     * La negación del valor de dicho metodo nos dice si el vertice es derecho. 
     * @param v un vertice
     * @return <code>true</code> si el vertice es hijo izquierdo del padre de v.
     *         <code>false</cose> en cualquier otro caso.   
     */
    private boolean esIzquierdo(VerticeRojinegro v){
        VerticeRojinegro padre = getPadre(v); 
        if(padre.izquierdo == v)
            return true; 
        return false; 
    }

    /**
     * Nos dice si el vertice es de color rojo. 
     * La negación del valor de dicho metodo nos dice si el vertice es negro.
     * @param vertice un vertice
     * @return <code>true</code> si el vertice es rojo o si no es null
     *         <code>false</code> en cualquier otro caso. 
     */
    private boolean esRojo(VerticeRojinegro vertice){
        return (vertice != null && vertice.color == Color.ROJO);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
