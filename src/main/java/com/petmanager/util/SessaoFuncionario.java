package com.petmanager.util;
import com.petmanager.model.Funcionario;

public class SessaoFuncionario {
    private static Funcionario funcionarioLogado;
    public static Funcionario getFuncionarioLogado() { return funcionarioLogado; }
    public static void setFuncionarioLogado(Funcionario f) { funcionarioLogado = f; }
    public static void limparSessao() { funcionarioLogado = null; }
}