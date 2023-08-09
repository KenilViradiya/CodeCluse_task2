package com.example.pdfscannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PDF_SELECTION_CODE = 200;

    private PDFView pdfView;

    private RecyclerView recyclerView;
    private List<File> pdfFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfView = findViewById(R.id.pdfView);
        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnListPdfs = findViewById(R.id.btn_list_pdfs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                selectPDFFile();
            } else {
                requestPermission();
            }
        } else {
            selectPDFFile();
        }
    }



    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPDFFile();
        } else {
            Toast.makeText(this, "Permission Denied! Please allow the permission to read PDF files.", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPDFFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), PDF_SELECTION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PDF_SELECTION_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            displayPDF(selectedFileUri);
        }
    }

    private void displayPDF(Uri fileUri) {
        pdfView.fromUri(fileUri)
                .defaultPage(0)
                .enableSwipe(true)
                .enableDoubletap(true)
                .swipeHorizontal(false)
                .onLoad(nbPages -> {
                })
                .onPageChange((page, pageCount) -> {
                })
                .scrollHandle(new DefaultScrollHandle(this))
                .enableAnnotationRendering(true)
                .password(null)
                .load();
    }
}
