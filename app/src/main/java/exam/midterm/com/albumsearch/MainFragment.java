package exam.midterm.com.albumsearch;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private AlbumAdapter mAdapter;
    private RecyclerView mRecycler;
    java.util.List<Album> data = new ArrayList<>();
    private ProgressDialog pd;
    private TextView mMessage;
    private EditText mSearch;
    private View view;
    private String mAlbum = "";
    private String url;

    private String TAG = MainActivity.class.getSimpleName();

    public MainFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mSearch = (EditText) view.findViewById(R.id.etxtSearch);
        mMessage = (TextView) view.findViewById(R.id.txtWarning);
        mRecycler = (RecyclerView) view.findViewById(R.id.rvCards);

        final WifiManager wifi = (WifiManager) view.getContext().getSystemService(Context.WIFI_SERVICE);
        setHasOptionsMenu(true);

        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!wifi.isWifiEnabled()) {
                        Toast.makeText(view.getContext(), "Connect to a network", Toast.LENGTH_SHORT).show();
                    }else {
                        mAlbum = String.valueOf(mSearch.getText());
                        url = "http://ws.audioscrobbler.com/2.0/?method=album.search&album=" + mAlbum + "&api_key=de769b1e94bfc795020076abff63be87&limit=50&format=json";

                        new AlbumAsyncTask().execute();
                    }
                    return true;
                }
                return false;
            }
        });
        return view;
    }



    private class AlbumAsyncTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            data = new ArrayList<>();
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject root = new JSONObject(jsonStr);
                    JSONObject results = root.getJSONObject("results");
                    JSONObject albumMatches = results.optJSONObject("albummatches");
                    JSONArray albumArray = albumMatches.getJSONArray("album");
                    for (int i = 0; i < albumArray.length(); i++) {
                        JSONObject a = albumArray.getJSONObject(i);
                        JSONArray imageArray = a.getJSONArray("image");
                        JSONObject image = imageArray.getJSONObject(2);

                        Album album = new Album(
                                image.getString("#text"),
                                a.getString("name"),
                                a.getString("artist"));

                        data.add(album);
                    }

                } catch (final JSONException e) {
                    Log.e(mAlbum, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(mAlbum, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress dialog
            pd = new ProgressDialog(view.getContext());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            InputMethodManager inputMethodManager =
                    (InputMethodManager) getActivity().getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    getActivity().getCurrentFocus().getWindowToken(), 0);

            // Dismiss the progress dialog
            if (pd.isShowing())
                pd.dismiss();

            if (data.isEmpty()||data.size()==0) {
                clearRecyclerView();
                mMessage.setText("There's no album present at this moment.");
                Toast.makeText(view.getContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            } else {
                mMessage.setText("");
                mAdapter = new AlbumAdapter(view.getContext(), data);
                mRecycler.setAdapter(mAdapter);
                mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_layout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ch_clear:
                clearRecyclerView();
                Toast.makeText(view.getContext(), "Data Erased", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void clearRecyclerView() {
        int size = this.data.size();
        data.clear();
        mAdapter.notifyItemRangeRemoved(0, size);
        mRecycler.setAdapter(new AlbumAdapter(view.getContext(),new ArrayList<Album>()));
        mMessage.setText("There's no album present at this moment.");
        mSearch.setText("");
    }
}

