package br.univille.fsoweb20242;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.univille.fsoweb20242.entity.Cidade;
import br.univille.fsoweb20242.service.CidadeService;
import io.micrometer.observation.Observation.Context;

@Component
public class startaup {
  @Autowired
  private CidadeService serviceCidade;

  @EventListener
  public void onApplicationEvent(ContextRefreshedEvent event) {
    var cidade = new Cidade();
    serviceCidade.save(cidade);
  }
}
