package com.example.yournotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yournotes.MainActivity;
import android.content.SharedPreferences;
import android.content.Context;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Button buttonFiles;
    public Button logoutButton;

    public Button seguiMaterieButton;

    private SharedPreferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonFiles = (Button) view.findViewById(R.id.buttonAppunti);
        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        seguiMaterieButton = (Button) view.findViewById(R.id.buttonSeguiMaterie);

        seguiMaterieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SeguiMaterie.class);
                intent.putExtra("username", getArguments().getString("username"));
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        buttonFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YourFiles.class);
                startActivity(intent);
            }
        });

        // Ottieni un riferimento al contesto (Context) del fragment
        Context context = getContext();

        // Ottieni un riferimento alle SharedPreferences
        sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        String username = getArguments().getString("username");

        String savedPassword = sharedPreferences.getString(username, "");
        String[] datiSeparati = savedPassword.split("£");

        TextView emailTextView = view.findViewById(R.id.provaEmail);
        TextView usernameTextView = view.findViewById(R.id.nomeUtente);
        TextView corsoTextView = view.findViewById(R.id.corsoSeguito);

        emailTextView.setText(datiSeparati[1]);
        usernameTextView.setText(datiSeparati[3]);
        corsoTextView.setText(datiSeparati[2]);
    }

}