package mad9132.doo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import mad9132.doo.model.BuildingPOJO;
import mad9132.doo.services.MyService;
import mad9132.doo.utils.NetworkHelper;

/**
 * Add credentials for basic authentication.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @author David Gassner (original)
 *
 * Reference: Chapter 3. Requesting Data over the Web
 *            "Android App Development: RESTful Web Services" with David Gassner
 */
public class MainActivity extends Activity {

    // TODO #1 - change URL to "secure"; requires basic authentication (i.e. username + password)
    private static final String JSON_URL = "https://doors-open-ottawa.mybluemix.net/buildings/secure";

    private boolean networkOk;
    private TextView output;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(MyService.MY_SERVICE_PAYLOAD)) {
                BuildingPOJO[] buildingsArray = (BuildingPOJO[]) intent
                        .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
                for (BuildingPOJO aBuilding : buildingsArray) {
                    output.append(aBuilding.getNameEN() + "\n");
                }
            } else if (intent.hasExtra(MyService.MY_SERVICE_EXCEPTION)){
                String message = intent.getStringExtra(MyService.MY_SERVICE_EXCEPTION);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(this);
        output.append("Network ok: " + networkOk);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public void runClickHandler(View view) {

        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearClickHandler(View view) {
        output.setText("");
    }
}
