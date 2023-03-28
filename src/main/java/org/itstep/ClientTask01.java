package org.itstep;

import java.io.IOException;
import java.util.Scanner;

public class ClientTask01 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int n;
        while (true) {
//            System.out.println("1-send request, 0-exit");
            System.out.println("1 - hello.html, 2 - welcome.html, 3 - welcome1.html, 0-exit");
            System.out.print(">>> ");
            n = scanner.nextInt();
            switch (n) {
                case 1 -> {
                    ClientHTTP clientHTTP = new ClientHTTP("localhost", 80);
                    String response = clientHTTP.get("hello.html");
                    System.out.println("-= Response =-");
                    System.out.println(response);
                }
                case 2 -> {
                    ClientHTTP clientHTTP = new ClientHTTP("localhost", 80);
                    String response = clientHTTP.get("welcome.html");
                    System.out.println("-= Response =-");
                    System.out.println(response);
                }
                case 3 -> {
                    ClientHTTP clientHTTP = new ClientHTTP("localhost", 80);
                    String response = clientHTTP.get("welcome1.html");
                    System.out.println("-= Response =-");
                    System.out.println(response);
                }
                case 0 -> System.exit(0);
                default -> System.out.println("Вы неправильно ввели исходные данные");
            }

        }
    }
}
