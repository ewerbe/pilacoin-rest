package br.ufsm.poli.csi.tapw.pilacoin.server.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Bloco {

    @Id
    @GeneratedValue
    private Long id;
    private Long nonce;
    private byte[] hashBlocoAnterior;
    @OneToMany(mappedBy = "bloco")
    private List<Transasao> transacoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public byte[] getHashBlocoAnterior() {
        return hashBlocoAnterior;
    }

    public void setHashBlocoAnterior(byte[] hashBlocoAnterior) {
        this.hashBlocoAnterior = hashBlocoAnterior;
    }

    public List<Transasao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transasao> transacoes) {
        this.transacoes = transacoes;
    }
}
