package assignment2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


class AddCard_AllRef implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        boolean c1ref = c1.next == c2 && c1.prev == c3;
        boolean c2ref = c2.next == c3 && c2.prev == c1;
        boolean c3ref = c3.next == c1 && c3.prev == c2;
        if (!(c1ref && c2ref && c3ref)){
            throw new AssertionError("Circular doubly linked list references are not correctly set up.");
        }
        System.out.println("Test passed.");
    }
}


class AddCard_CheckHead implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); //8C
        deck.addCard(c1);
        deck.addCard(c8);
        Deck.Card head = deck.head;
        if (head != c1){
            throw new AssertionError("addCard should add the input card to the bottom of the deck.\n" +
                    "Expected head to be " + c1 + " but got " + head);
        }
        System.out.println("Test passed.");
    }
}


class AddCard_Circular implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        Deck.Card c8 = deck.new PlayingCard(Deck.suitsInOrder[0], 8); //8C
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(c3);
        deck.addCard(c8);
        if (!(c1.prev == c8 && c8.next == c1)){
            throw new AssertionError("Circular references are not correctly set up.");
        }
        System.out.println("Test passed.");
    }
}


class AddCard_NumOfCards implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        Deck.Card c2 = deck.new PlayingCard(Deck.suitsInOrder[0], 2); //2C
        Deck.Card c3 = deck.new PlayingCard(Deck.suitsInOrder[0], 3); //3C
        Deck.Card d11 = deck.new PlayingCard(Deck.suitsInOrder[1], 11); //JD
        deck.addCard(c1);
        deck.addCard(c2);
        deck.addCard(d11);
        deck.addCard(c3);
        int expected = 4;
        int result = deck.numOfCards;
        if (expected != result){
            throw new AssertionError("numOfCards is not correctly updated.");
        }
        System.out.println("Test passed.");
    }
}


class AddCard_SingleCard implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c1 = deck.new PlayingCard(Deck.suitsInOrder[0], 1); //AC
        deck.addCard(c1);
        if (!(c1.prev == c1 && c1.next == c1)){
            throw new AssertionError("Card references are not correctly set up when the deck contains only ONE card.");
        }
        System.out.println("Test passed.");
    }
}


class DeepCopy_CheckRefs implements Runnable{
    @Override
    public void run() {
        HashSet<Deck.Card> cardSet = new HashSet<>();
        Deck deck = new Deck();
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 1));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 3));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[0], 5));
        cardSet.add(deck.new Joker("black"));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[1], 2));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[2], 4));
        cardSet.add(deck.new PlayingCard(Deck.suitsInOrder[3], 6));

        for(Deck.Card c: cardSet) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck); // should do a deep copy

        Deck.Card cur = copy.head;
        for (int i = 0; i < cardSet.size(); i++) {
            if (cardSet.contains(cur)){
                throw new AssertionError("Deep copy must create new object.");
            }
            cur = cur.next;
        }

        System.out.println("Test passed.");
    }
}


class DeepCopy_CircularNext implements Runnable {
    @Override
    public void run() {

        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck); // should do a deep copy

        Deck.Card cur = copy.head;
        for (int i = 0; i < cards.length; i++) {
            if (cur == null) {
                throw new AssertionError("Either head or one of the next pointers is null.");
            }

            if (cards[i].getClass() != cur.getClass()) { // Either one is Joker and other is PlayingCard or vice versa
                throw new AssertionError("The card at the next position of ."
                        + i + " from head, has type: " + cur.getClass().getName()
                        + " but expected: " + cards[i].getClass().getName());
            }

            if (cur instanceof Deck.PlayingCard) { // both are PlayingCard
                if (cards[i].getValue() != cur.getValue()) {
                    throw new AssertionError("The card at the next position of ."
                            + i + " from head must have value: " + cards[i].getValue() + " but got: " + cur.getValue());
                }
            } else { // both are Joker
                String cardColor = ((Deck.Joker) cards[i]).getColor();
                String curColor = ((Deck.Joker) cur).getColor();
                if (!cardColor.equals(curColor)) {
                    throw new AssertionError("The joker card at the next position of ."
                            + i + " from head must have color: " + cardColor + " but got: " + curColor);
                }
            }
            cur = cur.next;
        }

        if (cur != copy.head) {
            throw new AssertionError("The last card's next does not point to the head.");
        }

        System.out.println("Test passed.");
    }
}


class DeepCopy_CircularPrev implements Runnable {
    @Override
    public void run() {

        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck copy = new Deck(deck); // should do a deep copy

        Deck.Card cur = copy.head;
        for (int j = 0; j < cards.length; j++) {
            int i = Math.floorMod(-j, cards.length); // i goes 0, n-1, n-2, ..., 1
            if (cur == null) {
                throw new AssertionError("Either head or one of the prev pointers is null.");
            }

            if (cards[i].getClass() != cur.getClass()) { // Either one is Joker and other is PlayingCard or vice versa
                throw new AssertionError("The card at the prev position of ."
                        + j + " from head, has type: " + cur.getClass().getName()
                        + " but expected: " + cards[i].getClass().getName());
            }

            if (cur instanceof Deck.PlayingCard) { // both are PlayingCard
                if (cards[i].getValue() != cur.getValue()) {
                    throw new AssertionError("The card at the prev position of ."
                            + j + " from head must have value: " + cards[i].getValue() + " but got: " + cur.getValue());
                }
            } else { // both are Joker
                String cardColor = ((Deck.Joker) cards[i]).getColor();
                String curColor = ((Deck.Joker) cur).getColor();
                if (!cardColor.equals(curColor)) {
                    throw new AssertionError("The joker card at the prev position of ."
                            + j + " from head must have color: " + cardColor + " but got: " + curColor);
                }
            }
            cur = cur.prev;
        }

        if (cur != copy.head) {
            throw new AssertionError("The last card's prev does not point to the head.");
        }

        System.out.println("Test passed.");
    }
}


