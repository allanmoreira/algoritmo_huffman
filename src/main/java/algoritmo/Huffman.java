package algoritmo;

import java.io.*;
import java.util.*;

/**
 * Created by allanmoreira on 5/11/16.
 */
public class Huffman {

    private Nodo raiz;
    private String palavra;
    private int[] stringBinaria;
    private List<Nodo> listaRaizes;
    private HashMap<Character, String> codigos; // HashMap que sabe o código binário de cada caractere
    private HashMap<Character, Integer> frequencias; // HashMap que sabe a frequência de cada caractere
    private static String caminhoArquivoDeTeste = System.getProperty("user.dir")+File.separator+"teste_binario"+File.separator+"teste_arquivo";

    private class Nodo {
        Nodo direito;
        Nodo esquerdo;
        char caractere;
        int frequencia;

        public Nodo (char caractere, int frequencia) {
            this.caractere = caractere;
            this.frequencia = frequencia;
        }
    }

    public void comprime(String palavra) {
        this.palavra = palavra;
        frequenciaCaractere(palavra);
        geraRaizes();
        unirArvores();
        codificaCaractere();
        System.out.println(geraStringBinaria());
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
        Nodo raiz = new Nodo(' ', maior.frequencia + menor.frequencia);
        if(menor.caractere != ' ') {
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
     * Gera o código binário de cada caractere.
     * Percorre a ordem em pŕe ordem, e a cada vez que percorre o próximo nodo, adiciona 0 se for pra esquerda e 1 à direita.
     * Quando chega na folha, remove o último bit da String para poder prosseguir.
     */
    private void codificaCaractere(){
        codigos = new HashMap<Character, String>();
        codificaCaractere0(raiz, new StringBuilder());
    }

    private void codificaCaractere0(Nodo nodo, StringBuilder sb) {
        if(nodo != null){
            if(nodo.caractere != ' ') {
                codigos.put(nodo.caractere, sb.toString());
            }
            if(nodo.esquerdo != null) {
                codificaCaractere0(nodo.esquerdo, sb.append("0"));
                sb.setLength(sb.length()-1);
            }
            if(nodo.direito != null) {
                codificaCaractere0(nodo.direito, sb.append("1"));
                sb.setLength(sb.length()-1);
            }
        }

    }

    /**
     * Gera um array de inteiros que representa o código binário da árvore
     */
    private String geraStringBinaria(){
        // Gera um array com todos os caracteres da palavra inicial
        char[] chars = palavra.toCharArray();
        StringBuilder sb = new StringBuilder();

        // Para cada caractere, adiciona na StringBuilder o código em binário referente àquela caractere
        for (char c : chars)
            sb.append(codigos.get(c));

//        System.out.println(sb.toString());

        // Transforma a StringBuilder de binparios em um array de inteiros
        chars = sb.toString().toCharArray();
        stringBinaria = new int[sb.length()];
        for (int i = 0; i < chars.length; i++) {
            stringBinaria[i] = Integer.parseInt(String.valueOf(chars[i]));
        }
        return "Codificação da palavra: " + sb.toString();
    }

    public void decodificaCodigoBinario(byte[] byteArray){
        StringBuilder sb = new StringBuilder();
        String s = new String(byteArray);
        System.out.println(s);
        char[] chars = s.toCharArray();
        for (char b : chars) {
            sb.append(codigos.get(b));
        }
        System.out.println(sb.toString());
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
            nodo = "\""+n.frequencia+"\"";

            if(n.esquerdo != null) {
                if(n.esquerdo.caractere != ' ')
                    aux = "\""+n.esquerdo.caractere +","+n.esquerdo.frequencia+"\"\n";
                else
                    aux = "\""+n.esquerdo.frequencia+"\"\n";

                sb.append("\t"+nodo+"->"+aux);
                fila.add(n.esquerdo);
            }
            if(n.direito != null) {
                if(n.direito.caractere != ' ')
                    aux = "\""+n.direito.caractere +","+n.direito.frequencia+"\"\n";
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

<<<<<<< HEAD
    public void escreveArquivoBinario () {
        String caminhoArquivo = caminhoArquivoDeTeste;
=======
    private void escreveArquivoBinario (String caminhoArquivo) {
        String caminhoArquivoComtexto = caminhoArquivo+".txt";
>>>>>>> 094a7a7132d3406cbea2277314c975ecac1d1d71

        byte[] bytes = new byte[stringBinaria.length];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) stringBinaria[i];

        try {
<<<<<<< HEAD
            File file = new File(caminhoArquivo);
            FileOutputStream fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(bytes);
            fileOuputStream.close();

=======
            // Gera um novo arquivo, e escreve dentro dele a palavra e o código binário


            FileWriter fileWriter = new FileWriter(caminhoArquivoComtexto);
            fileWriter.write(palavra);
            fileWriter.close();

            FileOutputStream fileOuputStream = new FileOutputStream(caminhoArquivo);

            fileOuputStream.write(bytes);
            fileOuputStream.close();
            File file = new File(caminhoArquivoComtexto);
            file.delete();

            System.out.println("Bytes escritos no arquivo: " + Arrays.toString(bytes));
>>>>>>> 094a7a7132d3406cbea2277314c975ecac1d1d71
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    public byte[] leArquivoBinario () {
        String caminhoArquivo = caminhoArquivoDeTeste;
=======
    private void leArquivoBinario (String caminhoArquivo) {
        String caminhoArquivoComtexto = caminhoArquivo+".txt";
>>>>>>> 094a7a7132d3406cbea2277314c975ecac1d1d71
        FileInputStream fileInputStream=null;

        File file = new File(caminhoArquivo);

        byte[] bytes = new byte[(int) file.length()];

        try {
            fileInputStream = new FileInputStream(caminhoArquivo);
            fileInputStream.read(bytes);
            fileInputStream.close();

<<<<<<< HEAD
=======
            FileWriter fileWriter = new FileWriter(caminhoArquivoComtexto);
            fileWriter.write(palavra);
            fileWriter.close();

            System.out.println("Bytes lidos do arquivo:    " + Arrays.toString(bytes));

>>>>>>> 094a7a7132d3406cbea2277314c975ecac1d1d71
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}
