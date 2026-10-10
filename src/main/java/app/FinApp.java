package app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import controller.FinTracker;
import dao.Conexao;
import dao.TransacaoDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinApp extends Application {

    private static Stage janela;
    private static FinTracker finTracker;

    @Override
    public void start(Stage stage) throws SQLException {
        janela = stage;

        Connection conexao = Conexao.getConexao();
        TransacaoDAO transacaoDAO = new TransacaoDAO(conexao);
        finTracker = new FinTracker(transacaoDAO);

        stage.setTitle("FinTrack - Minhas finanças");
        abrirTela("principal.fxml");
        stage.show();
    }

    public static void abrirTela(String nomeArquivo) {
        try {
            FXMLLoader carregador = new FXMLLoader(FinApp.class.getResource("/view/" + nomeArquivo));
            Parent raiz = carregador.load();
            Scene cena = new Scene(raiz, 720, 480);
            cena.getStylesheets().add(FinApp.class.getResource("/css/estilo.css").toExternalForm());
            janela.setScene(cena);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir a tela " + nomeArquivo + ": " + erro.getMessage());
        }
    }

    public static FinTracker getFinTracker() {
        return finTracker;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