class LocateJoker_Test1 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,1 );
        Deck.Card expected = tdeck.head;
        for(int i =0; i<13; ++i)
            expected = expected.next;
        Deck.Card received = tdeck.locateJoker("red");
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LocateJoker_Test2 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,1 );
        Deck.Card expected = tdeck.head;
        for(int i =0; i<14; ++i)
            expected = expected.next;
        Deck.Card received = tdeck.locateJoker("black");
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LocateJoker_Test3 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,4 );
        Deck.Card expected = tdeck.head;
        for(int i =0; i<53; ++i)
            expected = expected.next;
        Deck.Card received = tdeck.locateJoker("black");
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LookUpCard_Test1 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(13,1 );
        Deck.Card expected = tdeck.head.next;
        Deck.Card received = tdeck.lookUpCard();
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. The second card should have " +
                    "been returned. Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LookUpCard_Test2 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(11, 4);
        Deck.Card old_head = tdeck.head;
        tdeck.head = tdeck.new PlayingCard(Deck.suitsInOrder[3], 2);
        tdeck.head.next = old_head.next;
        tdeck.head.prev = old_head.prev;

        Deck.Card expected = tdeck.head;

        for(int i = 0; i<41 ; ++i)
        {
            expected = expected.next;
        }

        Deck.Card received = tdeck.lookUpCard();
        if (expected != received){
            throw new AssertionError("The reference returned was incorrect. " +
                    "Expected the card " + expected.toString() + " with reference " + expected.hashCode()
                    +" but instead got the card " + received + " with reference " + received.hashCode());
        }
        System.out.println("Test passed.");
    }
}


class LookUpCard_Test3 implements Runnable{
    @Override
    public void run() {
        Deck tdeck = new Deck(8, 4);
        Deck.Card old_head = tdeck.head;
        tdeck.head = tdeck.new PlayingCard(Deck.suitsInOrder[2], 7);
        tdeck.head.next = old_head.next;
        tdeck.head.prev = old_head.prev;


        Deck.Card received = tdeck.lookUpCard();
        if (received != null){
            throw new AssertionError("Null should be returned in case a Joker is found.");
        }
        System.out.println("Test passed.");
    }
}


class MoveCard_CheckNext1 implements Runnable {
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[1], cards[3], cards[4],
                cards[5], cards[2], cards[6]};

        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            // System.out.println(cur);
            if(expected[i] != cur) {
                throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}


class MoveCard_CheckNext2 implements Runnable {
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[3], cards[1], cards[2],
                cards[4], cards[5], cards[6]};

        deck.moveCard(cards[3], 4);

        Deck.Card cur = deck.head;
        for (int i = 0; i < expected.length; i++) {
            // System.out.println(cur);
            if(expected[i] != cur) {
                throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}


class MoveCard_CheckPrev1 implements Runnable {
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[1], cards[3], cards[4],
                cards[5], cards[2], cards[6]};

        deck.moveCard(cards[2], 3);

        Deck.Card cur = deck.head;
        for (int j = 0; j < expected.length; j++) {
            int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
            // System.out.println(cur);
            if(expected[i] != cur) {
                throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
            }
            cur = cur.prev;
        }
        System.out.println("Test passed.");
    }
}


class MoveCard_CheckPrev2 implements Runnable {
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card[] cards = new Deck.Card[]{
                deck.new PlayingCard(Deck.suitsInOrder[0], 1),
                deck.new PlayingCard(Deck.suitsInOrder[0], 3),
                deck.new PlayingCard(Deck.suitsInOrder[0], 5),
                deck.new Joker("black"),
                deck.new PlayingCard(Deck.suitsInOrder[1], 2),
                deck.new PlayingCard(Deck.suitsInOrder[2], 4),
                deck.new PlayingCard(Deck.suitsInOrder[3], 6)
        };

        for (Deck.Card c : cards) {
            deck.addCard(c);
        }

        Deck.Card[] expected = new Deck.Card[]{
                cards[0], cards[3], cards[1], cards[2],
                cards[4], cards[5], cards[6]};

        deck.moveCard(cards[3], 4);

