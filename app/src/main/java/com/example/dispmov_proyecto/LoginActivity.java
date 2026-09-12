package com.example.dispmov_proyecto;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private RadioGroup rgRol;
    private EditText etPassword;
    private Button btnIngresar;
    private TextView tvError;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rgRol = findViewById(R.id.rgRol);
        etPassword = findViewById(R.id.etPassword);
        btnIngresar = findViewById(R.id.btnIngresar);
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressBar);

        btnIngresar.setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String rolSeleccionado = obtenerRolSeleccionado();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            mostrarError("Ingresa tu contraseña");
            return;
        }

        mostrarCargando(true);

        // TODO: llamar a la API para validar credenciales según el rol
        simularRespuestaServidor(rolSeleccionado, password);
    }

    private String obtenerRolSeleccionado() {
        int idSeleccionado = rgRol.getCheckedRadioButtonId();
        if (idSeleccionado == R.id.rbSupervisor) {
            return "supervisor";
        }
        return "empleado";
    }

    private void simularRespuestaServidor(String rol, String password) {
        btnIngresar.postDelayed(() -> {
            mostrarCargando(false);

            boolean loginValido = true; // aquí vendrá la respuesta real de la API

            if (loginValido) {
                irAPantallaSegunRol(rol);
            } else {
                mostrarError("Contraseña incorrecta");
            }
        }, 1000);
    }

    private void irAPantallaSegunRol(String rol) {
        Toast.makeText(this, "Bienvenido, rol: " + rol, Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, rol.equals("supervisor") ? SupervisorActivity.class : TrabajadorActivity.class);
        // startActivity(intent);
        // finish();
    }

    private void mostrarError(String mensaje) {
        tvError.setText(mensaje);
        tvError.setVisibility(View.VISIBLE);
    }

    private void mostrarCargando(boolean cargando) {
        progressBar.setVisibility(cargando ? View.VISIBLE : View.GONE);
        btnIngresar.setEnabled(!cargando);
        tvError.setVisibility(View.GONE);
    }
}