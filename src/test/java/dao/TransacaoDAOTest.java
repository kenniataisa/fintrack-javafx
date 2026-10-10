package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import model.Transacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransacaoDAOTest {

    private Connection conexao;
    private TransacaoDAO transacaoDAO;

    @BeforeEach
    public void preparar() throws SQLException {
        conexao = DriverManager.getConnection("jdbc:sqlite::memory:");
        transacaoDAO = new TransacaoDAO(conexao);
        transacaoDAO.criarTabela();
    }

    @AfterEach
    public void finalizar() throws SQLException {
        conexao.close();
    }

    @Test
    public void deveInserirTransacaoEGerarId() {
        Transacao transacao = new Transacao("Salario", 3000.0, true, LocalDate.of(2025, 6, 1));
        boolean inseriu = transacaoDAO.inserir(transacao);
        assertTrue(inseriu);
        assertTrue(transacao.getId() > 0);
    }

    @Test
    public void deveListarTransacoesInseridas() {
        transacaoDAO.inserir(new Transacao("Salario", 3000.0, true, LocalDate.of(2025, 6, 1)));
        transacaoDAO.inserir(new Transacao("Aluguel", 900.0, false, LocalDate.of(2025, 6, 5)));
        List<Transacao> lista = transacaoDAO.listarTodas();
        assertEquals(2, lista.size());
        assertEquals("Salario", lista.get(0).getDescricao());
        assertEquals(900.0, lista.get(1).getValor());
        assertFalse(lista.get(1).isEhReceita());
    }

    @Test
    public void deveBuscarTransacaoPorId() {
        Transacao transacao = new Transacao("Mercado", 250.0, false, LocalDate.of(2025, 6, 3));
        transacaoDAO.inserir(transacao);
        Transacao encontrada = transacaoDAO.buscarPorId(transacao.getId());
        assertNotNull(encontrada);
        assertEquals("Mercado", encontrada.getDescricao());
        assertEquals(LocalDate.of(2025, 6, 3), encontrada.getData());
    }

    @Test
    public void deveRetornarNuloQuandoIdNaoExiste() {
        assertNull(transacaoDAO.buscarPorId(999));
    }

    @Test
    public void deveAtualizarTransacao() {
        Transacao transacao = new Transacao("Internet", 100.0, false, LocalDate.of(2025, 6, 10));
        transacaoDAO.inserir(transacao);
        transacao.setValor(120.0);
        transacao.setDescricao("Internet fibra");
        assertTrue(transacaoDAO.atualizar(transacao));
        Transacao atualizada = transacaoDAO.buscarPorId(transacao.getId());
        assertEquals(120.0, atualizada.getValor());
        assertEquals("Internet fibra", atualizada.getDescricao());
    }

    @Test
    public void deveRemoverTransacao() {
        Transacao transacao = new Transacao("Cinema", 40.0, false, LocalDate.of(2025, 6, 15));
        transacaoDAO.inserir(transacao);
        assertTrue(transacaoDAO.remover(transacao.getId()));
        assertEquals(0, transacaoDAO.listarTodas().size());
    }

    @Test
    public void naoDeveRemoverIdInexistente() {
        assertFalse(transacaoDAO.remover(999));
    }
}
