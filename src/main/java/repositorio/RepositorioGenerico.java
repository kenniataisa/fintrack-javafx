package repositorio;

import java.util.ArrayList;
import java.util.List;

public class RepositorioGenerico<T> {

    private List<T> elementos = new ArrayList<T>();

    public void adicionar(T elemento) {
        elementos.add(elemento);
    }

    public boolean remover(T elemento) {
        return elementos.remove(elemento);
    }

    public List<T> listarTodos() {
        return new ArrayList<T>(elementos);
    }

    public int tamanho() {
        return elementos.size();
    }

    public void adicionarTodos(List<? extends T> novosElementos) {
        for (T elemento : novosElementos) {
            elementos.add(elemento);
        }
    }

    public void copiarPara(List<? super T> destino) {
        for (T elemento : elementos) {
            destino.add(elemento);
        }
    }

    public static int contarElementos(List<?> lista) {
        return lista.size();
    }

    public void limpar() {
        elementos.clear();
    }
}
