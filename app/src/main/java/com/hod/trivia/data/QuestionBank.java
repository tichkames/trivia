package com.hod.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hod.trivia.controller.AppController;
import com.hod.trivia.model.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    private static final String TAG = QuestionBank.class.getSimpleName();

    private List<Question> questionArrayList = new ArrayList<>();
    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callBack) {
        fetchWebQuestions(callBack);
        return questionArrayList;
    }

    private void fetchWebQuestions(final AnswerListAsyncResponse callBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            Question question = new Question(response.getJSONArray(i).get(0).toString(), (boolean) response.getJSONArray(i).get(1));
                            questionArrayList.add(question);
    //                        Log.d(TAG, "onResponse fetchWebQuestions: " + question);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(null != callBack)
                        callBack.processFinished(questionArrayList);

                }, error -> Log.d(TAG, "error fetchWebQuestions"));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
