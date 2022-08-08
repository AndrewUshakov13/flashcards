package com.company;

import java.io.*;
import java.util.*;

public class Main {
    public static StringBuilder sb = new StringBuilder();
    public static Scanner scanner = new Scanner(System.in);
    public static Map<String, String[]> flashCards = new LinkedHashMap<>();

    public static void add() {
        print("The card:");
        String term = scanner.nextLine();
        sb.append(term).append("\n");
        if (flashCards.containsKey(term)) {
            print("The card \"" + term + "\" already exists.");
        } else {
            print("The definition of the card:");
            String definition = scanner.nextLine();
            sb.append(definition).append("\n");
            List<String> answers = new ArrayList<>();
            for (Map.Entry<String, String[]> str : flashCards.entrySet()
            ) {
                answers.add(str.getValue()[0]);
            }
            if (answers.contains(definition)) {
                print("The definition \"" + definition + "\" already exists.");
            } else {
                flashCards.put(term, new String[]{definition, "0"});
                print("The pair (\"" + term + "\":\"" + definition +
                        "\") has been added.");
            }
        }
    }

    public static void remove() {
        print("Which card?");
        String remove = scanner.nextLine();
        sb.append(remove).append("\n");
        if (flashCards.containsKey(remove)) {
            flashCards.remove(remove);
            print("The card has been removed.");
        } else {
            print("Can't remove \"" + remove + "\": there is no such card.");
        }
    }

