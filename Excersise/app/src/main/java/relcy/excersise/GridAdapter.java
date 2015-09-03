package relcy.excersise;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mkankanala on 8/27/15.
 */
public class GridAdapter extends BaseAdapter {
    ArrayList<DataModel> dataModel;
    Context context;
    private LayoutInflater inflater = null;
    private LruCache<String, Bitmap> mMemoryCache;

    public GridAdapter(ArrayList<DataModel> dataModel, Context context) {
        this.dataModel = dataModel;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/4th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;

        mMemoryCache = new LruCache<>(cacheSize);

    }

    @Override
    public int getCount() {
        return dataModel.size();
    }

    @Override
    public Object getItem(int i) {
        return dataModel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, viewGroup, false);
            mViewHolder = new ViewHolder();
            mViewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (mMemoryCache.get(dataModel.get(i).getId()) != null) {
            mViewHolder.imageView.setImageBitmap(mMemoryCache.get(dataModel.get(i).getId()));
        } else {
            mViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.place_holder));
            new LoadImage(dataModel.get(i).getImageUrl(), dataModel.get(i).getId(), mViewHolder.imageView).execute();
        }
        mViewHolder.textView.setText(dataModel.get(i).getName());

        return convertView;

    }

    private static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }


    private class LoadImage extends AsyncTask<Void, Void, Bitmap> {
        String url;
        String id;
        ImageView imageView;

        public LoadImage(String url, String id, ImageView imageView) {
            this.url = url;
            this.id = id;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return RestServiceManager.getBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (id != null && bitmap != null) {
                mMemoryCache.put(id, bitmap);
                imageView.setImageBitmap(bitmap);
            }

        }
    }
}
