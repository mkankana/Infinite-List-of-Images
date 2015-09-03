package lazyloading.infinitegridofimages;

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

public class GridAdapter extends BaseAdapter {

    ArrayList<ImageData> data = new ArrayList<>();
    Context context;
    private LayoutInflater mInflater;
    private LruCache<String, Bitmap> mMemoryCache;


    public GridAdapter(ArrayList<ImageData> data, Context context) {
        this.data = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);


        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<>(cacheSize);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder mViewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            mViewHolder = new ViewHolder();
            mViewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        if (mMemoryCache.get(data.get(i).getImageId()) != null) {
            mViewHolder.imageView.setImageBitmap(mMemoryCache.get(data.get(i).getImageId()));
        } else {
            mViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.dummy));
            new LoadImage(data.get(i).getImageUrl(), data.get(i).getImageId(), mViewHolder.imageView).execute();
        }
        mViewHolder.textView.setText(data.get(i).getName());

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
