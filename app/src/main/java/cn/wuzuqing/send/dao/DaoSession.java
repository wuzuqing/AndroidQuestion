package cn.wuzuqing.send.dao;

import java.util.Arrays;
import java.util.List;

import cn.wuzuqing.lib_api.Database;
import cn.wuzuqing.lib_api.LifeCallBack;


public class DaoSession implements LifeCallBack {
    private QuestionTagBeanDao questionTagBeanDao;
    private AnswerBeanDao answerBeanDao;
    private QuestionBeanDao questionBeanDao;

    public DaoSession(Database database) {
        questionTagBeanDao = new QuestionTagBeanDao(database);
        answerBeanDao = new AnswerBeanDao(database);
        questionBeanDao = new QuestionBeanDao(database);
    }

    public QuestionTagBeanDao getQuestionTagBeanDao() {
        return questionTagBeanDao;
    }

    public AnswerBeanDao getAnswerBeanDao() {
        return answerBeanDao;
    }

    public QuestionBeanDao getQuestionBeanDao() {
        return questionBeanDao;
    }

    @Override
    public List<String> getAllCreateSql() {
        return Arrays.asList(QuestionTagBeanDao.CREATE_TABLE_SQL, AnswerBeanDao.CREATE_TABLE_SQL, QuestionBeanDao.CREATE_TABLE_SQL);
    }

    @Override
    public List<String> getAllDropSql() {
        return Arrays.asList(QuestionTagBeanDao.DROP_TABLE_SQL, AnswerBeanDao.DROP_TABLE_SQL, QuestionBeanDao.DROP_TABLE_SQL);
    }

}