package algoritmo;

import java.util.*;

/**
 * Created by allanmoreira on 5/11/16.
 */
public class Huffman {

    private Nodo raiz;
    private List<Nodo> listaRaizes;
    private HashMap<Character, String> codigos;
    private HashMap<Character, Integer> frequencias; // HashMap que sabe a frequência de cada caractere

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
        frequenciaCaractere(palavra);
        geraRaizes();
        unirArvores();
        geraStringBinario();
        System.out.println(geraGraphviz());
    }

    /**
     * Verifica a frequência de cada caractere e insere o par caractere/frequencia em um HashMap chamado frequencias
     */
    private void frequenciaCaractere(String palavra){
        frequencias = new HashMap<Character, Integer>();
        for (int i = 0; i < palavra.length(); i++) {
            char letra = palavra.charAt(i);
            if(frequencias.containsKey(letra))
                frequencias.put(letra, frequencias.get(letra) + 1);
            else {
                frequencias.put(letra, 1);
            }
        }
    }

    /**
     * Pega cada par do HashMap @frequencias e os insere em uma lista de raízes chamada @listaRaizes.
     *
     * A cada nova raiz inserida na lista, utiliza o insertionSort para manter a lista ordenda em ordem decrescente.
     */
    private void geraRaizes(){
        listaRaizes = new ArrayList<Nodo>();
        for (char letra: frequencias.keySet()) {
            listaRaizes.add(new Nodo(letra, frequencias.get(letra)));
            insertionSortDesc(listaRaizes);
        }
    }

    /**
     * Percorre a lista de raízes procurando as duas menores frequências e quando as encontra, utiliza o método auxiliar que une a raiz à árvore final.
     */
    private void unirArvores(){
        while(listaRaizes.size() > 1){
            Nodo menor1 = listaRaizes.remove(listaRaizes.size()-1);
            Nodo menor2 = listaRaizes.remove(listaRaizes.size()-1);
            raiz = adicionarRaizNaArvore(menor2, menor1);

            listaRaizes.add(raiz);
            insertionSortDesc(listaRaizes);
        }
    }

    /**
     * Une as duas árvores escolhidas como as menores, adicionando-as na árvore final.
     */
    private Nodo adicionarRaizNaArvore(Nodo maior, Nodo menor){
        Nodo raiz = new Nodo('*', maior.frequencia + menor.frequencia);
        if(menor.caracter != '*') {
            raiz.esquerdo = maior;
            raiz.direito = menor;
        }
        else {
            raiz.esquerdo = menor;
            raiz.direito = maior;
        }
        return raiz;
    }

    /**
     * Percorre a ordem em pŕe ordem, e a cada vez que percorre o próximo nodo, adiciona 0 se for pra esquerda e 1 à direita.
     * Quando chega na folha, remove o último bit da String para poder prosseguir.
     */
    private void geraStringBinario(){
        codigos = new HashMap<Character, String>();
        geraStringBinario0(raiz, new StringBuilder());
    }

    private void geraStringBinario0(Nodo nodo, StringBuilder sb) {
        if(nodo != null){
            if(nodo.caracter != '*') {
                codigos.put(nodo.caracter, sb.toString());
            }
            if(nodo.esquerdo != null) {
                geraStringBinario0(nodo.esquerdo, sb.append("0"));
                sb.setLength(sb.length()-1);
            }
            if(nodo.direito != null) {
                geraStringBinario0(nodo.direito, sb.append("1"));
                sb.setLength(sb.length()-1);
            }
        }

    }

    /**
     * InsertionSort percorrendo de forma decrescente
     */
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

    /**
     * Percorre a árvore em largura, montando os dados do graphviz nível a nível
     */
    public String geraGraphviz(){
        StringBuilder sb = new StringBuilder();
        Queue<Nodo> fila = new LinkedList<Nodo>();
        fila.add(raiz);
        sb.append("digraph G {\n");
        String nodo = null;
        String aux = null;

        while(!fila.isEmpty()){
            Nodo n = fila.remove();
            if(n.caracter != '*')
                nodo = "\""+n.caracter+","+n.frequencia+"\"";
            else
                nodo = "\""+n.frequencia+"\"";

            if(n.esquerdo != null) {
                if(n.esquerdo.caracter != '*')
                    aux = "\""+n.esquerdo.caracter+","+n.frequencia+"\"\n";
                else
                    aux = "\""+n.esquerdo.frequencia+"\"\n";

                sb.append("\t"+nodo+"->"+aux);
                fila.add(n.esquerdo);
            }
            if(n.direito != null) {
                if(n.direito.caracter != '*')
                    aux = "\""+n.direito.caracter+","+n.frequencia+"\"\n";
                else
                    aux = "\""+n.direito.frequencia+"\"\n";

                sb.append("\t"+nodo+"->"+aux);
                fila.add(n.direito);
            }
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("\n}");
        return sb.toString();
    }

    /*--------------------------AUXILIARES--------------------------------------------*/

    private void IMPRIME_ARVORE_TEST() {
        System.out.print("Raízes = ");
        imprimeRaizes();
        for (Nodo n : listaRaizes)
//            System.out.println(imprimeArvore(n));
            System.out.println("-------------------------------------------");
    }


    public void imprimeRaizes(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Nodo n : listaRaizes)
            sb.append(n.caracter).append("=").append(n.frequencia).append(", ");

        sb.delete(sb.length()-2, sb.length());
        sb.append("}");
        System.out.println(sb.toString());
    }
}
