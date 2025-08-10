package com.petmanager.model;

public class CarrinhoItem {
    private final Object item;
    private int quantidade;
    private double precoUnitario;
    private double precoTotal;

    public CarrinhoItem(Object item) {
        this.item = item;
        this.quantidade = 1; // Todo item começa com quantidade 1
        if (item instanceof Produto) {
            this.precoUnitario = ((Produto) item).getPrecoVenda();
        } else if (item instanceof Servico) {
            this.precoUnitario = ((Servico) item).getPreco();
        }
        atualizarPrecoTotal();
    }

    public String getNome() {
        if (item instanceof Produto) {
            return ((Produto) item).getNomeProduto();
        } else if (item instanceof Servico) {
            return ((Servico) item).getNomeServico();
        }
        return "";
    }

    public void atualizarPrecoTotal() {
        this.precoTotal = this.precoUnitario * this.quantidade;
    }

    public Object getItem() {
        return item;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        atualizarPrecoTotal();
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }
}