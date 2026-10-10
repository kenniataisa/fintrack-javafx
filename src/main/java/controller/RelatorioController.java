package controller;

import app.FinApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utils.Formatador;

public class RelatorioController {

    @FXML
    private Label labelReceitas;

    @FXML
    private Label labelDespesas;

    @FXML
    private Label labelSaldo;

    @FXML
    private Label labelQuantidade;

    @FXML
    private Button botaoVoltar;

    @FXML
    public void initialize() {
        FinTracker finTracker = FinApp.getFinTracker();

        labelReceitas.setText(Formatador.formatarValorMonetario(finTracker.calcularTotalReceitas()));
        labelDespesas.setText(Formatador.formatarValorMonetario(finTracker.calcularTotalDespesas()));
        labelQuantidade.setText(String.valueOf(finTracker.getQuantidadeTransacoes()));

        double saldo = finTracker.calcularSaldoTotal();
        labelSaldo.setText(Formatador.formatarValorMonetario(saldo));
        if (saldo < 0) {
            labelSaldo.getStyleClass().add("saldo-negativo");
        } else {
            labelSaldo.getStyleClass().add("saldo-positivo");
        }

        botaoVoltar.setOnAction(evento -> FinApp.abrirTela("principal.fxml"));
    }
}
