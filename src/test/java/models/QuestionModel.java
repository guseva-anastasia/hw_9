package models;

import java.util.List;

public class QuestionModel {
public String question;
public List<String> options;
public String answer;

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOption() {
        return options;
    }
}