        Deck.Card cur = deck.head;
        for (int j = 0; j < expected.length; j++) {
            int i = Math.floorMod(-j, expected.length); // i goes 0, n-1, n-2, ..., 1
            // System.out.println(cur);
            if(expected[i] != cur) {
                throw new AssertionError("Expect card: " + expected[i] + " but got: " + cur);
            }
            cur = cur.prev;
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_Empty implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();

        deck.shuffle();

        if (!(deck.head == null)){
            throw new AssertionError("Deck should be empty.");
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_Example implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // example in instruction pdf
        // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
        Deck.Card[] arrDeck = new Deck.Card[12];
        for (int i = 0; i < 10; i++) {
            int suit = i/5;
            int rank = i%5 + 1;
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
            arrDeck[i] = card;
            deck.addCard(card);
        }
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        arrDeck[10] = rj;
        arrDeck[11] = bj;
        deck.addCard(rj);
        deck.addCard(bj);

        int seed = 10;
        Deck.gen.setSeed(seed);
        deck.shuffle();

        // expected result
        // 3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D
        int[] shuffledIndex = {2,7,5,4,11,1,6,8,0,10,3,9};
        Deck.Card cur = deck.head;
        for (int i = 0; i < 12; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            if (cur.getValue() != expected.getValue()){
                throw new AssertionError("Deck is not correctly shuffled.\n" +
                        "Expected card at index " + i + " is " + expected + " but got " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_FullDeck implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // all 54 cards
        Deck.Card[] arrDeck = new Deck.Card[54];
        for (int i = 0; i < 52; i++) {
            int suit = i/13;
            int rank = i%13 + 1;
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
            arrDeck[i] = card;
            deck.addCard(card);
        }
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        arrDeck[52] = rj;
        arrDeck[53] = bj;
        deck.addCard(rj);
        deck.addCard(bj);

        int seed = 10;
        Deck.gen.setSeed(seed);
        deck.shuffle();

        // expected result
        // 7S QD 7H JH KH KD 8C 4C 9S JD KC 9C 5C QC 2S 5S 10H 10D
        // 4S 5D 6H 4D 9D 8D 3H 6D 4H 7C RJ 9H 3C 2D JC 6C 8H JS 5H
        // AH BJ 3S 6S 3D QS AS 7D 2C AD KS 10S 8S 10C QH AC 2H
        int[] shuffledIndex = {
                45, 24, 32, 36, 38, 25, 7, 3, 47, 23, 12, 8, 4, 11, 40, 43, 35, 22,
                42, 17, 31, 16, 21, 20, 28, 18, 29, 6, 52, 34, 2, 14, 10, 5, 33, 49, 30,
                26, 53, 41, 44, 15, 50, 39, 19, 1, 13, 51, 48, 46, 9, 37, 0, 27};
        Deck.Card cur = deck.head;
        for (int i = 0; i < 54; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            if (cur.getValue() != expected.getValue()){
                throw new AssertionError("Deck is not correctly shuffled.\n" +
                        "Expected card at index " + i + " is " + expected + " but got " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_NewCard implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // example in instruction pdf
        // AC 2C 3C 4C 5C AD 2D 3D 4D 5D RJ BJ
        Set<Deck.Card> cardSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            int suit = i/5;
            int rank = i%5 + 1;
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[suit], rank);
            deck.addCard(card);
            cardSet.add(card);
        }
        Deck.Card rj = deck.new Joker("red");
        Deck.Card bj = deck.new Joker("black");
        deck.addCard(rj);
        deck.addCard(bj);
        cardSet.add(rj);
        cardSet.add(bj);

        int seed = 10;
        Deck.gen.setSeed(seed);
        deck.shuffle();

        Deck.Card cur = deck.head;
        for (int i = 0; i < 12; i++) {
            if (!cardSet.contains(cur)){
                throw new AssertionError("Shuffle should not create new cards.");
            }
            cur = cur.next;
        }
        if (cur != deck.head){
            throw new AssertionError("Deck is not correctly shuffled. " +
                    "Tail does not connect to head or new cards were added.");
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_SingleCard implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        Deck.Card c = deck.new Joker("red");
        deck.addCard(c);

        deck.shuffle();

        if (!(deck.head.getValue() == c.getValue() &&
                c.next.getValue() == c.getValue() && c.prev.getValue() == c.getValue())){
            throw new AssertionError("Deck is not correctly shuffled when it only has one card.");
        }
        System.out.println("Test passed.");
    }
}


class Shuffle_Three implements Runnable{
    @Override
    public void run() {
        Deck deck = new Deck();
        // AC 2C 3C 4C 5C
        Deck.Card[] arrDeck = new Deck.Card[5];
        for (int i = 0; i < 5; i++) {
            Deck.Card card = deck.new PlayingCard(Deck.suitsInOrder[0], i+1);
            arrDeck[i] = card;
            deck.addCard(card);
        }

        int seed = 250;
        Deck.gen.setSeed(seed);
        deck.shuffle();
        deck.shuffle();
        deck.shuffle();

        // expected first pass
        // AC, 4C, 5C, 3C, 2C

        // expected second pass
        // 5C, AC, 4C, 2C, 3C

        // expected third pass
        // AC, 5C, 3C, 2C, 4C

        int[] shuffledIndex = {0, 4, 2, 1, 3};
        Deck.Card cur = deck.head;
        for (int i = 0; i < 5; i++) {
            Deck.Card expected = arrDeck[shuffledIndex[i]];
            if (cur.getValue() != expected.getValue()){
                throw new AssertionError("Deck is not correctly shuffled.\n" +
                        "Expected card at index " + i + " is " + expected + " but got " + cur);
            }
            cur = cur.next;
        }
        System.out.println("Test passed.");
    }
}

////////////////////////////////////
// Question 1

class default_deck implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck(1,1);
        Deck.Card current;

        current = d1.head;
        int numCards = d1.numOfCards;
        String currentCard="";
        String result = "";

        while(current!= d1.head || numCards !=0){

            result=result+current.toString();

            current=current.next;
            numCards--;
        }
        numCards = d1.numOfCards;
        String expected ="ACRJBJ";
        if (!result.equals(expected) || numCards != 3){
            throw new AssertionError("got " + result
                    + " but expected " + expected + "No Cards: " + numCards);
        }
        System.out.println("default_deck test passed.");
    }
}

class default_deck_more_than_one_card implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck(4,3);
        Deck.Card current;

        current = d1.head;
        int numCards = d1.numOfCards;
        String currentCard="";
        String result = "";

        while(current!= d1.head || numCards !=0){
            result=result+current.toString()+" ";

            current=current.next;
            numCards--;
        }

        numCards = d1.numOfCards;

        String expected ="AC 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H RJ BJ ";
        if (!result.equals(expected) || numCards != 14){
            throw new AssertionError("got returned " + result
                    + " but expected " + expected + "No Cards: " + numCards);
        }
        System.out.println("default_deck_more_than_one_card test passed.");
    }
}
////////////////////////////////////

////////////////////////////////////
// Question 2

class deep_copy_deck implements Runnable {
    @Override
    public void run() {
        // create a deck 4*3
        Deck oldDeck = new Deck(4,3);
        Deck newDeck = new Deck(oldDeck);

        Deck.Card currentOldDeck;
        Deck.Card currentNewDeck;
        currentOldDeck = oldDeck.head;
        currentNewDeck = newDeck.head;

        int numCards = oldDeck.numOfCards;
        //String current="";
        String resultOldDeck = "";
        String resultNewdDeck = "";

        while(currentOldDeck!= oldDeck.head || numCards !=0){
            resultOldDeck=resultOldDeck+currentOldDeck.toString()+" ";
            resultNewdDeck=resultNewdDeck+currentNewDeck.toString()+" ";

            currentOldDeck=currentOldDeck.next;
            currentNewDeck=currentNewDeck.next;
            numCards--;
        }

        numCards = oldDeck.numOfCards;

        String expected ="AC 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H RJ BJ ";
        if (!resultNewdDeck.equals(resultOldDeck) ||
                oldDeck.numOfCards!= newDeck.numOfCards){
            throw new AssertionError("got old deck " + resultOldDeck
                    + " new Deck" + resultNewdDeck + "No Cards in old and new deck"
                    + oldDeck.numOfCards + " " + newDeck.numOfCards);
        }
        System.out.println("Deep copy test passed.");
    }
}



////////////////////////////////////
////////////////////////////////////
// Question 3

class addOneCardToBottom implements Runnable {
    @Override
    public void run() {
        // create a deck 4*3
        Deck newDeck = new Deck(4,3);
        Deck.Card newCard = newDeck.new PlayingCard("c",5);
        Deck.Card current;

        newDeck.addCard(newCard);
        current = newDeck.head;
        int numCards = newDeck.numOfCards;
        String currentCard="";
        String result = "";

        while(current != newDeck.head || numCards !=0){

            result=result+current.toString()+" ";

            current=current.next;
            numCards--;
        }
        numCards = newDeck.numOfCards;
        String expected ="AC 2C 3C 4C AD 2D 3D 4D AH 2H 3H 4H RJ BJ 5C ";
        if (!result.equals(expected) || numCards != 15){
            throw new AssertionError("got " + result
                    + " but expected " + expected + "No Cards: " + numCards);
        }
        System.out.println("addOneCardToBottom test passed.");
    }
}

////////////////////////////////////
// Question 4

class shuffleDeck implements Runnable {
    @Override
    public void run() {
        // create a deck 4*3
        Deck newDeck = new Deck(4,3);
        Deck.Card current = newDeck.head;
        int numCards = newDeck.numOfCards;
        String beforeShuffle="";

        while(current != newDeck.head || numCards !=0){

            beforeShuffle=beforeShuffle+current.toString()+" ";

            current=current.next;
            numCards--;
        }
        newDeck.shuffle();

        current = newDeck.head;
        numCards = newDeck.numOfCards;
        String afterShuffle="";

        while(current != newDeck.head || numCards !=0){

            afterShuffle=afterShuffle+current.toString()+" ";

            current=current.next;
            numCards--;
        }

        if ((beforeShuffle.equals(afterShuffle))){
            throw new AssertionError("the deck was not shuffled " );
        }
        System.out.println("Shuffle test passed.");
        System.gc();
    }
}
class shuffleDeck2 implements Runnable {
    @Override
    public void run() {
        // create a deck
        Deck d1 = new Deck();
        String fullSuitName="";

        String[] suits = {"C","C","C","C","C","D","D","D","D","D","J","J"};
        int[] rank = {1,2,3,4,5,1,2,3,4,5,0,0};

        for (int i=0;i<rank.length;i++){
            if (i == 11){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 10 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                switch(suits[i]) {
                    case "C":
                        fullSuitName ="clubs";
                        break;
                    case "D":
                        fullSuitName ="diamonds";
                        break;
                    case "H":
                        fullSuitName ="hearts";
                        break;
                    case "S":
                        fullSuitName ="spades";
                        break;

                }
                Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
                d1.addCard(newCard);
            }
        }
        Deck.Card current = d1.head;
        int numCards = d1.numOfCards;
        String beforeShuffle="";

        while(current != d1.head || numCards !=0){
            beforeShuffle=beforeShuffle+current.toString()+" ";
            current=current.next;
            numCards--;
        }

        d1.shuffle();

        current = d1.head;
        numCards = d1.numOfCards;
        String afterShuffle="";
        System.out.println();
        while(current != d1.head || numCards !=0){
            afterShuffle=afterShuffle+current.toString()+" ";
            current=current.next;
            numCards--;
        }
        System.out.println("Original deck: " +beforeShuffle);
        System.out.println("Shuffle deck: "+ afterShuffle);
        String expected = "3C 3D AD 5C BJ 2C 2D 4D AC RJ 4C 5D ";
        if (!expected.equals(afterShuffle)){
            throw new AssertionError("got: " + afterShuffle +
                    " but expected: "+ expected);
        }
        System.out.println("shuffleDeck2 test passed.");
    }
}
////////////////////////////////////
// Question 5
class locatejoker implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck(4,3);
        Deck.Card getJoker=d1.locateJoker("black");
        String returnedJoker=getJoker.toString();

        if (!returnedJoker.equals("BJ")){
            throw new AssertionError("Wrong card returned");
        }
        System.out.println("locatejoker test passed.");
    }
}


