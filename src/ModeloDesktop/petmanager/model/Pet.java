package modelodesktop.petmanager.model;

public class Pet {
    public int id;
    public String nome;
    public String especie;
    public String dono;
    public String telefone;

    public Pet(int id, String nome, String especie, String dono, String telefone) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.dono = dono;
        this.telefone = telefone;
    }

    public Pet(String nome, String especie, String dono, String telefone) {
        this(0, nome, especie, dono, telefone);
    }
}
