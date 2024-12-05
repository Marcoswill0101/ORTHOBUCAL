package br.univille.fsoweb20242;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.univille.fsoweb20242.entity.Cidade;
import br.univille.fsoweb20242.entity.Produto;
import br.univille.fsoweb20242.service.CidadeService;
import br.univille.fsoweb20242.service.ProdutoService;

@Component
public class Startup {

    @Autowired
    private CidadeService serviceCidade;
    @Autowired
    private ProdutoService serviceProduto;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event){

    }   
}
