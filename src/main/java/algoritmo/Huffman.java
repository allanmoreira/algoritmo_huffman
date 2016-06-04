package algoritmo;

import java.util.*;

/**
 * Created by allanmoreira on 5/11/16.
 */
public class Huffman {

    private List<Nodo> listaRaizes;

    private class Nodo {
        Nodo direito;
        Nodo esquerdo;
        char caracter;
        int frequencia;

        public Nodo (char caracter, int frequencia) {
            this.caracter = caracter;
            this.frequencia = frequencia;
        }
    }

    public void comprime(String palavra) {
        geraRaizes(palavra);
        Nodo raiz;

        System.out.println("Antes da alteração:");
        System.out.println(imprimeRaizes());
        System.out.println("-------------------------------------------");

        while(listaRaizes.size() > 1){
            Nodo menor1 = listaRaizes.remove(listaRaizes.size()-1);
            Nodo menor2 = listaRaizes.remove(listaRaizes.size()-1);

//            if(menor1.frequencia == menor2.frequencia)
//                        if(raiz.caracter != '*')
//                raiz = unirArvores(menor1, menor2);
//            else
                raiz = unirArvores(menor2, menor1);

            listaRaizes.add(raiz);
//            if(raiz.caracter != '*')
            insertionSortDesc(listaRaizes);

            IMPRIME_ARVORE_TEST();
        }
    }

    private void IMPRIME_ARVORE_TEST() {
        System.out.println("Raízes = " + imprimeRaizes());
        for (Nodo n : listaRaizes)
            System.out.println(imprimeArvore(n));
        System.out.println("-------------------------------------------");
    }

    private Nodo unirArvores(Nodo maior, Nodo menor){
        Nodo raiz = new Nodo('*', maior.frequencia + menor.frequencia);
        raiz.esquerdo = maior;
        raiz.direito = menor;
        return raiz;
    }

    public String imprimeRaizes(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Nodo n : listaRaizes)
            sb.append(n.caracter).append("=").append(n.frequencia).append(", ");

        sb.delete(sb.length()-2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    /**
     * Caminhamento em largura, conhecido como Breadth First Traversal. Percorre a árvore imprimindo nível a nível.
     */
    public String imprimeArvore(Nodo raiz){
        StringBuilder sb = new StringBuilder();
        Queue<Nodo> fila = new LinkedList<Nodo>();
        fila.add(raiz);
        sb.append("{");

        while(!fila.isEmpty()){
            Nodo n = fila.remove();
            sb.append(n.caracter).append("=").append(n.frequencia).append(", ");
            if(n.esquerdo != null)
                fila.add(n.esquerdo);
            if(n.direito != null)
                fila.add(n.direito);
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("}");
        return sb.toString();
    }

    private void geraRaizes(String palavra){
        HashMap<Character, Integer> raizes = new HashMap<Character, Integer>();
        for (int i = 0; i < palavra.length(); i++) {
            char letra = palavra.charAt(i);
            if(raizes.containsKey(letra))
                raizes.put(letra, raizes.get(letra) + 1);
            else {
                raizes.put(letra, 1);
            }
        }
        listaRaizes = new ArrayList<Nodo>();
        for (char letra: raizes.keySet()) {
            listaRaizes.add(new Nodo(letra, raizes.get(letra)));
            insertionSortDesc(listaRaizes);
        }
    }

    private void insertionSortAsc(List<Nodo> lista) {
        int i;
        Nodo chave;

        for (int j = 1; j < lista.size(); j++) {
            chave = lista.get(j);
            i = j-1;
            while(i>=0 && lista.get(i).frequencia>chave.frequencia){
                lista.set(i+1,lista.get(i));
                i = i-1;
            }
            lista.set(i+1, chave);
        }
    }

    private List<Nodo> insertionSortDesc(List<Nodo> lista) {
        List<Nodo> listaOrdenada = lista;
        int i;
        Nodo nodo;

        for (int j = listaOrdenada.size()-2; j >= 0; j--) {
            nodo = listaOrdenada.get(j);
            i = j+1;
            while(i<listaOrdenada.size() && listaOrdenada.get(i).frequencia > nodo.frequencia){
                listaOrdenada.set(i-1,listaOrdenada.get(i));
                i = i+1;
            }
            listaOrdenada.set(i-1, nodo);
        }
        return listaOrdenada;
    }
}
