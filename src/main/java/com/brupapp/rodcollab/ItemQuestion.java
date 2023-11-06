package com.brupapp.rodcollab;

public class ItemQuestion {
    String word;
    String tip;

    protected ItemQuestion() {

    }

    ItemQuestion(String word, String tip) {
        this.word = word;
        this.tip = tip;
    }

    public String getWord() {
        return word;
    }



    @Override
    public String toString() {
        return String.format(
                "WordAnswer[word=%s, tip='%s']",
                word, tip);
    }
}