class noJokersInDeck implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck();
        for (int i=1;i<=13;i++){
            Deck.Card newCard = d1.new PlayingCard("c",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("d",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("h",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("s",i);
            d1.addCard(newCard);
        }

        Deck.Card getJoker=d1.locateJoker("black");


        if (d1.locateJoker("red") != null || d1.locateJoker("black")!= null){
            throw new AssertionError("this deck shouldn't have jokers");
        }
        System.out.println("noJokersInDeck test passed.");
    }
}


////////////////////////////////////

////////////////////////////////////
// Question 6

class moveCardHead implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck();
        for (int i=1;i<=1;i++){
            Deck.Card newCard = d1.new PlayingCard("c",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("d",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("h",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("s",i);
            d1.addCard(newCard);
        }


        Deck.Card current = d1.head;
        d1.moveCard(current,2);


        String result = "";
        int noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            result=result+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        String expected = "AC AS AD AH ";
        if (!expected.equals(result)){
            throw new AssertionError("got " + result
                    + " but expected " + expected );
        }
        System.out.println("moveCardHead test passed.");
    }
}



class moveCardTailby1 implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck();
        for (int i=1;i<=1;i++){
            Deck.Card newCard = d1.new PlayingCard("c",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("d",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("h",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("s",i);
            d1.addCard(newCard);
        }


        Deck.Card current = d1.head.prev;
        d1.moveCard(current,1);


        String result = "";
        int noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            result=result+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        String expected = "AC AS AD AH ";
        if (!expected.equals(result)){
            throw new AssertionError("got " + result
                    + " but expected " + expected );
        }
        System.out.println("moveCardTailby1 test passed.");
    }
}

class moveCardTailby2 implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck();
        for (int i=1;i<=1;i++){
            Deck.Card newCard = d1.new PlayingCard("c",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("d",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("h",i);
            d1.addCard(newCard);
            newCard = d1.new PlayingCard("s",i);
            d1.addCard(newCard);
        }


        Deck.Card current = d1.head.prev;
        d1.moveCard(current,2);


        String result = "";
        int noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            result=result+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        String expected = "AC AD AS AH ";
        if (!expected.equals(result)){
            throw new AssertionError("got " + result
                    + " but expected " + expected );
        }
        System.out.println("moveCardTailby2 test passed.");
    }
}

////////////////////////////////////
// Question 7

class tripleCut implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck();

        String[] suits = {"c","c","c","c","c","d","d","d","d","c","c",
                "j","c","c","d","d","d","d","c",
                "j","c","c","c","d","d","d","d","d"};
        int[] rank = {1,4,7,10,13,3,6,9,12,3,6,0,9,12,2,5,8,11,2,0,5,8,11,1,4,7,10,13};
        for (int i=0;i<rank.length;i++){
            if (i == 11){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 19 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                Deck.Card newCard = d1.new PlayingCard(suits[i],rank[i]);
                d1.addCard(newCard);

            }
        }


        Deck.Card current = d1.head;

        String originalDeck = "";
        int noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            originalDeck=originalDeck+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        System.out.println("original deck :" + originalDeck);
        Deck.Card firstJoker =d1.locateJoker("black");
        Deck.Card secondJoker =d1.locateJoker("red");
        d1.tripleCut(firstJoker,secondJoker);

        String result = "";

        noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            result=result+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        System.out.println("new deck :" + result);
        String expected = "5C 8C JC AD 4D 7D 10D KD BJ 9C QC 2D 5D 8D JD 2C RJ AC 4C 7C 10C KC 3D 6D 9D QD 3C 6C ";
        if (!expected.equals(result)){
            throw new AssertionError("got " + result
                    + " but expected " + expected );
        }
        System.out.println("tripleCut test passed.");
    }
}


class tripleCutFirstCardHead implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck();

        String[] suits = {"j","c","c","d","d","d","d","c",
                "j","c","c","c","d","d","d","d","d"};
        int[] rank = {0,9,12,2,5,8,11,2,0,5,8,11,1,4,7,10,13};
        for (int i=0;i<rank.length;i++){
            if (i == 0){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 8 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                Deck.Card newCard = d1.new PlayingCard(suits[i],rank[i]);
                d1.addCard(newCard);

            }
        }


        Deck.Card current = d1.head;

        String originalDeck = "";
        int noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            originalDeck=originalDeck+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        System.out.println("original deck :" + originalDeck);
        Deck.Card firstJoker =d1.locateJoker("black");
        Deck.Card secondJoker =d1.locateJoker("red");
        d1.tripleCut(firstJoker,secondJoker);

        String result = "";

        noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            result=result+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        System.out.println("new deck :" + result);
        String expected = "5C 8C JC AD 4D 7D 10D KD BJ 9C QC 2D 5D 8D JD 2C RJ ";
        if (!expected.equals(result)){
            throw new AssertionError("got " + result
                    + " but expected " + expected );
        }
        System.out.println("tripleCutFirstCardHead test passed.");
    }
}


