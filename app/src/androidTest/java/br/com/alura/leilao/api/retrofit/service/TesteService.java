package br.com.alura.leilao.api.retrofit.service;

import java.util.List;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TesteService {

    @POST("leilao")
    Call<Leilao> salva(@Body Leilao leilao);

    @GET("reset")
    Call<Void> limpaBancoDeDados();
}