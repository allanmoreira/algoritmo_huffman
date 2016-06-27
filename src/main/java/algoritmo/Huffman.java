package algoritmo;

import java.io.*;
import java.util.*;

/**
 * Created by allanmoreira on 5/11/16.
 */
public class Huffman {

    private Nodo raiz;
    private String palavra;
    private int[] stringDeBits; // Representação da palavra em bits
    private List<Nodo> listaRaizes;
    private HashMap<Character, String> tabelaDeCodigosDosCaracteres; // HashMap que sabe o código binário de cada caractere
    private HashMap<String, Character> tabelaDeCaracteresDosCodigos; // HashMap que sabe o caractere de cada código binário
    private HashMap<Character, Integer> tabelaDeFrequencias; // HashMap que sabe a frequência de cada caractere
    private static String caminhoArquivoBinario = System.getProperty("user.dir")+File.separator+"teste_binario"+File.separator+"arquivo_binario";
    private static String caminhoArquivoGerarGraphviz = System.getProperty("user.dir")+File.separator+"teste_binario"+File.separator+"arquivo_graphviz.gv";

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

    /**
     * O(2n caracteres da palavra) + O(n par caractere/frequência)
     *
     */
    public void codifica(String palavra) {
        this.palavra = palavra;
        frequenciaCaractere(palavra); // O(n caracteres da palavra)
        geraRaizes();  // O(n [par caractere/frequência]) + O(n) do insertionSort
        unirArvores(); // O(n [par caractere/frequência]) + O(n) do insertionSort + adicionarRaizNaArvore constante
        codificaCaractere(); // O(n) para percorrer toda a árvore
        codificaPalavra(); // O(n caracteres da palavra) + O(n bits gerados pela codificação)
        escreveArquivoBinario(); // O(n bits gerados pela árvore)
    }

    /**
     * Verifica a frequência de cada caractere e insere o par caractere/frequencia em
     * um HashMap chamado tabelaDeFrequencias.
     *
     * Sua complexidade é N caracteres da palavra.
     */
    private void frequenciaCaractere(String palavra){
        tabelaDeFrequencias = new HashMap<Character, Integer>();
        for (int i = 0; i < palavra.length(); i++) {
            char letra = palavra.charAt(i);
            if(tabelaDeFrequencias.containsKey(letra))
                tabelaDeFrequencias.put(letra, tabelaDeFrequencias.get(letra) + 1);
            else {
                tabelaDeFrequencias.put(letra, 1);
            }
        }
    }

    /**
     * Pega cada par do HashMap @tabelaDeFrequencias e os insere em uma lista de raízes chamada @listaRaizes.
     * A cada nova raiz inserida na lista, utiliza o insertionSort para manter a lista ordenda em ordem decrescente.
     *
     * Sua complexidade é N par caractere/frequência + insertionSort
     */
    private void geraRaizes(){
        listaRaizes = new ArrayList<Nodo>();
        for (char letra: tabelaDeFrequencias.keySet()) {
            listaRaizes.add(new Nodo(letra, tabelaDeFrequencias.get(letra)));
            insertionSortDesc(listaRaizes);
        }
    }

    /**
     * Percorre a lista de raízes procurando as duas menores frequências e quando as encontra,
     * utiliza o método auxiliar adicionarRaizNaArvore que une a raiz à árvore final.
     * 
     * Sua complexidade é N caracteres da palavra que não se repetem + insertionSort
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
     *
     * Sua complexidade é constante.
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
     * Gera o código binário de cada caractere. Percorre a árvore em  pŕe ordem, e cada vez
     * que percorre o próximo nodo, adiciona 0 se for pra esquerda e 1 à direita.
     * Quando chega na folha, remove o último bit da String a cada retorno da recursão para poder prosseguir.
     *
     * Sua complexidade é a complexidade para percorrer toda a árvore, ou seja, N.
     */
    private void codificaCaractere(){
        tabelaDeCodigosDosCaracteres = new HashMap<Character, String>();
        tabelaDeCaracteresDosCodigos = new HashMap<String, Character>();
        codificaCaractere0(raiz, new StringBuilder());
    }

