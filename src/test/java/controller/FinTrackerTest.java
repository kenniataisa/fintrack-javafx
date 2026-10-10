package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import dao.TransacaoDAO;
import exceptions.EntradaInvalidaException;
import model.Transacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FinTrackerTest {

    private Connection conexao;
    private FinTracker finTracker;

    @BeforeEach
    public void preparar() throws SQLException {
        conexao = DriverManager.getConnection("jdbc:sqlite::memory:");
        finTracker = new FinTracker(new TransacaoDAO(conexao));
    }

    @AfterEach
    public void finalizar() throws SQLException {
        conexao.close();
    }

    @Test
    public void deveCalcularSaldo() throws EntradaInvalidaException {
        finTracker.adicionarTransacao(new Transacao("Salario", 2000.0, true, LocalDate.now()));
        finTracker.adicionarTransacao(new Transacao("Aluguel", 750.0, false, LocalDate.now()));
        finTracker.adicionarTransacao(new Transacao("Freelance", 500.0, true, LocalDate.now()));
        assertEquals(2500.0, finTracker.calcularTotalReceitas());
        assertEquals(750.0, finTracker.calcularTotalDespesas());
        assertEquals(1750.0, finTracker.calcularSaldoTotal());
    }

    @Test
    public void saldoDeveSerZeroSemTransacoes() {
        assertEquals(0.0, finTracker.calcularSaldoTotal());
    }

    @Test
    public void deveRemoverTransacaoEAtualizarSaldo() throws EntradaInvalidaException {
        Transacao despesa = new Transacao("Mercado", 200.0, false, LocalDate.now());
        finTracker.adicionarTransacao(new Transacao("Salario", 1000.0, true, LocalDate.now()));
        finTracker.adicionarTransacao(despesa);
        finTracker.removerTransacao(despesa);
        assertEquals(1, finTracker.getQuantidadeTransacoes());
        assertEquals(1000.0, finTracker.calcularSaldoTotal());
    }

    @Test
    public void deveLancarExcecaoComDescricaoVazia() {
        Transacao invalida = new Transacao("   ", 50.0, false, LocalDate.now());
        assertThrows(EntradaInvalidaException.class, () -> finTracker.adicionarTransacao(invalida));
    }

    @Test
    public void deveLancarExcecaoComValorNegativo() {
        Transacao invalida = new Transacao("Teste", -10.0, false, LocalDate.now());
        assertThrows(EntradaInvalidaException.class, () -> finTracker.adicionarTransacao(invalida));
    }

    @Test
    public void deveLancarExcecaoAoRemoverNulo() {
        assertThrows(EntradaInvalidaException.class, () -> finTracker.removerTransacao(null));
    }

    @Test
    public void deveCarregarTransacoesJaSalvasNoBanco() throws EntradaInvalidaException {
        finTracker.adicionarTransacao(new Transacao("Salario", 1000.0, true, LocalDate.now()));
        FinTracker outroFinTracker = new FinTracker(new TransacaoDAO(conexao));
        assertEquals(1, outroFinTracker.getQuantidadeTransacoes());
    }
}
