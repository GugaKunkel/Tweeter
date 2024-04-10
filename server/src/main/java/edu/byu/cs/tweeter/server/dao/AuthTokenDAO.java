package edu.byu.cs.tweeter.server.dao;

import static edu.byu.cs.tweeter.server.dao.DAOUtils.enhancedClient;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.bean.AuthTokenBean;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenInterface;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class AuthTokenDAO implements AuthTokenInterface {
    private static final String TableName = "AuthToken";
    private static final DynamoDbTable<AuthTokenBean> table =
            enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenBean.class));

    public AuthToken generateAuthToken() {
        // Creates the new token
        AuthToken newToken = new AuthToken();

        // Adds the new token to the database
        AuthTokenBean authTokenBean = new AuthTokenBean();
        authTokenBean.setToken(newToken.getToken());
        authTokenBean.setTimestamp(newToken.getDatetime());
        table.putItem(authTokenBean);

        return newToken;
    }
    public void deleteAuthToken(AuthToken token) {
        Key key = Key.builder()
                .partitionValue(token.getToken())
                .build();
        table.deleteItem(key);
    }

    public void validateToken(AuthToken token) {
        if (token.getToken() == null || token.getDatetime() == null) {
            throw new RuntimeException("Server could not find auth token");
        }
        else {
            Key key = Key.builder()
                    .partitionValue(token.getToken())
                    .build();
            AuthTokenBean tokenBean = table.getItem(key);

            if (tokenBean == null) {
                throw new RuntimeException("Token Not Found In Database");
            }

            if (Long.parseLong(tokenBean.getTimestamp()) - System.currentTimeMillis() > 300000) {
                deleteAuthToken(token);
                throw new RuntimeException("Invalid AuthToken");
            }
            else {
                AuthToken validatedToken = new AuthToken(
                        token.getToken(), String.valueOf(System.currentTimeMillis()));
                AuthTokenBean authTokenBean = new AuthTokenBean();
                authTokenBean.setToken(validatedToken.getToken());
                authTokenBean.setTimestamp(validatedToken.getDatetime());
                table.updateItem(authTokenBean);
            }
        }
    }
}
