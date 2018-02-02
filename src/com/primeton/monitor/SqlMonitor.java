package com.primeton.monitor;


import com.primeton.data.ContextData;
import com.primeton.data.DatabaseData;
import com.primeton.data.SessionData;
import com.primeton.data.ThreadData;
import com.primeton.expression.ExpressionContext;
import com.primeton.expression.ExpressionExecutor;
import com.primeton.expression.JDIExpressionUtil;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.BreakpointEvent;
import org.jdiscript.JDIScript;
import org.jdiscript.handlers.OnBreakpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by clg on 2018/1/31.
 */
public class SqlMonitor extends Monitor {

    public SqlMonitor(JDIScript j) {
        super(j);
    }

    //get session
    String sessionStr  = "var requestAttrs = $org.springframework.web.context.request.RequestContextHolder$@getRequestAttributes();";
    {      sessionStr += "var request = requestAttrs@getRequest();";
           sessionStr += "var session = request@getSession();";
           sessionStr += "var sessionId = session@getId();";
    }

    @Override
    public void doMonitor() {
        //prepareStatement断点，准备sql
        Location connLocation = getClassMethodLocations("oracle.jdbc.driver.PhysicalConnection","prepareStatement",1).get(0);
        j.breakpointRequest(connLocation,breakpointEvent -> {
            try{
                StackFrame stackFrame1 = breakpointEvent.thread().frame(0);
                ThreadReference thread = breakpointEvent.thread();

                //sql
                String sql = (String) JDIExpressionUtil.getObjFromRefrence(stackFrame1.getArgumentValues().get(0));

                //session
                ExpressionContext context = new ExpressionContext();
                context.putObject("thread", thread);
                ExpressionExecutor.execute(sessionStr,context);

                String sessionId = (String) context.getObject("sessionId");
                //System.out.println("sessionID:"+sessionId);
                SessionData sessionData = ContextData.getSessionDataById(sessionId);
                ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));
                if(threadData==null){
                    threadData = new ThreadData(String.valueOf(thread.uniqueID()));
                    sessionData.addThreadDataByThread(threadData);
                }

                DatabaseData databaseData = new DatabaseData();
                databaseData.setSql(sql);
                databaseData.setPsSql(sql);

                threadData.addDatabaseData(databaseData);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).enable();

        //设置sql的参数
        setSqlParams();

        //监听执行前后，获取sql的前后变化值
        executSql();
    }


