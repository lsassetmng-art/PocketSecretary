package com.lsam.pocketsecretary.consult;

public class SelectInputProvider implements ConsultInputProvider {
    private final QuestionType q;
    public SelectInputProvider(QuestionType q){ this.q=q; }
    @Override public QuestionType getQuestion(){ return q; }
}
