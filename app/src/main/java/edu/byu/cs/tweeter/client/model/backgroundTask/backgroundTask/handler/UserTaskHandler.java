package edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.handler.observer.SingleArgObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.backgroundTask.task.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class UserTaskHandler extends BackgroundTaskHandler<SingleArgObserver<Intent>> {
    private final Context context;

    public UserTaskHandler(SingleArgObserver<Intent> observer, Context context,
            String failureString, String exceptionString) {
        super(observer, failureString, exceptionString);
        this.context = context;
    }

    @Override
    protected void handleSuccess(Bundle data, SingleArgObserver<Intent> observer) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        Intent intent = new Intent(this.context, MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
        observer.handleSuccess(intent);
    }
}
