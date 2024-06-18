package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.NoSuchElementException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;


// Arquivo responsavel pelas principais funções de automação, utilizando Selenium sendo executado por JavascriptExecutor
// Selenium requer um grande conhecimento sobre extrutura HTML, e coisas como elementos, e o tempo de movimentação e visualização dos mesmos é excencial e caso apenas uma dessas peças falhe, o programa pode não funcionar, apenas pelo elemento não estar na tela.

// Feito pelo João Luz https://github.com/Superjoa10;

public class WebScraper {

    public static void mainQuery(String searchFor, int total, String location) {
        //Caminho para o Webdriver 
        System.setProperty("webdriver.chrome.driver", "C:/Users/Joao/Agenda 2023 - 1.4.6. Ver_C/_internal/config_files/chromedriver.exe");

        WebDriver driver = getDriver();
        driver.get("https://www.google.com.br/maps");

        try {
            // Abre localização primeiro para que fique 'visivel' para o navegador
            TimeUnit.SECONDS.sleep(3);
            WebElement searchBox = driver.findElement(By.id("searchboxinput"));
            searchBox.sendKeys(location);
            TimeUnit.SECONDS.sleep(2);
            driver.findElement(By.id("searchbox-searchbutton")).click();
            TimeUnit.SECONDS.sleep(15);
            driver.findElement(By.xpath("//*[@id=\"searchbox\"]/div[2]/button")).click();
            TimeUnit.SECONDS.sleep(5);
            
            // Pesquisa o negocio juntamente a localização solicitada
            searchBox = driver.findElement(By.id("searchboxinput"));
            searchBox.sendKeys(searchFor);
            driver.findElement(By.id("searchbox-searchbutton")).click();
            TimeUnit.SECONDS.sleep(10);
            

            // Lista de elementos/negocios
            List<WebElement> listElements = driver.findElements(By.className("hfpxzc"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            
            // Primeiro o codigo localiza todos os possiveis, ou o numero de elementos que voce pediu
            int previouslyCounted = 0;
            while (true) {
                js.executeScript("arguments[0].scrollIntoView(true);", listElements.get(listElements.size() - 1));
                TimeUnit.SECONDS.sleep(2);
                js.executeScript("window.scrollBy(0, 1200);");
                TimeUnit.SECONDS.sleep(5);
                
                listElements = driver.findElements(By.className("hfpxzc"));
                if (listElements.size() == previouslyCounted) {
                    js.executeScript("arguments[0].scrollIntoView(true);", listElements.get(listElements.size() - 1));
                    js.executeScript("window.scrollBy(0, 1200);");
                    TimeUnit.SECONDS.sleep(5);
                }
                
                // Finalmentes, verificaça caso chegou no numero pedido, no maximo de possiveis, ou somente reinicia a lista e volta para o loop
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                if (listElements.size() >= total) {
                    System.out.println("Total extraidos: " + listElements.size()+ ";"+ now.format(formatter));
                    break;
                } else if (listElements.size() == previouslyCounted) {
                    System.out.println("Chegou em todos possiveis\nTotal extraidos: " + listElements.size()+ ";"+ now.format(formatter));
                    break;
                } else {
                    previouslyCounted = listElements.size();
                    System.out.println("Atualmente extraiu: " + listElements.size() + ";"+ now.format(formatter));
                }
            }
            
            // Começa a abrir um por um de todos contados, e guarda as informações na classe de lista criado no arquivo condizente
            BusinessList businessList = new BusinessList();
            for (int x = 0; x < listElements.size(); x++) {
                TimeUnit.SECONDS.sleep(2);

                // As ações de scrollIntoView são indispensaveis para o funcionamento, pois caso o elemento não esteja em visão ele não roda de geito nenhum
                if (x == 0) {
                    js.executeScript("arguments[0].scrollIntoView(true);", listElements.get(x));
                    listElements.get(x).click();
                } else {
                    // Erro ??? diz não é usado, porque? 
                    WebElement previousElement = listElements.get(x - 1);
                    js.executeScript("arguments[0].scrollIntoView(true);", listElements.get(x));
                    js.executeScript("window.scrollBy(0, 190);");
                    js.executeScript("arguments[0].scrollIntoView(true);", listElements.get(x));
                    listElements.get(x).click();
                }

                TimeUnit.SECONDS.sleep(5);
                Business business = new Business();

                // Nome do negocio
                try {
                    WebElement nameElement = driver.findElement(By.xpath("//*[@id=\"QA0Szd\"]/div/div/div[1]/div[3]/div/div[1]/div/div/div[2]/div[2]/div/div[1]/div[1]/h1"));
                    business.setName(nameElement.getText());
                } catch (NoSuchElementException e) {
                    business.setName("");
                }

                // Endereço do negocio
                try {
                    WebElement addressElement = driver.findElement(By.xpath("//button[@data-item-id=\"address\"]//div[contains(@class, \"fontBodyMedium\")]"));
                    business.setAddress(addressElement.getText());
                } catch (NoSuchElementException e) {
                    business.setAddress("");
                }
                
                // Website do negocio
                try {
                    WebElement websiteElement = driver.findElement(By.xpath("//a[@data-item-id=\"authority\"]//div[contains(@class, \"fontBodyMedium\")]"));
                    business.setWebsite(websiteElement.getText());
                } catch (NoSuchElementException e) {
                    business.setWebsite("");
                }
                
                // Numero de telefone do negocio  
                try{
                    WebElement phoneNumberElement = driver.findElement(By.xpath("//button[contains(@data-item-id, \"phone:tel:\")]//div[contains(@class, \"fontBodyMedium\")]"));
                    business.setPhoneNumber(phoneNumberElement.getText());
                } catch (NoSuchElementException e) {
                    business.setPhoneNumber("");
                }

                businessList.addBusiness(business);
            }

            String updatedString = searchFor.replace(" ", "_");
            Scanner scanner = new Scanner(System.in);
            driver.quit();

            //Após o final da as opções de salvação dos dados tanto para excel quanto csv
            while (true) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                System.out.println("Save results to file? Options: Save_excel ; Save_csv ; Exit.\nFinished at " + now.format(formatter));
                String responseCommand = scanner.nextLine();

                if (responseCommand.equalsIgnoreCase("save_excel")) {
                    System.out.println("Saving to excel file...");
                    businessList.saveToExcel("maps_data_" + updatedString);
                    System.out.println("Results saved successfully.");
                } else if (responseCommand.equalsIgnoreCase("save_csv")) {
                    System.out.println("Saving to CSV file...");
                    businessList.saveToCSV("maps_data_" + updatedString);
                    System.out.println("Results saved successfully.");
                } else if (responseCommand.equalsIgnoreCase("exit")) {
                    break;
                }
                
            }
            scanner.close();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }

    private static WebDriver getDriver() {
        // Abre o Chrome WebDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }
}