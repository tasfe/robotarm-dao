package robot.arm.dao.exql.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import robot.arm.dao.core.jdbc.SQLInterpreterResult;
import robot.arm.dao.exql.ExqlContext;
import robot.arm.dao.exql.util.ExqlUtils;

/**
 * 实现简单的输出上下文。
 * 
 * @author han.liao
 */
public class ExqlContextImpl implements ExqlContext, SQLInterpreterResult {
	private static final Logger log = Logger.getLogger(ExqlContextImpl.class);

    // 输出的常量
    private static final String NULL = "NULL";

    private static final char QUESTION = '?';

    private static final char COMMA = ',';

    // 参数列表
    protected final ArrayList<Object> params = new ArrayList<Object>();

    // 输出缓冲区
    protected final StringBuilder builder;

    /**
     * 构造上下文对象。
     * 
     */
    public ExqlContextImpl() {
        builder = new StringBuilder();
    }

    @Override
    public Object[] getParams() {
        return params.toArray();
    }

    @Override
    public void fillChar(char ch) {
        builder.append(ch);
    }

    @Override
    public void fillText(String string) {

        // 直接输出字符串
        builder.append(string);
    }

    @Override
    public void fillValue(Object obj) {

        if (obj instanceof Collection<?>) {

            // 展开  Collection 容器, 输出逗号分隔以支持 IN (...) 语法
            // "IN (:varlist)" --> "IN (?, ?, ...)"
            fillCollection((Collection<?>) obj);

        } else if ((obj != null) && obj.getClass().isArray() && obj.getClass() != byte[].class) {

            // 用数组构造  Collection 容器
            fillCollection(ExqlUtils.asCollection(obj));

        } else {

            // 直接输出参数, "uid > :var" --> "uid > ?"
            setParam(obj);

            builder.append(QUESTION);
        }
    }

    @Override
    public String flushOut() {
        return builder.toString();
    }

    @Override
    public String toString() {
        return flushOut();
    }

    /**
     * 设置参数的内容。
     * 
     * @param value - 参数的内容
     */
    protected void setParam(Object value) {
        params.add(value);
    }

    /**
     * 输出集合对象到语句内容, 集合将被展开成 IN (...) 语法。
     * 
     * PS: IN :varlist --> IN (?, ?, ...)
     * 
     * @param collection - 输出的集合
     */
    private void fillCollection(Collection<?> collection) {

        int count = 0;

        // 展开  Collection 容器, 输出逗号分隔以支持 IN (...) 语法
        // "IN :varlist" --> "IN (?, ?, ...)"
        if (collection.isEmpty()) {

            // 输出  "IN (NULL)" 保证不会产生错误
            builder.append(NULL);

        } else {

            // 输出逗号分隔的参数表
            for (Object value : collection) {

                if (value != null) {

                    if (count > 0) {
                        builder.append(COMMA);
                    }

                    // 输出参数内容
                    setParam(value);

                    builder.append(QUESTION);

                    count++;
                }
            }
        }
    }

    //------------------implements InterpreterResult methods
    @Override
    public Object[] getParameters() {
    	log.info("getParameters|"+getParams());
    	
        return getParams();
    }

    @Override
    public String getSQL() {
    	log.info("getSQL|"+flushOut());
    	
        return flushOut();
    }

    //--------------

    // 进行简单测试
    public static void main(String... args) throws Exception {

        ExqlContext context = new ExqlContextImpl();

        context.fillText("WHERE uid = ");
        context.fillValue(102);
        context.fillText(" AND sid IN (");
        context.fillValue(new int[] { 11, 12, 24, 25, 31, 32, 33 });
        context.fillText(") AND (create_time > ");
        context.fillValue(new Date());
        context.fillText(" OR create_time <= ");
        context.fillValue(new Date());
        context.fillChar(')');

        System.out.println(context.flushOut());
        System.out.println(Arrays.toString(context.getParams()));
    }
}
