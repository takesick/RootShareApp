package com.example.rootshareapp.adapter;

//public class SpotListAdapter extends RecyclerView.Adapter<SpotListAdapter.ItemViewHolder> {
//
//    public static final String TAG = "ItemListAdapter";
//
//    private List<Uri> mPhotoUriList;
//    private PostListSearchedAdapter.OnPostSelectedListener mListener;
//
//    public SpotListAdapter(List<Uri> PhotoUriList, SpotListAdapter.ItemViewHolder Listener) {
//        mPhotoUriList = PhotoUriList;
//        mListener = Listener;
//    }
//
//    @Override
//    public SpotListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_item, parent, false);
//        return new SpotListAdapter.ItemViewHolder(v, parent.getContext());
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SpotListAdapter.ItemViewHolder holder, int position) {
//        holder.bind(mPhotoUriList.get(position),mListener);
//    }
//
//    static class ItemViewHolder extends RecyclerView.ViewHolder {
//
//        Context mContext;
//        ImageView imageView;
//        EditText commentView;
//
//        public ItemViewHolder(@NonNull View itemView, Context context) {
//            super(itemView);
//            mContext = context;
//            imageView = itemView.findViewById(R.id.u_icon);
//            commentView = itemView.findViewById(R.id.author);
//        }
//
//        public void bind(final Uri uri, final SpotListAdapter.OnItemSelectedListener listener) {
//
//            Uri current = .get(position);
//            holder.itemView.setTag(current._id);
//            holder.numberView.setText(String.valueOf(current._id));
//            holder.timeView.setText("計測日時：" + current.created_at);
//            holder.titleView.setText("タイトル：" + current.title);
//
//            // Click listener
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener != null) {
//                        listener.onItemSelected(itemView, getAdapterPosition());
//                    }
//                }
//            });
//        }
//
//    }
//
//    public interface OnItemSelectedListener {
//        void onItemSelected(View view, int position);
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mPostList != null)
//            return mPostList.size();
//        else return 0;
//    }
//}
