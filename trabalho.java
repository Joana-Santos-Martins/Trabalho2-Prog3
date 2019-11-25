import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

class trabalho{
    List<Docente> docentes = new ArrayList<Docente>();
    List<Veiculo> veiculos = new ArrayList<Veiculo>();
    List<Publicacao> publicacoes = new ArrayList<Publicacao>();
    Regra regra;
    List<Qualis> qualis = new ArrayList<Qualis>();

    public trabalho(){}

    public void carregaArquivoDocentes(File docentes) throws Exception{
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(docentes,"UTF-8");
        scanner.nextLine();
        while(scanner.hasNext()){
            scanner.useDelimiter(";|\n");
            Long codigo = scanner.nextLong();
            String nome = scanner.next();
            Date dataNascimento = formato.parse(scanner.next());
            Date dataIngresso = formato.parse(scanner.next());
            String coordenador = scanner.next();

            Docente docente = new Docente(codigo, nome, dataNascimento, dataIngresso, coordenador);
            this.docentes.add(docente);
        }
        scanner.close();
    }

    public void carregaArquivoVeiculos(File veiculos) throws Exception{
        Scanner scanner = new Scanner(veiculos, "UTF-8");
        scanner.nextLine();
        while(scanner.hasNext()){
            scanner.useDelimiter(";|\n");
            String sigla = scanner.next();
            String nome = scanner.next();
            String tipo = scanner.next();
            Float impacto = scanner.nextFloat();

            if(tipo.equals("P")){
                String ISSN = scanner.next();
                Periodico periodico = new Periodico(sigla, nome, tipo, impacto, ISSN);
                this.veiculos.add(periodico);
            }
            if(tipo.equals("C")){
                Conferencia conferencia = new Conferencia(sigla, nome, tipo, impacto);
                this.veiculos.add(conferencia);
            }

            if(scanner.hasNextLine()) scanner.nextLine();
            else break;
        }
        scanner.close();
    }

    public void carregaArquivoPublicacoes(File publicacoes) throws Exception{
        Scanner scanner = new Scanner(publicacoes, "UTF-8");
        scanner.nextLine();
        while(scanner.hasNext()){
            scanner.useDelimiter(";|\n");
            int ano = scanner.nextInt();
            String veiculo_ = scanner.next();
            String titulo = scanner.next();
            String[] docentes_ = scanner.next().split(",");                 
            int numero = scanner.nextInt();
            String volume_ = scanner.next();
            String local = scanner.next();
            int paginaInicial = scanner.nextInt();
            int paginaFinal = scanner.nextInt();
            
            List<Docente> docentes = new ArrayList<Docente>();
            for(String autor:docentes_){
                for(Docente docente:this.docentes){
                    if(String.valueOf(docente.get_codigo()).equals(autor)) docentes.add(docente);
                }
            }

            Publicacao publicacao = null;
            for(Veiculo v:this.veiculos){
                if(v.get_sigla().equals(veiculo_)){
                    if(v.tipo.equals("P")){
                        int volume = Integer.parseInt(volume_);
                        Periodico periodico = (Periodico)v;
                        periodico.set_volume(volume);
                        publicacao = new Publicacao(ano, periodico, titulo, docentes, numero, paginaInicial, paginaFinal);
                    }
                    if(v.get_tipo().equals("C")){
                        Conferencia conferencia = (Conferencia)v;
                        conferencia.set_locais(local);
                        conferencia.set_numeros(numero);
                        publicacao = new Publicacao(ano, conferencia, titulo, docentes, numero, paginaInicial, paginaFinal);
                    }
                }
            }

            for(Docente d:docentes) d.set_publicacoes(publicacao);
            if(this.publicacoes.isEmpty()) this.publicacoes.add(publicacao);
            else{
                int contagem = 0;
                for(Publicacao p:this.publicacoes){
                    if(p.get_titulo()!=titulo) contagem++;
                }
                if(contagem==this.publicacoes.size()) this.publicacoes.add(publicacao);
            }
            if(scanner.hasNextLine()) scanner.nextLine();
            else break;
        }
        scanner.close();
    }

    public void carregaArquivosQualis(File qualis) throws Exception{
        Scanner scanner = new Scanner(qualis, "UTF-8");
        scanner.nextLine();
        while(scanner.hasNext()){
            scanner.useDelimiter(";|\n");
            int ano = scanner.nextInt();
            String veiculo = scanner.next();
            String nota = scanner.next();
            Qualis q = new Qualis(ano, nota);
            q.calcula_pontuacao(this.regra);
            this.qualis.add(q);
            for(Veiculo v:this.veiculos){
                if(v.get_sigla().equals(veiculo)) v.set_qualis(q);
            }
            if(scanner.hasNextLine()) scanner.nextLine();
            else break;
        }
    }

