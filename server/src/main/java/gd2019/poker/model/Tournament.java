package gd2019.poker.model;

import gd2019.poker.dto.ChatMessage;
import gd2019.poker.dto.TournamentDTO;
import gd2019.poker.model.enums.PlayerStatus;
import gd2019.poker.model.enums.TournamentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tournament {

    private static final PokerHandEval eval = PokerHandEval.defaultEvaluator();
    public static final int DEFAULT_BALANCE = 1000;
    public static final int DEFAULT_SMALL_BLIND = 10;
    public static final int DEFAULT_BIG_BLIND = 20;

    private List<Player> players = new ArrayList<>();
    private Player smallBlindPlayer;
    private Player bigBlindPlayer;
    private int smallBlindValue;
    private int bigBlindValue;
    private TournamentStatus status;
    private Player currentPlayer;
    private DeckOfCards deckOfCards = new DeckOfCards();
    private List<ClassicCard> tableCards = new ArrayList<>();
    private int round;
    private int prizePoll;
    private List<ChatMessage> messages = new ArrayList<>();

    public Tournament(){
        smallBlindValue = DEFAULT_SMALL_BLIND;
        bigBlindValue = DEFAULT_BIG_BLIND;
    }

    public void addPlayer(Player player){
        players.add(player);
        player.setStatus(PlayerStatus.WAITING);
        player.setCurrentTournament(this);
    }

    public void addTableCard(ClassicCard card) {
        tableCards.add(card);
    }

    public void addTableCards(List<ClassicCard> cards) {
        tableCards.addAll(cards);
    }

    public TournamentDTO toDto() {
        return TournamentDTO.builder()
                .players(players.stream()
                        .map(Player::toDTO).collect(Collectors.toList())
                )
                .messages(messages)
                .prizePool(prizePoll)
                .status(status)
                .currentPlayerId(currentPlayer != null ? currentPlayer.getId() : null)
                .tableCards(tableCards.stream()
                        .map(ClassicCard::toDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    public void addChatMessage(ChatMessage message) {
        messages.add(message);
    }

    public static PokerHandEval getEval() {
        return eval;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getSmallBlindPlayer() {
        return smallBlindPlayer;
    }

    public void setSmallBlindPlayer(Player smallBlindPlayer) {
        this.smallBlindPlayer = smallBlindPlayer;
    }

    public Player getBigBlindPlayer() {
        return bigBlindPlayer;
    }

    public void setBigBlindPlayer(Player bigBlindPlayer) {
        this.bigBlindPlayer = bigBlindPlayer;
    }

    public int getSmallBlindValue() {
        return smallBlindValue;
    }

    public void setSmallBlindValue(int smallBlindValue) {
        this.smallBlindValue = smallBlindValue;
    }

    public int getBigBlindValue() {
        return bigBlindValue;
    }

    public void setBigBlindValue(int bigBlindValue) {
        this.bigBlindValue = bigBlindValue;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public DeckOfCards getDeckOfCards() {
        return deckOfCards;
    }

    public void setDeckOfCards(DeckOfCards deckOfCards) {
        this.deckOfCards = deckOfCards;
    }

    public List<ClassicCard> getTableCards() {
        return tableCards;
    }

    public void setTableCards(List<ClassicCard> tableCards) {
        this.tableCards = tableCards;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getPrizePoll() {
        return prizePoll;
    }

    public void setPrizePoll(int prizePoll) {
        this.prizePoll = prizePoll;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<Player> getNotFoldedPlayers(){
        return players.stream().filter(Player::isNotFold).collect(Collectors.toList());
    }
}