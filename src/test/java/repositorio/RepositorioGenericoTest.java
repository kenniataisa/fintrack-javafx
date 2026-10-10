package repositorio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RepositorioGenericoTest {

    private RepositorioGenerico<String> repositorio;

    @BeforeEach
    public void preparar() {
        repositorio = new RepositorioGenerico<String>();
    }

    @Test
    public void deveAdicionarElementos() {
        repositorio.adicionar("Salario");
        repositorio.adicionar("Aluguel");
        assertEquals(2, repositorio.tamanho());
    }

    @Test
    public void deveRemoverElemento() {
        repositorio.adicionar("Salario");
        boolean removeu = repositorio.remover("Salario");
        assertTrue(removeu);
        assertEquals(0, repositorio.tamanho());
    }

    @Test
    public void naoDeveRemoverElementoInexistente() {
        assertFalse(repositorio.remover("Nada"));
    }

    @Test
    public void deveListarTodosOsElementos() {
        repositorio.adicionar("A");
        repositorio.adicionar("B");
        assertEquals(Arrays.asList("A", "B"), repositorio.listarTodos());
    }

    @Test
    public void deveAdicionarListaComCuringaExtends() {
        RepositorioGenerico<Number> numeros = new RepositorioGenerico<Number>();
        List<Integer> inteiros = Arrays.asList(1, 2, 3);
        numeros.adicionarTodos(inteiros);
        assertEquals(3, numeros.tamanho());
    }

    @Test
    public void deveCopiarParaListaComCuringaSuper() {
        repositorio.adicionar("A");
        repositorio.adicionar("B");
        List<Object> destino = new ArrayList<Object>();
        repositorio.copiarPara(destino);
        assertEquals(2, destino.size());
    }

    @Test
    public void deveContarElementosDeQualquerLista() {
        List<Double> valores = Arrays.asList(1.5, 2.5);
        assertEquals(2, RepositorioGenerico.contarElementos(valores));
    }
}
