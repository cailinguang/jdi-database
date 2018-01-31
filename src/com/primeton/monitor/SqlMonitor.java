package com.primeton.monitor;

import com.primeton.data.ContextData;
import com.primeton.data.DatabaseData;
import com.primeton.data.SessionData;
import com.primeton.data.ThreadData;
import com.primeton.expression.ExpressionContext;
import com.primeton.expression.ExpressionExecutor;
import com.sun.jdi.Location;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.BreakpointEvent;
import org.jdiscript.JDIScript;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by clg on 2018/1/31.
 */
public class SqlMonitor extends Monitor {

    public SqlMonitor(JDIScript j) {
        super(j);
    }

    @Override
    public Location setBreakPoint() throws Exception {
        return j.vm().classesByName("org.apache.ibatis.executor.SimpleExecutor").get(0).locationsOfLine(47).get(0);
    }

    @Override
    public void operate(BreakpointEvent breakpoint) throws Exception {
        StackFrame stackFrame = breakpoint.thread().frame(0);
        ThreadReference thread = stackFrame.thread();

        System.out.println("sql Monitor thread uid:"+thread.uniqueID());

        String getSql = "var boundSql = handler@delegate@boundSql;";
               getSql+= "var configuration = handler@delegate@configuration;";
               getSql+= "var parameterMappings = boundSql@getParameterMappings();";
               getSql+= "var parameterObject   = boundSql@getParameterObject();";
               getSql+= "var sql = boundSql@getSql();";
               getSql+= "var sql = sql.replaceAll(\"[\\s]+\", \" \");";

        ExpressionContext context = new ExpressionContext();
        context.putObject("thread",thread);
        context.putObject("handler",stackFrame.getValue(stackFrame.visibleVariableByName("handler")));
        ExpressionExecutor.execute(getSql,context);

        System.out.println(context.getObject("sql"));

        Object parameterMappings = context.getObject("parameterMappings");
        Object parameterObject = context.getObject("parameterObject");
        if((int)ExpressionExecutor.execute("parameterMappings@size()",context)>0 && parameterObject!=null){
            Object typeHandlerRegistry = ExpressionExecutor.execute("configuration@getTypeHandlerRegistry()",context);
            ExpressionExecutor.execute("var pClass = parameterObject@getClass()",context);
            if((boolean)ExpressionExecutor.execute("configuration@getTypeHandlerRegistry()@hasTypeHandler(pClass)",context)){
                ExpressionExecutor.execute("var sql = sql.replaceFirst(\"\\?\", getParameterValue(parameterObject))",context);
            }else{
                Object metaObject = ExpressionExecutor.execute("var metaObject = configuration@newMetaObject(parameterObject)",context);

                int size = (int) ExpressionExecutor.execute("parameterMappings@size()",context);
                int temp = 0;
                while(temp<size){
                    context.putObject("size",temp++);
                    ExpressionExecutor.execute("var parameterMapping = parameterMappings@get(size)",context);
                    ExpressionExecutor.execute("var propertyName = parameterMapping@getProperty()",context);

                    if((boolean)ExpressionExecutor.execute("metaObject@hasGetter(propertyName)",context)){
                        ExpressionExecutor.execute("var obj = metaObject@getValue(propertyName)",context);
                        context.putObject("obj",getParam(context.getObject("obj")));
                        ExpressionExecutor.execute("var sql = sql.replaceFirst(\"\\?\",obj)",context);
                    }
                    else if ((boolean)ExpressionExecutor.execute("boundSql@hasAdditionalParameter(propertyName)",context)) {
                        ExpressionExecutor.execute("var obj = boundSql@getAdditionalParameter(propertyName)",context);
                        context.putObject("obj",getParam(context.getObject("obj")));
                        ExpressionExecutor.execute("var sql = sql.replaceFirst(\"\\?\",obj)",context);
                    }
                }

            }
        }

        //get sql
        String sql = (String)context.getObject("sql");
        System.out.println(sql);

        //get session
        ExpressionExecutor.execute("var requestAttrs = $org.springframework.web.context.request.RequestContextHolder$@getRequestAttributes()",context);
        ExpressionExecutor.execute("var request = requestAttrs@getRequest()",context);
        ExpressionExecutor.execute("var session = request@getSession()",context);
        ExpressionExecutor.execute("var sessionId = session@getId()",context);

        String sessionId = (String) context.getObject("sessionId");
        System.out.println("sessionID:"+sessionId);
        SessionData sessionData = ContextData.getSessionDataById(sessionId);

        if(sessionData!=null){
            ThreadData threadData = sessionData.getThreadDataByThreadId(String.valueOf(thread.uniqueID()));
            if(threadData==null){
                threadData = new ThreadData(String.valueOf(thread.uniqueID()));
                sessionData.addThreadDataByThread(threadData);
            }

            DatabaseData databaseData = new DatabaseData();
            databaseData.setSql(sql);

            threadData.addDatabaseData(databaseData);
        }

    }

    private String getParam(Object obj){
        if(obj==null){
            return " null ";
        }
        if(obj instanceof Date){
            return "to_timestamp('" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format((Date)obj)+"','yyyy-MM-dd HH24:mi:ss ff')";
        }
        return "'"+obj.toString()+"'";
    }
}
