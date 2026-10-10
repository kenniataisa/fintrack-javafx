package controller;

import java.time.LocalDate;
import app.FinApp;
import exceptions.EntradaInvalidaException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Transacao;

public class NovaTransacaoController {

    @FXML
    private TextField campoDescricao;

    @FXML
    private TextField campoValor;

    @FXML
    private DatePicker campoData;

    @FXML
    private CheckBox checkReceita;

    @FXML
    private Label labelErro;

    @FXML
    private Button botaoCancelar;

    @FXML
    public void initialize() {
        campoData.setValue(LocalDate.now());
        botaoCancelar.setOnAction(evento -> FinApp.abrirTela("principal.fxml"));
    }

    @FXML
    private void salvarTransacao() {
        try {
            String descricao = campoDescricao.getText();
            double valor = Double.parseDouble(campoValor.getText().replace(",", "."));
            boolean ehReceita = checkReceita.isSelected();
            LocalDate data = campoData.getValue();

            Transacao novaTransacao = new Transacao(descricao, valor, ehReceita, data);
            FinApp.getFinTracker().adicionarTransacao(novaTransacao);

            FinApp.abrirTela("principal.fxml");
        } catch (NumberFormatException erro) {
            labelErro.setText("Digite apenas números no valor.");
        } catch (EntradaInvalidaException erro) {
            labelErro.setText(erro.getMessage());
        }
    }
}
