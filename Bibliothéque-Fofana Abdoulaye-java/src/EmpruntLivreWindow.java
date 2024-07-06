import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


// les J permettent de réaliser différentes actions 
public class EmpruntLivreWindow extends JFrame {
    private JComboBox<String> adherentsCombo;
    private JComboBox<String> livresCombo;
    private JLabel livreDetailsLabel;
    private JButton emprunterButton;
    private LibraryManagementPage libraryManagementPage;
    

    // il crée la fenètre dans une bbt
        public EmpruntLivreWindow(LibraryManagementPage libraryManagementPage) {
            super("Emprunter un Livre");
            this.libraryManagementPage = libraryManagementPage;
    
            JPanel panel = new JPanel();
            JButton backButton = new JButton("Retour à la Bibliothèque");
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    goBackToLibraryManagementPage();
                }
            });
            panel.add(backButton);
            add(panel);
    
            setLocationRelativeTo(libraryManagementPage);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pack();
            setVisible(true);
        }
    
        private void goBackToLibraryManagementPage() {
            libraryManagementPage.setVisible(true);
            dispose();
        }
    
// permet de créer le visuel et les éléments que l'on souhaite faire apparaitre
    public EmpruntLivreWindow() {
        super("Emprunter un livre");

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // JComboBox pour les listes deroulantes
        panel.add(new JLabel("Sélectionner un adhérent:"));
        adherentsCombo = new JComboBox<>();
        // Placer les éléments dans la liste deroulante (méthode)
        loadAdherentsCombo();
        panel.add(adherentsCombo);

        panel.add(new JLabel("Sélectionner un livre:"));
        livresCombo = new JComboBox<>();
        // Placer les éléments dans la liste deroulante (méthode)
        loadLivresCombo();
        livresCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateLivreDetails();
            }
        });
        panel.add(livresCombo);

        panel.add(new JLabel("Détails du livre:"));
        livreDetailsLabel = new JLabel();
        panel.add(livreDetailsLabel);

        emprunterButton = new JButton("Emprunter");
        emprunterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emprunterLivre();
            }
        });
        panel.add(emprunterButton);

        add(panel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
// détails de la méthode où l'on se connecte à la base de donnée
    private void loadAdherentsCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CONCAT(nom, ' ', prenom) AS nom_complet FROM adherent");

            while (rs.next()) {
                adherentsCombo.addItem(rs.getString("nom_complet"));
            }
//on ferme la connexion
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadLivresCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT titre FROM livre WHERE disponibilite > 0");
    
            while (rs.next()) {
                livresCombo.addItem(rs.getString("titre"));
            }
    
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
//Cette méthode updateLivreDetails() met à jour les détails du livre affichés dans une étiquette (livreDetailsLabel) en fonction du livre 
//sélectionné dans la liste déroulante des livres (livresCombo).
    private void updateLivreDetails() {
        String selectedLivre = (String) livresCombo.getSelectedItem();
        if (selectedLivre != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
                PreparedStatement stmt = con.prepareStatement("SELECT titre, prix, id_auteur FROM livre WHERE titre = ?");
                stmt.setString(1, selectedLivre);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String prix = rs.getString("prix");
                    int id_auteur = rs.getInt("id_auteur");

                    String auteur = getAuteurName(id_auteur);

                    livreDetailsLabel.setText(" Prix: " + prix + ", Auteur: " + auteur);
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getAuteurName(int id_auteur) {
        String auteur = "";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT nom, prenom FROM auteur WHERE id_auteur = ?");
            stmt.setInt(1, id_auteur);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                auteur = nom + " " + prenom;
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return auteur;
    }

    //Cette méthode emprunterLivre() gère le processus d'emprunt d'un livre sélectionné 
    //par un adhérent dans une bibliothèque.
    private void emprunterLivre() {
        String selectedAdherent = (String) adherentsCombo.getSelectedItem();
        String selectedLivre = (String) livresCombo.getSelectedItem();

        if (selectedAdherent == null || selectedLivre == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un adhérent et un livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");

            // Vérification de la disponibilité du livre
            PreparedStatement checkStmt = con.prepareStatement("SELECT disponibilite FROM livre WHERE titre = ?");
            checkStmt.setString(1, selectedLivre);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int disponibilite = rs.getInt("disponibilite");
                if (disponibilite > 0) {
                    // Mise à jour de la disponibilité
                    PreparedStatement updateStmt = con.prepareStatement("UPDATE livre SET disponibilite = ? WHERE titre = ?");
                    updateStmt.setInt(1, disponibilite - 1);
                    updateStmt.setString(2, selectedLivre);
                    updateStmt.executeUpdate();

                    // Calcul de la date de retour (4 semaines plus tard)
                    LocalDate dateEmprunt = LocalDate.now();
                    LocalDate dateRetour = dateEmprunt.plusWeeks(4);

                    // Insertion de l'emprunt dans la base de données
                    PreparedStatement insertStmt = con.prepareStatement("INSERT INTO emprunts (id_adherent, id_livre, date_emprunt, date_retour) VALUES (?, (SELECT id_livre FROM livre WHERE titre = ?), ?, ?)");
                    insertStmt.setInt(1, getAdherentId(selectedAdherent));
                    insertStmt.setString(2, selectedLivre);
                    insertStmt.setDate(3, Date.valueOf(dateEmprunt));
                    insertStmt.setDate(4, Date.valueOf(dateRetour));
                    insertStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Livre emprunté avec succès. Date de retour : " + dateRetour.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    JOptionPane.showMessageDialog(this, "Le livre sélectionné n'est pas disponible.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'emprunt du livre : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getAdherentId(String nomComplet) {
        int adherentId = -1;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librairie-fofana-abdoulaye", "root", "root");
            PreparedStatement stmt = con.prepareStatement("SELECT adhnum FROM adherent WHERE CONCAT(nom, ' ', prenom) = ?");
            stmt.setString(1, nomComplet);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                adherentId = rs.getInt("adhnum");
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return adherentId;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EmpruntLivreWindow();
            }
        });
    }
}
