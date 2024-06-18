package com.example;

import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.text.similarity.FuzzyScore;

public class Chatbot {
    private static String userName;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        iniciarChatbot();
    }

    public static void iniciarChatbot() {

        // Pergunta o nome do usuário
        System.out.print("Seja bem-vindo à minha automação, Qual é o seu nome? ");
        userName = scanner.nextLine();

        retornarMenu();
    }
    

    private static void retornarMenu() {
        Scanner scanner = new Scanner(System.in);
        FuzzyScore fuzzyScore = new FuzzyScore(new Locale("pt", "BR"));


        // Exibe os comandos possíveis
        System.out.println("  _____  ____   ____   _____ _      ______   __  __          _____   _____    ____  _    _ ______ _______     __\n" +
                           " / ____|/ __ \\ / __ \\ / ____| |    |  ____| |  \\/  |   /\\   |  __ \\ / ____|  / __ \\| |  | |  ____|  __ \\ \\   / /\n" +
                           "| |  __| |  | | |  | | |  __| |    | |__    | \\  / |  /  \\  | |__) | (___   | |  | | |  | | |__  | |__) \\ \\_/ / \n" +
                           "| | |_ | |  | | |  | | | |_ | |    |  __|   | |\\/| | / /\\ \\ |  ___/ \\___ \\  | |  | | |  | |  __| |  _  / \\   /  \n" +
                           "| |__| | |__| | |__| | |__| | |____| |____  | |  | |/ ____ \\| |     ____) | | |__| | |__| | |____| | \\ \\  | |   \n" +
                           " \\_____|\\____/ \\____/ \\_____|______|______| |_|  |_/_/    \\_|_|    |_____/   \\___\\_\\____/ |______|_|  \\_\\ |_|   \n" +
                           "                                                                                                                 ");
    
        System.out.println("Olá " + userName + "! Comandos:\nquery_maps ; FAQ ; data ; ajuda ; sair... ou faça uma pergunta do FAQ.");
        System.out.println("Ferramenta feita pelo João. https://github.com/Superjoa10");

        // Lista de perguntas frequentes e suas respostas
        String[] perguntas = {"o que é o chatbot?",
        "posso obter a data e hora atuais?",
        "como posso buscar contatos de um tipo de negócio específico?",
        "como faço para buscar contatos em uma localização específica?",
        "posso buscar contatos de múltiplos tipos de negócios ao mesmo tempo?",
        "como posso salvar os contatos extraídos em um arquivo?",
        "posso buscar contatos de negócios em múltiplas localizações?",
        "como posso exportar os contatos extraídos para excel?",
        "como o chatbot extrai os contatos do google maps?",
        "como posso configurar o número máximo de contatos a serem extraídos?",
        "como faço para sair do chatbot?"};

        String[] respostas = {"Esse chatbot contem uma automação de extração de empresas utilizando o google maps para obter essas informações.",
        "digitando data, ele ira te informar a data e hora atual, ou conforme o codigo roda, ele ira atualizando o horario.",
        "Para buscar contatos de um tipo de negócio específico, insira o tipo de negócio desejado.",
        "Para buscar contatos em uma localização específica, insira o nome da cidade ou endereço.",
        "Para buscar múltiplos tipos de negócios, separe os tipos por vírgula ao fazer a busca.",
        "Para salvar os contatos extraídos em um arquivo, use a função de exportação disponível no final da automação, sendo disponivel CSV ou EXCEL.",
        "Não é recomendado, somente se os locais forem 'colado', caso queira basta inserira todas as localizações separadas por vírgula.",
        "Para exportar contatos para Excel, use a opção de exportação no formato .xlsx.",
        "O chatbot utiliza a biblioteca Selenium para automatizar a navegação no Google Maps. Ele realiza buscas específicas e extrai os dados dos negócios, como nome, endereço, site e telefone.",
        "Durante a execução do comando 'query_maps', você será solicitado a inserir o número de lugares para pesquisar. Basta digitar o número desejado e o chatbot irá extrair até esse limite, o limite maximo de contatos é 120",
        "Para sair do chatbot, digite 'sair'."};



        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("sair")) {
                System.out.println("Adeus " + userName + "!");
                break;

            } else if (input.equals("faq")) {
                System.out.println("Perguntas Frequentes:");
                for (String pergunta : perguntas) {
                    System.out.println("- " + pergunta);
                }
            
            } else if (input.equals("query_maps")) {
                // Chamada da classe WebScraper e principal codigo.
                System.out.print("Digite o nome para pesquisar: ");
                String name = scanner.nextLine();

                System.out.print("Digite a localização para pesquisar: ");
                String location = scanner.nextLine();

                System.out.print("Digite o número de lugares para pesquisar: ");
                int total = Integer.parseInt(scanner.nextLine());

                String searchFor = name + " " + location;
                WebScraper.mainQuery(searchFor, total, location);

            } else if (input.equals("data")) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                System.out.println("Data e hora atuais: " + now.format(formatter));

            } else if (input.equals("ajuda")) {
                System.out.println("");

            } else {
                // Verifica se a entrada corresponde a alguma pergunta frequente.
                // Utiliza lib Apache FuzzyScore, Escolhida pela semelhança a Lib FuzzyWuzzy do Python, que utilizei em um projeto.
                boolean encontrouPergunta = false;
                int highestScore = -1;
                int bestMatchIndex = -1;

                for (int i = 0; i < perguntas.length; i++) {
                    int score = fuzzyScore.fuzzyScore(input, perguntas[i]);
                    if (score > highestScore) {
                        highestScore = score;
                        bestMatchIndex = i;
                    }
                }

                if (highestScore > 0) { // Definição de limite de pontuação/semelhança.
                    System.out.println(respostas[bestMatchIndex]);
                    encontrouPergunta = true;
                }

                if (!encontrouPergunta) {
                    System.out.println("Não entendi, por favor refaça a pergunta ou cheque se utilizou o comando certo.\ncaso haja alguma duvida use o comando AJUDA ou FAQ para ver as perguntas frequentes.");
                }
        }

        
    }
    scanner.close();
} 
}

    
