import java.io.*;
import java.util.*;

public class CaesarCipher {
    /*ALPHABET: Esta cadena contiene todos los caracteres válidos que se pueden
    cifrar o descifrar. Incluye letras minúsculas, signos de puntuación y espacio.*/
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz.,\"\":-!? ";

    /*ALPHABET_SIZE: Esta constante almacena la longitud del alfabeto criptográfico.*/
    private static final int ALPHABET_SIZE = ALPHABET.length();

        /*Crea un objeto Scanner para leer la entrada del usuario desde la consola.
          Solicita al usuario que ingrese la ruta del archivo de texto.
          Pide al usuario que seleccione el modo de operación: cifrado/descifrado o criptoanálisis por fuerza bruta.
          Dependiendo del modo seleccionado por el usuario, llama al método correspondiente.*/
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la ruta del archivo de texto:");
        String filePath = scanner.nextLine();
        System.out.println("Modo: (1) Cifrado/Descifrado (2) Criptoanálisis por fuerza bruta");
        int mode = scanner.nextInt();

        switch (mode) {
            case 1:
                System.out.println("Ingrese la clave criptográfica:");
                int key = scanner.nextInt();
                encryptDecryptFile(filePath, key);
                break;
            case 2:
                bruteForceDecrypt(filePath);
                break;
            default:
                System.out.println("Modo no válido.");
        }
    }
    /*Este método se encarga de cifrar o descifrar el contenido del archivo de texto.
    Utiliza un BufferedReader para leer el contenido del archivo de texto.
    Utiliza un BufferedWriter para escribir el resultado cifrado o descifrado en un nuevo archivo llamado "encrypted_decrypted.txt".
    Lee cada línea del archivo, la procesa (cifra o descifra) utilizando el método processLine y escribe el resultado en el archivo de salida.
    Cierra automáticamente los recursos (buffers) cuando termina la operación.*/
    private static void encryptDecryptFile(String filePath, int key) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter("encrypted_decrypted.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = processLine(line, key);
                writer.write(processedLine);
                writer.newLine();
            }
            System.out.println("Operación completada. Resultado guardado en 'encrypted_decrypted.txt'");
        } catch (IOException e) {
            System.out.println("Error al leer/escribir el archivo: " + e.getMessage());
        }
    }
    /*Este método procesa cada línea de texto, cifrando o descifrando cada caracter según la clave proporcionada.
    Recorre cada caracter de la línea.
    Para cada caracter, busca su índice en el alfabeto criptográfico.
    Si el caracter está en el alfabeto, aplica la transformación (cifrado o descifrado) utilizando la clave.
    Mantiene la capitalización del caracter original.
    Construye una nueva cadena con los caracteres procesados y la devuelve.*/
    private static String processLine(String line, int key) {

        StringBuilder result = new StringBuilder();
        for (char c : line.toCharArray()) {
            int index = ALPHABET.indexOf(Character.toLowerCase(c));
            if (index != -1) {
                int shiftedIndex = (index + key) % ALPHABET_SIZE;
                if (shiftedIndex < 0) {
                    shiftedIndex += ALPHABET_SIZE;
                }
                char newChar = ALPHABET.charAt(shiftedIndex);
                result.append(Character.isUpperCase(c) ? Character.toUpperCase(newChar) : newChar);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
        /*Este método realiza un criptoanálisis por fuerza bruta para descifrar el texto cifrado.
        Lee el texto cifrado del archivo de entrada.
        Prueba todas las posibles claves (números de 1 a ALPHABET_SIZE - 1) para descifrar el texto.
        Imprime el resultado descifrado para cada clave probada.*/
    private static void bruteForceDecrypt(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String encryptedText = reader.readLine();
            for (int i = 1; i < ALPHABET_SIZE; i++) {
                String decryptedText = processLine(encryptedText, -i);
                System.out.println("Key " + i + ": " + decryptedText);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
