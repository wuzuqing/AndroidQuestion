package cn.wuzuqing.send.dao;
import cn.wuzuqing.send.bean.QuestionBean;
import cn.wuzuqing.lib_api.AbstractDao;
import cn.wuzuqing.lib_api.Database;
import android.database.sqlite.SQLiteStatement;
import android.database.Cursor;
public class QuestionBeanDao extends AbstractDao<Long, QuestionBean> {

	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS question";

	public QuestionBeanDao(Database database) {
		super(database);
		 isSetPrimaryKey = CREATE_TABLE_SQL.contains("autoincrement");
	}

	@Override 
	protected String tableName() { return "question"; }

	@Override 
	protected String keyName() { return "id"; }


	@Override 
	protected void setKey(QuestionBean entity,Long id) {
		 entity.setId(id) ;
	 }


	@Override 
	protected Long readKey(QuestionBean entity) {
 return entity.getId(); 
}

	public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS question (id INTEGER primary key ,questionTag TEXT,question TEXT,createDate TEXT)";

	@Override 
	protected String[] getColumns(){
		 return new String[]{"id","questionTag","question","createDate"};
	}


	@Override 
	protected QuestionBean readEntity(Cursor cursor, int offset) {
		return new QuestionBean(
			cursor.isNull(offset + 0)? null : cursor.getLong(0 + offset),
			cursor.isNull(offset + 1)? null : cursor.getString(1 + offset),
			cursor.isNull(offset + 2)? null : cursor.getString(2 + offset),
			cursor.isNull(offset + 3)? null : cursor.getString(3 + offset));
	}

	@Override 
	protected void bindValue(SQLiteStatement stmt, QuestionBean entity) {
		stmt.clearBindings();
		Long id = entity.getId();
		if ( id != null) {
			stmt.bindLong(1, id);
		}
		String questionTag = entity.getQuestionTag();
		if ( questionTag != null) {
			stmt.bindString(2, questionTag);
		}
		String question = entity.getQuestion();
		if ( question != null) {
			stmt.bindString(3, question);
		}
		String createDate = entity.getCreateDate();
		if ( createDate != null) {
			stmt.bindString(4, createDate);
		}
	}
}
