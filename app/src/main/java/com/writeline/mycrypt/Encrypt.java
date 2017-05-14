package com.writeline.mycrypt;

import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Encrypt extends AppCompatActivity implements View.OnClickListener {

    EditText keyEditText, plainEditText;
    TextView outputTextView;
    Button encryptButton, cleanButton;

    String key, plainText, cipherText;
    char tmpCh = ' ';
    int tmpInt = 0, j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encrypt);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_mycrypt);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        keyEditText = (EditText) findViewById(R.id.key_input_e);
        plainEditText = (EditText) findViewById(R.id.text_input_e);
        outputTextView = (TextView) findViewById(R.id.output_tv_e);

        encryptButton = (Button) findViewById(R.id.encrypt_btn_e);
        assert encryptButton != null;
        encryptButton.setOnClickListener(this);
        cleanButton = (Button) findViewById(R.id.clean_btn_e);
        assert cleanButton != null;
        cleanButton.setOnClickListener(this);

        outputTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (outputTextView.getText().toString().isEmpty()) {
                    return true;
                }
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setText(outputTextView.getText());
                Toast.makeText(getApplicationContext(), R.string.msg_copied_toast, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        cleanButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (plainEditText.getText().toString().matches("") && keyEditText.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.already_clean_info, Toast.LENGTH_SHORT).show();
                } else {
                    keyEditText.setText("");
                    plainEditText.setText("");
                    outputTextView.setText("");
                }
                return true;
            }
        });
    }



    @Override
    public void onClick(View v) {
        // Reset variabel
        plainText = key = cipherText = "";
        tmpCh = ' ';
        tmpInt = j = 0;

        switch (v.getId()) {
            case R.id.encrypt_btn_e:
                vigenereAlg(v);
                break;
            case R.id.clean_btn_e:
                if (plainEditText.getText().toString().matches("") && keyEditText.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.already_clean_info, Toast.LENGTH_SHORT).show();
                } else {
                    plainEditText.setText("");
                    outputTextView.setText("");
                }
                break;
            default:
                break;
        }
    }

    public void vigenereAlg(View v) {
        if (plainEditText.getText().toString().matches("") && keyEditText.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), R.string.text_n_key_info, Toast.LENGTH_SHORT).show();
        } else if (plainEditText.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), R.string.text_info, Toast.LENGTH_SHORT).show();
        } else if (keyEditText.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), R.string.key_info, Toast.LENGTH_SHORT).show();
        } else {
            key = keyEditText.getText().toString();
            plainText = plainEditText.getText().toString();
            int keyArray[] = new int[key.length()];
            char cipherArray[] = new char[plainText.length()];
            for (int i = 0; i < key.length(); i++) {
                if (Character.isLetter(key.charAt(i)) && Character.isUpperCase(key.charAt(i))) {
                    keyArray[i] = key.charAt(i) - 'A';
                } else if (Character.isLetter(key.charAt(i)) && Character.isLowerCase(key.charAt(i))) {
                    keyArray[i] = key.charAt(i) - 'a';
                } else {
                    continue;
                }
            }
            for (int i = 0; i < plainText.length(); i++) {
                if (Character.isLetter(plainText.charAt(i))) {
                    if (Character.isUpperCase(plainText.charAt(i))) {
                        tmpCh = plainText.charAt(i);
                        tmpCh -= 'A';
                        switch (v.getId()) {
                            case R.id.encrypt_btn_e:
                                tmpInt = ((tmpCh + keyArray[j]) % 26) + 'A';
                                break;
                        }
                        cipherArray[i] = (char) tmpInt;
                    } else if (Character.isLowerCase(plainText.charAt(i))) {
                        tmpCh = plainText.charAt(i);
                        tmpCh -= 'a';
                        switch (v.getId()) {
                            case R.id.encrypt_btn_e:
                                tmpInt = ((tmpCh + keyArray[j]) % 26) + 'a';
                                break;
                        }
                        cipherArray[i] = (char) tmpInt;
                    }
                    if (j >= 0 && j < key.length() - 1) {
                        j++;
                    } else {
                        j = 0;
                    }
                } else {
                    cipherText += plainText.charAt(i);
                }
                cipherText += cipherArray[i];
            }
            outputTextView.setText(cipherText);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menambahkan icon menu pada toolbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //memberi aksi menu
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
