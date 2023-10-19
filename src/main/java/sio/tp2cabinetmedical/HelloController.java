package sio.tp2cabinetmedical;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;
//import java.time.LocalDate;


public class HelloController implements Initializable {
    @FXML
    private Label lblTitre;
    @FXML
    private Label lblNom;
    @FXML
    private Label lblPathologie;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblHeure;
    @FXML
    private Label lblMinute;
    @FXML
    private Button btnValider;
    @FXML
    private ComboBox cboPathologies;
    @FXML
    private Spinner spHeure;
    @FXML
    private Spinner spMinute;
    @FXML
    private TextField txtNom;
    @FXML
    private DatePicker dte;
    @FXML
    private TreeView treeV;
    private TreeItem root;



    private TreeMap<String, TreeMap<String, RendezVous>> monPlanning;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        monPlanning = new TreeMap<>();

        root = new TreeItem("Mon planning");
        treeV.setRoot(root);

        cboPathologies.getItems().addAll("Angine", "Grippe", "Covid", "Gastro");
        cboPathologies.getSelectionModel().selectFirst();// si on met pas cette ligne la, a l'affichage il n'ya pas de pathologie par defaut. on peut faire pareil pr la date

        SpinnerValueFactory spinnerValueFactoryHeure = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 19, 8, 1);
        spHeure.setValueFactory(spinnerValueFactoryHeure);
        SpinnerValueFactory spinnerValueFactoryMinute = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15);
        spMinute.setValueFactory(spinnerValueFactoryMinute);

    }

    @FXML
    public void btnValider(Event event)
    {

        if (txtNom.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisi");
            alert.setContentText("veuillez saisir le nom du patient");
            alert.showAndWait();
        } else if (dte.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisi");
            alert.setContentText("veuillez saisir une date");
            alert.showAndWait();

        }
        else
        { //tout est ok !
            String nom = txtNom.getText();
            String patho = cboPathologies.getSelectionModel().getSelectedItem().toString();
            String dateSelectionne = dte.getValue().toString();


            //on veut QUE TOUTES les heures soit formaté de la mem facon 09 08 ou 00
            String heureSelectionne = spHeure.getValue().toString();//on recuper l'heure complete
            String minuteSelectionne = spMinute.getValue().toString(); //on recupere les minute
            if (heureSelectionne.length() == 1) //cad que 9 ou 8
            {
                heureSelectionne = "0" + heureSelectionne;
            }
            if (minuteSelectionne.length() == 1) {
                minuteSelectionne = minuteSelectionne + "0";
            }
            String heureComplte = heureSelectionne + ":" + minuteSelectionne;

        /*if (spinutes.getValue().toString().equals(0)){
         heureSelectionne=spHeure.getValue().toString()+":"+spinutes.getValue().toString()+"00";}*/


            //est ce que la date choisi est presente dans la treemap
            if (!monPlanning.containsKey(dateSelectionne))//si elle n'est pas contenue
            {
                //la dagte existe pas donc ya pas les horraire donc je doit cree ma treemap interne
                TreeMap<String, RendezVous> lesHeures = new TreeMap<>();
                RendezVous unRdv = new RendezVous(heureComplte, nom, patho);
                lesHeures.put(heureComplte, unRdv);
                monPlanning.put(dateSelectionne, lesHeures);
         /*
            interne.put(heureComplte,unRdv); //moi je lai initialisé en haut
            monPlaning.put(dateSelectionne,interne);*/

            } else if (monPlanning.get(dateSelectionne).containsKey(heureComplte)) //pour cette date la je recupere la valeur qui est aussi un treemap et donc je demande a voir si la cle heure existe deja ou non
            //ca veut dire que ùa date est dans la collection donc ya deja eu un rdv a cettte date la et c'est possible que j'ai deux rdv a la meme heure
            {
                //date existe => ya donc deja au moin 1 rdv
                //verifiez l'heure car on peut pas prendre de rdv a la meme heure
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("erreur d'horraire ");
                alert.setHeaderText("");
                alert.setContentText("veuillez saisir une autre horraire  car celle-ci est indisponible");
                alert.showAndWait();
            } else //date existe et pas lheyure selectionne
            { //la date existe maisn pas lheure  donc ma treemap existe deja donc pas besoin d'en cree une autre
                RendezVous unRdv = new RendezVous(heureComplte, nom, patho);
                monPlanning.get(dateSelectionne).put(heureComplte, unRdv);
            }
            //affichage dans le treeview
            root.getChildren().clear();//on efface les enfant de la racine on supprime le treeview et on le reparcour a chaque fois
            // TreeItem noeudHeure=null;
            for (String dateRdv : monPlanning.keySet())//leyset ca me prend toute les cle de la collection donc ca me prend toute mes dates
            {

                TreeItem noeudDate = new TreeItem(dateRdv);
                noeudDate.setExpanded(true);//ca deroule directement le noeud sans avoir a appuiiyer sur le triangle
                for (String heureRdv : monPlanning.get(dateRdv).keySet())//on parcours toute les heure de mes date
                {
                    TreeItem noeudHeure = new TreeItem(heureRdv);
                    noeudHeure.setExpanded(true);

                    TreeItem noeudRdv = new TreeItem<>(monPlanning.get(dateRdv).get(heureRdv).getNomPatient());
                    noeudHeure.getChildren().add(noeudRdv);
                    noeudRdv = new TreeItem<>(monPlanning.get(dateRdv).get(heureRdv).getNomPathologie());
                    noeudHeure.getChildren().add(noeudRdv);
                    noeudDate.getChildren().add(noeudHeure);
                }
                root.getChildren().add(noeudDate);
            }


        }


    }
}

/*

        if (txtNom.getText().isEmpty())
        {
            afficherErreur("Veuillez saisir le nom du patient.");
        }


    private void afficherErreur(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setContentText(message);
        alert.showAndWait();
    }*/
