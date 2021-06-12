package com.spark_exam.models;

public enum Game {
    BACCARAT("baccarat"),
    POKER("poker"),
    BLACKJACK("blackjack"),
    CANASTA("canasta"),
    CRIBBAGE("cribbage"),
    FARO("faro"),
    MONTE("monte"),
    RUMMY("rummy"),
    WHIST("whist"),
    BACCARAT_DEMO("baccarat-demo"),
    POKER_DEMO("poker-demo"),
    BLACKJACK_DEMO("blackjack-demo"),
    CANASTA_DEMO("canasta-demo"),
    CRIBBAGE_DEMO("cribbage-demo"),
    FARO_DEMO("faro-demo"),
    MONTE_DEMO("monte-demo"),
    RUMMY_DEMO("rummy-demo"),
    WHIST_DEMO("whist-demo");

    public final String gameName;

    private Game(String gameName) {
        this.gameName = gameName;
    }

}
