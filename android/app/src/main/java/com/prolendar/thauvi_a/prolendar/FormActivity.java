package com.prolendar.thauvi_a.prolendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thauvi_a on 11/13/17.
 *
 * ******************************
 * * BOOM BITCH GET OUT THE WAY *
 * ******************************
 *
 */

public class FormActivity extends Activity {

        Spinner spinner;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.form);

            //Récupération du Spinner déclaré dans le fichier main.xml de res/layout
            spinner = findViewById(R.id.spinner);
            //Création d'une liste d'élément à mettre dans le Spinner(pour l'exemple)
            List<String> exp = new ArrayList<>();
            exp.add("Coiffeur(se) ");
            exp.add("Infirmier(e)");

		/*Le Spinner a besoin d'un adapter pour sa presentation alors on lui passe le context(this) et
                un fichier de presentation par défaut( android.R.layout.simple_spinner_item)
		Avec la liste des elements (exemple) */
            ArrayAdapter adapter = new ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    exp
            );


               /* On definit une présentation du spinner quand il est déroulé         (android.R.layout.simple_spinner_dropdown_item) */
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Enfin on passe l'adapter au Spinner et c'est tout
            spinner.setAdapter(adapter);

        }
}
