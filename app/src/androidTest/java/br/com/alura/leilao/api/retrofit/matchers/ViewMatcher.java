package br.com.alura.leilao.api.retrofit.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.alura.leilao.R;
import br.com.alura.leilao.formatter.FormatadorDeMoeda;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

public class ViewMatcher {

    public static Matcher<? super View> apareceLeilao (final int posicaoEsperdada, final String descricaoEsperada, final double maiorLanceEsperado) {


        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            private final Matcher<View> displayed = isDisplayed();
            private final FormatadorDeMoeda formatador = new FormatadorDeMoeda();
            private final String formata = formatador.formata(maiorLanceEsperado);

            @Override
            public void describeTo(Description description) {
                description.appendText("View com descrição ").appendValue(descricaoEsperada)
                        .appendText(", maior lance ").appendValue(formata)
                        .appendText("na posição ").appendValue(posicaoEsperdada)
                        .appendText(" ");
                description.appendDescriptionOf(displayed);

            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                RecyclerView.ViewHolder viewHolderDevolvido = item.findViewHolderForAdapterPosition(posicaoEsperdada);
                if (viewHolderDevolvido == null ) {
                    throw new IndexOutOfBoundsException("View do ViewHolder na posição " +posicaoEsperdada+
                            " não foi encontrada");

                }

                View viewDoViewHolder = viewHolderDevolvido.itemView;
                boolean temDescricaoEsperada = apareceDescricaoEsperada(viewDoViewHolder);
                boolean temMaiorLanceEsperado = apareceMariorLanceEsperado(viewDoViewHolder);
                return temDescricaoEsperada && temMaiorLanceEsperado && displayed.matches(viewDoViewHolder);

            }

            private boolean apareceMariorLanceEsperado(View viewDoViewHolder) {
                TextView textViewMaiorLance = viewDoViewHolder.findViewById(R.id.item_leilao_maior_lance);

                return textViewMaiorLance.getText().toString()
                        .equals(formata) &&
                        displayed.matches(textViewMaiorLance);
            }

            private boolean apareceDescricaoEsperada(View viewDoViewHolder) {
                TextView textViewDescricao = viewDoViewHolder.findViewById(R.id.item_leilao_descricao);
                return textViewDescricao.getText()
                        .toString().equals(descricaoEsperada) &&
                        displayed.matches(textViewDescricao);
            }

        };

    }
}
