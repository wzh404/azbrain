package com.blueocean.azbrain.vo;

import com.blueocean.azbrain.common.QuestionStatus;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionVo {
    private String title;
    private String content;
    @JsonProperty("create_id")
    private int createId;
    List<AnswerVo> answers;

    @Data
    public static class AnswerVo {
        private String content;
        @JsonProperty("create_id")
        private int createId;

        public Answer asAnswer(){
            Answer answer = new Answer();
            answer.setCommentNum(0);
            answer.setLikeNum(0);
            answer.setLikeNum(0);
            answer.setCreateBy(createId);
            answer.setContent(content);
            answer.setCreateTime(new Date());
            answer.setStatus(QuestionStatus.NORMAL.getCode());

            return answer;
        }
    }

    public Question asQuestion(){
       Question question = new Question();
       question.setContent(content);
       question.setCreateBy(createId);
       question.setTitle(title);
       question.setCreateTime(new Date());
       question.setAnswerNum(answers.size());
       question.setFollowerNum(0);
       question.setIcon("");
       question.setStatus(QuestionStatus.NORMAL.getCode());

       return question;
    }
}
