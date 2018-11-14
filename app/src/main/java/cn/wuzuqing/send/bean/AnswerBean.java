package cn.wuzuqing.send.bean;

import cn.wuzuqing.lib_annotation.*;

@Entity( nameInDb = "answer")
public class AnswerBean {

	@Id(autoincrement = true)
	private Long id;

	private Integer questionId;

	private String content;

	private String url;

	@Gentrace(-1114233960)
	public AnswerBean(){
	}

	@Gentrace(1070085828)
	public AnswerBean(Long id,Integer questionId,String content,String url){
		this.id = id;
		this.questionId = questionId;
		this.content = content;
		this.url = url;
	}

	public void setId ( Long id ){
		this.id = id;
	}

	public Long getId () {
		return this.id ;
	}

	public void setQuestionId ( Integer questionId ){
		this.questionId = questionId;
	}

	public Integer getQuestionId () {
		return this.questionId ;
	}

	public void setContent ( String content ){
		this.content = content;
	}

	public String getContent () {
		return this.content ;
	}

	public void setUrl ( String url ){
		this.url = url;
	}

	public String getUrl () {
		return this.url ;
	}

	@Override
	public String toString() {
		return "AnswerBean{" +
				"id=" + id + 
				",questionId=" + questionId + 
				",content=" + content + 
				",url=" + url + 
				"}";
	}

}
