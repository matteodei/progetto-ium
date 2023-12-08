package com.example.yournotes;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AggiungiFragment extends Fragment {

    private DatabaseHelper dbHelper;

    public AggiungiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_aggiungi, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        Button buttonConferma = rootView.findViewById(R.id.buttonConferma);
        Button buttonElimina = rootView.findViewById(R.id.buttonElimina);

        buttonConferma.setOnClickListener(v -> confermaInserimento());

        buttonElimina.setOnClickListener(v -> eliminaDatabase());

        return rootView;
    }

    private void confermaInserimento() {

        // Recupera i dati dai campi di input EditText
        EditText editTextName = requireView().findViewById(R.id.editTextName);
        EditText editTextCdL = requireView().findViewById(R.id.editTextCdL);
        EditText editTextYear = requireView().findViewById(R.id.editTextYear);
        EditText editTextSemester = requireView().findViewById(R.id.editTextSemester);
        EditText editTextCourseTopics = requireView().findViewById(R.id.editTextCourseTopics);

        String nome = editTextName.getText().toString();
        String corsoDiLaurea = editTextCdL.getText().toString();
        String anno = editTextYear.getText().toString();
        String semestre = editTextSemester.getText().toString();
        String argomenti = editTextCourseTopics.getText().toString();

        if (!nome.isEmpty() && !corsoDiLaurea.isEmpty() && !anno.isEmpty() && !semestre.isEmpty() && !argomenti.isEmpty()) {

            ContentValues values = new ContentValues();
            values.put(CoursesContract.COLUMN_NAME, nome);
            values.put(CoursesContract.COLUMN_CDL, corsoDiLaurea);
            values.put(CoursesContract.COLUMN_YEAR, anno);
            values.put(CoursesContract.COLUMN_SEMESTER, semestre);
            values.put(CoursesContract.COLUMN_TOPICS, argomenti);
            values.put(CoursesContract.COLUMN_FOLLOW, "1");

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(CoursesContract.TABLE_NAME, null, values);

            db.close();

        } else {
            Toast.makeText(requireContext(), "Riempi tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void eliminaDatabase() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(CoursesContract.TABLE_NAME, null, null);

        db.close();

    }
}