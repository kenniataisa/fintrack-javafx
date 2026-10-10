package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Transacao;

public class TransacaoDAO {

    private static final Logger LOG = Logger.getLogger(TransacaoDAO.class.getName());

    private Connection conexao;

    public TransacaoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS transacoes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "descricao VARCHAR(100), "
                + "valor DECIMAL(10,2), "
                + "tipo VARCHAR(10), "
                + "data DATE)";
        try (Statement comando = conexao.createStatement()) {
            comando.execute(sql);
        } catch (SQLException erro) {
            LOG.log(Level.SEVERE, "Erro ao criar a tabela transacoes", erro);
        }
    }

    public boolean inserir(Transacao transacao) {
        String sql = "INSERT INTO transacoes (descricao, valor, tipo, data) VALUES (?, ?, ?, ?)";
        try {
            conexao.setAutoCommit(false);
            try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                comando.setString(1, transacao.getDescricao());
                comando.setDouble(2, transacao.getValor());
                comando.setString(3, transacao.getTipoDescricao());
                comando.setString(4, transacao.getData().toString());
                comando.executeUpdate();
            }
            try (Statement consulta = conexao.createStatement();
                 ResultSet chaves = consulta.executeQuery("SELECT last_insert_rowid()")) {
                if (chaves.next()) {
                    transacao.setId(chaves.getInt(1));
                }
            }
            conexao.commit();
            conexao.setAutoCommit(true);
            return true;
        } catch (SQLException erro) {
            LOG.log(Level.SEVERE, "Erro ao inserir transacao", erro);
            try {
                conexao.rollback();
                conexao.setAutoCommit(true);
            } catch (SQLException erroRollback) {
                LOG.log(Level.SEVERE, "Erro ao desfazer a insercao", erroRollback);
            }
            return false;
        }
    }

    public List<Transacao> listarTodas() {
        List<Transacao> lista = new ArrayList<Transacao>();
        String sql = "SELECT id, descricao, valor, tipo, data FROM transacoes ORDER BY id";
        try (PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                boolean ehReceita = resultado.getString("tipo").equals("Receita");
                LocalDate data = LocalDate.parse(resultado.getString("data"));
                Transacao transacao = new Transacao(
                        resultado.getString("descricao"),
                        resultado.getDouble("valor"),
                        ehReceita,
                        data);
                transacao.setId(resultado.getInt("id"));
                lista.add(transacao);
            }
        } catch (SQLException erro) {
            LOG.log(Level.SEVERE, "Erro ao listar transacoes", erro);
        }
        return lista;
    }

    public Transacao buscarPorId(int id) {
        String sql = "SELECT id, descricao, valor, tipo, data FROM transacoes WHERE id = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            ResultSet resultado = comando.executeQuery();
            if (resultado.next()) {
                boolean ehReceita = resultado.getString("tipo").equals("Receita");
                LocalDate data = LocalDate.parse(resultado.getString("data"));
                Transacao transacao = new Transacao(
                        resultado.getString("descricao"),
                        resultado.getDouble("valor"),
                        ehReceita,
                        data);
                transacao.setId(resultado.getInt("id"));
                return transacao;
            }
        } catch (SQLException erro) {
            LOG.log(Level.SEVERE, "Erro ao buscar transacao", erro);
        }
        return null;
    }

    public boolean atualizar(Transacao transacao) {
        String sql = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, data = ? WHERE id = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, transacao.getDescricao());
            comando.setDouble(2, transacao.getValor());
            comando.setString(3, transacao.getTipoDescricao());
            comando.setString(4, transacao.getData().toString());
            comando.setInt(5, transacao.getId());
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            LOG.log(Level.SEVERE, "Erro ao atualizar transacao", erro);
            return false;
        }
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM transacoes WHERE id = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            LOG.log(Level.SEVERE, "Erro ao remover transacao", erro);
            return false;
        }
    }
}
