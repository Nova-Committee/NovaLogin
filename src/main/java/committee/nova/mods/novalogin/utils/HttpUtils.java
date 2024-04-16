package committee.nova.mods.novalogin.utils;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * HttpUtils
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/3/20 13:33
 */
public class HttpUtils {
    public static String getResponseMsg(HttpURLConnection con){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sbf = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            con.disconnect();
            return sbf.toString();
        } catch (Exception e) {
            return "";
        }
    }
    public static int getResponseCode(HttpURLConnection con) {
        try {
            int code = con.getResponseCode();
            con.disconnect();
            return code;
        } catch (Exception e) {
            return -1;
        }
    }

    public static HttpURLConnection connect(String dest, int timeOut, @Nullable Properties header) throws IOException {
        URL url = new URL(dest);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(timeOut);
        con.setReadTimeout(timeOut);
        if (header != null) {
            for (String s : header.stringPropertyNames()) {
                con.setRequestProperty(s, header.getProperty(s));
            }
        }
        return con;
    }
}
