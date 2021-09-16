package br.com.alura.leilao.api;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.api.retrofit.client.RespostaListener;
import br.com.alura.leilao.exception.LanceSeguidoDoMesmoUsuarioException;
import br.com.alura.leilao.exception.UsuarioJaDeuCincoLancesException;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.ui.dialog.AvisoDialogManager;

@RunWith(MockitoJUnitRunner.class)
public class EnviadorDeLanceTest extends TestCase {
    @Mock
    private Context context;
    @Mock
    private LeilaoWebClient client;
    @Mock
    private EnviadorDeLance.LanceProcessadoListener listener;
    @Mock
    private AvisoDialogManager manager;
    @Mock
    private Leilao leilao;

    @Test
    public void deve_MostrarMensagemDeFalha_quando_LanceMenorQueUltimoLance() {
        EnviadorDeLance enviador = new EnviadorDeLance(client, listener, context, manager);

        Leilao computador = new Leilao("Computador");
        computador.propoe(new Lance(new Usuario("Luiza"), 200));

        enviador.envia(computador, new Lance(new Usuario("Lola"), 100));

        verify(manager).mostraAvisoLanceMenorQueUltimoLance();
    }

    @Test
    public void deve_MostrarMensagemDeFalha_quando_UsuarioComCincoLancesDerNovoLance() {
        EnviadorDeLance enviador = new EnviadorDeLance(client, listener, context, manager);

        doThrow(UsuarioJaDeuCincoLancesException.class)
                .when(leilao).propoe(ArgumentMatchers.<Lance>any());

        enviador.envia(leilao, new Lance(new Usuario("Lola"), 200));

        verify(manager).mostraAvisoUsuarioJaDeuCincoLances();
    }

    @Test
    public void dee_MostrarMensagemFalha_quando_UsuarioDoUltimoLanceDerLance() {
        EnviadorDeLance enviador = new EnviadorDeLance(client, listener, context, manager);

        doThrow(LanceSeguidoDoMesmoUsuarioException.class)
                .when(leilao).propoe(ArgumentMatchers.<Lance>any());

        enviador.envia(leilao, new Lance(new Usuario("Luiza"), 200));

        verify(manager).mostraAvisoLanceSeguidoDoMesmoUsuario();
    }

    @Test
    public void dee_MostrarMensagemFalha_quando_FalharEnvioDeLanceParaApi() {
        EnviadorDeLance enviador = new EnviadorDeLance(client, listener, context, manager);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                RespostaListener<Void> argument = invocationOnMock.getArgument(2);
                argument.falha("");
                return null;
            }
        }).when(client).propoe(any(Lance.class), anyLong(), any(RespostaListener.class));

        enviador.envia(leilao, new Lance(new Usuario("Luiza"), 200));

        verify(manager).mostraToastFalhaNoEnvio();
    }
}