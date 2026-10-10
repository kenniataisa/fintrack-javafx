package controller;

import java.time.LocalDate;
import java.util.Optional;
import app.FinApp;
import exceptions.EntradaInvalidaException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Transacao;

public class PrincipalController {

    @FXML
    private TableView<Transacao> tabelaTransacoes;

    @FXML
    private TableColumn<Transacao, LocalDate> colunaData;

    @FXML
    private TableColumn<Transacao, String> colunaDescricao;

    @FXML
    private TableColumn<Transacao, String> colunaValor;

    @FXML
    private TableColumn<Transacao, String> colunaTipo;

    @FXML
    public void initialize() {
        colunaData.setCellValueFactory(new PropertyValueFactory<Transacao, LocalDate>("data"));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<Transacao, String>("descricao"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<Transacao, String>("valorFormatado"));
        colunaTipo.setCellValueFactory(new PropertyValueFactory<Transacao, String>("tipoParaTela"));

        atualizarTabela();
    }

    private void atualizarTabela() {
        tabelaTransacoes.setItems(FXCollections.observableArrayList(FinApp.getFinTracker().listarTransacoes()));
    }

    @FXML
    private void abrirNovaTransacao() {
        FinApp.abrirTela("nova-transacao.fxml");
    }

    @FXML
    private void abrirRelatorio() {
        FinApp.abrirTela("relatorio.fxml");
    }

    @FXML
    private void removerTransacao() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            Alert aviso = new Alert(Alert.AlertType.WARNING, "Selecione um item da lista primeiro.");
            aviso.showAndWait();
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja excluir \"" + selecionada.getDescricao() + "\"?");
        Optional<ButtonType> resposta = confirmacao.showAndWait();

        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            try {
                FinApp.getFinTracker().removerTransacao(selecionada);
                atualizarTabela();
            } catch (EntradaInvalidaException erro) {
                Alert alertaErro = new Alert(Alert.AlertType.ERROR, erro.getMessage());
                alertaErro.showAndWait();
            }
        }
    }
}
