package modelodesktop.petmanager.view;

import modelodesktop.petmanager.controller.PetController;
import modelodesktop.petmanager.model.Pet;

import javax.swing.*;
import java.awt.*;

public class TelaCadastro extends JDialog {

    public TelaCadastro(TelaPrincipal telaPrincipal) {
        setTitle("Cadastrar Novo Pet");
        setModal(true);
        setLayout(new GridLayout(5, 2));

        JTextField txtNome = new JTextField();
        JTextField txtEspecie = new JTextField();
        JTextField txtDono = new JTextField();
        JTextField txtTelefone = new JTextField();

        add(new JLabel("Nome:")); add(txtNome);
        add(new JLabel("Espécie:")); add(txtEspecie);
        add(new JLabel("Dono:")); add(txtDono);
        add(new JLabel("Telefone:")); add(txtTelefone);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            PetController.salvarPet(new Pet(
                txtNome.getText(),
                txtEspecie.getText(),
                txtDono.getText(),
                txtTelefone.getText()
            ));
            telaPrincipal.atualizarTabela();
            dispose();
        });

        add(new JLabel());
        add(btnSalvar);

        setSize(300, 200);
        setLocationRelativeTo(telaPrincipal);
        setVisible(true);
    }
}
