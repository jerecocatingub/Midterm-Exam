package exam.midterm.com.albumsearch;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private AlbumAdapter mAdapter;
    private RecyclerView mRecycler;
    java.util.List<Album> data = new ArrayList<>();
    private ProgressDialog pd;
    private AlbumAsyncTask task;
    private TextView defaultMessage;
    private EditText mSearch;
    private View view;
    private  String mAlbum;

    public MainFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mSearch = (EditText) view.findViewById(R.id.etxtSearch);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        return view;
    }
    private class AlbumAsyncTask extends AsyncTask<String, Void, List<Album>> {
        @Override
        protected List<Album> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Album> result = QueryUtils.fetchNewsData(urls[0]);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            defaultMessage = (TextView) view.findViewById(R.id.textView);
            defaultMessage.setText("");

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(List<Album> data) {

            if(pd.isShowing()){
                pd.dismiss();
            }
        }
    }

    public void performSearch(){
        mRecycler = (RecyclerView) view.findViewById(R.id.cards);
        mAdapter = new AlbumAdapter((FragmentActivity) getActivity(), data, R.layout.card_layout);
        mAlbum = String.valueOf(mSearch.getText());
        String url = "http://ws.audioscrobbler.com/2.0/?method=album.search&album=Believe&api_key=de769b1e94bfc795020076abff63be87limit=50&format=json";
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        task = new AlbumAsyncTask();
        task.execute(url);
    }
}