class tripleCutsecondcardbottom implements Runnable {
    @Override
    public void run() {
        Deck d1 = new Deck();

        String[] suits = {"d","c","c","j","d","d","d","c",
                "c","c","c","c","d","d","d","d","j"};
        int[] rank = {0,9,12,2,5,8,11,2,0,5,8,11,1,4,7,10,4};
        for (int i=0;i<rank.length;i++){
            if (i == 3){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 16 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                Deck.Card newCard = d1.new PlayingCard(suits[i],rank[i]);
                d1.addCard(newCard);

            }
        }


        Deck.Card current = d1.head;

        String originalDeck = "";
        int noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){
            originalDeck=originalDeck+current.toString()+ " ";
            current=current.next;
            noCards--;
        }
        System.out.println("original deck: " + originalDeck);
        Deck.Card firstJoker =d1.locateJoker("black");
        Deck.Card secondJoker =d1.locateJoker("red");
        d1.tripleCut(firstJoker,secondJoker);

        String result = "";

        noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){
            result=result+current.toString()+ " ";
            current=current.next;
            noCards--;
        }
        System.out.println("new deck :" + result);
        String expected = "BJ 5D 8D JD 2C 0C 5C 8C JC AD 4D 7D 10D RJ 0D 9C QC ";
        if (!expected.equals(result)){
            throw new AssertionError("got " + result
                    + " but expected " + expected );
        }
        System.out.println("tripleCutsecondcardbottom test passed.");
    }
}

