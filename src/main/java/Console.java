import algoritmo.Huffman;

/**
 * Created by allanmoreira on 02/06/16.
 */
public class Console {
    public static void main(String[] args) {
        Huffman huffman = new Huffman();
        huffman.codifica("aaaaabbbbcccdde");
        huffman.decodifica();
        System.out.println(huffman.geraGraphviz());
    }
}