    /**
     * 设置OraclePreparedStatement 的 setObject等方法断点，依次替换?的值
     */
    private void setSqlParams() {
        Location statementLocation1 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setNull",2).get(0);
        Location statementLocation2 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setBoolean",2).get(0);
        Location statementLocation3 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setByte",2).get(0);
        Location statementLocation4 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setShort",2).get(0);
        Location statementLocation5 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setInt",2).get(0);
        Location statementLocation6 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setLong",2).get(0);
        Location statementLocation7 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setFloat",2).get(0);
        Location statementLocation8 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setDouble",2).get(0);
        Location statementLocation9 = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setBigDecimal",2).get(0);
        Location statementLocationa = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setString",2).get(0);
        Location statementLocationb = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setBytes",2).get(0);
        Location statementLocationc = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setDate",2).get(0);
        Location statementLocationd = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setTime",2).get(0);
        Location statementLocatione = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setTimestamp",2).get(0);
        Location statementLocationf = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement","setObject",2).get(0);

        //set session-thread-sql-param
        OnBreakpoint setParamHandler = new OnBreakpoint() {
            @Override
            public void breakpoint(BreakpointEvent breakpointEvent) {
                try{
                    StackFrame stackFrame = breakpointEvent.thread().frame(0);
                    ThreadReference thread = breakpointEvent.thread();

                    ExpressionContext context = new ExpressionContext();
                    context.putObject("thread", thread);

                    int index = (int)JDIExpressionUtil.getObjFromRefrence(stackFrame.getArgumentValues().get(0));
                    Object obj = JDIExpressionUtil.getObjFromRefrence(stackFrame.getArgumentValues().get(1));

                    //System.out.println("thread uid:"+thread.uniqueID()+",index:"+index+",obj:"+obj);

                    //getpssql
                    context.putObject("ps",stackFrame.thisObject());
                    String psSql = (String)ExpressionExecutor.execute("ps@sqlObject@originalSql",context);

                    //session
                    ExpressionExecutor.execute(sessionStr,context);

                    String sessionId = (String) context.getObject("sessionId");
                    //System.out.println("sessionID:"+sessionId);
                    SessionData sessionData = ContextData.getSessionDataById(sessionId);
                    ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));

                    DatabaseData databaseData = threadData.getDatabaseDataByPssql(psSql);
                    databaseData.setParam(index,obj);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        j.breakpointRequest(statementLocation1,setParamHandler).enable();
        j.breakpointRequest(statementLocation2,setParamHandler).enable();
        j.breakpointRequest(statementLocation3,setParamHandler).enable();
        j.breakpointRequest(statementLocation4,setParamHandler).enable();
        j.breakpointRequest(statementLocation5,setParamHandler).enable();
        j.breakpointRequest(statementLocation6,setParamHandler).enable();
        j.breakpointRequest(statementLocation7,setParamHandler).enable();
        j.breakpointRequest(statementLocation8,setParamHandler).enable();
        j.breakpointRequest(statementLocation9,setParamHandler).enable();
        j.breakpointRequest(statementLocationa,setParamHandler).enable();
        j.breakpointRequest(statementLocationb,setParamHandler).enable();
        j.breakpointRequest(statementLocationc,setParamHandler).enable();
        j.breakpointRequest(statementLocationd,setParamHandler).enable();
        j.breakpointRequest(statementLocatione,setParamHandler).enable();
        j.breakpointRequest(statementLocationf,setParamHandler).enable();
    }


    /**
     * 设置OraclePreparedStatement#execute断点，
     * 当sql为 insert update delete 时执行sql，获取前后对比数据
     */
    private void executSql() {
        List<Location> executeLocations = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement", "execute", 0);

        //start
        j.breakpointRequest(executeLocations.get(0),breakpointEvent -> {
            try{
                StackFrame stackFrame = breakpointEvent.thread().frame(0);
                ThreadReference thread = breakpointEvent.thread();

                ExpressionContext context = new ExpressionContext();
                context.putObject("thread", thread);

                //getpssql
                context.putObject("ps",stackFrame.thisObject());
                String psSql = (String)ExpressionExecutor.execute("ps@sqlObject@originalSql",context);
                //session
                ExpressionExecutor.execute(sessionStr,context);
                String sessionId = (String) context.getObject("sessionId");
                //System.out.println("sessionID:"+sessionId);


                ObjectReference conn = (ObjectReference) ExpressionExecutor.execute("ps@connection", context);

                SessionData sessionData = ContextData.getSessionDataById(sessionId);
                ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));

                DatabaseData databaseData = threadData.getDatabaseDataByPssql(psSql);

                String sql = databaseData.getSql();
                if(sql.matches("^\\s{0,}(?i)delete.*")){
                    databaseData.setType("delete");
                    // not execute
                }
                else if(sql.matches("^\\s{0,}(?i)update.*")){
                    databaseData.setType("update");
                    // befer update
                    String tableName = databaseData.getTableName();

                    if(sql.split("where").length==2){
                        sql = "select * from "+tableName+" where "+sql.split("where")[1];
                        List<String[]> result = getSqlResult(sql,thread,conn);
                        databaseData.setOriginalData(result);
                    }
                    //all table update
                    else{
                        sql = "select * from "+tableName;
                    }
                }
                else if(sql.matches("^\\s{0,}(?i)insert.*")){
                    databaseData.setType("insert");
                    // not execute
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }).enable();
        //end
        j.breakpointRequest(executeLocations.get(executeLocations.size()-2),breakpointEvent -> {
            try{
                StackFrame stackFrame = breakpointEvent.thread().frame(0);
                ThreadReference thread = breakpointEvent.thread();

                ExpressionContext context = new ExpressionContext();
                context.putObject("thread", thread);

                //getpssql
                context.putObject("ps",stackFrame.thisObject());
                String psSql = (String)ExpressionExecutor.execute("ps@sqlObject@originalSql",context);
                //session
                ExpressionExecutor.execute(sessionStr,context);
                String sessionId = (String) context.getObject("sessionId");

                ObjectReference conn = (ObjectReference) ExpressionExecutor.execute("ps@connection", context);

                SessionData sessionData = ContextData.getSessionDataById(sessionId);
                ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));

                DatabaseData databaseData = threadData.getDatabaseDataByPssql(psSql);

                String sql = databaseData.getSql();
                String tableName = databaseData.getTableName();
                if(sql.matches("^\\s{0,}(?i)delete.*")){
                    // not execute
                }
                else if(sql.matches("^\\s{0,}(?i)update.*")){
                    sql = "select * from "+tableName+" where "+sql.split("where")[1];
                    List<String[]> result = getSqlResult(sql,thread,conn);
                    databaseData.setData(result);
                }
                else if(sql.matches("^\\s{0,}(?i)insert.*")){
                    List<String> column_ = new ArrayList();
                    Pattern p = Pattern.compile("(?="+tableName+"\\s{0,}\\(\\s{0,})(\\w+)(?=[,\\s]?)");
                    Matcher m = p.matcher(sql);
                    while (m.find()){
                        column_.add(m.group(1));
                    }

                    List<String> values_ = new ArrayList();
                    Pattern p1 = Pattern.compile("(?=(?i)values\\s{0,}\\(\\s{0,})(\\w+)(?=[,\\s]?)");
                    Matcher m1 = p1.matcher(sql);
                    while (m1.find()){
                        values_.add(m1.group(1));
                    }

                    String where = " where ";
                    for(int i=0;i<column_.size();i++){
                        where+= " "+column_.get(i)+" = "+values_.get(0);
                    }
                    sql = "select * from "+tableName+where;
                    List<String[]> result = getSqlResult(sql,thread,conn);
                    databaseData.setData(result);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }).enable();
    }

    public Map<String,String> getTableColumn(String tableName, ThreadReference thread, ObjectReference conn){
        if(ContextData.tableMetaDataCache.containsKey(tableName)){
            return ContextData.tableMetaDataCache.get(tableName);
        }else{
            Map<String,String> map = new HashMap();
            String sql = "SELECT * FROM user_col_comments  WHERE table_name='"+tableName.toUpperCase()+"'";
            ExpressionContext context = new ExpressionContext();
            context.putObject("thread",thread);
            context.putObject("conn",conn);
            context.putObject("sql",sql);
            String expression  = "var statement = conn@createStatement();";
                   expression += "var rs = statement@executeQuery(sql)";
            ExpressionExecutor.execute(expression,context);
            while ((boolean)ExpressionExecutor.execute("rs@next()",context)){
                String columnName = (String)ExpressionExecutor.execute("rs@getString(\"COLUMN_NAME\")",context);
                String comments = (String)ExpressionExecutor.execute("rs@getString(\"COMMENTS\")",context);
                map.put(columnName,comments);
            }

            ContextData.tableMetaDataCache.put(tableName,map);
            return map;
        }
    }

    public List<String[]> getSqlResult(String sql, ThreadReference thread, ObjectReference conn){
        List<String[]> result = new ArrayList();
        ExpressionContext context = new ExpressionContext();
        context.putObject("thread",thread);
        context.putObject("conn",conn);
        context.putObject("sql",sql);
        String expression  = "var statement = conn@createStatement();";
        expression += "var rs = statement@executeQuery(sql);";
        expression += "var metaData = rs@getMetaData();";
        ExpressionExecutor.execute(expression,context);

        int count = (int)ExpressionExecutor.execute("metaData@getColumnCount()",context);
        String[] columnNames = new String[count];
        do{
            columnNames[count-1] = (String)ExpressionExecutor.execute("metaData@getColumnName("+(count)+")",context);
        }while (--count>0);


        while ((boolean)ExpressionExecutor.execute("rs@next()",context)){
            for(String c:columnNames){
                String[] keyValue = new String[2];
                keyValue[0] = c;
                keyValue[1] = (String)ExpressionExecutor.execute("rs@getString(\""+c+"\")",context);
                result.add(keyValue);
            }
        }

        return result;
    }

}
