import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Gson gson = new Gson();
        boolean exit = false;

        while (!exit) {
            try {
                // URL de la API
                URL url = new URL("https://v6.exchangerate-api.com/v6/a530f2088af2c6feec6e6d0f/latest/USD");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }

                ExchangeRateResponse response = gson.fromJson(new InputStreamReader(conn.getInputStream()), ExchangeRateResponse.class);

                if (response.getRates() == null) {
                    System.out.println("Rates map is null");
                    continue;
                }

                // Menú interactivo
                System.out.println("Seleccione la denominación inicial (ej. USD): ");
                String fromCurrency = scanner.nextLine().toUpperCase();
                System.out.println("Seleccione la denominación final (ej. EUR): ");
                String toCurrency = scanner.nextLine().toUpperCase();
                System.out.println("Ingrese el valor en " + fromCurrency + ": ");
                double amountInFromCurrency = Double.parseDouble(scanner.nextLine());

                Double rate = response.getRates().get(toCurrency);
                if (rate == null) {
                    System.out.println("No se encontró la tasa de cambio para " + toCurrency);
                } else {
                    double amountInToCurrency = amountInFromCurrency * rate;
                    System.out.println(amountInFromCurrency + " " + fromCurrency + " = " + amountInToCurrency + " " + toCurrency);
                }

                // Preguntar si desea realizar otra conversión o salir
                System.out.println("¿Desea realizar otra conversión? (s/n): ");
                String anotherConversion = scanner.nextLine().toLowerCase();
                if (!anotherConversion.equals("s")) {
                    exit = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        scanner.close();

    }
}