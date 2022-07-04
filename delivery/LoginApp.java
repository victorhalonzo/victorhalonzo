package app.elingeniero.atupuerta.delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class LoginApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_app);
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.login,new Login()).commit();
    }
}
