/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.controller.HttpClientController.java <2018年08月22日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.controller.areaPage;

import com.baodiwang.crawler4j.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月22日 11时03分
 */
public class HttpClientController {
    public static void main(String[] args) throws Exception{

        getDetailPageConent();


    }

    public static boolean pressureTest()throws Exception{
        int count = 0;
        long start = System.currentTimeMillis();
        while (true){
            count++;
            if(!getDetailPageConent() || count > 1000){
                long end = System.currentTimeMillis();
                System.out.println("count="+count + ",耗时:" + (end-start) + "毫秒");
                return true;
            }
        }
    }

    public static boolean getDetailPageConent()throws Exception{
        String str = StringUtils.stringToHex("1366,768");
        String detailPageUrl = "http://www.landchina.com/DesktopModule/BizframeExtendMdl/workList/bulWorkView.aspx?wmguid=20aae8dc-4a0c-4af5-aedf-cc153eb6efdf&recorderguid=JYXT_ZJGG_4465&sitePath&security_verify_data=" + str;
//        String listPageUrl = "http://www.landchina.com/default.aspx?tabid=261&ComName=default";
        //创建client实例
        HttpClient client= HttpClients.createDefault();
        //创建httpget实例
        HttpGet httpGet=new HttpGet(detailPageUrl);
//        httpGet.addHeader("Host","www.landchina.com");
//        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3278.0 Safari/537.36");
        httpGet.addHeader("Cookie","yunsuo_session_verify=108ac5d03c448e34f9faad26f25927a2; srcurl=687474703a2f2f7777772e6c616e646368696e612e636f6d2f64656661756c742e617370783f74616269643d323631; security_session_mid_verify=d70d231ed4e7b195938aac569dccf384; ASP.NET_SessionId=pabax0qh5pj1d2ocmexjcwuc;");
//        httpGet.addHeader("Cookie","yunsuo_session_verify=108ac5d03c448e34f9faad26f25927a2;");
        //执行 get请求
        HttpResponse response=client.execute(httpGet);
        if(null != response.getHeaders("Set-Cookie") && response.getHeaders("Set-Cookie").length > 0 ){
            System.out.println(response.getHeaders("Set-Cookie")[0].getValue());
        }

        //返回获取实体
        HttpEntity entity=response.getEntity();
        //获取网页内容，指定编码
        String web= EntityUtils.toString(entity,"UTF-8");
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,sss").format(new Date())+":网页字符长度：" + web.length());
        //输出网页
        System.out.println(web);
        Thread.sleep(200);//每抓一次休眠200毫秒
        return web.length() > 5000;
    }

}
