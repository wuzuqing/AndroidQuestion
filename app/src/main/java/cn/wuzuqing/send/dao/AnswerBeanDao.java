package cn.wuzuqing.send.dao;
import cn.wuzuqing.send.bean.AnswerBean;
import cn.wuzuqing.lib_api.AbstractDao;
import cn.wuzuqing.lib_api.Database;
import android.database.sqlite.SQLiteStatement;
import android.database.Cursor;
public class AnswerBeanDao extends AbstractDao<Long, AnswerBean> {

	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS answer";

	public AnswerBeanDao(Database database) {
		super(database);
		 isSetPrimaryKey = CREATE_TABLE_SQL.contains("autoincrement");
	}

	@Override 
	protected String tableName() { return "answer"; }

	@Override 
	protected String keyName() { return "id"; }


	@Override 
	protected void setKey(AnswerBean entity,Long id) {
		 entity.setId(id) ;
	 }


	@Override 
	protected Long readKey(AnswerBean entity) {
 return entity.getId(); 
}

	public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS answer (id INTEGER primary key autoincrement ,questionId INTEGER,content TEXT,url TEXT)";

	@Override 
	protected String[] getColumns(){
		 return new String[]{"id","questionId","content","url"};
	}


	@Override 
	protected AnswerBean readEntity(Cursor cursor, int offset) {
		return new AnswerBean(
			cursor.isNull(offset + 0)? null : cursor.getLong(0 + offset),
			cursor.isNull(offset + 1)? null : cursor.getInt(1 + offset),
			cursor.isNull(offset + 2)? null : cursor.getString(2 + offset),
			cursor.isNull(offset + 3)? null : cursor.getString(3 + offset));
	}

	@Override 
	protected void bindValue(SQLiteStatement stmt, AnswerBean entity) {
		stmt.clearBindings();
		Long id = entity.getId();
		if ( id != null) {
			stmt.bindLong(1, id);
		}
		Integer questionId = entity.getQuestionId();
		if ( questionId != null) {
			stmt.bindLong(2, questionId);
		}
		String content = entity.getContent();
		if ( content != null) {
			stmt.bindString(3, content);
		}
		String url = entity.getUrl();
		if ( url != null) {
			stmt.bindString(4, url);
		}
	}
}
