package com.example.rootshareapp.adapter;

public class ItemListAdapter
//        extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>
{

//    public static final String TAG = "ItemListAdapter";
//
//    private List<Post> mPostList;
//    private PostListSearchedAdapter.OnPostSelectedListener mListener;
//
//    public ItemListAdapter(List<Post> postList, ItemListAdapter.ItemViewHolder Listener) {
//        mPostList = postList;
//        mListener = Listener;
//    }
//
//    @Override
//    public ItemListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.e("post1", "hello");
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_item, parent, false);
//        return new ItemListAdapter.ItemViewHolder(v, parent.getContext());
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ItemListAdapter.ItemViewHolder holder, int position) {
//        Log.e("post2", "hello");
//        holder.bind(mPostList.get(position), mListener);
//    }
//
//    static class ItemViewHolder extends RecyclerView.ViewHolder {
//
//        Context mContext;
//        ImageView uIconBtn;
//        TextView authorView;
//        TextView unameView;
//        TextView created_atView;
//        TextView bodyView;
//
//        public ItemViewHolder(@NonNull View itemView, Context context) {
//            super(itemView);
//            mContext = context;
//            uIconBtn = itemView.findViewById(R.id.u_icon);
//            authorView = itemView.findViewById(R.id.author);
//            unameView = itemView.findViewById(R.id.uname);
//            created_atView = itemView.findViewById(R.id.post_created_at);
//            bodyView = itemView.findViewById(R.id.body);
//        }
//
//        public void bind(final Post post, final ItemListAdapter.OnItemSelectedListener listener) {
//
//            authorView.setText(String.valueOf(document.getData().get("display_name")));
//            unameView.setText(String.valueOf(document.getData().get("user_name")));
//            created_atView.setText(post.getCreated_at());
//            bodyView.setText(post.getBody());
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
}