////////////////////////////////////
// Question 8

class countCut implements Runnable {

    @Override
    public void run() {
        Deck d1 = new Deck();
        String fullSuitName="";

        /*String[] suits = {"c","c","c","c","c","d","d","d","d","c","c",
                "j","c","c","d","d","d","d","c",
                "j","c","c","c","d","d","d","d","d"};
        int[] rank = {1,4,7,10,13,3,6,9,12,3,6,0,9,12,2,5,8,11,2,0,5,8,11,1,4,7,10,4};*/

        //String[] suits = {"c","c","c","c","c","c","c","c","c","c","c","c","c"};
        //int[] rank = {13,12,11,10,9,8,7,6,5,4,3,2,5};
        String[] suits = {"C","C","C","D","D","D", "D", "D", "J",
                "C", "C", "D", "D", "D", "D", "C", "J",
                "C", "C", "C", "C", "C", "D", "D","D", "D", "C", "C"};

        int[] rank = {5,  8, 11,  1, 4,  7, 10,13, 0,
                9, 12, 2,  5, 8, 11, 2,0,
                1,  4, 7, 10,13, 3, 6, 9,12, 3, 6};
        for (int i=0;i<rank.length;i++){
            if (i == 8){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 16 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                switch(suits[i]) {
                    case "C":
                        fullSuitName ="clubs";
                        break;
                    case "D":
                        fullSuitName ="diamonds";
                        break;
                    case "H":
                        fullSuitName ="hearts";
                        break;
                    case "S":
                        fullSuitName ="spades";
                        break;

                }
                Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
                d1.addCard(newCard);

            }
        }


        Deck.Card current = d1.head;

        String originalDeck = "";
        int noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            originalDeck=originalDeck+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        System.out.println("original deck :" + originalDeck);

        d1.countCut();

        String result = "";

        noCards=d1.numOfCards;
        current = d1.head;
        while(current != d1.head || noCards !=0){

            result=result+current.toString()+ " ";

            current=current.next;
            noCards--;
        }
        System.out.println("new deck :" + result);
        String expected = "10D KD BJ 9C QC 2D 5D 8D JD 2C RJ AC 4C 7C 10C KC 3D 6D 9D QD 3C 5C 8C JC AD 4D 7D 6C ";
        if (!expected.equals(result)){
            throw new AssertionError("got " + result
                    + " but expected " + expected );
        }
        System.out.println("countCut test passed.");
    }
}


