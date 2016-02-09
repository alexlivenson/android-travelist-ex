package bhouse.travellist_starterproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by alexlivenson1 on 2/8/16.
 */
// 1. Extend to override logic
public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.ViewHolder> {
    Context mContext;
    OnItemClickListener mItemClickListener;

    // 2. This is where context can be passed when create TravelListAdapter in MainActivity
    public TravelListAdapter(Context context) {
        this.mContext = context;
    }

    // 3. View Holder class is Optional in ListView but required in RecyclerView (improves scrolling and performance by avoiding calling findViewById()
    // NOTE: Usually always use RecyclerView instead of ListView
    // NOTE: Unlike ListView, RecycleView does not come with an onClickListener
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView placeName;
        public ImageView placeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            placeName = (TextView) itemView.findViewById(R.id.placeName);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            placeImage = (ImageView) itemView.findViewById(R.id.placeImage);

            placeHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    // 1. Returns the number of items in the data array
    @Override
    public int getItemCount() {
        return new PlaceData().placeList().size();
    }

    // 2. Returns new instance of view holder by passing in inflated view of row_places
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }

    // 3. binds the Place object to the UI element in ViewHolder. You'll use Picasso to Cache those images
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Place place = new PlaceData().placeList().get(position);
        holder.placeName.setText(place.name);
        Picasso.with(mContext).load(place.getImageResourceId(mContext)).into(holder.placeImage);

        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), place.getImageResourceId(mContext));
        /*
            NOTE: The Palette API can extract the following profiles from an image
             1. Vibrant
             2. Vibrant Dark / Light
             3. Muted
             4. Muted Dark / Light
         */
        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int bgColor = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                holder.placeNameHolder.setBackgroundColor(bgColor);
            }
        });
    }
}
