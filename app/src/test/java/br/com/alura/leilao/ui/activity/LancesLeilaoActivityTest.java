package br.com.alura.leilao.ui.activity;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.ui.recyclerview.adapter.ListaLeilaoAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LancesLeilaoActivityTest {

    @Test
    public void deve_AtualizarListaLeiloes_quando_ReceberListaLeiloes() {
        ListaLeilaoAdapter adapter = new ListaLeilaoAdapter(null);
        adapter.atualiza(new ArrayList<Leilao>(Arrays.asList(
                new Leilao("Console"),
                new Leilao("Computador")
        )));

        int quantidadeLeiloesDevolvida = adapter.getItemCount();
        assertThat(quantidadeLeiloesDevolvida, is(2));
    }
}