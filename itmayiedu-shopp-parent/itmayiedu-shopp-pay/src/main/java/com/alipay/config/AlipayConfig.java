package com.alipay.config;

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
	public static String app_id = "2016101300679261";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDJZWzkIOKX7o2V2g0MFu3pULznv5avaiFKurO8bWhRg6MDVZB3+s1E2m5TtCV3WrNCO2MMwjR94T7Glvly9U04rOEI1zblgx1uS8KBQcS5q7VWOBFozUWsWo3SOEWKPO0RfdDG9zTaxw/GTmrY7Z059QieSFEFPKRweDOXBw+0O+rI7Ou067lv1EJ3ERISxxhF0dl3exhB3fGVdLCSZYY1od2XP2WzschXwZsPTDbMQLPOzTLKGuoMVID8SJGGLg3ccX+z8yNP7rHR1HES5cl+gG+le2oWfz8Qb5x3/1eiKH71a1p9UhqRqU1vrlB0pAIfM8eDkPlj7r4QOpVZb2VLAgMBAAECggEACFGTLN0a3vGwkXb/QPeVfiSimOP2Amrc0yxhDjBqtas9SX74R6xpXCW1lumC/qoVa2/uiHFhH631HNjRlukA9o1Vgjr5foNq7MpeT157FLw98kDAn1aTQSpAX2WJocsgc6+BtyYw3bSJN0S37xvpeaJgZ8FO72tw02BzqRJWmLgqEBjBVzXrFjOqnSegAibPOYaKSTvPYWlWFFEUInFORoLfm4pVNx4pBjQuUSKBLN7g3gvPXe0y7Guqp83vLJMP7orLdJebinalR0gf7huyB05HiaW1DEe1K7QPrus9E0NPtTpu9EzEdDbWD8UStQdX8YSR1sNJk8uK9VKXg2AjEQKBgQDxtdcfIm7BRWqpbchx8lOI6YaiGxP/R1LznhrEu5zv/usRkxxLQpJwdu3VieKv4FYRFlOl38TwU4sZcKXEczNQYxFJoOEzrcS9/GTfx7psM1t4jRYm0Tq4HXt+GSp6DCVuMjWHQaauHQmQjbmillEFqw8wMO59Q0MGc2Shj2FQQwKBgQDVTXOqV1uO5feoqDuPOEzYbmD8LocBCBfauXzejMnXxI3ufrR63Dv8BEV9VavitpL+jgsmDGwGPbIiFjgg6Mdvfdixc4Zu6t7LRS3SVeQj+/XiZDedTVRHE155EIkFcmrPpckluRhkIprLS2NgByxnhQW6SDhYd5Gjq9H+85qqWQKBgQCD+/FNtPWZX5hQj/vmDJaEqk+z8OlIed/X2ztutauKeT5OyG+owN9Jvgc8lSBNPUiB6Bkg+EGHoXbRG2vJC8a3XIEyHUEbgQVvYISMlXk8YAbQmum/pHxV0PN/9an2Hu/Jbm1AQB8N8Bkq79iv6TiwyalvfSidCb9tpFEMz0gqFQKBgDieWnl4vsDrohWOsmCN3TgF9JB7+TH45/wTCGY6kMlJTzNjRpzmILYAnEFsoDeJpvMS2Tq8SWBOuB38L+HsaCPcD8zqkWCXz5O9RTBzxB8ZHmd9mpWSlTBYn9ca4IQlntyHcSEmRKcv2E7mL/apcMv5r0+HJA23j10WmqNGHmPhAoGBAOLgcujF6CoGcH0mQLNRuWkDWSFhueHgr3X7TSooqRXOCwwH2gRyuxc5EnCvo1a2HaFDv9GLlo7ho43RogT+dcyO2gZtTNGZysIjmxBLqXOfFA5sRx9I7SSfrNR1GODtr0A2Rpmw/E0Yf1ZXDp9g1jqqeFdPvazZTUbSoVevAJGB";
    
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1UdacF09FtttQMwJfeYpBMCeeUUS6v3HNcqBDUqj5+uCHLEeZTwqkuth2INvQOWJGB+M2VKPkekNBVIe/UqhV3FYaVCiAOhlfN5YEeRkvi4aA0F8UQZ+KOon12QGi5CBsmxaVOk3Oen5MXMtgh78DJxCbSHwYRA7c856do7+s/IuPdGjjj1LIiZXRqaewr0aAw00vjQQSft4XYI5yO28HoK372VONkrP11cWLlihY28wwffLKxCCpn3XIYiLZuVS+lQsN/ubpQa1mejzw84P21CNcTB5IFWU7C9G5GaM/T5qV3mhJLnrMawtKDH3i/Kn/1RDxXztXnANPwwp3p4NUwIDAQAB";
    
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = " http://3ae6f9ec.ngrok.io/alibaba/callBack/notifyUrl";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://3ae6f9ec.ngrok.io/alibaba/callBack/returnUrl";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
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

