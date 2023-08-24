package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityLoginBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.SharePrefereces;


public class ActivityLogin extends AppCompatActivity {
    ActivityLoginBinding binding;
    ActivityLogin activity;
    private String cookies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activity = this;

        LoadPage();
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage();
            }
        });

    }

    public void LoadPage() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.clearCache(true);
        binding.webView.setWebViewClient(new MyBrowser());
        CookieSyncManager.createInstance(activity);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        binding.webView.loadUrl("https://www.instagram.com/accounts/login/");
        binding.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    binding.swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }

    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if (cookies != null && !cookies.isEmpty()) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(CookieName)) {
                    String[] temp1 = ar1.split("=");
                    CookieValue = temp1[1];
                    break;
                }
            }
        }
        return CookieValue;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            //getCookieString(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            //getCookieString(str);

            cookies = CookieManager.getInstance().getCookie(str);

            try {
                String session_id = getCookie(str, "sessionid");
                String csrftoken = getCookie(str, "csrftoken");
                String userid = getCookie(str, "ds_user_id");
                if (session_id != null && csrftoken != null && userid != null) {
                    SharePrefereces.getInstance(activity).putString(SharePrefereces.COOKIES, cookies);
                    SharePrefereces.getInstance(activity).putString(SharePrefereces.CSRF, csrftoken);
                    SharePrefereces.getInstance(activity).putString(SharePrefereces.SESSIONID, session_id);
                    SharePrefereces.getInstance(activity).putString(SharePrefereces.USERID, userid);
                    SharePrefereces.getInstance(activity).putBoolean(SharePrefereces.ISINSTALOGIN, true);

                    binding.webView.destroy();
                    Intent intent= new Intent();
                    intent.putExtra("result","result");
                    setResult(RESULT_OK,intent);
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }
}