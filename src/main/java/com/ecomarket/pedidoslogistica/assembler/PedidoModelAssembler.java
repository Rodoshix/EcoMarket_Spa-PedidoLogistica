package com.ecomarket.pedidoslogistica.assembler;

import com.ecomarket.pedidoslogistica.controller.PedidoControllerV2;
import com.ecomarket.pedidoslogistica.model.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoControllerV2.class).obtenerPedido(pedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoControllerV2.class).listarPedidos()).withRel("pedidos"));
    }
}