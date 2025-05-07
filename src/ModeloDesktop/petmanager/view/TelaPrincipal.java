package modelodesktop.petmanager.view;

import modelodesktop.petmanager.controller.PetController;
import modelodesktop.petmanager.model.Pet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaPrincipal extends JFrame {
    JTable tabela;

    public TelaPrincipal() {
        super("PetManager - Lista de Pets");
        setLayout(new BorderLayout());

        tabela = new JTable(new DefaultTableModel(new Object[]{"ID", "Nome", "Espécie", "Dono", "Telefone"}, 0));
        atualizarTabela();

        JButton btnNovo = new JButton("Novo Pet");
        btnNovo.addActionListener(e -> new TelaCadastro(this));

        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(btnNovo, BorderLayout.SOUTH);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);
        List<Pet> pets = PetController.listarPets();
        for (Pet p : pets) {
            model.addRow(new Object[]{p.id, p.nome, p.especie, p.dono, p.telefone});
        }
    }

    public static void main(String[] args) {
      
        new TelaPrincipal();
    }
}