    public static void importFile() {
        print("File name:");
        String fileName = scanner.nextLine();
        sb.append(fileName).append("\n");
        File file = new File(fileName);
        int count = 0;

        try(Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                String[] scannerStr = fileScanner.nextLine().split(" ");
                flashCards.put(scannerStr[0], new String[]{scannerStr[1], scannerStr[2]});
                count++;
            }
            print(count + " cards have been loaded.\n");
        } catch (FileNotFoundException e) {
            print("File not found.");
        }
    }

    public static void importFile(String fileName) {
        File file = new File(fileName);
        int count = 0;

        try(Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                String[] scannerStr = fileScanner.nextLine().split(" ");
                flashCards.put(scannerStr[0], new String[]{scannerStr[1], scannerStr[2]});
                count++;
            }
            print(count + " cards have been loaded.\n");
        } catch (FileNotFoundException e) {
            print("File not found.");
        }
    }

    public static void log() {
        print("File name: ");
        String logFileName = scanner.nextLine();
        sb.append(logFileName).append("\n");
        File file = new File(logFileName);
        print("The log has been saved.");
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(String.valueOf(sb));
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }
    }

    public static void showHardest() {
        int maxValue = 0;
        List<String> hardest = new ArrayList<>();
        int[] maxValues = new int[flashCards.size()];
        int i = 0;
        for (Map.Entry<String, String[]> entry : flashCards.entrySet()
        ) {
            maxValues[i] = Integer.parseInt(entry.getValue()[1]);
            i++;
        }
        Arrays.sort(maxValues);

        try {
            maxValue = maxValues[flashCards.size() - 1];
        } catch (Exception ignored) {

        }

        if (maxValue == 0) {
            print("There are no cards with errors.");
        } else {
            for (Map.Entry<String, String[]> entry : flashCards.entrySet()
            ) {
                if (Integer.parseInt(entry.getValue()[1]) == maxValue) {
                    hardest.add(entry.getKey());
                }
            }
            if (hardest.size() == 1) {
                print("The hardest card is \"" + hardest.get(0) + "\". " +
                        "You have " + maxValue + " errors answering it.");
            } else {
                StringBuilder answers = new StringBuilder();
                for (String s : hardest) {
                    answers.append("\"").append(s).append("\", ");
                }

                answers = new StringBuilder(answers.substring(0, answers.length() - 2));
                print("The hardest cards are " + answers + ". " +
                        "You have " + maxValue + " errors answering them.");
            }
        }
    }

    public static void exportFile(){
        print("File name:");
        String pathToFile = scanner.nextLine();
        sb.append(pathToFile).append("\n");
        File file2 = new File(pathToFile);
        int count = 0;
        try (PrintWriter printWriter = new PrintWriter(file2)) {
            for (var str : flashCards.entrySet()) {
                String[] values = str.getValue();
                printWriter.println(str.getKey() + " " + values[0] + " " + values[1]);
                count++;
            }
        } catch (IOException e) {
            print("An exception occurred");
        }
        print(count + " cards have been saved.\n");
    }

    public static void exportFile(String fileName){
        File file2 = new File(fileName);
        int count = 0;
        try (PrintWriter printWriter = new PrintWriter(file2)) {
            for (var str : flashCards.entrySet()) {
                String[] values = str.getValue();
                printWriter.println(str.getKey() + " " + values[0] + " " + values[1]);
                count++;
            }
        } catch (IOException e) {
            print("An exception occurred");
        }
        print(count + " cards have been saved.\n");
    }

    public static void quiz(){
        print("How many times to ask?");
        String enter = scanner.nextLine();
        sb.append(enter).append("\n");
        int number = Integer.parseInt(enter);
        int counter = 0;
        ArrayList<String> answers = new ArrayList<>();
        for (Map.Entry<String, String[]> str : flashCards.entrySet()) {
            String[] values = str.getValue();
            answers.add(values[0]);
        }

        while (counter < number) {
            for (Map.Entry<String, String[]> entry : flashCards.entrySet()) {
                if (counter == number) {
                    break;
                }
                print("Print the definition of \"" + entry.getKey() + "\":");
                String answer = scanner.nextLine();
                sb.append(answer).append("\n");
                String[] rightAnswers = entry.getValue();

                if (answer.equals(rightAnswers[0])) {
                    print("Correct!");
                } else if (answers.contains(answer)) {
                    String wrongKey = "";
                    int mistake = Integer.parseInt(rightAnswers[1]);
                    mistake++;
                    rightAnswers[1] = String.valueOf(mistake);
                    for (Map.Entry<String, String[]> str : flashCards.entrySet()) {
                        String[] values = str.getValue();
                        if (answer.equals(values[0])) {
                            wrongKey = str.getKey();
                        }
                    }
                    print("Wrong. The right answer is \"" + rightAnswers[0] +
                            "\", but your definition is correct for \"" +
                            wrongKey + "\".");
                } else {
                    int mistake = Integer.parseInt(rightAnswers[1]);
                    mistake++;
                    rightAnswers[1] = String.valueOf(mistake);
                    print("Wrong. The right answer is \"" + rightAnswers[0] + "\".");
                }
                counter++;
            }
        }
    }

    public static void reset(){
        for (var str : flashCards.entrySet()) {
            String[] values = str.getValue();
            values[1] = String.valueOf(0);
        }
        print("Card statistics have been reset.");
    }

    public static void exit(){
        print("Bye bye!");
    }

    public static void start(){
        boolean exit = false;
        while(!exit) {
            print("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String choice = scanner.nextLine();
            sb.append(choice).append("\n");
            switch (choice) {
                case "add" -> add();
                case "exit" -> {
                    exit();
                    exit = true;
                }
                case "remove" -> remove();
                case "import" -> importFile();
                case "log" -> log();
                case "hardest card" -> showHardest();
                case "export" -> exportFile();
                case "ask" -> quiz();
                case "reset stats" -> reset();
                default -> print("Wrong command. Try again!");
            }
        }

    }
    public static void print(String str) {
        System.out.println(str);
        sb.append(str).append("\n");
    }

    public static void main(String[] args) {
        if(args.length > 0 && args.length <= 2) {
            if ("-import".equals(args[0])) {
                importFile(args[1]);
                start();
            } else if ("-export".equals(args[0])) {
                start();
                exportFile(args[1]);
            } else {
                start();
            }
        } else if (args.length > 2) {
            if ("-import".equals(args[0]) && "-export".equals(args[2])) {
                importFile(args[1]);
                start();
                exportFile(args[3]);
            } else if ("-export".equals(args[0]) && "-import".equals(args[2])) {
                importFile(args[3]);
                start();
                exportFile(args[1]);
            }
        } else {
            start();
        }

    }
}




