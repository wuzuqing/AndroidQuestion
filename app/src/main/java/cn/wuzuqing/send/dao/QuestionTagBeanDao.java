package cn.wuzuqing.send.dao;
import cn.wuzuqing.send.bean.QuestionTagBean;
import cn.wuzuqing.lib_api.AbstractDao;
import cn.wuzuqing.lib_api.Database;
import android.database.sqlite.SQLiteStatement;
import android.database.Cursor;
public class QuestionTagBeanDao extends AbstractDao<Long, QuestionTagBean> {

	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS question_tag_bean";

	public QuestionTagBeanDao(Database database) {
		super(database);
		 isSetPrimaryKey = CREATE_TABLE_SQL.contains("autoincrement");
	}

	@Override 
	protected String tableName() { return "question_tag_bean"; }

	@Override 
	protected String keyName() { return "id"; }


	@Override 
	protected void setKey(QuestionTagBean entity,Long id) {
		 entity.setId(id) ;
	 }


	@Override 
	protected Long readKey(QuestionTagBean entity) {
 return entity.getId(); 
}

	public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS question_tag_bean (id INTEGER primary key autoincrement ,tag TEXT,enable INTEGER)";

	@Override 
	protected String[] getColumns(){
		 return new String[]{"id","tag","enable"};
	}


	@Override 
	protected QuestionTagBean readEntity(Cursor cursor, int offset) {
		return new QuestionTagBean(
			cursor.isNull(offset + 0)? null : cursor.getLong(0 + offset),
			cursor.isNull(offset + 1)? null : cursor.getString(1 + offset),
			cursor.isNull(offset + 2)? null : cursor.getInt(2 + offset));
	}

	@Override 
	protected void bindValue(SQLiteStatement stmt, QuestionTagBean entity) {
		stmt.clearBindings();
		Long id = entity.getId();
		if ( id != null) {
			stmt.bindLong(1, id);
		}
		String tag = entity.getTag();
		if ( tag != null) {
			stmt.bindString(2, tag);
		}
		Integer enable = entity.getEnable();
		if ( enable != null) {
			stmt.bindLong(3, enable);
		}
	}
}
