package br.com.alura.leilao.ui.activity;


import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.R;
import br.com.alura.leilao.api.retrofit.client.TesteWebClient;
import br.com.alura.leilao.model.Leilao;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.alura.leilao.api.retrofit.matchers.ViewMatcher.apareceLeilao;


public class ListaLeilaoTelaTest extends BaseTesteIntegracao{

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setUp() throws IOException {
        limpaBancoDeDadosDaApi();
    }

    @Test
    public void deve_AparecerUmLeilao_QuandoCarregarUmLeilaoNaApi() throws IOException {

        tentaSalvarLeiloesApi(new Leilao("Carro"));
        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilao(0, "Carro", 0.00)));
    }


        @Test
    public void deve_AparecerDoisLeiloes_QuandoCarregarDoisLeiloesDaApi() throws IOException {

        tentaSalvarLeiloesApi(
                new Leilao("Carro"),
                new Leilao("Computador"));

        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilao(0, "Carro", 0.00)));

        onView(withId(R.id.lista_leilao_recyclerview))
                .check(matches(apareceLeilao(1, "Computador", 0.00)));
    }

    @Test
    public void deve_AparecerUltimoLeilao_QuandoCarregarDezLeiloesDaApi() throws IOException {

        tentaSalvarLeiloesApi(
                new Leilao("Carro"),
                new Leilao("Computador"),
                new Leilao("TV"),
                new Leilao("Notebook"),
                new Leilao("Console"),
                new Leilao("Jogo"),
                new Leilao("Estante"),
                new Leilao("Quadro"),
                new Leilao("Smartphone"),
                new Leilao("Casa"));

        activity.launchActivity(new Intent());

        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(RecyclerViewActions.scrollToPosition(9))
                .check(matches(apareceLeilao(9,"Casa",0.00)));
    }

    @After
    public void tearDown () throws IOException {
        limpaBancoDeDadosDaApi();
    }
}

