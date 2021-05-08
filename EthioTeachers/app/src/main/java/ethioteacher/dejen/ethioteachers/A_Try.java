package ethioteacher.dejen.ethioteachers;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by Temu on 1/7/2018.
 */

public class A_Try extends Activity {
    private RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Button button = new Button(this);
        button.setText("Save");
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);

        relativeLayout.addView(button);
        setContentView(relativeLayout, layoutParams);
    }
}
