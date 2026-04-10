package com.example.ac1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etCity;
    private Spinner spCategory, spFilter;
    private CheckBox cbFavorite;
    private Button btnSave;
    private ListView lvContacts;

    private DatabaseHelper dbHelper;
    private AdaptarContato adapter;
    private Contato selectedContato = null;

    private final String[] categories = {"Família", "Amigos", "Trabalho", "Outros"};
    private final String[] filterOptions = {"Todos", "Família", "Amigos", "Trabalho", "Outros"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etCity = findViewById(R.id.etCity);
        spCategory = findViewById(R.id.spCategory);
        spFilter = findViewById(R.id.spFilter);
        cbFavorite = findViewById(R.id.cbFavorite);
        btnSave = findViewById(R.id.btnSave);
        lvContacts = findViewById(R.id.lvContacts);

        setupSpinners();
        loadContacts();

        btnSave.setOnClickListener(v -> saveContact());

        lvContacts.setOnItemClickListener((parent, view, position, id) -> {
            selectedContato = (Contato) parent.getItemAtPosition(position);
            loadContactToFields(selectedContato);
        });

        lvContacts.setOnItemLongClickListener((parent, view, position, id) -> {
            Contato contato = (Contato) parent.getItemAtPosition(position);
            showDeleteConfirmation(contato);
            return true;
        });

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadContacts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSpinners() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filterOptions);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(filterAdapter);
    }

    private void loadContacts() {
        String filter = spFilter.getSelectedItem().toString();
        List<Contato> contatoList = dbHelper.getAllContacts(filter);
        adapter = new AdaptarContato(this, contatoList);
        lvContacts.setAdapter(adapter);
    }

    private void saveContact() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        boolean favorite = cbFavorite.isChecked();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nome, Telefone e E-mail são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedContato == null) {
            // New Contact
            Contato contato = new Contato(0, name, phone, email, category, city, favorite);
            dbHelper.addContact(contato);
            Toast.makeText(this, "Contato salvo com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            // Update Contact
            selectedContato.setName(name);
            selectedContato.setPhone(phone);
            selectedContato.setEmail(email);
            selectedContato.setCategory(category);
            selectedContato.setCity(city);
            selectedContato.setFavorite(favorite);
            dbHelper.updateContact(selectedContato);
            Toast.makeText(this, "Contato atualizado com sucesso", Toast.LENGTH_SHORT).show();
            selectedContato = null;
        }

        clearFields();
        loadContacts();
    }

    private void loadContactToFields(Contato contato) {
        etName.setText(contato.getName());
        etPhone.setText(contato.getPhone());
        etEmail.setText(contato.getEmail());
        etCity.setText(contato.getCity());
        cbFavorite.setChecked(contato.isFavorite());

        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(contato.getCategory())) {
                spCategory.setSelection(i);
                break;
            }
        }
    }

    private void showDeleteConfirmation(Contato contato) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Contato")
                .setMessage("Deseja realmente excluir " + contato.getName() + "?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    dbHelper.deleteContact(contato.getId());
                    loadContacts();
                    Toast.makeText(MainActivity.this, "Contato excluído", Toast.LENGTH_SHORT).show();
                    if (selectedContato != null && selectedContato.getId() == contato.getId()) {
                        clearFields();
                        selectedContato = null;
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etCity.setText("");
        spCategory.setSelection(0);
        cbFavorite.setChecked(false);
        selectedContato = null;
    }
}
