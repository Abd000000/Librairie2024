 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// creation de la class ManageBooksWindow pour gerer les livres
public class ManageBooksWindow extends JFrame {
    private JComboBox<String> livresCombo;
    private JTextField titreField;
    private JTextField prixField;
    private JComboBox<String> auteursCombo;
    private JTextField disponibiliteField;
    private String selectedid_livre;

    // On cree la fenetre et son contenu front que l'utilisateur verra
    public ManageBooksWindow() {
        super("Gérer les Livres");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sélectionner ou ajouter un livre"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        livresCombo = new JComboBox<>();
        loadBooksCombo();
        livresCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillFieldsWithSelectedBook();
            }
        });
        panel.add(livresCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Titre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        titreField = new JTextField(20);
        panel.add(titreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prix:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        prixField = new JTextField(20);
        panel.add(prixField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Auteur:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        auteursCombo = new JComboBox<>();
        loadAuthorsCombo();
        panel.add(auteursCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Disponibilité:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        disponibiliteField = new JTextField(20);
        panel.add(disponibiliteField, gbc);

        // Creation du bouton ajouter un livre
        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterLivre();
            }
        });
        panel.add(ajouterButton, gbc);

        // Creation du bouton modifier un livre
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierLivre();
            }
        });
        panel.add(modifierButton, gbc);

        // Creation du bouton supprimer un livre
        gbc.gridx = 2;
        gbc.gridy = 5;
        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerLivre();
            }
        });
        panel.add(supprimerButton, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // Creation de la methode loadAuthorsCombo qui permet de reccuperer les auteurs 
    private void loadAuthorsCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root"); //Connexion a la bdd
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CONCAT(nom, ' ', prenom) AS auteur FROM auteur");

            while (rs.next()) {
                auteursCombo.addItem(rs.getString("auteur"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // Creation de la methode loadBooksCombo qui permet de reccuperer le titre des livres 
    private void loadBooksCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root"); //Connexion a la bdd
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT titre FROM livre");

            while (rs.next()) {
                livresCombo.addItem(rs.getString("titre"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Creation de la methode fillFieldsWithSelectedBook qui permet de reccuperer tout les info sur un livre selectionné (titre, prix auteur, disponibilité)
    private void fillFieldsWithSelectedBook() {
        String selectedBook = (String) livresCombo.getSelectedItem();
        if (selectedBook != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");//Connexion a la bdd
                PreparedStatement stmt = con.prepareStatement("SELECT titre, prix, id_auteur, disponibilite, id_livre FROM livre WHERE titre = ?");
                stmt.setString(1, selectedBook);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    titreField.setText(rs.getString("titre"));
                    prixField.setText(rs.getString("prix"));
                    int id_auteur = rs.getInt("id_auteur");
                    auteursCombo.setSelectedItem(getAuteurName(id_auteur));
                    disponibiliteField.setText(String.valueOf(rs.getInt("disponibilite")));
                    selectedid_livre = rs.getString("id_livre"); // Charger l'id_livre du livre sélectionné
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Methode permettant d'afficher les auteurs 
    private String getAuteurName(int id_auteur) {
        String auteur = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root"); //Connexion a la bdd
            PreparedStatement stmt = con.prepareStatement("SELECT CONCAT(nom, ' ', prenom) AS auteur FROM auteur WHERE id_auteur = ?");
            stmt.setInt(1, id_auteur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                auteur = rs.getString("auteur");
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return auteur;
    }

    // Methode permettant d'ajouter un livre
    private void ajouterLivre() {
        String titre = titreField.getText();
        String prix = prixField.getText();
        String auteur = (String) auteursCombo.getSelectedItem(); // Récupérer l'auteur sélectionné
        int disponibilite = Integer.parseInt(disponibiliteField.getText());

        if (titre.isEmpty() || prix.isEmpty() || auteur.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkDuplicateLivre(titre)) {
            JOptionPane.showMessageDialog(this, "Ce livre existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root"); //Connexion a la bdd

            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO livre (titre, prix, id_auteur, disponibilite) VALUES (?, ?, (SELECT id_auteur FROM auteur WHERE CONCAT(nom, ' ', prenom) = ?), ?)");
            insertStmt.setString(1, titre);
            insertStmt.setString(2, prix);
            insertStmt.setString(3, auteur);
            insertStmt.setInt(4, disponibilite);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Livre ajouté avec succès.");
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // methode permttant de veirfier si un le livre a ajouté ou a lorsqu'il est modifié n'existe pas deja 
    private boolean checkDuplicateLivre(String titre) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");

            PreparedStatement stmt = con.prepareStatement("SELECT titre FROM livre WHERE titre = ?");
            stmt.setString(1, titre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // methode permettant de modfier un livre
    private void modifierLivre() {
        String titre = titreField.getText();
        String prix = prixField.getText();
        String auteur = (String) auteursCombo.getSelectedItem();
        int disponibilite = Integer.parseInt(disponibiliteField.getText());

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");

            PreparedStatement updateStmt = con.prepareStatement("UPDATE livre SET titre = ?, prix = ?, id_auteur = (SELECT id_auteur FROM auteur WHERE CONCAT(nom, ' ', prenom) = ?), disponibilite = ? WHERE id_livre = ?");
            updateStmt.setString(1, titre);
            updateStmt.setString(2, prix);
            updateStmt.setString(3, auteur);
            updateStmt.setInt(4, disponibilite);
            updateStmt.setString(5, selectedid_livre); // Utiliser l'id_livre pour la mise à jour
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Livre mis à jour avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Aucun livre trouvé avec ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // methode permettant de modfier un livre sachant que si on supprime un livre tout les emprunt au non de ce livre sera automatiquement supprimé aussi
    private void supprimerLivre() {
        String titre = titreField.getText();

        if (titre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir le champ titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce livre ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");

                PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM livre WHERE titre = ?");
                deleteStmt.setString(1, titre);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Livre supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun livre trouvé avec ce titre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // execution de la class ManageBooksWindow
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManageBooksWindow();
            }
        });
    }
}
