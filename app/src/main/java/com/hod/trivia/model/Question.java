package com.hod.trivia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Question {
    private String question;
    private boolean answerTrue;
}