package org.t_robop.ika.twittertest;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;

import java.util.List;


public class TimeAct extends ListActivity {

    //投稿用のtextView
    EditText TwitterText;
    //Tweetを入れる用
    String TweetStr;

    private SwipeRefreshLayout mSwipeRefreshLayout;





    //表示ツイート数の設定
    final int TWEET_NUM = 100;
    TwitterApiClient twitterApiClient;

    //アダプターの設定
    final TweetViewFetchAdapter adapter =
            new TweetViewFetchAdapter<CompactTweetView>(
                    TimeAct.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        //Tweetを入力するeditTextの関連付け
        TwitterText = (EditText) findViewById(R.id.editText);
        //アダプターをセット
        setListAdapter(adapter);
        //データを読み込む
        twitterApiClient = TwitterCore.getInstance().getApiClient();


        //SwipeRefreshLayoutとListenerの設定
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);


        //自分のタイムラインを表示するとき
        homeTimeline();
    }
    //swipeでリフレッシュした時の通信処理とグルグルを止める設定を書く
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
           /*リフレッシュした時の通信処理を書く*/

            homeTimeline();


                        //setRefreshing(false)でグルグル終了できる
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    //ツイートボタンをおした時の処理
    public void Tweet(View view) {
        TweetStr = String.valueOf(TwitterText.getText());
        StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        statusesService.update(TweetStr, null, null, null, null, null, null, null, new Callback<Tweet>() {

            //成功した場合
            @Override
            public void success(Result<Tweet> tweetResult) {
                //toastできた
                Toast.makeText(TimeAct.this, "ツイート完了", Toast.LENGTH_LONG).show();
                Log.d("ツイート完了", "");
            }

            //失敗した場合
            @Override
            public void failure(TwitterException e) {
                Toast.makeText(TimeAct.this, "ツイート失敗", Toast.LENGTH_LONG).show();
                Log.d("ツイート失敗", "");
            }
        });
    }

        //自分のタイムラインを表示するメソッド
        void homeTimeline() {

            // statusAPI用のserviceクラス
            StatusesService statusesService = twitterApiClient.getStatusesService();
            //ログインユーザーのタイムラインを表示する

            statusesService.homeTimeline(TWEET_NUM, null, null, false, false, false, false,
                    new Callback<List<Tweet>>() {
                        @Override
                        public void success(Result<List<Tweet>> listResult) {
                            Log.d("aaa", String.valueOf(listResult));
                            adapter.setTweets(listResult.data);


                        }

                        @Override
                        public void failure(TwitterException e) {
                        }
                    });
        }

        void search() {
            //検索で使う
            SearchService searchService = twitterApiClient.getSearchService();
            //指定したワードを使った検索
    /*
        データ形式
        String 検索したい文字列, GeoCode 緯度経度?,String 検索したい文字列1,String 検索したい文字列2,String 検索したい文字列3,
        Integer 取得するツイート数,String 検索したい文字列4,Long 検索したいLong?時間?,Long 検索したいLong1?時間?Boolean 謎,Callback 返り値
      */
            searchService.tweets("東海大学", null, null, null, null, TWEET_NUM, null, null, null, false, new Callback<Search>() {
                //成功した時
                @Override
                public void success(Result<Search> listResult) {
                    Log.d("aa", String.valueOf(listResult.data.tweets));
                    if (listResult.data.tweets.equals("[]")) {
                        toastMake("データがありません", 0, -200);
                        Log.d("aa", String.valueOf(listResult.data.tweets));
                    }
                    adapter.setTweets(listResult.data.tweets);

                }

                @Override
                public void failure(TwitterException e) {
                }
            });
        }

    //Toastを出力させるメソッド
    private void toastMake(String message, int x, int y){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER, x, y);
        toast.show();
        }
    }

