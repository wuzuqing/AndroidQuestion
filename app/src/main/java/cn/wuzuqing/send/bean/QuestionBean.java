package cn.wuzuqing.send.bean;

import cn.wuzuqing.lib_annotation.*;

@Entity( nameInDb = "question")
public class QuestionBean {

	@Id()
	private Long id;

	private String questionTagStr;

	private String question;

	private String createDate;

	@Gentrace(-1353866761)
	public QuestionBean(){
	}

	@Gentrace(766554944)
	public QuestionBean(Long id,String questionTagStr,String question,String createDate){
		this.id = id;
		this.questionTagStr = questionTagStr;
		this.question = question;
		this.createDate = createDate;
	}

	public void setId ( Long id ){
		this.id = id;
	}

	public Long getId () {
		return this.id ;
	}

	public void setQuestionTagStr ( String questionTagStr ){
		this.questionTagStr = questionTagStr;
	}

	public String getQuestionTagStr () {
		return this.questionTagStr ;
	}

	public void setQuestion ( String question ){
		this.question = question;
	}

	public String getQuestion () {
		return this.question ;
	}

	public void setCreateDate ( String createDate ){
		this.createDate = createDate;
	}

	public String getCreateDate () {
		return this.createDate ;
	}

	@Override
	public String toString() {
		return "QuestionBean{" +
				"id=" + id + 
				",questionTagStr=" + questionTagStr + 
				",question=" + question + 
				",createDate=" + createDate + 
				"}";
	}

}
