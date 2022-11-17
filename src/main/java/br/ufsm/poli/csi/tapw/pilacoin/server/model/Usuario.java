package br.ufsm.poli.csi.tapw.pilacoin.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    //user pra cadastrar no server
    //chavePublica
    //id = null
    //nome:
    private String chavePublica;
    private Long id;
    private String nome;
}