package com.brupapp.rodcollab;

public class Word {
    private int id;
    private String word;

    protected Word() {

    }

    Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }



    @Override
    public String toString() {
        return String.format(
                "Word[id=%d, word='%s']",
                id, word);
    }


}
