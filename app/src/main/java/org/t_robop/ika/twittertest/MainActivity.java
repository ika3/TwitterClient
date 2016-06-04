package org.t_robop.ika.twittertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "1pf62uRZyEFPPINCzBw4tgkiM";
    private static final String TWITTER_SECRET = "MV9lTxnks18puV8s2cA9ZzP8lM1iI0PYF9LNwuH9KeuDtDx69T";

    //ログインボタンの宣言
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);


        //ログインボタンの実装
        //Twitterのボタンの関連付け
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        //押した後のコールバック
        loginButton.setCallback(new Callback<TwitterSession>() {
            //成功した時
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model

                //ログインしたTwitter名とIDをToastで出力
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            //失敗した時
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    //Twitter認証から帰ってきた時に呼ばれる
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    //Tweet画面にIntentする
    public void Tweet(View view) {
        Intent intent = new Intent();
        intent.setClass(this, tweetAct.class);
        startActivity(intent);
    }

    //タイムライン表示画面にIntentする
    public void TimelineBtn(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TimeAct.class);
        startActivity(intent);
    }
}