////////////////////////////////////
// Question 9

class lookUpCard implements Runnable {

    @Override
    public void run() {
        Deck d1 = new Deck();
        String fullSuitName="";


        String[] suits = {"C","C","C","D","D","D", "D", "D", "J",
                "C", "C", "D", "D", "D", "D", "C", "J",
                "C", "C", "C", "C", "C", "D", "D","D", "D", "C", "C"};

        int[] rank = {5,  8, 11,  1, 4,  7, 10,13, 0,
                9, 12, 2,  5, 8, 11, 2,0,
                1,  4, 7, 10,13, 3, 6, 9,12, 3, 6};
        for (int i=0;i<rank.length;i++){
            if (i == 8){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 16 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                switch(suits[i]) {
                    case "C":
                        fullSuitName ="clubs";
                        break;
                    case "D":
                        fullSuitName ="diamonds";
                        break;
                    case "H":
                        fullSuitName ="hearts";
                        break;
                    case "S":
                        fullSuitName ="spades";
                        break;

                }
                Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
                d1.addCard(newCard);

            }
        }

        Deck.Card current = d1.head;
        d1.countCut();

        Deck.Card cardReturned = d1.lookUpCard();
        System.out.println (cardReturned.toString());

        String expected = "JC";

        if (!cardReturned.toString().equals(expected)){
            throw new AssertionError("got " + cardReturned.toString()
                    + " but expected " + expected );
        }
        System.out.println("lookUpCard test passed.");
    }
}

////////////////////////////////////
// Question 10

class generateNextKeystreamValue implements Runnable {

    @Override
    public void run() {
        Deck d1 = new Deck();
        String fullSuitName="";


        String[] suits = {"C","C","C","C","C","D","D","D","D",
                "J","C","C","C","C","D","D","D","D",
                "J","C","C","C","C","D","D","D","D","D"};

        int[] rank = { 1, 4, 7,10,13, 3, 6, 9,12,
                0, 3, 6, 9,12, 2, 5, 8,11,
                0,2, 5, 8,11, 1,4, 7,10,13};
        for (int i=0;i<rank.length;i++){
            if (i == 9){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 18 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                switch(suits[i]) {
                    case "C":
                        fullSuitName ="clubs";
                        break;
                    case "D":
                        fullSuitName ="diamonds";
                        break;
                    case "H":
                        fullSuitName ="hearts";
                        break;
                    case "S":
                        fullSuitName ="spades";
                        break;

                }
                Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
                d1.addCard(newCard);

            }
        }
        int valueReturned = d1.generateNextKeystreamValue()
                ;
        //d1.countCut();
        if (valueReturned != 11){
            throw new AssertionError("got " + valueReturned
                    + " but expected 11 " );
        }
        System.out.println("generateNextKeystreamValue test passed.");
    }
}

////////////////////////////////////
// Question 10

class SolitaireCiphertest implements Runnable {
    // helper to print the deck
    public void printdeck (Deck deckToPrint){
        String result="";
        Deck.Card actual=deckToPrint.head;
        int noCard=deckToPrint.numOfCards;
        while(actual != deckToPrint.head || noCard !=0){

            result=result+actual.toString()+ " ";

            actual=actual.next;
            noCard--;
        }
        System.out.println(result);
    }



    @Override
    public void run() {
        Deck d1 = new Deck();
        String fullSuitName="";


        String[] suits = {"C","D","D","C","J","C","D","D","C","J","C","D"};
        int[] rank = {3,3,1,5,0,2,2,4,1,0,4,5};

        for (int i=0;i<rank.length;i++){
            if (i == 4){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 9 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                switch(suits[i]) {
                    case "C":
                        fullSuitName ="clubs";
                        break;
                    case "D":
                        fullSuitName ="diamonds";
                        break;
                    case "H":
                        fullSuitName ="hearts";
                        break;
                    case "S":
                        fullSuitName ="spades";
                        break;

                }
                Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
                d1.addCard(newCard);

            }
        }
        printdeck(d1);
        // d1.shuffle();
        //printdeck(d1);
        SolitaireCipher sc = new SolitaireCipher(d1);

        int [] key;
        key = sc.getKeystream(12);

        int [] expected = {4, 4, 15, 3, 3, 2, 1, 14, 16, 17, 17, 14};
        boolean flag =true;
        for (int i = 0;i<expected.length;i++){
            if (expected[i]!=key[i]){

            }
        }
        if (!flag){
            throw new AssertionError("got " + Arrays.toString(key)
                    + " but expected  " + Arrays.toString(expected));
        }
        System.out.println("SolitaireCiphertest test passed.");
    }
}

////////////////////////////////////
// Question 12

class Encodetest implements Runnable {




