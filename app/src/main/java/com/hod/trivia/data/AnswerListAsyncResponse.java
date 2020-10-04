package com.hod.trivia.data;

import com.hod.trivia.model.Question;

import java.util.List;

public interface AnswerListAsyncResponse {
    void processFinished(List<Question> questionList);
}