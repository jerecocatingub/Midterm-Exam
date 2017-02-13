package exam.midterm.com.albumsearch;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Jereco on 2/11/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<Album> mAlbums;
    private Context mContext;
    private ViewHolder mHolder;
    int layout;

    public AlbumAdapter(Context mContext, List<Album> albums){
        this.mContext = mContext;
        this.mAlbums = albums;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        mHolder = new ViewHolder(v);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder viewHolder, int position) {
        final Album current = mAlbums.get(position);
        if(!current.getImage().isEmpty()){
            Picasso.with(mContext)
                    .load(mAlbums.get(position).getImage())
                    .into(viewHolder.image);

        }

        mHolder.title.setText(current.getTitle());
        mHolder.artist.setText(current.getArtist());
    }

    @Override
    public int getItemCount() {
        if(mAlbums==null){
            return 0;
        }
        return mAlbums.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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

}

