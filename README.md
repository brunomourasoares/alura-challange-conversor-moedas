# Conversor de Moedas - Terminal Java

Um conversor de moedas moderno, interativo e profissional para terminal, desenvolvido em Java, que consome a Exchange Rate API para realizar conversões entre diversas moedas do mundo. O sistema mantém um histórico das conversões realizadas, com data e hora, e permite ao usuário consultar esse histórico a qualquer momento, mesmo após fechar o programa.

## Funcionalidades
- Conversão entre várias moedas (USD, BRL, EUR, GBP, JPY, CHF, CAD, AUD, CNY, MXN, ARS, COP, entre outras)
- Histórico de conversões com data/hora, valores, moedas e taxa utilizada
- Histórico persistente: todas as conversões são salvas no arquivo `historico-conversoes.csv` na pasta `src/main/resources`
- Consulta ao histórico a qualquer momento
- Uso da Exchange Rate API para obter taxas de câmbio atualizadas
- Formatação monetária correta para cada moeda
- Registro de logs de conversão usando `java.time`
- Menu interativo e fácil de usar
- Configuração profissional da API key (variável de ambiente ou `application.properties`)

## Pré-requisitos
- Java 17 ou superior
- Maven (para build e dependências)
- Uma conta gratuita na [Exchange Rate API](https://www.exchangerate-api.com/) para obter sua API key

## Instalação
1. **Clone o repositório:**  
    HTTPS:
    ```sh
      git clone https://github.com/brunomourasoares/alura-challange-conversor-moedas.git
      cd alura-challange-conversor-moedas
    ```
    SSH:
    ```sh
      git clone git@github.com:brunomourasoares/alura-challange-conversor-moedas.git
      cd alura-challange-conversor-moedas
    ```
2. **Configure sua API key:**
   - Edite o arquivo `src/main/resources/application.properties` e coloque sua chave:
    ```properties
      exchange.rate.api.key=SUA_API_KEY_AQUI
    ```
3. **Compile o projeto:**
    ```sh
      mvn clean package
    ```

4. **Execute a aplicação:**
   - **Via Maven (recomendado):**
    ```sh
    mvn exec:java
    ```
    - **Via JAR compilado:**
    ```sh
      java -jar target/conversor-1.0-SNAPSHOT.jar
    ```
(Verifique o nome do arquivo `.jar` na pasta `target/` se for diferente)

## Como usar
- Escolha a opção desejada no menu:

  **1- Converter moedas:** Uma lista de moedas será exibida. Escolha a de origem e a de destino pelo número, digite o valor e veja o resultado formatado.  
  **2- Exibir histórico:** Visualize todas as conversões já realizadas, com data/hora, moedas, valores e taxa.  
  **3- Sair:** Encerra o programa.

## Histórico Persistente
- Todas as conversões são salvas automaticamente no arquivo `src/main/resources/historico-conversoes.csv`.
- O arquivo pode ser aberto em qualquer editor de texto ou planilha (Excel, LibreOffice, Google Sheets).
- O histórico exibido no menu é formatado para o padrão brasileiro.
- Exemplo de linha no arquivo:

```csv
  2025-10-17 10:30:00,USD,BRL,100.000000,512.340000,5.123400
```

## Observações
- Para adicionar mais moedas, basta incluir no mapa `moedasSuportadas` em `MenuConsole.java`.
- O projeto segue boas práticas de organização e uso de recursos modernos do Java.

## Licença
Este projeto é livre para uso acadêmico e pessoal.
