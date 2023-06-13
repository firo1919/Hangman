import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

public class Hangman {
  static int remainingGuess = 6;
  static String secretword, guess, correct = "";
  static ArrayList<Integer> index = new ArrayList<Integer>();
  static ArrayList<String> guessedwords = new ArrayList<String>();
  static String[] bodypart = { "   ______\n  |      |\n  |\n  |\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |      |\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\\\n  |\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\\\n  |     /\n__|__",
      "   ______\n  |      |\n  |      O\n  |     /|\\\n  |     / \\\n__|__\n" };

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

  public static void display() {
    if (!gamewon()) {
      System.out.println(bodypart[6 - remainingGuess]);
    }
  }

  public static void inputval() {
    Scanner sc = new Scanner(System.in);
    if (!gamewon()) {
      System.out.print("\nenter your guess: ");
      guess = sc.nextLine();
    }
  }

  public static void underscore() {
    if (!gamewon()) {
      System.out.print("secretword: ");
      for (int i = 0; i < (secretword.length()); i++) {
        if (index.contains(i)) {
          System.out.print(secretword.charAt(i)+" ");
        } else {
          System.out.print("_ ");
        }
      }
      System.out.print("\nguessedwords: ");
      for (String i : guessedwords) {
        if(correct.contains(i))
        {System.out.print("\u001B[32m"+i+"\u001B[0m"+" ");}
        else {System.out.print("\u001B[31m"+i+"\u001B[0m"+" ");}
      }
    }
  }

  public static boolean gamewon() {
    return secretword.length() == correct.length();
  }

  public static void checkword() {
    if (!gamewon()) {
      if (secretword.contains(guess) && (!correct.contains(guess))) {
        System.out.println("Correct guess!!\n");
        correct += guess;
        guessedwords.add(guess);
        int n = 0;
        while (n < secretword.length()) {
          index.add(secretword.indexOf(guess, n));
          n++;
        }
      } else {
        System.out.println("wrong guess try again\n");
        guessedwords.add(guess);
        remainingGuess--;
      }
    }
  }

  public static void main(String[] args) {

    pickword();
    while (remainingGuess > 0) {
      display();
      underscore();
      inputval();
      checkword();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
      if (gamewon())
        break;
    }
    if (gamewon()) {
      System.out.println("\u001B[32m You have won Congratulation :)\u001B[0m \n");
    } else {
      System.out.println(bodypart[6]);
      System.out.println("\u001B[31m well not this time :( \u001B[0m The correct word was '" + secretword + "'\n");
    }
  }
}