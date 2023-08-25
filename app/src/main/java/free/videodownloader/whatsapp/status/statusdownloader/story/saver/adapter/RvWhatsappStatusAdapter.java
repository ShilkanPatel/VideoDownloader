package free.videodownloader.whatsapp.status.statusdownloader.story.saver.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mylibrary.BaseClass;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity.ActivityGallery;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity.ActivityMain;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity.HomeActivity;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ItemsWhatsappViewBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.WhatsappStatusModel;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.RootDirectoryWhatsappShow;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.createFileFolder;


public class RvWhatsappStatusAdapter extends RecyclerView.Adapter<RvWhatsappStatusAdapter.ViewHolder> {
    private Activity context;
    private ArrayList<WhatsappStatusModel> fileArrayList;
    private LayoutInflater layoutInflater;
    public String SaveFilePath = RootDirectoryWhatsappShow+ "/";
    public RvWhatsappStatusAdapter(Activity context, ArrayList<WhatsappStatusModel> files) {
        this.context = context;
        this.fileArrayList = files;
    }

    @NonNull
    @Override
    public RvWhatsappStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new ViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.items_whatsapp_view, viewGroup, false));
    }
   
    @Override
    public void onBindViewHolder(@NonNull RvWhatsappStatusAdapter.ViewHolder viewHolder, int i) {
        WhatsappStatusModel fileItem = fileArrayList.get(i);
        if (fileItem.getUri().toString().endsWith(".mp4")){
            viewHolder.binding.ivPlay.setVisibility(View.VISIBLE);
        }else {
            viewHolder.binding.ivPlay.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(fileItem.getUri2())
                .into(viewHolder.binding.pcw);


        viewHolder.binding.tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseClass.getInstance(context).showInterstitialAds(() -> {
                    createFileFolder();
                    final String path = fileItem.getUri().toString();
                    String filename=path.substring(path.lastIndexOf("/")+1);
                    final File file = new File(path);
                    File destFile = new File(SaveFilePath);
//                try {
//                    FileUtils.copyFileToDirectory(file, destFile);
                    copyFileInSavedDir(context,fileItem.getUri2().toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                    String fileNameChange=filename.substring(12);
                    File newFile = new File(SaveFilePath+fileNameChange);
                    String ContentType = "image/*";
                    if (fileItem.getUri().toString().endsWith(".mp4")){
                        ContentType = "video/*";
                    }else {
                        ContentType = "image/*";
                    }
                    MediaScannerConnection.scanFile(context, new String[]{newFile.getAbsolutePath()}, new String[]{ContentType},
                            new MediaScannerConnection.MediaScannerConnectionClient() {
                                public void onMediaScannerConnected() {
                                }

                                public void onScanCompleted(String path, Uri uri) {
                                }
                            });

                    File from = new File(SaveFilePath,filename);
                    File to = new File(SaveFilePath,fileNameChange);
                    from.renameTo(to);

                    showDownload();
                    Toast.makeText(context, context.getResources().getString(R.string.saved_to) + SaveFilePath + fileNameChange, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void showDownload() {
        final Dialog dialog = new Dialog(context,R.style.CustomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.download_suucess_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


//       Dialog customDialog;
//        customDialog = new Dialog(context);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View mView = inflater.inflate(R.layout.download_suucess_dialog, null);
        TextView showButton = dialog.findViewById(R.id.tvShow);
        LinearLayout native_layout = dialog.findViewById(R.id.native_layout);
        BaseClass.getInstance(context).showNativeAd(native_layout, null);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BaseClass.getInstance(context).showInterstitialAds(() -> {
                    context.startActivity(new Intent(context, ActivityGallery.class));
                });
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public  boolean copyFileInSavedDir(Context context, String str) {
        String absolutePath = getDirectory("StatusDownloader").getAbsolutePath();
        Uri fromFile = Uri.fromFile(new File(SaveFilePath + File.separator + new File(str).getName()));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read > 0) {
                    openOutputStream.write(bArr, 0, read);
                } else {
                    openInputStream.close();
                    openOutputStream.flush();
                    openOutputStream.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(fromFile);
                    context.sendBroadcast(intent);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


//    public  boolean copyFileInSavedDir(Context context, Uri str,File out) {
//
//        try {
//            InputStream openInputStream = context.getContentResolver().openInputStream(str);
//            OutputStream openOutputStream = context.getContentResolver().openOutputStream(Uri.fromFile(out.getAbsoluteFile()));
//            byte[] bArr = new byte[1024];
//            while (true) {
//                int read = openInputStream.read(bArr);
//                if (read > 0) {
//                    openOutputStream.write(bArr, 0, read);
//                } else {
//                    openInputStream.close();
//                    openOutputStream.flush();
//                    openOutputStream.close();
//                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
//                    intent.setData(Uri.fromFile(out));
//                    context.sendBroadcast(intent);
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public static File getDirectory(String str) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + NotificationCompat.CATEGORY_STATUS + File.separator + str);
        file.mkdirs();
        return file;
    }

    @Override
    public int getItemCount() {
        return fileArrayList == null ? 0 : fileArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemsWhatsappViewBinding binding;

        public ViewHolder(ItemsWhatsappViewBinding mbinding) {
            super(mbinding.getRoot());
            this.binding = mbinding;
        }
    }
}