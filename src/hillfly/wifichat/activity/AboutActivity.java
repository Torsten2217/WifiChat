package hillfly.wifichat.activity;

import hillfly.wifichat.BaseActivity;
import hillfly.wifichat.R;
import android.os.Bundle;
import android.view.MenuItem;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initEvents();
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {
        setTitle(getString(R.string.setting_text_sysconfig_aboutus));
        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    // actionBar Listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    } 

}
