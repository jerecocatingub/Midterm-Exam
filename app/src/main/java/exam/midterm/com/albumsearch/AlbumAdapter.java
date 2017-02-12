package exam.midterm.com.albumsearch;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Jereco on 2/11/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> mAlbums;
    private Context mContext;
    int layout;

    public AlbumAdapter(FragmentActivity activity, List<Album> albums, int layout) {
        mAlbums = albums;
        this.layout = layout;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        final View itemLayoutView = LayoutInflater.from(mContext)
                .inflate(layout, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder viewHolder, int position) {
        Album album = mAlbums.get(position);

        TextView artist = viewHolder.artist;
        TextView title = viewHolder.title;

        artist.setText(album.getArtist());
        title.setText(album.getTitle());
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView artist;
        public TextView title;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            artist = (TextView) itemView.findViewById(R.id.txtArtist);
            title = (TextView) itemView.findViewById(R.id.txtTitle);
            image = (ImageView) itemView.findViewById(R.id.imgAlbum);
        }
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

}

