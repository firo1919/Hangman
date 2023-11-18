import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

public class Hangman {
  static int remainingGuess = 6;
  static String secretword, correct = "";
  static Character guess;
  static ArrayList<Integer> index = new ArrayList<Integer>();
  static ArrayList<String> guessedwords = new ArrayList<String>();

  // The skeletal structure of hangman represented by an array of strings
  static String[] bodypart = { "   ______\n  |      |\n  |\n  |\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |      |\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\\\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\\\n  |     /\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\\\n  |     / \\\n__|__\n" };
  static String message = "\n                       WELCOME TO HANGMAN!\n" +
      "\n" +
      "--> In this game, the computer will choose a random word, and your goal is to guess the word by suggesting letters.\n"
      +
      "\n" +
      "--> You have a limited number of guesses, and with each incorrect guess, a part of the hangman is drawn.\n" +
      "\n" +
      "--> If the hangman is completely drawn before you guess the word, you lose.\n" +
      "\n" +
      "Lets get started!".toUpperCase();

  // for picking a random word from the file
  public static void pickword() {
    Random r = new Random();
    int random = r.nextInt(4500);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader("words.txt"));
      while (random > 0) {
        reader.readLine();
        random--;
      }
      secretword = reader.readLine();
      reader.close();
    } catch (IOException e) {
      System.out.println("error has occured: " + e.getMessage());
    }
  }

  static boolean gameover() {
    if (remainingGuess == 0) {
      display();
      underscore();
      System.out.println("\u001B[31m well not this time :( \u001B[0m The correct word was '" + secretword + "'\n");
    }
    return remainingGuess == 0;
  }

  static void display() {
    // first clears out the console for the given os
    try {
      String os = System.getProperty("os.name").toLowerCase();
      ProcessBuilder pb;
      if (os.contains("win")) {
        pb = new ProcessBuilder("cmd", "/c", "cls");
      } else {
        pb = new ProcessBuilder("clear");
      }
      pb.inheritIO().start().waitFor();
    } catch (IOException | InterruptedException e) {
    }
    System.out.println(message);
    System.out.println(bodypart[6 - remainingGuess]);
  }

  // for accepting user input
  static void inputval() {
    Scanner sc = new Scanner(System.in);
    System.out.print("\nenter your guess: ");
    guess = sc.nextLine().charAt(0);
  }

  static void underscore() {
    System.out.print("secretword: ");
    // prints the answered letters , otherwise underscore
    for (int i = 0; i < (secretword.length()); i++) {
      if (index.contains(i)) {// index contains the index of correct letter in the secretword
        System.out.print(secretword.charAt(i) + " ");
      } else {
        System.out.print("_ ");
      }
    }
    System.out.print("\nguessedwords: ");
    // prints the overall letters tried with descriptive color
    for (String i : guessedwords) {
      if (correct.contains(i)) {
        System.out.print("\u001B[32m" + i + "\u001B[0m" + " ");
      } else {
        System.out.print("\u001B[31m" + i + "\u001B[0m" + " ");
      }
    }
    System.out.println();
  }

  static boolean gamewon() {
    return secretword.length() == correct.length();
  }

  static void checkword() {
    guessedwords.add("" + guess);
    if (secretword.contains("" + guess) && (!correct.contains("" + guess))) {
      System.out.println("Correct guess!!\n");
      for (int i = 0; i < secretword.length(); i++) {// to get all instances of the letter in the word, duplicate
                                                     // letters are considered found as once
        if (secretword.charAt(i) == guess) {
          index.add(i);
          correct += guess;// holdes each correct letter, to control repeated input of the same letter(seen
                       // as wrong guess)
        }
      }
    } else {// else decrimenting the remainingguess
      System.out.println("wrong guess\n");
      --remainingGuess;
    }
  }

  public static void main(String[] args) {
    pickword();
    while (!gameover()) {
      display();
      underscore();
      inputval();
      checkword();
      if (gamewon()) {
        System.out.println("\u001B[32m You have won Congratulation :)\u001B[0m \n");
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }
  }
}