    @Override
    public void run() {
        Deck d1 = new Deck();
        String fullSuitName="";


        //String[] suits = {"C","C","C","C","C","D","D","D","D","D","J","J"};
        //int[] rank = {1,2,3,4,5,1,2,3,4,5,0,0};

         String[] suits = {"C","D","D","C","J","C","D","D","C","J","C","D"};
        int[] rank = {3,3,1,5,0,2,2,4,1,0,4,5};

        for (int i=0;i<rank.length;i++){
            if (i == 4){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 9 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                switch(suits[i]) {
                    case "C":
                        fullSuitName ="clubs";
                        break;
                    case "D":
                        fullSuitName ="diamonds";
                        break;
                    case "H":
                        fullSuitName ="hearts";
                        break;
                    case "S":
                        fullSuitName ="spades";
                        break;

                }
                Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
                d1.addCard(newCard);

            }
        }

        //d1.shuffle();
        Deck.Card actual=d1.head;
        int noCard = d1.numOfCards;
        String result="";
        while(actual != d1.head || noCard !=0){

            result=result+actual.toString()+ " ";

            actual=actual.next;
            noCard--;
        }
        System.out.println(result);


        SolitaireCipher sc = new SolitaireCipher(d1);

        String encripted = sc.encode("Is that you, Bob?");
        String expected = "MWIKDVZCKSFP";
        boolean flag =true;

        if (!encripted.equals(expected)){
            throw new AssertionError("got " + encripted
                    + " but expected  " + expected);
        }
        System.out.println("Encode test passed.");
    }
}

////////////////////////////////////
// Question 12

class Decodetest implements Runnable {

    @Override
    public void run() {
        Deck d1 = new Deck();
        String fullSuitName="";


        String[] suits = {"C","D","D","C","J","C","D","D","C","J","C","D"};
        int[] rank = {3,3,1,5,0,2,2,4,1,0,4,5};

        for (int i=0;i<rank.length;i++){
            if (i == 4){
                Deck.Card newJoker =d1.new Joker("black");
                d1.addCard(newJoker);
            }
            else if (i== 9 ){
                Deck.Card newJoker =d1.new Joker("red");
                d1.addCard(newJoker);

            }else{
                switch(suits[i]) {
                    case "C":
                        fullSuitName ="clubs";
                        break;
                    case "D":
                        fullSuitName ="diamonds";
                        break;
                    case "H":
                        fullSuitName ="hearts";
                        break;
                    case "S":
                        fullSuitName ="spades";
                        break;

                }
                Deck.Card newCard = d1.new PlayingCard(fullSuitName, rank[i]);
                d1.addCard(newCard);

            }
        }

        // d1.shuffle();
        //printdeck(d1);
        SolitaireCipher sc = new SolitaireCipher(d1);

        String encripted = sc.decode("MWIKDVZCKSFP");


        String expected = "ISTHATYOUBOB";
        boolean flag =true;

        if (!encripted.equals(expected)){
            throw new AssertionError("got " + encripted
                    + " but expected  " + expected);
        }
        System.out.println("Encode test passed.");
    }
}
////////////////////////////////////


public class A2_Minitester_Updated1 {
    // To skip running some tests, just comment them out below.
    static String[] tests = {
        "assignment2.AddCard_AllRef",
        "assignment2.AddCard_CheckHead",
        "assignment2.AddCard_Circular",
        "assignment2.AddCard_NumOfCards",
        "assignment2.AddCard_SingleCard",
        "assignment2.DeepCopy_CheckRefs",
        "assignment2.DeepCopy_CircularNext",
        "assignment2.DeepCopy_CircularPrev",
        "assignment2.LocateJoker_Test1",
        "assignment2.LocateJoker_Test2",
        "assignment2.LocateJoker_Test3",
        "assignment2.LookUpCard_Test1",
        "assignment2.LookUpCard_Test2",
        "assignment2.LookUpCard_Test3",
        "assignment2.MoveCard_CheckNext1",
        "assignment2.MoveCard_CheckNext2",
        "assignment2.MoveCard_CheckPrev1",
        "assignment2.MoveCard_CheckPrev2",
        "assignment2.Shuffle_Empty",
        "assignment2.Shuffle_Example",
        "assignment2.Shuffle_FullDeck",
        "assignment2.Shuffle_NewCard",
        "assignment2.Shuffle_SingleCard",
        "assignment2.Shuffle_Three",
            //////
            "assignment2.default_deck",
            "assignment2.default_deck_more_than_one_card",
            "assignment2.deep_copy_deck",
            "assignment2.addOneCardToBottom",
            // "assignment2.shuffleDeck2",
            // "assignment2.shuffleDeck",
            "assignment2.locatejoker",
            "assignment2.noJokersInDeck",
            "assignment2.moveCardHead",
            "assignment2.moveCardTailby1",
            "assignment2.moveCardTailby2",
            "assignment2.tripleCut",
            "assignment2.tripleCutFirstCardHead",
            "assignment2.tripleCutsecondcardbottom",
            "assignment2.countCut",
            "assignment2.lookUpCard",
            "assignment2.generateNextKeystreamValue",
            "assignment2.SolitaireCiphertest",
            "assignment2.Encodetest",
            "assignment2.Decodetest"
    };
    public static void main(String[] args) {
        int numPassed = 0;
        for(String className: tests)    {
            System.out.printf("%n======= %s =======%n", className);
            System.out.flush();
            try {
                Runnable testCase = (Runnable) Class.forName(className).getDeclaredConstructor().newInstance();
                testCase.run();
                numPassed++;
            } catch (AssertionError e) {
                System.out.println(e);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        System.out.printf("%n%n%d of %d tests passed.%n", numPassed, tests.length);
    }
}