    public void carregaArquivoRegras(File regras) throws Exception{
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Scanner scanner = new Scanner(regras, "UTF-8");
        scanner.nextLine();
        while(scanner.hasNext()){
            scanner.useDelimiter(";|\n");
            Date inicioVigencia = formato.parse(scanner.next());
            Date fimVigencia = formato.parse(scanner.next());
            String[] notas = scanner.next().split(",");
            String[] pontos = scanner.next().split(",");      
            Float multiplicador = scanner.nextFloat();
            int anos = scanner.nextInt();
            Float minimoPontos = scanner.nextFloat();
            Regra regra = new Regra(inicioVigencia, fimVigencia, notas, pontos, multiplicador, anos, minimoPontos);
            this.regra = regra;
            if(scanner.hasNextLine()) scanner.nextLine();
            else break;
        }
    }

	public void imprimeArquivoDocentes(FileWriter docentes) throws Exception{
        FileWriter fr = null;
        BufferedWriter bufferWriter = new BufferedWriter(docentes);
        for(Docente docente:this.docentes){
            bufferWriter.append(docente.get_codigo()+";");
            bufferWriter.append(docente.get_nome()+";");
            bufferWriter.append(docente.get_dataNascimento()+";");
            bufferWriter.append(docente.get_dataIngresso()+";");
            for(Publicacao publicacao:docente.publicacoes){
                bufferWriter.append(publicacao.get_titulo()+",");
            }
            bufferWriter.append(";");
            bufferWriter.append(docente.get_coordenador()+"\n");
        }
        bufferWriter.close();
    }

    public void imprimeArquivoPublicacoes(FileWriter publicacoes) throws Exception{
        this.publicacoes.sort(publicacao);
        FileWriter fr = null;
        BufferedWriter bufferWriter = new BufferedWriter(publicacoes);
        for(Publicacao publicacao:this.publicacoes){
            bufferWriter.append(publicacao.get_ano()+";");
            bufferWriter.append(publicacao.get_veiculo().get_sigla()+";");
            bufferWriter.append(publicacao.get_veiculo().get_nome()+";");
            bufferWriter.append(publicacao.get_veiculo().get_qualis().get_nota()+";");
            bufferWriter.append(publicacao.get_veiculo().get_impacto()+";");
            bufferWriter.append(publicacao.get_titulo()+";");
            int contagem = 1;
            for(Docente docente:publicacao.autores){
                bufferWriter.append(docente.get_nome());
                if(publicacao.autores.size() - contagem != 0){
                    bufferWriter.append(",");
                    contagem++;
                }
            }
            bufferWriter.append("\n");
        }
        bufferWriter.close();
    }

    Comparator<Publicacao> publicacao = new Comparator<Publicacao>(){
        public int compare(Publicacao p1, Publicacao p2){
            if(Integer.compare(p1.get_ano(), p2.get_ano())==0){
                if(p1.get_veiculo().compare(p2.get_veiculo())==0){
                    return p1.get_titulo().compareTo(p2.get_titulo());
                }
                return p1.get_veiculo().compare(p2.get_veiculo());
            }
            return Integer.compare(p2.get_ano(), p1.get_ano());
        }
    };

    public static void main(String argv[]) throws Exception {
        trabalho t = new trabalho();

        for(int i=0;i<argv.length;i++){
            if(argv[i].equals("-d")){
                File docentes = new File("docentes.csv");
                t.carregaArquivoDocentes(docentes);
            }
            if(argv[i].equals("-v")){
                File veiculos = new File("veiculos.csv");
                t.carregaArquivoVeiculos(veiculos);
            }
            if(argv[i].equals("-p")){
                File publicacoes = new File("publicacoes.csv");
                t.carregaArquivoPublicacoes(publicacoes);
            }
            if(argv[i].equals("-q")){
                File qualis = new File("qualis.csv");
                t.carregaArquivosQualis(qualis);
            }
            if(argv[i].equals("-r")){
                File regras = new File("regras.csv");
                t.carregaArquivoRegras(regras);
            }
        }
        //FileWriter docentes = new FileWriter("docentes.txt");
        FileWriter publicacoes = new FileWriter("publicacoes.txt");
        //t.imprimeArquivoDocentes(docentes);
        t.imprimeArquivoPublicacoes(publicacoes);
    }
}