// Design a class that represents a deck of cards
// It should be used for game where multiple decks are involved
// suits - heart, club, diamond, spade
// implement shuffle decks for the game
// implement draw a random card from deck
// (deal cards to X players)

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeckOfCardsDesign {
    public enum Suit {
        Club,
        Spade,
        Heart,
        Diamond
    }

    public interface ICard {
        Suit getSuit();

        int getNumber();
    }

    public class Card implements ICard {
        private Suit suit;
        private int number;

        public Card(Suit suit, int number) {
            this.suit = suit;
            this.number = number;
        }

        public Suit getSuit() {
            return this.suit;
        }

        public int getNumber() {
            return this.number;
        }
    }

    public class CardDeck {
        public List<Card> cards;
        public List<Card> original;

        public CardDeck() {
            cards = new ArrayList<>();
            for (Suit suit : Suit.values()) {
                for (int i = 1; i <= 13; i++)
                    cards.add(new Card(suit, i));
            }
            original = new ArrayList<>(cards);
        }

        public void shuffleCard() {
            Random random = new Random();
            for (int i = 0; i < 52; i++) {
                int idx = random.nextInt(i + 1);
                Card temp = cards.get(i);
                cards.set(i, cards.get(idx));
                cards.set(idx, temp);
            }
        }

        public void unShuffleCard() {
            cards = original;
            original = new ArrayList<>();
        }

        public Card drawCard() {
            Card firstCard = cards.get(0);
            cards.remove(0);
            return firstCard;
        }

        public Card drawCardRandomly() {
            Random random = new Random();
            int idx = random.nextInt(cards.size() + 1);
            Card card = cards.get(idx);
            cards.remove(idx);
            return card;
        }
    }
}
