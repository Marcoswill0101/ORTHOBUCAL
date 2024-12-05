package br.univille.fsoweb20242.controller;

import java.nio.file.AccessDeniedException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.univille.fsoweb20242.entity.ItemPedido;
import br.univille.fsoweb20242.entity.Pedido;
import br.univille.fsoweb20242.service.PedidoService;
import br.univille.fsoweb20242.service.ProdutoService;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    @Autowired
    private PedidoService service;
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    //@PreAuthorize("hasAuthority('APPROLE_Admin')")
    public ModelAndView index(){
        var listaPedidos = service.getAll();
        var formatoData = new SimpleDateFormat("dd/MM/yyyy");

    List<Pedido> listaPedidosFormatados = listaPedidos.stream()
            .map(pedido -> {
                List<ItemPedido> itens = pedido.getItens();
                pedido.valor_total = itens.stream()
                .mapToDouble(item -> item.getQuantidade() * item.getValor())
                .sum();
                pedido.setDataFormatada(formatoData.format(pedido.getData())); // Atualiza o valor formatado
                return pedido;
            })
            .collect(Collectors.toList());
        
        return new ModelAndView("pedido/index",
                "listaPedidos",listaPedidosFormatados);
    }

    @GetMapping("/novo") 
    public ModelAndView novo(){
        var pedido = new Pedido();

        var listaProdutos = produtoService.getAll();

        HashMap<String,Object> dados = 
            new HashMap<>();
        dados.put("pedido",pedido);
        dados.put("novoItem", new ItemPedido());
        dados.put("listaProdutos",listaProdutos);
        
        return new ModelAndView("pedido/form", 
                    dados);

    }
    @PostMapping(params = "incitem")
    public ModelAndView incluirItem(Pedido pedido, 
                ItemPedido novoItem){
        pedido.getItens().add(novoItem);

        var listaProdutos = produtoService.getAll();
        HashMap<String,Object> dados = new HashMap<>();
        dados.put("pedido",pedido);
        dados.put("novoItem", new ItemPedido());
        dados.put("listaProdutos",listaProdutos);

        return new ModelAndView("pedido/form",dados);
    }

    @PostMapping(params = "save")
    public ModelAndView save(Pedido pedido){
        service.save(pedido);
        return new ModelAndView("redirect:/pedidos");
    }

    @GetMapping("/alterar/{id}")
    public ModelAndView alterar(@PathVariable("id") long id){
        var pedido = service.getById(id);

        var listaProdutos = produtoService.getAll();

        HashMap<String,Object> dados = 
            new HashMap<>();
        dados.put("pedido",pedido);
        dados.put("novoItem", new ItemPedido());
        dados.put("listaProdutos",listaProdutos);
        

        return new ModelAndView("pedido/form", dados);
    }
    @PostMapping(params = "removeitem")
    public ModelAndView removerItem(@RequestParam("removeitem") int index, 
                                Pedido pedido){
        pedido.getItens().remove(index);

        var listaProdutos = produtoService.getAll();

        HashMap<String,Object> dados = 
            new HashMap<>();
        dados.put("pedido",pedido);
        dados.put("novoItem", new ItemPedido());
        dados.put("listaProdutos",listaProdutos);

        return new ModelAndView("pedido/form",dados);
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") long id){
        var pedido = service.getById(id);
        if(pedido != null){
            service.delete(id);
        }
        return new ModelAndView("redirect:/pedidos");
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handle404Exception(AccessDeniedException ex) {
        return new ModelAndView("erro/403");
    }
}
