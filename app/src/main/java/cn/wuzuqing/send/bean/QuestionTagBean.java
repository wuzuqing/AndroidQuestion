package cn.wuzuqing.send.bean;

import cn.wuzuqing.lib_annotation.*;

@Entity()
public class QuestionTagBean {

	@Id(autoincrement = true)
	private Long id;

	private String tag;

	private Integer enable;

	@Gentrace(-1793051172)
	public QuestionTagBean(){
	}

	@Gentrace(-2064684945)
	public QuestionTagBean(Long id,String tag,Integer enable){
		this.id = id;
		this.tag = tag;
		this.enable = enable;
	}

	public void setId ( Long id ){
		this.id = id;
	}

	public Long getId () {
		return this.id ;
	}

	public void setTag ( String tag ){
		this.tag = tag;
	}

	public String getTag () {
		return this.tag ;
	}

	public void setEnable ( Integer enable ){
		this.enable = enable;
	}

	public Integer getEnable () {
		return this.enable ;
	}

	@Override
	public String toString() {
		return "QuestionTagBean{" +
				"id=" + id + 
				",tag=" + tag + 
				",enable=" + enable + 
				"}";
	}

}
