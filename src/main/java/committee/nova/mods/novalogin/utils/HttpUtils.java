package committee.nova.mods.novalogin.utils;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
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
    public static String get(String dest, long timeOut,@Nullable Properties header) {
        try {
            URL url = new URL(dest);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            if (header != null) {
                for (String s : header.stringPropertyNames()) {
                    con.setRequestProperty(s, header.getProperty(s));
                }
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sbf = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            return sbf.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
