package info.justaway.fragment.profile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import info.justaway.JustawayApplication;
import info.justaway.R;
import info.justaway.adapter.FriendListAdapter;
import twitter4j.PagableResponseList;
import twitter4j.User;

public class FollowingListFragment extends Fragment {
    private FriendListAdapter mAdapter;
    private long mUserId;
    private long mCursor = -1;
    private ProgressBar mFooter;
    private Boolean mAutoLoader = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list, container, false);

        User user = (User) getArguments().getSerializable("user");
        mUserId = user.getId();

        // リストビューの設定
        ListView listView = (ListView) v.findViewById(R.id.list_view);

        // コンテキストメニューを使える様にする為の指定、但しデフォルトではロングタップで開く
        registerForContextMenu(listView);

        mFooter = (ProgressBar) v.findViewById(R.id.guruguru);
        mFooter.setVisibility(View.GONE);

        // Status(ツイート)をViewに描写するアダプター
        mAdapter = new FriendListAdapter(getActivity(), R.layout.row_user);
        listView.setAdapter(mAdapter);

        new FriendsListTask().execute(mUserId);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 最後までスクロールされたかどうかの判定
                if (totalItemCount == firstVisibleItem + visibleItemCount) {
                    additionalReading();
                }
            }
        });
        return v;
    }

    private void additionalReading() {
        if (!mAutoLoader) {
            return;
        }
        mFooter.setVisibility(View.VISIBLE);
        new FriendsListTask().execute(mUserId);
    }

    private class FriendsListTask extends AsyncTask<Long, Void, PagableResponseList<User>> {
        @Override
        protected PagableResponseList<User> doInBackground(Long... params) {
            try {
                PagableResponseList<User> friendsList = JustawayApplication.getApplication().getTwitter().getFriendsList(params[0], mCursor);
                mCursor = friendsList.getNextCursor();
                return friendsList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(PagableResponseList<User> friendsList) {
            mFooter.setVisibility(View.GONE);
            if (friendsList == null) {
                return;
            }
            for (User friendUser : friendsList) {
                mAdapter.add(friendUser);
            }
            if (friendsList.hasNext()) {
                mAutoLoader = true;
            }
        }
    }
}