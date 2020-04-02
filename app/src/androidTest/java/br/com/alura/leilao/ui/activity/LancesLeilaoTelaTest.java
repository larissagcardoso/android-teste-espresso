package br.com.alura.leilao.ui.activity;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.R;
import br.com.alura.leilao.database.dao.UsuarioDAO;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class LancesLeilaoTelaTest extends BaseTesteIntegracao {
    UsuarioDAO dao = new UsuarioDAO(InstrumentationRegistry.getTargetContext());

    @Rule
    public ActivityTestRule<ListaLeilaoActivity> activity = new ActivityTestRule<>(ListaLeilaoActivity.class, true, false);

    @Before
    public void setUp() throws IOException {
        limpaBancoDeDadosDaApi();
        limpaBancoDeDadosInterno();
    }

    @Test
    public void deve_AtualizerLancesDosLeilao_QuandoReceberTresLances() throws IOException {

        //Salvar Leilao API e usuarios
        tentaSalvarLeiloesApi( new Leilao("Carro"));

       tentaSalvarUsuariosBancoDeDados( new Usuario("Larissa"),
                                        new Usuario("Wesley"));

        //Inicializar a main Activity
        activity.launchActivity(new Intent());

        //Clica no Leilao
       onView(withId(R.id.lista_leilao_recyclerview))
                .perform(actionOnItemAtPosition(0,click()));

        propoeNovoLance("200", 1, "Larissa");
        propoeNovoLance("300", 2, "Wesley");
        propoeNovoLance("400", 1, "Larissa");

        // Fazer assertion para as views de maior e menor lance, e também, para
        FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatadorDeMoeda.formata(400)),
                        isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance)).check(matches(allOf(withText(formatadorDeMoeda.formata(200)),
                isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatadorDeMoeda.formata(400) +" - (1) Larissa\n" +
                                formatadorDeMoeda.formata(300) +" - (2) Wesley\n"+
                                formatadorDeMoeda.formata(200) +" - (1) Larissa\n"),
                        isDisplayed())));
    }

    @Test
    public void deve_atualizarLancesDoLeilao_QuandoReceberUmLanceMuitoAlto() throws IOException {
        //Salvar Leilao API e usuarios
        tentaSalvarLeiloesApi( new Leilao("Carro"));

        tentaSalvarUsuariosBancoDeDados( new Usuario("Larissa"),
                new Usuario("Wesley"));

        //Inicializar a main Activity
        activity.launchActivity(new Intent());

        //Clica no Leilao
        onView(withId(R.id.lista_leilao_recyclerview))
                .perform(actionOnItemAtPosition(0,click()));

        propoeNovoLance("2000000000", 1, "Larissa");

        // Fazer assertion para as views de maior e menor lance, e também, para
        FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatadorDeMoeda.formata(2000000000)),
                        isDisplayed())));

        onView(withId(R.id.lances_leilao_menor_lance)).check(matches(allOf(withText(formatadorDeMoeda.formata(2000000000)),
                isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatadorDeMoeda.formata(2000000000) + " - (1) Larissa\n"),
                        isDisplayed())));
    }

    private void propoeNovoLance(String valorLance, int idUsuario, String nomeUsuario) {
        //Clica no fab lances leilão
        onView(allOf(withId(R.id.lances_leilao_fab_adiciona),
                isDisplayed()))
                .perform(click());

        //Verifica visibilidade do Dialog com o titulo
        onView(allOf(withText("Novo lance"),
                withId(R.id.alertTitle)))
                .check(matches(isDisplayed()));


        // Clicar no edittext de valor e preenche
        onView(allOf(withId(R.id.form_lance_valor_edittext),
                isDisplayed()))
                .perform(click(),
                        typeText(valorLance),
                        closeSoftKeyboard());

        // Seleciona o usuario
        onView(allOf(withId(R.id.form_lance_usuario), isDisplayed()))
                .perform(click());
        onData(is(new Usuario(idUsuario, nomeUsuario)))
                .inRoot(isPlatformPopup())
                .perform(click());

        //Clica no botao "Propor"
        onView(allOf(withText("Propor"), isDisplayed())).perform(click());
    }

    private void tentaSalvarUsuariosBancoDeDados( Usuario... usuarios) {

        for (Usuario usuario:usuarios) {
            Usuario usuarioSalvo = dao.salva(usuario);
            if (usuarioSalvo == null) {
                Assert.fail("Usuário" + usuario + "não foi salvo");
            }
        }
    }

    @Test
    public void deve_AtualizarLancesDoLeilao_QuandoReceberUmLance() throws IOException {
        //Salvar Leilao API
        tentaSalvarLeiloesApi( new Leilao("Carro"));

        //Inicializar a main Activity
        activity.launchActivity(new Intent());

        //Clica no Leilao
        onView(withId(R.id.lista_leilao_recyclerview))
            .perform(actionOnItemAtPosition(0,click()));

        //Clica no fab da tela de lances do leilao
        onView (allOf(withId(R.id.lances_leilao_fab_adiciona),
            isDisplayed()))
            .perform(click());

        //Verifica se aparece dialog de aviso por nao ter usuário com titulo com titulo e mensagem esperada
        onView(allOf(withText("Usuários não encontrados"),
                withId(R.id.alertTitle)))
                .check(matches(isDisplayed()));

        onView(allOf(withText("Não existe usuários cadastrados! Cadastre um usuário para propor o lance."),
                withId(android.R.id.message)))
                .check(matches(isDisplayed()));

        //CLica no botao Cadastrar Usuário
        onView(allOf(withText("Cadastrar usuário"),isDisplayed()))
                .perform(click());

        //CLica no fab tela de lista de usuários
        onView(
                allOf(withId(R.id.lista_usuario_fab_adiciona),
                 isDisplayed()))
                 .perform(click());

        //Clica no EditText e preenche com nome do usuário

        onView(
                allOf(withId(R.id.form_usuario_nome_edittext),
                        isDisplayed()))
                .perform(click(), typeText("Larissa"), closeSoftKeyboard());

        //Clica em Adicionar

        onView(
                allOf(withId(android.R.id.button1),
                        withText("Adicionar"),isDisplayed()))
                .perform(scrollTo(), click());

        onView(
                allOf(withId(R.id.item_usuario_id_com_nome),
                        isDisplayed()))
                .check(matches(withText("(1) Larissa")));

        //Clicar no Back do Android
        pressBack();

        //Verifica visibilidade do Dialog com o titulo
        propoeNovoLance("200", 1, "Larissa");

        // Fazer assertion para as views de maior e menor lance, e também, para
        FormatadorDeMoeda formatadorDeMoeda = new FormatadorDeMoeda();
        onView(withId(R.id.lances_leilao_maior_lance))
                .check(matches(allOf(withText(formatadorDeMoeda.formata(200)),
                        isDisplayed())));

        onView(withId(R.id.lances_leilao_maior_lance)).check(matches(allOf(withText(formatadorDeMoeda.formata(200)),
                isDisplayed())));

        onView(withId(R.id.lances_leilao_maiores_lances))
                .check(matches(allOf(withText(formatadorDeMoeda.formata(200) + " - (1) Larissa\n"),
                        isDisplayed())));

    }

    @After
    public void tearDown() throws IOException {
        limpaBancoDeDadosDaApi();
        limpaBancoDeDadosInterno();
    }
}
