package committee.nova.mods.novalogin.utils.yggdrasil.request;

import com.mojang.authlib.Agent;
import committee.nova.mods.novalogin.utils.yggdrasil.YggdrasilUserAuthentication;

public class AuthenticationRequest {
    private Agent agent;
    private String username;
    private String password;
    private String clientToken;
    private boolean requestUser = true;

    public AuthenticationRequest(final YggdrasilUserAuthentication authenticationService, final String username, final String password) {
        this.agent = authenticationService.getAgent();
        this.username = username;
        this.clientToken = authenticationService.getAuthenticationService().getClientToken();
        this.password = password;
    }
}
