package com.manas.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER =  2;
    private static final int RC_SIGN_IN=1;

    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotoStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseStorage=FirebaseStorage.getInstance();

        mMessageDatabaseReference=mFirebaseDatabase.getReference().child("messages");
        mPhotoStorageReference=mFirebaseStorage.getReference().child("chat_photos");

        // Initialize references to views
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);


        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);

        // Initialize message ListView and its adapter
        final List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            public void afterTextChanged(Editable editable) { }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click

                FriendlyMessage mfriendlyMessage=new FriendlyMessage(mMessageEditText.getText().toString(),mUsername,null);
                mMessageDatabaseReference.push().setValue(mfriendlyMessage);
                // Clear input box
                mMessageEditText.setText("");
            }
        });

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    onSignedInInitialize(user.getDisplayName());
                }
                else {
                    onSignedOutCleanUp();
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(MainActivity.this,"Signed In",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode==RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Signed In Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else if(requestCode==RC_PHOTO_PICKER && resultCode==RESULT_OK){
            Uri selectedPhotoUri=data.getData();
            final StorageReference photoref=mPhotoStorageReference.child(selectedPhotoUri.getLastPathSegment());
            photoref.putFile(selectedPhotoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    // Forward any exceptions
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }


                    // Request the public download URL
                    return photoref.getDownloadUrl();
                }
            })
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(@NonNull Uri downloadUri) {

                    FriendlyMessage friendlyMessage=new FriendlyMessage(null,mUsername,downloadUri.toString());
                    mMessageDatabaseReference.push().setValue(friendlyMessage);
                }
            });

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void onPause(){
        super.onPause();
        if(mFirebaseAuth!=null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        mMessageAdapter.clear();
        detachDatabaseReferenceListener();
    }
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void onSignedInInitialize(String username){
        mUsername=username;
        attachDatabaseReferenceListener();
    }
    private void onSignedOutCleanUp(){
        mUsername=ANONYMOUS;
        mMessageAdapter.clear();
        detachDatabaseReferenceListener();
    }
    private void attachDatabaseReferenceListener(){
        if(mChildEventListener==null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);
                }

                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
            mMessageDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
    private void detachDatabaseReferenceListener(){
        if(mChildEventListener!=null) {
            mMessageDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}

