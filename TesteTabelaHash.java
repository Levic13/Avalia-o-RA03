import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TesteTabelaHash {

    private static final long seed = 123456789L;

    private static Registro[] geraConjunto(int tamanho) {
        Random rand = new Random(seed);
        Registro[] registros = new Registro[tamanho];
        for (int i = 0; i < tamanho; i++) {
            registros[i] = new Registro(100000000 + rand.nextInt(900000000));
        }
        return registros;
    }

    private static void writeToCSV(String fileName, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] metodos = {"hashXOR", "multiplicacao", "resto"};
        int[] conjuntos = {20000, 100000, 500000, 1000000, 5000000};
        List<String> csvLines = new ArrayList<>();
        csvLines.add("Capacidade,Método,Conjunto,Colisões Inserção,Colisões Busca,Tempo de Inserção (ns),Tempo Médio de Busca (ns)");

        for (String metodo : metodos) {
            int[] capacidades;

            if (metodo.equals("hashXOR")) {
                capacidades = new int[]{5500000, 10000000, 20000000, 30000000, 40000000};
            } else if (metodo.equals("multiplicacao")) {
                capacidades = new int[]{5500000, 10000000, 20000000, 30000000, 40000000};
            } else if (metodo.equals("resto")) {
                capacidades = new int[]{5500000, 10000000, 20000000, 30000000, 40000000};
            } else {
                capacidades = new int[]{};
            }

            for (int capacidade : capacidades) {
                for (int conjuntoSize : conjuntos) {

                    TabelaHash tabela = new TabelaHash(capacidade, metodo);

                    Registro[] registros = geraConjunto(conjuntoSize);

                    long inicioInsercao = System.nanoTime();
                    for (Registro reg : registros) {
                        if (tabela.getTamanho() >= capacidade) break;
                        tabela.insere(reg);
                    }
                    long fimInsercao = System.nanoTime();

                    long tempoBuscaTotal = 0;
                    for (int i = 0; i < 5; i++) {
                        long inicioBusca = System.nanoTime();
                        for (Registro reg : registros) {
                            tabela.busca(reg.getChave());
                        }
                        long fimBusca = System.nanoTime();
                        tempoBuscaTotal += (fimBusca - inicioBusca);
                    }
                    long mediaBusca = tempoBuscaTotal / 5;

                    String linha = capacidade + "," + metodo + "," + conjuntoSize + "," + tabela.getColisoesInsercao() + "," + tabela.getColisoesBusca() + "," + (fimInsercao - inicioInsercao) + "," + mediaBusca;
                    csvLines.add(linha);

                    System.out.println("Capacidade: " + capacidade + ", Método: " + metodo + ", Conjunto: " + conjuntoSize);
                    System.out.println("Colisões Inserção: " + tabela.getColisoesInsercao());
                    System.out.println("Colisões Busca: " + tabela.getColisoesBusca());
                    System.out.println("Tempo Inserção: " + (fimInsercao - inicioInsercao) + " ns");
                    System.out.println("Tempo Médio Busca: " + mediaBusca + " ns");
                    System.out.println("--------------");
                }
            }
        }
        writeToCSV("resultados_hash.csv", csvLines);
    }
}
