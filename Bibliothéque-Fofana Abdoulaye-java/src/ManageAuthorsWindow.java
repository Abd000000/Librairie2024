import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageAuthorsWindow extends JFrame {
    private JComboBox<String> auteursCombo;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField dateNaissanceField;
    private JTextField descriptionField;

    // Variable pour stocker l'ID de l'auteur sélectionné
    private int selectedAuthorId;

    public ManageAuthorsWindow() {
        super("Gérer les Auteurs");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sélectionner ou ajouter un auteur"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        auteursCombo = new JComboBox<>();
        loadAuthorsCombo();
        auteursCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillFieldsWithSelectedAuthor();
            }
        });
        panel.add(auteursCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        nomField = new JTextField(20);
        panel.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Prénom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        prenomField = new JTextField(20);
        panel.add(prenomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Date de naissance:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        dateNaissanceField = new JTextField(20);
        panel.add(dateNaissanceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        descriptionField = new JTextField(20);
        panel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterAuteur();
            }
        });
        panel.add(ajouterButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifierAuteur();
            }
        });
        panel.add(modifierButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerAuteur();
            }
        });
        panel.add(supprimerButton, gbc);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void loadAuthorsCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CONCAT(nom, ' ', prenom) AS auteur, id_auteur FROM auteur");

            while (rs.next()) {
                auteursCombo.addItem(rs.getString("auteur"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fillFieldsWithSelectedAuthor() {
        String selectedAuthor = (String) auteursCombo.getSelectedItem();
        if (selectedAuthor != null) {
            String[] nomPrenom = selectedAuthor.split(" ");
            String nom = nomPrenom[0];
            String prenom = nomPrenom[1];

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
                PreparedStatement stmt = con.prepareStatement("SELECT id_auteur, date_naissance, description FROM auteur WHERE nom = ? AND prenom = ?");
                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    selectedAuthorId = rs.getInt("id_auteur"); // Stockage de l'ID de l'auteur sélectionné
                    nomField.setText(nom);
                    prenomField.setText(prenom);
                    dateNaissanceField.setText(rs.getString("date_naissance"));
                    descriptionField.setText(rs.getString("description"));
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void ajouterAuteur() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String dateNaissance = dateNaissanceField.getText();
        String description = descriptionField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");

            // Vérifier si l'auteur existe déjà
            if (auteurExists(nom, prenom)) {
                JOptionPane.showMessageDialog(this, "Cet auteur existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                con.close();
                return;
            }

            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO auteur (nom, prenom, date_naissance, description) VALUES (?, ?, ?, ?)");
            insertStmt.setString(1, nom);
            insertStmt.setString(2, prenom);
            insertStmt.setString(3, dateNaissance);
            insertStmt.setString(4, description);
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Auteur ajouté avec succès.");
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierAuteur() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String dateNaissance = dateNaissanceField.getText();
        String description = descriptionField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || dateNaissance.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir modifier cet auteur ?", "Confirmation de modification", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");

                PreparedStatement updateStmt = con.prepareStatement("UPDATE auteur SET nom = ?, prenom = ?, date_naissance = ?, description = ? WHERE id_auteur = ?");
                updateStmt.setString(1, nom);
                updateStmt.setString(2, prenom);
                updateStmt.setString(3, dateNaissance);
                updateStmt.setString(4, description);
                updateStmt.setInt(5, selectedAuthorId); // Utilisation de l'ID de l'auteur sélectionné
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Auteur mis à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun auteur trouvé avec ces informations.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void supprimerAuteur() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();

        if (nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet auteur ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");

                PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM auteur WHERE nom = ? AND prenom = ?");
                deleteStmt.setString(1, nom);
                deleteStmt.setString(2, prenom);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Auteur supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun auteur trouvé avec ces informations.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Méthode pour vérifier si un auteur existe déjà dans la base de données
    private boolean auteurExists(String nom, String prenom) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) AS count FROM auteur WHERE nom = ? AND prenom = ?");
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManageAuthorsWindow();
            }
        });
    }
}
