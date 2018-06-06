package com.blueocean.azbrain.vo;

import com.blueocean.azbrain.common.status.QuestionStatus;
import com.blueocean.azbrain.model.Answer;
import com.blueocean.azbrain.model.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionVo {
    private Integer id;
    private String title;
    private String content;
    private String icon;
    List<AnswerVo> answers;

    @Data
    public static class AnswerVo {
        private Integer id;
        private String content;
        @JsonProperty("create_id")
        private int createId;

        public Answer asAnswer(){
            Answer answer = new Answer();
            answer.setId(id);
            answer.setCommentNum(0);
            answer.setLikeNum(0);
            answer.setViewNum(0);
            answer.setCreateBy(this.createId);
            answer.setContent(this.content);
            answer.setCreateTime(new Date());
            answer.setStatus(QuestionStatus.NORMAL.getCode());

            return answer;
        }
    }

    public Question asQuestion(){
       Question question = new Question();
       question.setId(id);
       question.setContent(this.content);
       question.setTitle(this.title);
       question.setCreateTime(new Date());
       question.setAnswerNum(this.answers.size());
       question.setFollowerNum(0);
       question.setIcon(this.icon);
       question.setStatus(QuestionStatus.NORMAL.getCode());
       question.setRecommend(0);

       return question;
    }

    public List<Answer> asUpdateAnswers(){
        List<Answer> answerList = new ArrayList<>();
        for (AnswerVo v : this.answers){
            if (v.id != null && v.id > 0) {
                answerList.add(v.asAnswer());
            }
        }

        return answerList;
    }

    public List<Answer> asNewAnswers(){
        List<Answer> answerList = new ArrayList<>();
        for (AnswerVo v : this.answers){
            if (v.id == null || v.id == 0) {
                answerList.add(v.asAnswer());
            }
        }

        return answerList;
    }
}
