package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        quickSortAuxiliar(arreglo,comparador,0,arreglo.length-1);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Metodo auxiliar recursivo para implementar QuickSort.
     * @param arreglo arreglo a ordenar de tipo T, sus elementos son comparables.
     * @param comp objeto para comparar los elementos del arreglo.
     * @param a iniicio del subarreglo.
     * @param b final del subarreglo.
     */
    private static <T> void quickSortAuxiliar (T[] arreglo, Comparator<T> comp, int a, int b){
        if(b <= a)
            return; 

        int i = a + 1; 
        int j = b; 

        while(i < j){
            if(comp.compare(arreglo[i],arreglo[a]) > 0 && 
               comp.compare(arreglo[j],arreglo[a]) <= 0 ){
                intercambia(arreglo,i,j);
                i++; 
                j--;
            }
            else if(comp.compare(arreglo[i],arreglo[a]) <= 0)
                i++;
            else //(comp(arreglo[j],arreglo[a]) > 0)
                j--;
        }

        if(comp.compare(arreglo[i],arreglo[a]) > 0)
            i--; 

        intercambia(arreglo,i,a);
        quickSortAuxiliar(arreglo,comp,a,i-1);
        quickSortAuxiliar(arreglo,comp,i+1,b);
        return;     
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for(int i = 0; i < arreglo.length; i++){
            int m = i; 
            for(int j = i+1; j < arreglo.length; j++){
                if(comparador.compare(arreglo[j],arreglo[m]) < 0)
                    m = j; 
            }
            intercambia(arreglo,i,m);
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        return busquedaBinariaAuxiliar(arreglo,elemento,comparador,0,arreglo.length-1);
    }

    /**
     * Metodo auxiliar recursivo para implementar busqueda binaria.
     * @param arreglo el arreglo donde buscar.
     * @param elemento elemento a buscar.
     * @param comparador el comparador para hacer la busqueda.
     * @param a inicio del subarreglo.
     * @param b final del subarreglo.
     * @return el indice del elemento a buscar, o -1 si no lo encuentra. .
     */
    private static <T> int busquedaBinariaAuxiliar(T[] arreglo, T elemento, 
                                                  Comparator<T> comparador, 
                                                  int a, int b){
        if(b < a)
            return -1; 

        int m = a + ((b-a)/2);

        if(comparador.compare(elemento,arreglo[m]) == 0)
            return m; 
        else if(comparador.compare(elemento,arreglo[m]) < 0){
            return busquedaBinariaAuxiliar(arreglo,elemento,comparador,a,m-1);
        }
        else if (comparador.compare(elemento,arreglo[m]) > 0){
            return busquedaBinariaAuxiliar(arreglo,elemento,comparador,m+1,b);
        }
        return -1;
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }

    /**
     * Metodo auxiliar para intercambiar dos elementos de un arreglo.
     * @param x el arreglo.
     * @param a indice de un elemento a intercambiar.
     * @param b indice de un elemento a intercambiar.
     */
    private static <T> void intercambia(T[] x, int a, int b){
        T aux = x[a]; 
        x[a] = x[b]; 
        x[b] = aux; 
    }
}
