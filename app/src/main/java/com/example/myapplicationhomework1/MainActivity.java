package com.example.myapplicationhomework1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplicationhomework1.Student;

public class MainActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName, etEmail;
    private RadioGroup rgGrade;
    private Spinner spinnerMajor;
    private CheckBox cbNotifications, cbTerms;
    private Button btnRegister;
    private TextView tvResult;
    private TextView tvMajorError;

    private static final String PREFS_NAME = "StudentPrefs";
    private static final String KEY_FIRST = "first";
    private static final String KEY_LAST = "last";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_MAJOR = "major";
    private static final String KEY_NOTIF = "notif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // קריאות אתחול ממוקמות כאן, בחוץ ובסדר הנכון
        initViews();
        setupSpinner();
        loadSavedData();

        btnRegister.setOnClickListener(v -> attemptRegistration());
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        rgGrade = findViewById(R.id.rgGrade);
        spinnerMajor = findViewById(R.id.spinnerMajor);
        cbNotifications = findViewById(R.id.cbNotifications);
        cbTerms = findViewById(R.id.cbTerms);
        btnRegister = findViewById(R.id.btnRegister);
        tvResult = findViewById(R.id.tvResult);
        tvMajorError = findViewById(R.id.tvMajorError);
    }

    private void setupSpinner() {
        String[] majors = {getString(R.string.prompt_major), "מדעי המחשב", "פיזיקה", "ביולוגיה", "כימיה", "אמנות"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, majors);
        spinnerMajor.setAdapter(adapter);
    }

    private void attemptRegistration() {
        etFirstName.setError(null);
        etLastName.setError(null);
        etEmail.setError(null);
        tvMajorError.setVisibility(View.GONE);

        boolean isValid = true;

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (firstName.isEmpty()) {
            etFirstName.setError(getString(R.string.error_field_required));
            etFirstName.requestFocus();
            isValid = false;
        }

        if (lastName.isEmpty()) {
            etLastName.setError(getString(R.string.error_field_required));
            if (isValid) etLastName.requestFocus();
            isValid = false;
        }

        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.error_field_required));
            if (isValid) etEmail.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_invalid_email));
            if (isValid) etEmail.requestFocus();
            isValid = false;
        }

        String grade = "";
        int selectedGradeId = rgGrade.getCheckedRadioButtonId();
        if (selectedGradeId == -1) {
            Toast.makeText(this, R.string.error_select_grade, Toast.LENGTH_SHORT).show();
            isValid = false;
        } else {
            RadioButton selectedRb = findViewById(selectedGradeId);
            grade = selectedRb.getText().toString();
        }

        String major = "";
        if (spinnerMajor.getSelectedItemPosition() == 0) {
            tvMajorError.setText(R.string.error_select_major);
            tvMajorError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            major = spinnerMajor.getSelectedItem().toString();
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, R.string.error_terms, Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        boolean notifications = cbNotifications.isChecked();

        Student student = new Student(firstName, lastName, email, grade, major, notifications);
        saveStudentData(student);

        tvResult.setText(student.toString());
        tvResult.setVisibility(View.VISIBLE);
    }

    private void saveStudentData(Student student) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FIRST, student.getFirstName());
        editor.putString(KEY_LAST, student.getLastName());
        editor.putString(KEY_EMAIL, student.getEmail());
        editor.putString(KEY_GRADE, student.getGrade());
        editor.putString(KEY_MAJOR, student.getMajor());
        editor.putBoolean(KEY_NOTIF, student.isNotifications());
        editor.apply();
    }

    private void loadSavedData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains(KEY_FIRST)) {
            String firstName = prefs.getString(KEY_FIRST, "");
            String lastName = prefs.getString(KEY_LAST, "");
            String email = prefs.getString(KEY_EMAIL, "");
            String grade = prefs.getString(KEY_GRADE, "");
            String major = prefs.getString(KEY_MAJOR, "");
            boolean notif = prefs.getBoolean(KEY_NOTIF, false);

            Student savedStudent = new Student(firstName, lastName, email, grade, major, notif);
            tvResult.setText(savedStudent.toString());
            tvResult.setVisibility(View.VISIBLE);
        } else {
            tvResult.setVisibility(View.GONE);
        }
    }
}