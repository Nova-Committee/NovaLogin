package committee.nova.mods.novalogin.utils.yggdrasil.response;

import com.mojang.authlib.GameProfile;

public class AuthenticationResponse extends Response {
    private String accessToken;
    private String clientToken;
    private GameProfile selectedProfile;
    private GameProfile[] availableProfiles;
    private committee.nova.mods.novalogin.utils.yggdrasil.response.User user;

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientToken() {
        return clientToken;
    }

    public GameProfile[] getAvailableProfiles() {
        return availableProfiles;
    }

    public GameProfile getSelectedProfile() {
        return selectedProfile;
    }

    public User getUser() {
        return user;
    }
}
