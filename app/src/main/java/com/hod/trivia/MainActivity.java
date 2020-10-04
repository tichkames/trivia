package com.hod.trivia;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.hod.trivia.data.QuestionBank;
import com.hod.trivia.model.Question;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView tvQuestion;
    private TextView tvCounter;
    private Button btnTrue;
    private Button btnfalse;
    private ImageButton btnNext;
    private ImageButton btnPrev;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion = findViewById(R.id.tv_question);
        tvCounter = findViewById(R.id.tv_counter);
        btnTrue = findViewById(R.id.btn_true);
        btnfalse = findViewById(R.id.btn_false);
        btnNext = findViewById(R.id.ibtn_next);
        btnPrev = findViewById(R.id.ibtn_prev);

        btnTrue.setOnClickListener(this);
        btnfalse.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);

        new QuestionBank().getQuestions(questionList -> {
            tvCounter.setText(String.format("%d out of %d" , currentQuestionIndex + 1, questionList.size()));
            tvQuestion.setText(questionList.get(currentQuestionIndex).getQuestion());
            this.questionList = questionList;
            Log.d(TAG, "onCreate: " + questionList);
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_true:
                checkAnswer(questionList.get(currentQuestionIndex).isAnswerTrue(), true);
                break;
            case R.id.btn_false:
                checkAnswer(questionList.get(currentQuestionIndex).isAnswerTrue(), false);
                break;

            case R.id.ibtn_next:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                btnPrev.setEnabled(true);
                break;

            case R.id.ibtn_prev:
                if(currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                } else {
                    btnPrev.setEnabled(false);
                    Toast.makeText(this, "This is the first question", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void updateQuestion() {
        tvQuestion.setText(questionList.get(currentQuestionIndex).getQuestion());
        tvCounter.setText(String.format("%d out of %d" , currentQuestionIndex + 1, questionList.size()));
        Log.d(TAG, "currentQuestionIndex: " + currentQuestionIndex);
    }

    private void checkAnswer(boolean answer, boolean userAnswer){
        updateQuestion();
        int toastMessageId = 0;
        if(answer == userAnswer) {
            fadeView();
            toastMessageId = R.string.correct_answer;
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        } else {
            shakeAnimation();
            toastMessageId = R.string.wrong_answer;
        }

        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT)
                .show();
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView() {
        CardView cardView =findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}