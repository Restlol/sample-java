package com.test.sample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;
import sun.awt.image.ByteComponentRaster;
import sun.misc.VM;

import javax.naming.*;
import javax.naming.directory.*;
import javax.sql.DataSource;
import java.Test;
import java.awt.*;
import java.awt.image.BandedSampleModel;
import java.awt.image.Raster;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
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

    /**
     *
     * @param input 为负数就会出现异常
     */
    @ApiOperation("Negative Array Size")
    @GetMapping("negative-array-size")
    public void negativeArraySize(int input)  {
        System.out.println("url: [negative-array-size] , param is : " + input);
        String[] stringArray2 = new String[input];
    }

    /**
     *
     * @param dataType
     * @param w  假设w的输入为：55555555 即会报错
     * @param h
     * @param numBands
     */
    @ApiOperation("Raster Format Exception")
    @GetMapping("raster")
    public void raster(int dataType, int w, int h, int numBands)  {
        System.out.println("url: [raster] , dataType is : " + dataType + ", w is :" + w + ", h is :" + h + ", numBands is : " + numBands);
        BandedSampleModel model = new BandedSampleModel(dataType,w,h,numBands);
        ByteComponentRaster raster =new ByteComponentRaster(model, new Point());
    }

    /**
     *
     * @param input 参数为英文字符串即会报错
     */
    @ApiOperation("Class Cast Exception")
    @GetMapping("classCast")
    public void classCast(Object input)  {
        System.out.println("url: [classCaste] , param is : " + input);
        Integer a = (Integer)input;
    }

    /**
     *
     * @param input 参数大于1即会报错
     */
    @ApiOperation("Array Out of Bound")
    @GetMapping("arrayOutOfBound")
    public void arrayBound(int input)  {
        System.out.println("url: [arrayOutOfBound] , param is : " + input);
        int[] a = {1};
        int i = a[input];
    }

    /**
     *
     * @param input 参数小于等于0即会报错
     */
    @ApiOperation("Illegal Argument Exception")
    @GetMapping("illegalArgument")
    public void illegalArgument(int input)  {
        System.out.println("url: [illegalArgument] , param is : " + input);
        if(input <= 0){
            throw new IllegalArgumentException("参数不能小于0");
        }
    }

    /**
     *
     * @param input 参数为1 或者 true的时候，则会触发Out of memory,需要调整jvm参数：-Xms15M -Xmx15M
     */
    @ApiOperation("Out of memory,需要调整jvm参数：-Xms15M -Xmx15M")
    @GetMapping("outOfMemory")
    public void outOfMemory(boolean input)  {
        System.out.println("url: [outOfMemory] , param is : " + input);

        ArrayList list = new ArrayList();
       while (input){
           list.add(new Object());
           System.out.println("创建了");
       }
    }

    /**
     *
     * @param input 参数为字母字符串即会触发
     */
    @ApiOperation("Number Format Exception")
    @GetMapping("numberFormat")
    public void numberFormat(String input)  {
        System.out.println("url: [numberFormat] , param is : " + input);
        int i = Integer.parseInt(input);
    }

    /**
     *
     * @param input 参数无论输入什么都会触发该异常
     */
    @ApiOperation("Security Exception,参数无论输入什么都会触发异常")
    @GetMapping("security")
    public void security(String input) throws ClassNotFoundException {
        System.out.println("url: [security] , param is : " + input + ", 此接口无论输入什么都会触发该异常");
        Class<?> aClass = Class.forName("java.Test");

    }

    /**
     *
     * @param input 参数输入0 或者 false 即触发
     */
    @ApiOperation("Dereference of Null Object")
    @GetMapping("nullObject")
    public void nullObject(boolean input)  {
        System.out.println("url: [nullObject] , param is : " + input );
        String str = null;
        if (input) {
            str = "aaa";
        }
        str.length();
    }

    /**
     *
     * @param input 参数大于等于3即报错
     */
    @ApiOperation("String Index Out Of Bounds Exception")
    @GetMapping("stringOutBound")
    public void stringOutBound(int input)  {
        System.out.println("url: [stringOutBound] , param is : " + input );
        String s = "abc";
        s.charAt(input);
    }

    /**
     *
     * @param input 参数无论输入什么都会触发该异常
     */
    @ApiOperation("Uncaught Exception,参数无论输入什么都会触发异常")
    @GetMapping("uncaught")
    public void uncaught(String input) throws InterruptedException {
        System.out.println("url: [uncaught] , param is : " + input + ", 此接口无论输入什么都会触发该异常");
        Thread t = new Thread(() -> {
            throw new RuntimeException();
        });
        t.start();
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println("uncaught end");
    }

    /**
     *
     * @param input 参数的长度小于3，即会报AssertionError ，JVM去加入参数 -ea
     */
    @ApiOperation("Failed AssertionJVM,JVM需加入参数 -ea")
    @GetMapping("failedAssertion")
    public void failedAssertion(String input)  {
        System.out.println("url: [failedAssertion] , param is : " + input);
        assert (input.length()  > 3);
    }

    /**
     *
     * @param input 参数为0 则异常
     */
    @ApiOperation("Arithmetic  Exception")
    @GetMapping("arithmetic")
    public void arithmetic(int input)  {
        System.out.println("url: [arithmetic] , param is : " + input);
        int a = 1 / input;
    }


    private static class User {
        private String name;
        private String gender;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", gender='" + gender + '\'' +
                    '}';
        }
    }

    /**
     *
     * @param user 参数无论输入什么都会触发该异常
     */
    @ApiOperation("ArrayStore Exception,参数无论输入什么都会触发异常")
    @PostMapping("arrayStore")
    public void arrayStore(@RequestBody User user)  {
        System.out.println("url: [arrayStore] , param is : " + user);
        Object[] strings = new String[3];
        strings[0] = user;
    }

    /**
     *
     * @param input 参数无论输入什么都会触发IO Exception的子类异常
     */
    @ApiOperation("IOException,将触发IO Exception的子类异常")
    @GetMapping("io")
    public void io(String input) throws IOException {
        System.out.println("url: [io] , param is : " + input);
        URL url = new URL(input);
        URLConnection urlConnection = url.openConnection();
        urlConnection.getContent();
    }

    /**
     *
     * @param input 参数无论输入什么都会触发异常
     */
    @ApiOperation("NoSuchMethodException,参数无论输入什么都会触发异常")
    @GetMapping("noMethod")
    public void noMethod(String input) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println("url: [noMethod] , param is : " + input);
        A a = A.class.newInstance();
        Method handle =  A.class.getMethod("handle", null);
        handle.invoke(a,null);
    }

    /**
     *
     * @param input 参数无论输入什么都会触发异常
     */
    @ApiOperation("FileNotFoundException")
    @GetMapping("fileNotFound")
    public void fileNotFound(String input) throws FileNotFoundException {
        System.out.println("url: [fileNotFound] , param is : " + input);
        BufferedReader reader = new BufferedReader(new FileReader(new File(input)));
    }

    /**
     *
     * @param input 参数无论输入什么都会触发异常
     */
    @ApiOperation("SQLException,参数无论输入什么都会触发异常")
    @GetMapping("sqlException")
    public void sqlException(String input) throws SQLException {
        System.out.println("url: [sqlException] , param is : " + input);
        throw new SQLException();
    }

    /**
     *
     * @param input 参数无论输入什么都会触发该异常
     */
    @ApiOperation("Illegal ThreadState Exception,参数无论输入什么都会触发异常")
    @GetMapping("illegalThreadState")
    public void illegalThreadState(String input)  {
        System.out.println("url: [illegalThreadState] , param is : " + input + ", 此接口无论输入什么都会触发该异常");
        Thread t = new Thread(() -> {
            // non-implement
        });
        t.start();
        t.start();
    }

    /**
     *
     * @param input 参数无论输入什么都会触发该异常
     */
    @ApiOperation("Illegal MonitorState Exception,参数无论输入什么都会触发异常")
    @GetMapping("illegalMonitorState")
    public void illegalMonitorState(String input) throws InterruptedException {
        System.out.println("url: [illegalMonitorState] , param is : " + input + ", 此接口无论输入什么都会触发该异常");
        Object o = new Object();
        o.wait();
    }


}
