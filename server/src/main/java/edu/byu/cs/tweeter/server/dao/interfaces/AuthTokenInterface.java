package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenInterface {
    AuthToken generateAuthToken();
    void deleteAuthToken(AuthToken authToken);
    void validateToken(AuthToken authToken);
}
