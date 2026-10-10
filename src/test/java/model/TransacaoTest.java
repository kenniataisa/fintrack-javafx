package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransacaoTest {

    private Transacao receita;
    private Transacao despesa;

    @BeforeEach
    public void preparar() {
        receita = new Transacao("Salario", 2500.0, true, LocalDate.of(2025, 5, 10));
        despesa = new Transacao("Mercado", 300.5, false, LocalDate.of(2025, 5, 12));
    }

    @Test
    public void deveCriarTransacaoComOsDadosInformados() {
        assertEquals("Salario", receita.getDescricao());
        assertEquals(2500.0, receita.getValor());
        assertEquals(LocalDate.of(2025, 5, 10), receita.getData());
        assertTrue(receita.isEhReceita());
    }

    @Test
    public void deveRetornarTipoCorreto() {
        assertEquals("Receita", receita.getTipoDescricao());
        assertEquals("Despesa", despesa.getTipoDescricao());
        assertFalse(despesa.isEhReceita());
    }

    @Test
    public void deveRetornarTipoParaTela() {
        assertEquals("Entrada", receita.getTipoParaTela());
        assertEquals("Saída", despesa.getTipoParaTela());
    }

    @Test
    public void deveFormatarValor() {
        assertEquals("R$ 300,50", despesa.getValorFormatado().replace(".", ","));
    }

    @Test
    public void deveAlterarValorEDescricao() {
        despesa.setValor(100.0);
        despesa.setDescricao("Feira");
        assertEquals(100.0, despesa.getValor());
        assertEquals("Feira", despesa.getDescricao());
    }
}
