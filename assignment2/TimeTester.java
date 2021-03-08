package assignment2;

import java.util.Random;
import RuntimeTester.Visualizer;
import RuntimeTester.benchmark;

public class TimeTester
{
	private static Random rand = new Random();
	private static Deck deck1 = new Deck(10, 2);


	public static void main(String[] args)
	{
		Visualizer.launch(TimeTester.class);
	}


	@benchmark(name = "Deck.generateNextKeystreamValue()")
	public static long runTimeOverall(long size)
	{
		int rank;
		int suit;
		Deck d = new Deck();
		for (int i = 0; i < size; i++)
		{
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

	@benchmark(name = "Deck.addCard()")
	public static long runTimeForAddCard(long size)
	{
		int rank;
		int suit;
		rank = 1 + rand.nextInt(13);
		suit = rand.nextInt(4);
		Deck.Card c = deck1.new PlayingCard(Deck.suitsInOrder[suit], rank);
		deck1.addCard(c);

		long start = System.nanoTime();
		deck1.addCard(c);
		long end = System.nanoTime();
		return end - start;
	}

	@benchmark(name = "Deck.tripleCut()")
	public static long runTimeTripleCut(long size)
	{
		int rank;
		int suit;
		Deck d = new Deck();
		for (int i = 0; i < size; i++)
		{
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
}
