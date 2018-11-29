package actions;

import enumerations.UserProfileEnum;

import java.util.Optional;

public class AdminAction extends AuthMeAction {

    @Override
    protected Optional<UserProfileEnum> getProfile(){
        return Optional.of(UserProfileEnum.ADMIN);
    }

}
