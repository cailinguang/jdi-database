package com.primeton.monitor;


import com.primeton.data.*;
import com.primeton.expression.ExpressionContext;
import com.primeton.expression.ExpressionExecutor;
import com.primeton.expression.JDIExpressionUtil;
import com.primeton.monitor.sql.SqlConvert;
import com.sun.jdi.*;
import com.sun.jdi.event.BreakpointEvent;
import org.jdiscript.JDIScript;
import org.jdiscript.handlers.OnBreakpoint;

import java.util.*;
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

    /**
     * execute RequestContextHolder get sessionId
     * @param thread
     * @return
     */
    private String evalSessionId(ThreadReference thread){
        //session
        ExpressionContext context = new ExpressionContext();
        context.putObject("thread", thread);
        ExpressionExecutor.execute(sessionStr,context);

        String sessionId = (String) context.getObject("sessionId");
        return sessionId;
    }

    @Override
    public void doMonitor() {
        //prepareStatement断点，准备sql
        Location connLocation = getClassMethodLocations("oracle.jdbc.driver.PhysicalConnection","prepareStatement",1).get(0);
        j.breakpointRequest(connLocation,breakpointEvent -> {
            try{
                StackFrame stackFrame1 = breakpointEvent.thread().frame(0);
                ThreadReference thread = breakpointEvent.thread();

                //sql from prepareStatement 0 arg
                String sql = (String) JDIExpressionUtil.getObjFromRefrence(stackFrame1.getArgumentValues().get(0),thread);

                //get sessionData
                String sessionId = evalSessionId(thread);
                SessionData sessionData = ContextData.getSessionDataById(sessionId);
                if(sessionData==null){
                    return;
                }
                if(!sessionData.isMonitor()){
                    return;
                }

                //add or get threadData
                ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));
                if(threadData==null){
                    threadData = new ThreadData(String.valueOf(thread.uniqueID()));
                    //skip query sql
                    if('R' == new SqlConvert(sql).getCRUD()){
                        return;
                    }
                    sessionData.addThreadDataByThread(threadData);
                }

                //add databaseData
                DatabaseData databaseData = new DatabaseData();
                databaseData.setSql(sql);
                databaseData.setPsSql(sql);
                threadData.addDatabaseData(databaseData);

                System.out.println("tid:"+thread.uniqueID()+" add prepareStatement,sql:"+databaseData.getSql());
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
     * return prepareStatement field originalSql
     * sql
     * @param thread
     * @param prepareStatement
     * @return
     */
    private String evalPrepareStatementSql(ThreadReference thread,ObjectReference prepareStatement){
        ExpressionContext context = new ExpressionContext();
        context.putObject("thread", thread);
        context.putObject("ps",prepareStatement);
        String psSql = (String)ExpressionExecutor.execute("ps@sqlObject@originalSql",context);
        return psSql;
    }

    private ObjectReference getConnectionFromPs(ThreadReference thread,ObjectReference prepareStatement){
        ExpressionContext context = new ExpressionContext();
        context.putObject("thread", thread);
        context.putObject("ps",prepareStatement);
        ObjectReference conn = (ObjectReference) ExpressionExecutor.execute("ps@connection", context);
        return conn;
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
                    ObjectReference thisObject = stackFrame.thisObject();
                    Value indexObject = stackFrame.getArgumentValues().get(0);
                    Value valueObject = stackFrame.getArgumentValues().get(1);

                    //get sessionData
                    String sessionId = evalSessionId(thread);
                    SessionData sessionData = ContextData.getSessionDataById(sessionId);
                    if(sessionData==null){
                        return;
                    }
                    if(!sessionData.isMonitor()){
                        return;
                    }
                    //get thread data
                    ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));
                    if(threadData==null){
                        return;
                    }
                    //get databaseData
                    String psSql = evalPrepareStatementSql(thread,thisObject);
                    DatabaseData databaseData = threadData.getDatabaseDataByPssql(psSql);
                    if(databaseData==null){
                        return;
                    }

                    //set sql param
                    int index = (int)JDIExpressionUtil.getObjFromRefrence(indexObject,thread);
                    Object obj = JDIExpressionUtil.getObjFromRefrence(valueObject,thread);
                    databaseData.setParam(index,obj);

                    System.out.println("tid:"+thread.uniqueID()+" set param: index:"+index+",obj:"+obj);
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

        //before sql execute event
        OnBreakpoint before = new OnBreakpoint() {
            @Override
            public void breakpoint(BreakpointEvent breakpointEvent) {
                try{
                    StackFrame stackFrame = breakpointEvent.thread().frame(0);
                    ThreadReference thread = breakpointEvent.thread();
                    ObjectReference thisObject = stackFrame.thisObject();


                    //get sessionData
                    String sessionId = evalSessionId(thread);
                    SessionData sessionData = ContextData.getSessionDataById(sessionId);
                    if(sessionData==null){
                        return;
                    }

                    //get thread data
                    ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));
                    if(threadData==null){
                        return;
                    }
                    //get databaseData
                    String psSql = evalPrepareStatementSql(thread,thisObject);
                    DatabaseData databaseData = threadData.getDatabaseDataByPssql(psSql);
                    if(databaseData==null){
                        return;
                    }

                    ObjectReference conn = getConnectionFromPs(thread,thisObject);
                    String sql = databaseData.getSql();

                    SqlConvert convert = new SqlConvert(sql);
                    databaseData.setTableMetaData(getTableColumn(convert.getTableName(),thread,conn));
                    databaseData.setTableName(convert.getTableName());

                    String beforeSql = convert.getBrforeSql();
                    if(beforeSql.length()!=0){
                        List<DatabaseRow> datas = getSqlResult(beforeSql,thread,conn);
                        databaseData.setOriginalData(datas);

                        System.out.println("tid:"+thread.uniqueID()+" before sql:"+beforeSql);
                        System.out.println("tid:"+thread.uniqueID()+" before data:"+ Arrays.toString(datas.toArray()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        //after sql execute event
        OnBreakpoint after = new OnBreakpoint() {
            @Override
            public void breakpoint(BreakpointEvent breakpointEvent) {
                try{
                    StackFrame stackFrame = breakpointEvent.thread().frame(0);
                    ThreadReference thread = breakpointEvent.thread();
                    ObjectReference thisObject = stackFrame.thisObject();

                    //get sessionData
                    String sessionId = evalSessionId(thread);
                    SessionData sessionData = ContextData.getSessionDataById(sessionId);
                    if(sessionData==null){
                        return;
                    }

                    //get thread data
                    ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));
                    if(threadData==null){
                        return;
                    }
                    //get databaseData
                    String psSql = evalPrepareStatementSql(thread,thisObject);
                    DatabaseData databaseData = threadData.getDatabaseDataByPssql(psSql);
                    if(databaseData==null){
                        return;
                    }

                    ObjectReference conn = getConnectionFromPs(thread,thisObject);

                    String sql = databaseData.getSql();
                    SqlConvert convert = new SqlConvert(sql);
                    String afterSql = convert.getAfterSql();
                    if(afterSql.length()!=0){
                        List<DatabaseRow> datas = getSqlResult(afterSql,thread,conn);
                        databaseData.setData(datas);

                        System.out.println("tid:"+thread.uniqueID()+" after sql:"+afterSql);
                        System.out.println("tid:"+thread.uniqueID()+" after data:"+ Arrays.toString(datas.toArray()));
                    }
                    //last set type
                    databaseData.setType(convert.getCRUD());
                    //trigger list listener
                    threadData.databaseDatas.set(threadData.databaseDatas.indexOf(databaseData),databaseData);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        //set before/after event request
        List<Location> executeLocations = getClassMethodLocations("oracle.jdbc.driver.OraclePreparedStatement", "executeInternal", 0);
        //start
        j.breakpointRequest(executeLocations.get(0),before).enable();
        //end
        j.breakpointRequest(executeLocations.get(executeLocations.size()-2),after).enable();

    }

    /**
     * execute oracle sql get column remarks
     * @param tableName
     * @param thread
     * @param conn
     * @return
     */
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

    /**
     * execute sql and return  results
     * @param sql
     * @param thread
     * @param conn
     * @return
     */
    public List<DatabaseRow> getSqlResult(String sql, ThreadReference thread, ObjectReference conn){
        List<DatabaseRow> result = new ArrayList();
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
            DatabaseRow row = new DatabaseRow();
            row.setColumns(columnNames);
            row.setValues(new String[columnNames.length]);
            for(int i=0;i<columnNames.length;i++){
                row.getValues()[i] =
                        (String)ExpressionExecutor.execute("rs@getString(\""+columnNames[i]+"\")",context);
            }
            result.add(row);
        }

        return result;
    }

}
