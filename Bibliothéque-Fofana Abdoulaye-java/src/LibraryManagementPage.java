import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibraryManagementPage extends JFrame {
    public LibraryManagementPage() {
        super("Gérer la Bibliothèque");

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton gererAdherentsButton = new JButton("Gérer les Adhérents");
        gererAdherentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openManageAdherentWindow();
            }
        });
        panel.add(gererAdherentsButton);

        JButton gererLivresButton = new JButton("Gérer les Livres");
        gererLivresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openManageBooksWindow();
            }
        });
        panel.add(gererLivresButton);

        JButton gererAuteursButton = new JButton("Gérer les Auteurs");
        gererAuteursButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openManageAuthorsWindow();
            }
        });
        panel.add(gererAuteursButton);

        JButton emprunterLivreButton = new JButton("Emprunter un Livre");
        emprunterLivreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEmpruntLivreWindow();
            }
        });
        panel.add(emprunterLivreButton);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void openManageAdherentWindow() {
        new ManageAdherentWindow();
    }

    private void openManageBooksWindow() {
        new ManageBooksWindow();
    }

    private void openManageAuthorsWindow() {
        new ManageAuthorsWindow();
    }

    private void openEmpruntLivreWindow() {
        new EmpruntLivreWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryManagementPage();
            }
        });
    }
}
