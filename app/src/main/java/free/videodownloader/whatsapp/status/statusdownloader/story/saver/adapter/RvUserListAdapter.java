package free.videodownloader.whatsapp.status.statusdownloader.story.saver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ItemUserListBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.interfaces.UserListInterface;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.story.TrayModel;

import java.util.ArrayList;

public class RvUserListAdapter extends RecyclerView.Adapter<RvUserListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TrayModel> ModelArrayList;
    private UserListInterface userListInterface;

    public RvUserListAdapter(Context context, ArrayList<TrayModel> list, UserListInterface listInterface) {
        this.context = context;
        this.ModelArrayList = list;
        this.userListInterface = listInterface;
    }

    @NonNull
    @Override
    public RvUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_user_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RvUserListAdapter.ViewHolder viewHolder, int position) {

        viewHolder.binding.realName.setText(ModelArrayList.get(position).getUser().getFull_name());
        Glide.with(context).load(ModelArrayList.get(position).getUser().getProfile_pic_url())
                .thumbnail(0.2f).into(viewHolder.binding.storyIcon);

        viewHolder.binding.RLStoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userListInterface.userListClick(position,ModelArrayList.get(position));
            }
        });

    }
    @Override
    public int getItemCount() {
        return ModelArrayList == null ? 0 : ModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         ItemUserListBinding binding;
        public ViewHolder(ItemUserListBinding mbinding) {
            super(mbinding.getRoot());
            this.binding = mbinding;
        }
    }
}