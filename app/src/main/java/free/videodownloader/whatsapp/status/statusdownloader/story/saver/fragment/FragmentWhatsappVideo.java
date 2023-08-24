package free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.adapter.RvWhatsappStatusAdapter;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.FragmentWhatsappImageBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.WhatsappStatusModel;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Pref;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static androidx.databinding.DataBindingUtil.inflate;

public class FragmentWhatsappVideo extends Fragment {
    FragmentWhatsappImageBinding binding;

//    private File[] allfiles;
    private DocumentFile[] allFiles;
    ArrayList<WhatsappStatusModel> statusModelArrayList=new ArrayList<>();
    private RvWhatsappStatusAdapter rvWhatsappStatusAdapter;
    Pref pref;
    int REQUEST_ACTION_OPEN_DOCUMENT_TREE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, R.layout.fragment_whatsapp_image, container, false);
        Permission();
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        statusModelArrayList = new ArrayList<>();
        getWhatsStatus(this.pref.get_WhatsApp_Uri());
        binding.swiperefresh.setOnRefreshListener(() -> {
            statusModelArrayList = new ArrayList<>();
            getWhatsStatus(this.pref.get_WhatsApp_Uri());
            binding.swiperefresh.setRefreshing(false);
        });

    }
    @SuppressLint("WrongConstant")
    public void Permission() {
        Intent intent;
        Pref pref2 = new Pref(getContext());
        this.pref = pref2;
        if (pref2.get_WhatsApp_Uri().equalsIgnoreCase("")) {
            try {
                @SuppressLint("WrongConstant") StorageManager storageManager = (StorageManager) getActivity().getSystemService("storage");
                String Get_WhatsApp_Folder = Get_WhatsApp_Folder();
                if (Build.VERSION.SDK_INT >= 29) {
                    intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                    String replace = ((Uri) intent.getParcelableExtra("android.provider.extra.INITIAL_URI")).toString().replace("/root/", "/document/");
                    intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(replace + "%3A" + Get_WhatsApp_Folder));
                } else {
                    intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
                    intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + Get_WhatsApp_Folder));
                }
                intent.addFlags(2);
                intent.addFlags(1);
                intent.addFlags(128);
                intent.addFlags(64);
                startActivityForResult(intent, this.REQUEST_ACTION_OPEN_DOCUMENT_TREE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getWhatsStatus(this.pref.get_WhatsApp_Uri());
        }
    }


//    private void getData() {
//        WhatsappStatusModel whatsappStatusModel;
//        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
//        File targetDirector = new File(targetPath);
//        allfiles = targetDirector.listFiles();
//
//        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses";
//        File targetDirectorBusiness = new File(targetPathBusiness);
//        File[] allfilesBusiness = targetDirectorBusiness.listFiles();
//
//        try {
//            Arrays.sort(allfiles, (Comparator) (o1, o2) -> {
//                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
//                    return -1;
//                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
//                    return +1;
//                } else {
//                    return 0;
//                }
//            });
//
//            for (int i = 0; i < allfiles.length; i++) {
//                File file = allfiles[i];
//                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
//                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatus: " + (i + 1),
//                            Uri.fromFile(file),
//                            allfiles[i].getAbsolutePath(),
//                            file.getName());
//                    statusModelArrayList.add(whatsappStatusModel);
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            Arrays.sort(allfilesBusiness, (Comparator) (o1, o2) -> {
//                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
//                    return -1;
//                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
//                    return +1;
//                } else {
//                    return 0;
//                }
//            });
//
//            for (int i = 0; i < allfilesBusiness.length; i++) {
//                File file = allfilesBusiness[i];
//                if (Uri.fromFile(file).toString().endsWith(".mp4")) {
//                    whatsappStatusModel = new WhatsappStatusModel("WhatsStatusB: " + (i + 1),
//                            Uri.fromFile(file),
//                            allfilesBusiness[i].getAbsolutePath(),
//                            file.getName());
//                    statusModelArrayList.add(whatsappStatusModel);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (statusModelArrayList.size() != 0) {
//            binding.tvNoResult.setVisibility(View.GONE);
//        } else {
//            binding.tvNoResult.setVisibility(View.VISIBLE);
//        }
//        rvWhatsappStatusAdapter = new RvWhatsappStatusAdapter(getActivity(), statusModelArrayList);
//        binding.rvFileList.setAdapter(rvWhatsappStatusAdapter);
//    }

    @SuppressLint("WrongConstant")
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == this.REQUEST_ACTION_OPEN_DOCUMENT_TREE && i2 == -1) {
            Uri data = intent.getData();
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    getActivity().getContentResolver().takePersistableUriPermission(data, 3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.pref.set_WhatsApp_Uri(data.toString());
            getWhatsStatus(this.pref.get_WhatsApp_Uri());
            Log.e("@@TAG", "onActivityResult: " + this.pref.get_WhatsApp_Uri());
        }
    }

    public String Get_WhatsApp_Folder() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append(File.separator);
        sb.append("Android/media/com.whatsapp/WhatsApp");
        sb.append(File.separator);
        sb.append("Media");
        sb.append(File.separator);
        sb.append(".Statuses");
        return new File(sb.toString()).isDirectory() ? "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses" : "WhatsApp%2FMedia%2F.Statuses";
    }

    @SuppressLint("WrongConstant")
    private void getWhatsStatus(String str) {
        if (!str.isEmpty()) {
            this.statusModelArrayList.clear();
            DocumentFile[] fromSdcard = getFromSdcard(str);
            this.allFiles = fromSdcard;
            if (fromSdcard != null && fromSdcard.length != 0) {
                int i = 0;
                while (true) {
                    DocumentFile[] documentFileArr = this.allFiles;
                    if (i >= documentFileArr.length) {
                        break;
                    }
                    if (!documentFileArr[i].getUri().toString().contains(".nomedia") && this.allFiles[i].getUri().toString().contains(".mp4")) {
                        File file = new File(allFiles[i].getUri().getPath());
                        WhatsappStatusModel   whatsappStatusModel = new WhatsappStatusModel("WhatsStatusB: " + (i + 1),
                            Uri.fromFile(file),
                                allFiles[i].getUri(),
                            file.getName());
                    statusModelArrayList.add(whatsappStatusModel);
//                        this.d1.add(new StatusModel(this.allFiles[i].getUri().toString()));
                    }
                    i++;
                }
               if (statusModelArrayList.size() != 0) {
                   binding.tvNoResult.setVisibility(View.GONE);
               } else {
                   binding.tvNoResult.setVisibility(View.VISIBLE);
               }
               rvWhatsappStatusAdapter = new RvWhatsappStatusAdapter(getActivity(), statusModelArrayList);
               binding.rvFileList.setAdapter(rvWhatsappStatusAdapter);
            }
        }
    }
    private DocumentFile[] getFromSdcard(String str) {
        DocumentFile fromTreeUri = DocumentFile.fromTreeUri(getContext(), Uri.parse(str));
        if (fromTreeUri == null || !fromTreeUri.exists() || !fromTreeUri.isDirectory() || !fromTreeUri.canRead() || !fromTreeUri.canWrite()) {
            return null;
        }
        return fromTreeUri.listFiles();
    }
}
