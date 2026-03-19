import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class LanguageTranslator {

    // Function to decode Unicode (uXXXX to actual text)
    public static String decodeUnicode(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\\' && i + 5 < text.length() && text.charAt(i + 1) == 'u') {
                String hex = text.substring(i + 2, i + 6);
                sb.append((char) Integer.parseInt(hex, 16));
                i += 5;
            } else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Step 1: Input text
            System.out.print("Enter text: ");
            String text = sc.nextLine();

            // Step 2: Source language
            System.out.print("Source language (en, hi, fr): ");
            String src = sc.nextLine();

            // Step 3: Target language
            System.out.print("Target language (en, hi, fr): ");
            String dest = sc.nextLine();

            // Step 4: Encode text
            String encodedText = URLEncoder.encode(text, "UTF-8");

            // Step 5: API URL
            String urlStr = "https://api.mymemory.translated.net/get?q="
                    + encodedText + "&langpair=" + src + "|" + dest;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Step 6: Read response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Step 7: Extract translated text
            String result = response.toString();
            int start = result.indexOf("translatedText\":\"") + 17;
            int end = result.indexOf("\"", start);

            String translatedText = result.substring(start, end);

            // Step 8: Decode Unicode
            String decodedText = decodeUnicode(translatedText);

            // Step 9: Output
            System.out.println("Translated Text: " + decodedText);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}