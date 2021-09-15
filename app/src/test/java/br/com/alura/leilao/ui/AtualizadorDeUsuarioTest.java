package br.com.alura.leilao.ui;

import static org.mockito.Mockito.*;

import android.support.v7.widget.RecyclerView;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.alura.leilao.database.dao.UsuarioDAO;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.ui.recyclerview.adapter.ListaUsuarioAdapter;

@RunWith(MockitoJUnitRunner.class)
public class AtualizadorDeUsuarioTest extends TestCase {
    @Mock
    private UsuarioDAO dao;
    @Mock
    private ListaUsuarioAdapter adapter;
    @Mock
    private RecyclerView recyclerView;

    @Test
    public void deve_AtualizarListaDeUsuario_quando__SalvarUsuario() {
        AtualizadorDeUsuario atualizador = new AtualizadorDeUsuario(dao, adapter, recyclerView);

        Usuario luiza = new Usuario("Luiza");
        when(dao.salva(luiza)).thenReturn(new Usuario(1, "Luiza"));
        when(adapter.getItemCount()).thenReturn(1);
        atualizador.salva(luiza);

        verify(dao).salva(luiza);

        verify(adapter).adiciona(new Usuario(1, "Luiza"));
        verify(recyclerView).smoothScrollToPosition(0);
    }
}