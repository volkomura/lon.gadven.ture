package lon.gadven.ture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {


    public  String appId = "";

    public String link;
    private final int CODE_PERMISSIONS = 100;
    String template;
    String regex1 = "\\{";
    String regex2 = "\\}";
    String device_id_key = regex1+ "device_id" + regex2;
    String deviceId;


    @Override
    public void onBackPressed() {
    }



    public void toWeb()
    {
        Intent intent = new Intent(MainActivity.this, Landing.class);
        intent.putExtra("url",link);
        startActivity(intent);
        finish();
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        setProgressBarIndeterminateVisibility(true);

        appId = BuildConfig.APPLICATION_ID;

        deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        template = getResources().getString(R.string.link);

        link = template.replaceAll(device_id_key, deviceId);

        askUserToGivePermissions();

    }


    public void askUserToGivePermissions() {
        if (Build.VERSION.SDK_INT < 23) {

            toWeb();

        }
        else {

            int permissionCode = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

            if (permissionCode != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        CODE_PERMISSIONS);

            }

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CODE_PERMISSIONS && grantResults.length == 2) {
            toWeb();
        } else {

            toWeb();
        }
    }


}

