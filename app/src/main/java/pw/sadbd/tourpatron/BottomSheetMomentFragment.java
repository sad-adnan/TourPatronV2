package pw.sadbd.tourpatron;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pw.sadbd.tourpatron.PojoClass.Moment;
import pw.sadbd.tourpatron.PojoClass.StaticData;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

public class BottomSheetMomentFragment extends BottomSheetDialogFragment {
    private static final int REQUEST_IMAGE_CAPTURE_CAMERA = 838;
    private static final int PERMISSION_CODE = 788;
    private static final int SELECT_IMAGE = 999;
    private static final int REQUEST_TAKE_PHOTO =10 ;
    private String currentPhotoPath;
    private boolean isPermissionGranated =false;
    private ImageButton cameraButton;
    private ImageButton galleary;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.bottom_sheet_moment_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        cameraButton = view.findViewById(R.id.camerabutton);
        galleary = view.findViewById(R.id.gallery_button);
        checkStoragePermission();
        galleary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_IMAGE);
    }
    private void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.tourmate",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE && resultCode == RESULT_OK){
          Uri imguri =  data.getData();
            firebaseStorageUpload(imguri);
        }else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //setPic();
            Uri fileUri = Uri.fromFile(new File(currentPhotoPath));

            firebaseStorageUpload(fileUri);
        }
    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void firebaseStorageUpload(Uri imgUir){
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        final Bitmap mainBitmap;
        Bitmap outbitmap = null;

        try {
             mainBitmap =
                    MediaStore
                            .Images
                            .Media
                            .getBitmap(getActivity().getContentResolver(),imgUir);
            outbitmap = Bitmap.createScaledBitmap(mainBitmap,
                    (int) ( mainBitmap.getWidth()*0.3),
                    (int)( mainBitmap.getHeight()*0.3),
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        outbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef =
                storageReference.child("moment").child(imgUir.getLastPathSegment());
        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return imgRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                progressDialog.dismiss();
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                DatabaseReference ref  = FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Event")
                        .child(StaticData.eventID)
                        .child("Moments");
                DatabaseReference keyref= ref.push();
                String key = keyref.getKey();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String date = dateFormat.format(new Date());
                Moment moment = new Moment(key,username,date,task.getResult().toString());
                keyref.setValue(moment);
                dismiss();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                dismiss();
            }
        });


    }

    public void checkStoragePermission(){
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE);
        }else {
            isPermissionGranated =true;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] !=PackageManager.PERMISSION_GRANTED
                && grantResults[1] != PackageManager.PERMISSION_GRANTED){
            isPermissionGranated = false;
            checkStoragePermission();
        }else {
            isPermissionGranated =true;
        }
    }
}
