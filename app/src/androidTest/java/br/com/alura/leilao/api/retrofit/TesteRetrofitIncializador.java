package br.com.alura.leilao.api.retrofit;


import br.com.alura.leilao.api.retrofit.service.TesteService;

public class TesteRetrofitIncializador extends RetrofitInicializador {
    public TesteService getTesteService() {
        return retrofit.create(TesteService.class);
    }
}
