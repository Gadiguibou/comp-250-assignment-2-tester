package assignment2;

import java.util.Random;
import RuntimeTester.Visualizer;
import RuntimeTester.benchmark;

public class TimeTester {
  private static Random rand = new Random();
  private static Deck deck1 = new Deck(10, 2);

  public static void main(String[] args) {
    Visualizer.launch(TimeTester.class);
  }

  @benchmark(name = "Deck.Deck(d)", expectedEfficiency = "O(n)")
  public static long runTimeConstructor(long size) {
    int rank;
    int suit;
    Deck d = new Deck();
    for (int i = 0; i < size; i++) {
      rank = 1 + rand.nextInt(13);
      suit = rand.nextInt(4);
      Deck.Card c = d.new PlayingCard(Deck.suitsInOrder[suit], rank);
      d.addCard(c);
    }

    long start = System.nanoTime();
    new Deck(d);
    long end = System.nanoTime();
    return end - start;
  }

  @benchmark(name = "Deck.addCard()", expectedEfficiency = "O(1)")
  public static long runTimeForAddCard(long size) {
    int rank;
    int suit;
    Deck d = new Deck();
    for (int i = 0; i < size; i++) {
      rank = 1 + rand.nextInt(13);
      suit = rand.nextInt(4);
      Deck.Card c = d.new PlayingCard(Deck.suitsInOrder[suit], rank);
      d.addCard(c);
    }

    rank = 1 + rand.nextInt(13);
    suit = rand.nextInt(4);
    long start = System.nanoTime();
    d.addCard(d.new PlayingCard(Deck.suitsInOrder[suit], rank));
    long end = System.nanoTime();
    return end - start;
  }

  @benchmark(name = "Deck.shuffle()", expectedEfficiency = "O(n)")
  public static long runTimeShuffle(long size) {
    int rank;
    int suit;
    Deck d = new Deck();
    for (int i = 0; i < size; i++) {
      rank = 1 + rand.nextInt(13);
      suit = rand.nextInt(4);
      Deck.Card c = d.new PlayingCard(Deck.suitsInOrder[suit], rank);
      d.addCard(c);
    }

    long start = System.nanoTime();
    d.shuffle();
    long end = System.nanoTime();
    return end - start;
  }

  @benchmark(name = "Deck.moveCard()", expectedEfficiency = "O(n)")
  public static long runTimeMoveCard(long size) {
    int rank;
    int suit;
    Deck d = new Deck();
    for (int i = 0; i < size; i++) {
      rank = 1 + rand.nextInt(13);
      suit = rand.nextInt(4);
      Deck.Card c = d.new PlayingCard(Deck.suitsInOrder[suit], rank);
      d.addCard(c);
    }

    long start = System.nanoTime();
    d.moveCard(d.head.next, (int) size);
    long end = System.nanoTime();
    return end - start;
  }

  @benchmark(name = "Deck.tripleCut()", expectedEfficiency = "O(1)")
  public static long runTimeTripleCut(long size) {
    int rank;
    int suit;
    Deck d = new Deck();
    for (int i = 0; i < size; i++) {
      rank = 1 + rand.nextInt(13);
      suit = rand.nextInt(4);
      Deck.Card c = d.new PlayingCard(Deck.suitsInOrder[suit], rank);
      d.addCard(c);
    }
    Deck.Card joker1 = d.new Joker("red");
    d.addCard(joker1);
    Deck.Card joker2 = d.new Joker("black");
    d.addCard(joker2);
    d.shuffle();

    long start = System.nanoTime();
    d.tripleCut(joker1, joker2);
    long end = System.nanoTime();
    return end - start;
  }

  @benchmark(name = "Deck.generateNextKeystreamValue()", expectedEfficiency = "O(n)")
  public static long runTimeOverall(long size) {
    int rank;
    int suit;
    Deck d = new Deck();
    for (int i = 0; i < size; i++) {
      rank = 1 + rand.nextInt(13);
      suit = rand.nextInt(4);
      Deck.Card c = d.new PlayingCard(Deck.suitsInOrder[suit], rank);
      d.addCard(c);
    }
    d.addCard(d.new Joker("red"));
    d.addCard(d.new Joker("black"));
    d.shuffle();

    long start = System.nanoTime();
    d.generateNextKeystreamValue();
    long end = System.nanoTime();
    return end - start;
  }
}