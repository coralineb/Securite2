package com.example.louloutte.securite;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLoad(View view) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException {
        EditText mdp = (EditText) findViewById(R.id.mdp);
        EditText chaine = (EditText) findViewById(R.id.chaine);
        String texte;
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(mdp.getText().toString().getBytes());
        byte[] digest = md.digest();
        byte[] skfromdigest = Arrays.copyOf(digest,16);
        //creation secret key
        SecretKey sk=new SecretKeySpec(skfromdigest,"AES");
        Cipher c = Cipher.getInstance("AES");
        c.init (javax.crypto.Cipher.DECRYPT_MODE, sk);
        File file=new File(view.getContext().getFilesDir()+File.separator+"fic");
        ObjectInputStream oos=new ObjectInputStream(new CipherInputStream(new FileInputStream(file),c));
        texte=(String)oos.readObject();
        chaine.setText(texte);
    }

    public void onClickSave(View view) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
        EditText mdp = (EditText) findViewById(R.id.mdp);
        EditText chaine = (EditText) findViewById(R.id.chaine);
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(mdp.getText().toString().getBytes());
        byte[] digest = md.digest();
        byte[] skfromdigest = Arrays.copyOf(digest,16);
        //creation secret key
        SecretKey sk=new SecretKeySpec(skfromdigest,"AES");
        Cipher c = Cipher.getInstance("AES");
        c.init (javax.crypto.Cipher.ENCRYPT_MODE, sk);
        File file=new File(view.getContext().getFilesDir()+File.separator+"fic");
        ObjectOutputStream oos=new ObjectOutputStream(new CipherOutputStream(new FileOutputStream(file),c));
        oos.writeObject(chaine.getText().toString());
        oos.flush();
        oos.close();
    }
}
