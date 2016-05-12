package algoritmo;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by allanmoreira on 5/11/16.
 */
public class ArvoreTest {
    Arvore arvore;

    @org.junit.Before
    public void setUp() throws Exception {
        arvore = new Arvore();
        arvore.inserir(10);
        arvore.inserir(20);
        arvore.inserir(30);
    }

    @org.junit.Test
    public void testTamanho() throws Exception {
        assertEquals(arvore.tamanho(), 3);
    }

    @org.junit.Test
    public void testAltura() throws Exception {
        assertEquals(arvore.altura(), 2);
    }
}