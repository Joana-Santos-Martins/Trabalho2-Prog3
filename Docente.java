import java.util.*;

class Docente{
    Long codigo;
    String nome;
    Date dataNascimento;
    Date dataIngresso;
    Boolean coordenador;

    // Construtor
    public Docente(Long codigo, String nome, Date dataNascimento, Date dataIngresso, String coordenador){
        this.set_codigo(codigo);
        this.set_nome(nome);
        this.set_dataNascimento(dataNascimento);
        this.set_dataIngresso(dataIngresso);
        this.set_coordenador(coordenador);
    }

    // Sets
    public void set_codigo(Long codigo){
        this.codigo = codigo;
    }
    public void set_nome(String nome){
        this.nome = nome;
    }
    public void set_dataNascimento(Date dataNascimento){
        this.dataNascimento = dataNascimento;
    }
    public void set_dataIngresso(Date dataIngresso){
        this.dataIngresso = dataIngresso;
    }
    public void set_coordenador(String coordenador){
        if(coordenador.equals("X")) this.coordenador = true;
        else this.coordenador = false;
    }

    // Gets
    public Long get_codigo(){
        return this.codigo;
    }
    public String get_nome(){
        return this.nome;
    }
    public Date get_dataNascimento(){
        return this.dataNascimento;
    }
    public Date get_dataIngresso(){
        return this.dataIngresso;
    }
    public Boolean get_coordenador(){
        return this.coordenador;
    }
}