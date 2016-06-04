import algoritmo.Huffman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by allanmoreira on 02/06/16.
 */
public class Console {
    public static void main(String[] args) {
        Huffman h = new Huffman();
        h.comprime("aaaaabbbbcccdde");

        /*
        List<Integer> lista = new ArrayList<Integer>();
        lista.add(25);
        lista.add(5);
        lista.add(4);
        lista.add(1);
        lista.add(7);
        lista.add(10);
        lista.add(9);
        lista.add(3);
        List<Integer> novaLista = insertionSortDesc(lista);
        System.out.println(novaLista.toString());
        */
    }

    private static List<Integer> insertionSortDesc(List<Integer> lista) {
        int i;
        int chave;

        for (int j = lista.size()-2; j >= 0; j--) {
            chave = lista.get(j);
            i = j+1;
            while(i<lista.size() && lista.get(i)> chave){
                lista.set(i-1,lista.get(i));
                i = i+1;
            }
            lista.set(i-1, chave);
        }
        return lista;
    }
}
