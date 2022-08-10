package com.test.sample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

import javax.naming.*;
import javax.naming.directory.*;
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Api("测试接口")
@RestController
@RequestMapping("test")
public class TestController {

    @ApiOperation("path")
    @GetMapping("path")
    /**
     * 如果入参是 %5E* -> ^* 即会抛出异常
     */
    public void file(@RequestParam("filePath") String filePath) throws InvalidPathException {
        System.out.println("url: [path] , param is : " + filePath);
        File file = new File(filePath);
        Path path = file.toPath();
    }

    @ApiOperation("contextLookup")
    @GetMapping("contextLookup")
    public void contextLookup(String param) throws NamingException, SQLException {
        System.out.println("url: [contextLookup] , param is : " + param);
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup(param);
        DataSource ds = (DataSource) envContext.lookup(param);
        Connection conn = ds.getConnection();
        conn.close();
    }

    @ApiOperation("os-cmd")
    @GetMapping("os-cmd")
    /**
     *  param 为 calc可调起 计算器
     */
    public void OScmd(String param) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("url: [os-cmd] , param is : " + param);
        ProcessBuilder builder = new ProcessBuilder();
        ProcessBuilder cmd = builder.command(param);
        Process process = cmd.start();

        //-----------------------------------------
        Class<?> clz = Class.forName("java.lang.ProcessImpl");
        Method start = clz.getDeclaredMethod("start", String[].class, Map.class, String.class, ProcessBuilder.Redirect[].class, boolean.class);
        start.setAccessible(true);
        Object invoke = start.invoke(null, new String[]{param}, null, null, null, false);
    }

    @ApiOperation("crlf")
    @GetMapping("crlf")
    public void crlf(String url) throws IOException {
        System.out.println("url: [crlf] , param is : " + url);
        URL conn =new URL(url);
        URLConnection urlConnection = conn.openConnection();
    }

    @ApiOperation("ladp")
    @GetMapping("ladp")
    public void ladp(String param) throws NamingException {
        System.out.println("url: [ladp] , param is : " + param);

        DirContext context = new InitialDirContext() ;
        Attributes attributes = new BasicAttributes();
        NamingEnumeration<SearchResult> enumerations = context.search("param", attributes);
    }

    @ApiOperation("regex")
    @GetMapping("regex")
    /**
     *  如果regex 参数传 %5B -> '[' 就会报PatternSyntaxException
      */
    public void regex(String regex)  throws PatternSyntaxException{
        System.out.println("url: [regex] , param is : " + regex);
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher("hhhhhhhhhh");
    }

    @ApiOperation("error")
    @GetMapping("error")
    public void error(String regex)  throws PatternSyntaxException{
       int a = 1/0;
    }

}
