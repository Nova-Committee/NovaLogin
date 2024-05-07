package committee.nova.mods.novalogin.utils.yggdrasil.request;

import com.mojang.authlib.GameProfile;
import committee.nova.mods.novalogin.utils.yggdrasil.YggdrasilUserAuthentication;

public class RefreshRequest {
    private String clientToken;
    private String accessToken;
    private GameProfile selectedProfile;
    private boolean requestUser = true;

    public RefreshRequest(final YggdrasilUserAuthentication authenticationService) {
        this(authenticationService, null);
    }

    public RefreshRequest(final YggdrasilUserAuthentication authenticationService, final GameProfile profile) {
        this.clientToken = authenticationService.getAuthenticationService().getClientToken();
        this.accessToken = authenticationService.getAuthenticatedToken();
        this.selectedProfile = profile;
    }
}
