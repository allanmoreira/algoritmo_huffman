package algoritmo;

/**
 * Created by allanmoreira on 5/11/16.
 */
public class Arvore {

    private Nodo raiz;
    private int contador;

    private class Nodo {
        Nodo direito;
        Nodo esquerdo;
        int chave;

        public Nodo (int chave) {
            this.chave = chave;
        }
    }

    public void inserir(int chave){
        raiz = inserir0(raiz, chave);
    }

    private Nodo inserir0(Nodo nodo, int chave) {
        if(nodo == null) {
            contador++;
            return new Nodo(chave);
        }
        if(chave > nodo.chave)
            nodo.direito = inserir0(nodo.direito, chave);
        else if(chave < nodo.chave)
            nodo.esquerdo = inserir0(nodo.esquerdo, chave);
        else
            throw new IllegalArgumentException("Chave duplicada!");
        return nodo;
    }

    public int tamanho(){
        return contador;
    }

    public int altura(){
        return altura0(raiz);
    }

    private int altura0(Nodo nodo) {
        if(nodo == null)
            return -1;
        int altDir = 1 + altura0(nodo.direito);
        int altEsq = 1 + altura0(nodo.esquerdo);

        return Math.max(altDir, altEsq);
    }
}
