package Proiect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Frame implements ActionListener, MouseListener {
    DefaultListModel model;
    JMenuBar meniu = new JMenuBar();
    JMenu menu;
    RedBlackTree arbore;
    JMenuItem adauga;
    JFileChooser file_chooser = new JFileChooser();
    List<File> fileList = new ArrayList<>();
    JList lista;
    JButton button_sterge;
    JFrame frame = new JFrame("MP3 manager");
    JScrollPane scrollPane;
    String[] nume_mel = new String[100000];
    Integer[] number = new Integer[100000];
    JLabel messageLabel = new JLabel();
    int k = 1;
    int pozitie;
    int nr_elemente = 0;

    public void adauga_elem() {

        SwingUtilities.updateComponentTreeUI(frame);
        int r = file_chooser.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            File selectedFile = file_chooser.getSelectedFile();
            String nume = selectedFile.getName();
            if (nume.endsWith("mp3") || nume.endsWith("mp4")) {
               // fileList.add(selectedFile);
                button_sterge.setVisible(true);
                arbore.insert(nr_elemente, selectedFile, nume);
                scrollPane.setVisible(true);
                lista.setVisible(true);
                nume_mel[k] = nume;
                number[k] = nr_elemente;
                k++;
                nr_elemente++;
                model.addElement(nume);
                //  System.out.println(nume);
            } else {
                messageLabel.setForeground(Color.BLACK);
                JOptionPane.showMessageDialog(messageLabel, "Formatul fisierului selectat nu este valid!");
            }
        }
        SwingUtilities.updateComponentTreeUI(frame);
        scrollPane.setBounds(10, 50, 465, 300);

        scrollPane.addMouseListener(this);
        lista.addMouseListener(this);
        frame.add(scrollPane);
        SwingUtilities.updateComponentTreeUI(frame);

    }

    public void deschide_fisier(Node nod) {
        try {
            File file = new File(String.valueOf(nod.locatie));
            if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
            {
                JOptionPane.showMessageDialog(messageLabel, "Elementul nu este suportat!");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (file.exists())         //checks file exists or not
                desktop.open(file);              //opens the specified file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Frame() {
        menu = new JMenu("Muzica");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "Creare/stergere de alimente");
        meniu.add(menu);
        adauga = new JMenuItem("Adauga melodie",
                KeyEvent.VK_T);
        adauga.getAccessibleContext().setAccessibleDescription(
                "Adauga melodie");
        adauga.addActionListener(this);
        menu.add(adauga);

        frame.setJMenuBar(meniu);
        button_sterge = new JButton("Sterge melodie");
        button_sterge.setBounds(120, 375, 200, 25);
        button_sterge.setVisible(false);
        button_sterge.addActionListener(this);
        frame.add(button_sterge);
        model = new DefaultListModel();
        lista = new JList(model);
        scrollPane = new JScrollPane(lista);
        frame.add(meniu);
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        arbore = new RedBlackTree();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "MP3 & MP4", "mp3", "mp4");
        file_chooser.setFileFilter(filter);
    }

    private void sterge_elem() {
        arbore.inorder();
        frame.setSize(600, 500);
        SwingUtilities.updateComponentTreeUI(frame);
        try {
            pozitie=cautare((String) lista.getModel().getElementAt(lista.getSelectedIndex()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node new_arbore = arbore.searchTree(pozitie);
        arbore.deleteNode(new_arbore.data);
        model.remove(lista.getSelectedIndex());
        SwingUtilities.updateComponentTreeUI(frame);
        if (model.size() == 0) {
            scrollPane.setVisible(false);
            model.removeAllElements();
            lista.removeAll();
            button_sterge.setVisible(false);
        } else {
            arbore.inorder();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == adauga) {
            adauga_elem();
        }
        if (e.getSource() == button_sterge) {
            if (!lista.isSelectionEmpty()) {

                sterge_elem();
            } else
                JOptionPane.showMessageDialog(messageLabel, "Niciun element selectat!");

        }
    }

    public int cautare(String nume) {
        for (int i = k-1; i >0 ; i--) {
            if (nume_mel[i].equals(nume))
                return number[i];
        }
        return 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            pozitie = lista.getSelectedIndex();
            int nr=cautare((String) lista.getModel().getElementAt(pozitie));
            System.out.println("NR:" + nr + "Vector" + cautare((String) lista.getModel().getElementAt(pozitie)));
            Node new_arbore = arbore.searchTree(nr);
            if (new_arbore.nume_melodie.equals(lista.getModel().getElementAt(pozitie))) {
                deschide_fisier(new_arbore);
            } else {
                JOptionPane.showMessageDialog(messageLabel, "Eroare!");
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
