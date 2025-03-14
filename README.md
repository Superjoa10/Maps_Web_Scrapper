# Java Automation tool/chatbot Google maps Scrapper

This is a version of my google_map_query tool originely made with python, adapted to java for a college project. It implement the same functionality, using Selenium to query information like: Name, Address, Website & Phone number from businesses.
Original project:

https://github.com/Superjoa10/Google-Maps-Scraper-Bussiness-Client-Capture ;

It does this by opening chrome with selenium, this code uses a javascript emulator to access selenium. It takes the location and bussiness type you typed, it opens the location on Maps to better visualize the itens, then it scrolls the page rendering all the objects, then clicks and scrapes them one by one.

I implemented a higher level chatbot to the tool as requered by my project, with a list of FAQ, and used a technique called FuzzyScore to connect questions with the desired answer, doing so by assinging a score of likeness to each of them, determining which is closer to it.
Chosen becouse i used a similar one in python called FuzzyWuzzy on my "Mluz.db-project" repository, in there it is used to search for names with higher accuracy. 

https://github.com/Superjoa10/Mluz.db-projects ; 

I do not take any resposibility as to how this tool is used, it is mearly for educational pourposes.

                                                               
# Ferramenta de automação Java/chatbot Google maps Scrapper

Esta é uma versão da minha ferramenta google_map_query originalmente criada com python, adaptada em java para um projeto de faculdade. Ela implementa a mesma funcionalidade, usando Selenium para extrair informações como: Nome, endereço, site e número de telefone.
Tive problemas para executar o código do selenium diretamente, então usei um executor de javascript para executar o código do selenium. Implementei um chatbot de nível superior à ferramenta, conforme exigido pelo meu projeto, com uma lista FAQ (lista de perguntas frequentes), e usei uma técnica chamada FuzzyScore, escolhida porque usei uma técnica semelhante em python chamada FuzzyWuzzy no meu repositório "Mluz.db-project", na qual ela é usada para pesquisar nomes com maior precisão. 

https://github.com/Superjoa10/Mluz.db-projects ; 

Não me responsabilizo pela forma como essa ferramenta é usada, ela serve apenas para fins educacionais.
