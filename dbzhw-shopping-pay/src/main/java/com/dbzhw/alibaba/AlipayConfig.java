package com.dbzhw.alibaba;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101100661249";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCvAi2Zj/ZX0Wu7vd6JYoSUK2wAe8Eg9i4V0CeKh0mn/63MLo9jiAWO/CZYdOfeB5+jGsgRy0N/IF5Y2CXAlsUyHZzazdzISH6s4G/jeVmzvYHJbYn2EItM3hXi39dnM5M7s34yaerVPNGGqB7TJAnsmEk24HQE4fPcpzbzZXuM4rPSmBiy3HBlBLmSG61lemtWekjoQ1fQLobblzTqRqnenylJkP+i4UEUZ8JK7j6ZjSuposcVe9uygHpQSmPyPlo12F7gW3sjFu8CLqPqRHeGlG1H3kMhZFW90ksc94t+1lZ3w46AHokpgJTEWM8dJIi4U+wfUdqxrZFbAKDCOk3bAgMBAAECggEALznCPl8n1bjQfqAU7jPknOs+wL6kdhWYiBBcqzgouyn2p1OlI/Gvtk76tgIOE88w193BQXP9ac+9RfJF9bt5yTVOf4jv3Cgb7uC9sL+tYiBwZn+lq/346SxA8bD/unyjbYfZNRd55EL+M30onEdsWikaELHu/OZQXSnOXU30UibkkxovnmJM+3W8WyANCKhcCpe4sbGmkR2NP54LHndkbiB5rQD4PbBHHYLutIRW1nskJakv3N+ZqbKIbnqn+bEjat5o2X33HdWrHUN/rEgHiSZoBfN34WLeli6W7UF0Iln3UdzcLtDHZQx3YnFN0zFL8eZREyx1SLZo5IQw1uZRsQKBgQDcx0njmM0UfPmrIlOJw+jRD009GmdPnT6D2SXT9/2PpH9E+ezpUqjXAyBS4cP61A2tJT9uTW7DLBlfrYRLssCn+vnANqeorVg+Oj0d5HAKFY8NyB/hkjA8VYRgb3zTvERLB3DHX/rUaFxIpTdk4OVrf+L3g6kqUxJZZTzaB5uwOQKBgQDK7Z6fhIsTgNJ5YkyWjoNkW3lfGtmU9mngR79gcma02Oh/LZIXLCAVck7UPa4PNp/swFOTSmz5WtfPC1u8fzmlJNrUzYg2hI51cl6gajUgdTv1MyzngPLfclVXUaBkWtKjqoMu8udpkiuwJiBw9ZM+d8wBzXneT/2OgqdtkqLGswKBgGR6BjSgm+TRI8cwrhgqotBSSHKgi5f+Fi9nKgIYALvYv1si6yEcz15bO+C8graqvzanlwCaGOiWL04N6AGTbn6EiBlkRlVL3rcjRiFOj3bnbN120JuWLoGm4cIJuyOZsPL8heUW107H2AfvuRAeEgskfqTqtJhgih9JJbKMd9xxAoGATfLUmYW6PnWfY40EylhX1Dbm+kdACN5WcRZeRtfq59DiDegdU9VZRaOw8An5UFK1P6Xo9I0EKwxgFWHRjSh5phvwCwogJoDoj+isvIXPfnZJ37gWqZ54rKgoa/Ao4iOZAdE/rssTZRQGwBUFJR7PvpEZxh99jo2/YfyB/67x480CgYBI7fwWgcllfQXo3o0EViiE2uS5QPQZQXcmwVWRfBufCc77WOqbIpXfHNmJ4jNOFlwdrm929bhKNCNY0SHEwbwVAREI7gg2/SwJrfWJrkKuxaqqTGhymqGCVF8ynFOMCDBp5cCxn1PaXDKe5GZe7D/Wrh1B0B4L32N25MSz8CclDA==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAobHccx54f067YGcq0eZGjk0J0GdDrO0SoyPptkP7FtR0dtzioZo3DS1pTN6xxo+vZ5QivrkVeqoJFKCCDnOUe0mQDIYONwzYLj3Bq2ED2b1DiN3SgMkvNkV4SWq66CzZNTMuCJ+/W8A8C5mrNMQIvSi1hyaNpLSV6eP1HlxdORuuRr5RjqbTlruCdfYWFJlAjWkSLd06oPs4zyPy7qAyhFbT8REKEgdiAK7zv4Ht/Fisw3zgXqgR9YeUh97wPL1knTcogk2g328Y9hcYtDJwcQaId7SY3M+Ektr0l2TCVOAcN1S/GhfUIF0StIixmteQXadu5EVmQZS5g1jnCG0C1QIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://dbzhw.ngrok.xiaomiqiu.cn/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://dbzhw.ngrok.xiaomiqiu.cn/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "E:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

