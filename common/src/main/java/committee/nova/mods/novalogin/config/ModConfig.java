package committee.nova.mods.novalogin.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Config
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/4/17 下午11:39
 */
@Data
public class ModConfig {
    public String getConfigName() {
        return "config";
    }

    @SerializedName("common")
    private CommonConfig common = new CommonConfig();

    @SerializedName("mail")
    private MailConfig mail = new MailConfig();

    @SerializedName("yggdrasil")
    private YggdrasilConfig yggdrasil = new YggdrasilConfig();

    @Data
    public static class MailConfig {
        @Expose String protocol = "smtp";
        @Expose String host = "smtp.gmail.com";
        @Expose int port = 465;
        @Expose boolean auth = true;
        @Expose boolean ssl = false;
        @Expose String username = "nova";
        @Expose String password = "nova";
    }

    @Data
    public static class CommonConfig {
        @SerializedName("uuid_trans")
        private boolean uuidTrans = true;
        @SerializedName("out_time")
        private int outTime = 600;
    }

    @Data
    public static class YggdrasilConfig {
        @SerializedName("enable")
        private boolean enable = false;
        @SerializedName("api_url")
        private String apiUrl = "https://littleskin.cn/api/yggdrasil";
        @SerializedName("api_name")
        private String apiName = "LittleSkin";
    }

}
