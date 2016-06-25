import algoritmo.Huffman;

import java.util.*;

/**
 * Created by allanmoreira on 02/06/16.
 */
public class Console {
    public static void main(String[] args) {
        Huffman h = new Huffman();
        h.comprime("aaaaabbbbcccdde");
//        h.escreveArquivoBinario();
        byte[] bytes = h.leArquivoBinario();
        h.decodificaCodigoBinario(bytes);
//        System.out.println(h.geraGraphviz());
    }
}
