package giubotta.gbsoft.mygallery;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Cell> allFilesPaths;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // per i permessi di STORAGE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000 );
        } else {
            // visualizza immagine
            showImages();
        }
    }

    // visualizza immagine nello schermo
    private void showImages() {
        // questo e per la cartella per tutte le immagini
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/image/";
        allFilesPaths = new ArrayList<>();
        allFilesPaths = listAllFiles(path);

        RecyclerView recyclerView = (RecyclerView) findViewById( R.id.gallery );
        recyclerView.setHasFixedSize(true);

        //  imposta la lista di 3 colonne
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( getApplicationContext(), 3 );
        recyclerView.setLayoutManager(layoutManager);

        // ottimizazzione
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        ArrayList<Cell> cells = prepareData();
        MyAdapter adapter = new MyAdapter( getApplicationContext(), cells );
        recyclerView.setAdapter(adapter);
    }

    // preparo le immagini per la lista
    private ArrayList<Cell> prepareData() {
        ArrayList<Cell> allImages = new ArrayList<>();
        for (Cell c : allFilesPaths) {
            Cell cell = new Cell();
            cell.setTitle( c.getTitle() );
            cell.setPath( c.getPath() );
            allImages.add( cell );
        }
        return allImages;
    }

    // load tuuti i file della cartella
    private List<Cell> listAllFiles(String pathName) {
        List<Cell> allFiles = new ArrayList<>();
        File file = new File( pathName );
        File[] files = file.listFiles();
        if (file != null) {
            for (File f : files) {
                Cell cell = new Cell();
                cell.setTitle( f.getName() );
                cell.setPath( f.getAbsolutePath() );
                allFiles.add( cell );
            }
        }
            return allFiles;
    }


        public void onRequestPermissionsResult(int reqestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
            if (reqestCode == 1000) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // show the image
                    showImages();
                } else {
                    Toast.makeText( this, "Permessi non garantiti", Toast.LENGTH_SHORT ).show();
                    finish();
                }
            }
        }

    }
