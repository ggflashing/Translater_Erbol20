package com.example.translater_erbol20.ui.home;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import com.example.translater_erbol20.R;
import com.example.translater_erbol20.databinding.FragmentTranslateBinding;
import com.example.translater_erbol20.models.DualModels;
import com.example.translater_erbol20.models.ModelLanguage;
import com.example.translater_erbol20.room.AppDatabase;
import com.example.translater_erbol20.room.LingvoDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TranslateFragment extends Fragment {

private FragmentTranslateBinding binding;

private ArrayList<ModelLanguage> languageArrayList;

public static final int REQUEST_PERMISSION_CODE = 1;

public static final String TAG = "MAIN TAG";

String languageTitle;

List<String> languageCodelist;

private String sourceLanguageCode = "ru";

private String sourceLanguageTitle = "Rus";

private String targetLanguageCode = "en";

private String targetLanguageTitle = "English";

private TranslatorOptions translatorOptions;

private Translator translator;

private ProgressDialog progressDialog;

private String sourceLanguageText;

AppDatabase appDatabase;

LingvoDao lingvoDao;

DualModels new_l_model;

NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentTranslateBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    get_Load_able_Languages();

    progressDialog = new ProgressDialog(requireActivity());
    progressDialog.setTitle("Please wait! Please wait...");
    progressDialog.setCanceledOnTouchOutside(false);

    binding.sourceLanguageChooseBtn.setOnClickListener(v1 -> {
        sourceLanguageChoose();
    });
    binding.targetLanguageChooseBtn.setOnClickListener(v2 -> {
        targetLanguageChoose();
    });
    binding.btnTranslate.setOnClickListener(v3 -> {
        checkWords();
    });

    binding.mircophone.setOnClickListener(v4 -> {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to convert into text");
        try {
            startActivityForResult(intent,REQUEST_PERMISSION_CODE);
        }catch (Exception ex) {
            ex.printStackTrace();
            showToast("ERROR RECOGNIZING THE VOICE"+ex.getMessage());
        }
    });
    return root;
    }

    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(resultCode == RESULT_OK && data !=null){

                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                binding.enterEditText.setText(result.get(0));

                sourceLanguageText = Objects.requireNonNull(
                        binding.enterEditText.getText()).toString().trim();
                makeTranslation();

            }
        }
    }




    private void sourceLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(requireActivity().getApplicationContext(),
                binding.sourceLanguageChooseBtn);
        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());

        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                sourceLanguageCode = languageArrayList.get(position).getLanguageCode();
                sourceLanguageTitle = languageArrayList.get(position).getLanguageTitle();
                binding.sourceLanguageChooseBtn.setText(sourceLanguageTitle);
                return false;
            }
        });
    }


    private void targetLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(requireActivity().getApplicationContext(),
                binding.targetLanguageChooseBtn);
        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());

        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                targetLanguageCode = languageArrayList.get(position).getLanguageCode();
                targetLanguageTitle = languageArrayList.get(position).getLanguageTitle();
                binding.targetLanguageChooseBtn.setText(targetLanguageTitle);
                return false;
            }
        });

    }


    private void checkWords() {
        sourceLanguageText = Objects.requireNonNull(binding.enterEditText.getText()).toString().trim();
        Log.e(TAG, "checkWords: sourceLanguageText:" + sourceLanguageText);
        if(sourceLanguageText.isEmpty()){
            showToast("Enter source text");

        }else {
            makeTranslation();
        }

    }

    private void makeTranslation() {
        progressDialog.setTitle("Processing: Language Vocabulary download...");
        progressDialog.show();

        translatorOptions = new TranslatorOptions
                .Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(targetLanguageCode)
                .build();
        translator = Translation.getClient(translatorOptions);

        DownloadConditions downloadConditions;
        downloadConditions = new DownloadConditions
                .Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showToast("Vocabulary ready, start translation...");
                        progressDialog.setMessage("Translating...");
                        translator.translate(sourceLanguageText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String result_ml_kit) {
                                        progressDialog.dismiss();
                                        binding.resultText.setText(result_ml_kit);

                                        new_l_model = new DualModels(binding.enterEditText.getText().toString(), result_ml_kit);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Log.e(TAG, "FALURE TRANSLATING!!!!!:"+ e.toString());
                                        showToast("Failed to translate: impossible"+e.getMessage().toString());
                                    }
                                });
                    }
                });
    }


    private void get_Load_able_Languages() {

        languageArrayList = new ArrayList<>();
        languageCodelist = TranslateLanguage.getAllLanguages();

        for (String languageCode : languageCodelist) {

            languageTitle = new Locale(languageCode).getDisplayLanguage();
            languageCode = new Locale(languageCode).getLanguage();

            Log.d(TAG, "loadAvailableLanguages: languagecode:"+languageCode);
            Log.d(TAG, "loadAvailableLanguages: languageTitle:"+ languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);

            languageArrayList.add(modelLanguage);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveToMemory.setOnClickListener(v5 -> {
            appDatabase = Room.databaseBuilder(
                    binding.getRoot().getContext(),
                    AppDatabase.class, "database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
            lingvoDao = appDatabase.lingvoDao();

            if (new_l_model !=null) {
                lingvoDao.insert(new_l_model);
            } else {
                showToast("Вы не ввели слово для перевода");
            }
            navController = Navigation.findNavController(requireActivity(),
                    R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_navigation_home_to_navigation_dashboard);
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
    }



@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}