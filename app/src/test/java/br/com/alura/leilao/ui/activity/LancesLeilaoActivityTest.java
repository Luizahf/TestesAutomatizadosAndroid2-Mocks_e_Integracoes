package br.com.alura.leilao.ui.activity;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.api.retrofit.client.RespostaListener;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.ui.recyclerview.adapter.ListaLeilaoAdapter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LancesLeilaoActivityTest {

    @Mock
    private Context context;
    @Spy
    private final ListaLeilaoAdapter adapter = new ListaLeilaoAdapter(context);

    @Mock
    private LeilaoWebClient client;

    @Test
    public void deve_AtualizarListaLeiloes_quando_BuscaLeiloalDaApi() throws InterruptedException {
        ListaLeilaoActivity activity = new ListaLeilaoActivity();
        Mockito.doNothing().when(adapter).atualizaLista();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) {
                RespostaListener<List<Leilao>> argument = invocationOnMock.getArgument(0);
                argument.sucesso(new ArrayList<>(Arrays.asList(
                        new Leilao("Computador"),
                        new Leilao("Carro")
                )));
                return null;
            }
        }).when(client).todos(ArgumentMatchers.<RespostaListener<List<Leilao>>>any());

        activity.buscaLeiloes(adapter, client);
        Thread.sleep(3000);
        int qtdLeiloes = adapter.getItemCount();
        assertThat(qtdLeiloes, is(2));
    }
}