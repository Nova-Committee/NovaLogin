package committee.nova.mods.novalogin.config;

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
    @SerializedName("uuid_trans")
    private boolean uuidTrans = false;
    @SerializedName("status")
    private Status status = new Status();


    public String getConfigName() {
        return "config";
    }
}
