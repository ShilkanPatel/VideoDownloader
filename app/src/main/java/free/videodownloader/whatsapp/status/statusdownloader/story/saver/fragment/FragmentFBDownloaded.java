package free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity.ActivityFullView;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity.ActivityGallery;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.adapter.RvFileListAdapter;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.FragmentHistoryBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.interfaces.FileListClickInterface;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import static androidx.databinding.DataBindingUtil.inflate;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.RootDirectoryFacebookShow;

public class FragmentFBDownloaded extends Fragment implements FileListClickInterface {
    private FragmentHistoryBinding binding;
    private RvFileListAdapter rvFileListAdapter;
    private ArrayList<File> fileArrayList;
    private ActivityGallery activity;

    public static FragmentFBDownloaded newInstance(String param1) {
        FragmentFBDownloaded fragment = new FragmentFBDownloaded();
        Bundle args = new Bundle();
        args.putString("m", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context _context) {
        super.onAttach(_context);
        activity = (ActivityGallery) _context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("m");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (ActivityGallery) getActivity();
        getAllFiles();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, R.layout.fragment_history, container, false);
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        binding.swiperefresh.setOnRefreshListener(() -> {
            getAllFiles();
            binding.swiperefresh.setRefreshing(false);
        });
    }

    private void getAllFiles() {
        fileArrayList = new ArrayList<>();
        File[] files = RootDirectoryFacebookShow.listFiles();
        if (files != null) {
            for (File file : files) {
                fileArrayList.add(file);
            }

            rvFileListAdapter = new RvFileListAdapter(activity, fileArrayList, FragmentFBDownloaded.this);
            binding.rvFileList.setAdapter(rvFileListAdapter);
        }

    }

    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(activity, ActivityFullView.class);
        inNext.putExtra("ImageDataFile", fileArrayList);
        inNext.putExtra("Position", position);
        activity.startActivity(inNext);
    }
}