    private void codificaCaractere0(Nodo nodo, StringBuilder sb) {
        if(nodo != null){
            if(nodo.caractere != ' ') {
                tabelaDeCodigosDosCaracteres.put(nodo.caractere, sb.toString());
                tabelaDeCaracteresDosCodigos.put(sb.toString(), nodo.caractere);
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
     * Gera um vetor de inteiros que representa o codificação binária da palavra.
     *
     * Sua complexidade é N caracteres da palavra + N bits gerados pela codificação
     */
    private void codificaPalavra(){

        // Gera um Array com todos os caracteres da palavra inicial
        char[] chars = palavra.toCharArray();
        StringBuilder sb = new StringBuilder();

        // Para cada caractere, adiciona na StringBuilder o código em binário referente àquele caractere
        for (char c : chars)
            sb.append(tabelaDeCodigosDosCaracteres.get(c));

        // Transforma a StringBuilder de binários em um Array de inteiros
        chars = sb.toString().toCharArray();
        stringDeBits = new int[sb.length()];
        for (int i = 0; i < chars.length; i++) {
            stringDeBits[i] = Integer.parseInt(String.valueOf(chars[i]));
        }

        System.out.println("Codificação da palavra:   " + sb.toString());
    }

    /**
     * Transforma o vetor de inteiros que representam a codificação da palavra em um vetor de bytes e
     * gera um arquivo com essa codificação. 
     *
     * Sua complexidade é N bits gerados pela árvore.
     */
    private void escreveArquivoBinario () {
        String caminhoArquivo = caminhoArquivoBinario;

        byte[] bytes = new byte[stringDeBits.length];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) stringDeBits[i];

        try {
            File file = new File(caminhoArquivo);
            FileOutputStream fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(bytes);
            fileOuputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * InsertionSort percorrendo de forma decrescente. Como a lista sempre está ordenada, sua complexidade é O(n)
     */
    private void insertionSortDesc(List<Nodo> lista) {
        int i;
        Nodo nodo;

        for (int j = lista.size()-2; j >= 0; j--) {
            nodo = lista.get(j);
            i = j+1;
            while(i< lista.size() && lista.get(i).frequencia > nodo.frequencia){
                lista.set(i-1, lista.get(i));
                i = i+1;
            }
            lista.set(i-1, nodo);
        }
    }

    // ----------------------------------------------------------------------------------

    public void decodifica(){
        byte[] byteArray = leArquivoBinario(); // constante
        converteBitsParaInt(byteArray); // O(n bits do arquivo)
        decodificaCodigoBinario(); // O(n bits do arquivo)
    }

    /**
     * Lê o arquivo binário gerado e retorna um vetor com os bits do arquivo.
     * 
     * Sua complexidade é constante;
     */
    private byte[] leArquivoBinario () {
        String caminhoArquivo = caminhoArquivoBinario;
        FileInputStream fileInputStream = null;

        File file = new File(caminhoArquivo);

        byte[] bytes = new byte[(int) file.length()];

        try {
            fileInputStream = new FileInputStream(caminhoArquivo);
            fileInputStream.read(bytes);
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * Converte o vetor de bytes em um vetor de inteiros
     *
     * Sua complexidade é a quantidade N de bits no vetor.
     */
    private void converteBitsParaInt(byte[] byteArray){
        StringBuilder sb = new StringBuilder();
        stringDeBits = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            stringDeBits[i] = byteArray[i];
            sb.append(stringDeBits[i]);
        }
        System.out.println("Código do arquivo:        " + sb.toString());
    }

    /**
     * Lê um vetor de inteiros que representam os bits do arquivo um a um, procurando se existe um valor
     * igual na tabela tabelaDeCaracteresDosCodigos. Caso não exista, concatena o próximo bit até encontrar 
     * a codificação do caractere compatível. Encontrando, reinicia a ler o vetor da próxima posição
     * não concatenada como valor encontrado.
     *
     * Sua complexidade é a quantidade de bits que representam a palavra.
     */
    private void decodificaCodigoBinario() {
        StringBuilder sbAux = new StringBuilder();
        StringBuilder palavraDecodificada = new StringBuilder();

        int cont = 0;
        int contAux;
        boolean encontrou;

        while (cont < stringDeBits.length) {
            encontrou = false;
            contAux = cont;
            while (!encontrou) {
                sbAux.append(stringDeBits[contAux]);

                if (tabelaDeCaracteresDosCodigos.containsKey(sbAux.toString())) {
                    palavraDecodificada.append(String.valueOf(tabelaDeCaracteresDosCodigos.get(sbAux.toString())));
                    sbAux.setLength(0);
                    cont = contAux;
                    encontrou = true;
                }
                else {
                    contAux++;
                }
            }
            cont++;
        }

        System.out.println("Decodificação do arquivo: "+palavraDecodificada.toString());
    }

    /**
     * Percorre a árvore em largura, montando os dados do graphviz nível a nível.
     *
     * Complexidade O(n)
     */
    public void geraGraphviz(){
        StringBuilder sb = new StringBuilder();
        Queue<Nodo> fila = new LinkedList<Nodo>();
        fila.add(raiz);
        System.out.println("\nGraphviz:\n");
        sb.append("digraph G {\n");
        String nAtual;
        String aux;

        while(!fila.isEmpty()){
            Nodo n = fila.remove();
            nAtual = "\""+n.frequencia+"\"";

            if(n.esquerdo != null) {
                if(n.esquerdo.caractere != ' ')
                    aux = "\""+n.esquerdo.caractere +","+n.esquerdo.frequencia+"\"\n";
                else
                    aux = "\""+n.esquerdo.frequencia+"\"\n";

                sb.append("\t").append(nAtual).append("->").append(aux);

                fila.add(n.esquerdo);
            }
            if(n.direito != null) {
                if(n.direito.caractere != ' ')
                    aux = "\""+n.direito.caractere +","+n.direito.frequencia+"\"\n";
                else
                    aux = "\""+n.direito.frequencia+"\"\n";

                sb.append("\t"+nAtual+"->"+aux);

                fila.add(n.direito);
            }
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("\n}");

        try {
            FileWriter fileWriter = new FileWriter(caminhoArquivoGerarGraphviz);
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(sb.toString());
    }

}
