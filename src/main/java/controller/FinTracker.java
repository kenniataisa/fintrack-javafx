package controller;

import java.util.List;
import dao.TransacaoDAO;
import exceptions.EntradaInvalidaException;
import model.Transacao;
import repositorio.RepositorioGenerico;

public class FinTracker {

    private TransacaoDAO transacaoDAO;
    private RepositorioGenerico<Transacao> repositorio;

    public FinTracker(TransacaoDAO transacaoDAO) {
        this.transacaoDAO = transacaoDAO;
        this.repositorio = new RepositorioGenerico<Transacao>();
        this.transacaoDAO.criarTabela();
        this.repositorio.adicionarTodos(transacaoDAO.listarTodas());
    }

    public void adicionarTransacao(Transacao transacao) throws EntradaInvalidaException {
        if (transacao.getDescricao() == null || transacao.getDescricao().trim().isEmpty()) {
            throw new EntradaInvalidaException("Informe a descrição.");
        }
        if (transacao.getValor() <= 0) {
            throw new EntradaInvalidaException("O valor deve ser maior que zero.");
        }
        if (transacao.getData() == null) {
            throw new EntradaInvalidaException("Escolha uma data.");
        }
        boolean salvou = transacaoDAO.inserir(transacao);
        if (!salvou) {
            throw new EntradaInvalidaException("Não foi possível salvar. Tente novamente.");
        }
        repositorio.adicionar(transacao);
    }

    public void removerTransacao(Transacao transacao) throws EntradaInvalidaException {
        if (transacao == null) {
            throw new EntradaInvalidaException("Selecione um item para excluir.");
        }
        boolean removeu = transacaoDAO.remover(transacao.getId());
        if (!removeu) {
            throw new EntradaInvalidaException("Não foi possível excluir. Tente novamente.");
        }
        repositorio.remover(transacao);
    }

    public List<Transacao> listarTransacoes() {
        return repositorio.listarTodos();
    }

    public double calcularTotalReceitas() {
        double total = 0.0;
        for (Transacao transacao : repositorio.listarTodos()) {
            if (transacao.isEhReceita()) {
                total += transacao.getValor();
            }
        }
        return total;
    }

    public double calcularTotalDespesas() {
        double total = 0.0;
        for (Transacao transacao : repositorio.listarTodos()) {
            if (!transacao.isEhReceita()) {
                total += transacao.getValor();
            }
        }
        return total;
    }

    public double calcularSaldoTotal() {
        return calcularTotalReceitas() - calcularTotalDespesas();
    }

    public int getQuantidadeTransacoes() {
        return repositorio.tamanho();
    }
}
