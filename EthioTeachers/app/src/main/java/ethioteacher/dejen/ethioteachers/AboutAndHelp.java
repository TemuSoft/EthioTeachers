package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

public class AboutAndHelp extends Activity {

    private WebView webView;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about_and_help);

        webView = (WebView) findViewById(R.id.webview);
        AboutOrHelp();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");
    }

    public void AboutOrHelp() {
        if (getIntent().getStringExtra("operation").equals("about")) {
            text = "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body bgcolor=\"#d2d2d5\"><br><br><br>\n" +
                    "<p align = \"center\">Teaching is the supreme art of teachers</p>\n" +
                    "<p align = \"center\">to awaken joy in creative </p>\n" +
                    "<p align = \"center\">expression and knowldge!</p>\n" +
                    "<h3 align = \"center\">Developer :Temesgen kefie</h3>\n" +
                    "<h3 align = \"center\">Contact</h3>\n" +
                    "<h4 align =\"center\"> Gmail  :temesgenkefie26@gmail.com</h4>\n" +
                    "<p align=\"center\">Ethio teachers V1.0</p>\n" +
                    "<p align=\"center\">Copyright 2009 E.C</p>\n" +
                    "</body>\n" +
                    "</html>";
        } else if (getIntent().getStringExtra("operation").equals("help")) {
            text = "<html>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body bgcolor=\"#4a7d49\">\n" +
                    "<h4 align =\"center\">About account</h4>\n" +
                    "<p align =\"center\">\n" +
                    "To create account user name length<br> \n" +
                    "must between 1 and 25 <br>\n" +
                    "and <br> \n" +
                    "password length must <br> \n" +
                    "between 1 and 12</p>\n" +
                    "<h4 align =\"center\">Add,Update,Delete,Search student</h4><br>\n" +
                    "<p align =\"center\" >If you want to register student by<br>\n" +
                    " information with out details result <br>\n" +
                    " like out of 100, 60 ,40, ... you can,<br>\n" +
                    " but at the time of registration <br>\n" +
                    " Student name ,father name, Grade <br>\n" +
                    " and Number can't be null and<br>\n" +
                    " at the time Grade and number <br>\n" +
                    " can't be repeated!<br><br>\n" +
                    " after register successfuly you<br>\n" +
                    " can update any student information and result. </p>\n" +
                    "<h4 align =\"center\">Send and Call</h4>\n" +
                    "<p align =\"center\">Search or click at the menu button <br>\n" +
                    "show all then from the list click<br>\n" +
                    " you want to send full and detail <br>\n" +
                    " information by SMS to the selected student only <br>\n" +
                    " but <br>\n" +
                    " use airtime <br>\n" +
                    " which may cost you money.</p>\n" +
                    "</body>\n" +
                    "</html>";
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AboutAndHelp.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
