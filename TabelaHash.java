// classe Registro para representar um elemento armazenado na tabela hash
class Registro {
    private int chave; // código de registro

    public Registro(int chave) {  // construtor
        this.chave = chave;
    }

    public int getChave() { // método getter para a chave
        return chave;
    }
}

// Implementação de tabela hash com rehashing para suportar 3 diferentes métodos de hash (resto da divisão, multiplicação e hashXOR)
// você vai implementar e analisar o desempenho de diferentes tabelas hash
public class TabelaHash {
    private static final Registro VAZIO = null; // constante para representar uma posição vazia na tabela hash, posição vazia é representada por null
    private final Registro[] tabela; // array que representa a tabela hash
    private final int capacidade; // capacidade da tabela
    private final String metodo; // método de hash utilizado
    private int colisoesInsercao;
    private int colisoesBusca;
    private int tamanho; // contador de registros inseridos na tabela

    // construtor
    public TabelaHash(int capacidade, String metodo) {
        this.capacidade = capacidade;
        this.tabela = new Registro[capacidade];
        this.metodo = metodo;
        this.colisoesInsercao = 0;
        this.colisoesBusca = 0;
        this.tamanho = 0;
    }

    // função de hash que decide qual método usar com base no valor do metodo, resto da divisão, multiplicação, dobramento
    private int h(int chave) {
        int index;
        switch (metodo) {
            case "resto": index = restoDivisao(chave); break;
            case "multiplicacao": index = multiplicacao(chave); break;
            case "hashXOR": index = hashXOR(chave); break;
            default: index = 0;
        }
        if (index < 0) {
            return 0;
        } else {
            return index % capacidade;
        }
        // garante que o índice esteja dentro dos limites da tabela
    }

    // função de rehashing
    private int r(int i) {
        return (i + 1) % capacidade;  // já estava garantindo limites, mas deixo aqui para clareza
    }

    // busca
    public int busca(int chave) {
        int i = h(chave);
        int tentativas = 0;  // adicionado para evitar loop infinito

        // procura até encontrar a chave ou um slot vazio
        while (tabela[i] != VAZIO && tabela[i].getChave() != chave && tentativas < capacidade) {
            i = r(i);
            colisoesBusca++;
            tentativas++;
        }

        if (tentativas == capacidade || tabela[i] == VAZIO) {
            return -1; // não encontrou
        } else {
            return i;  // encontrou na posição i
        }
    }

    // insere
    // código inspirado no do prof corrigido (mas é necessário tomar algum cuidado para que os algoritmos anteriores não entrem em loop. Por exemplo, quando um elemento for inserido em uma tabela que já está cheia. entao resolve esse problema utilizando-se um contador para o número de chaves inseridas.)
    public void insere(Registro registro) {
        if (tamanho == capacidade) {
            System.out.println("Tabela cheia.");
            return;
        }
        int i = h(registro.getChave());
        int tentativas = 0;  // adicionado para evitar loop infinito

        while (tabela[i] != VAZIO && tentativas < capacidade) {
            i = r(i);
            colisoesInsercao++;
            tentativas++;
        }

        if (tentativas == capacidade) {
            System.out.println("Não foi possível inserir. Muitas colisões.");
            return;
        }
        tabela[i] = registro;
        tamanho++;
    }

    // funcoes hash

    // resto da divisão
    private int restoDivisao(int chave) {
        return chave % capacidade;
    }

    // multiplicação
    private int multiplicacao(int chave) {
        double A = (Math.sqrt(5) - 1) / 2;  // Constante A
        return (int) (capacidade * ((chave * A) % 1));
    }

    private int hashXOR(int chave) {
        chave = ((chave >> 16) ^ chave) * 0x45d9f3b;
        chave = ((chave >> 16) ^ chave) * 0x45d9f3b;
        chave = (chave >> 16) ^ chave;
        return chave % capacidade;
    }


    // método getter para o número de colisões
    public int getColisoesInsercao() {
        return colisoesInsercao;
    }

    public int getColisoesBusca() {
        return colisoesBusca;
    }

    // método getter para o tamanho atual da tabela
    public int getTamanho() {
        return tamanho;
    }
}