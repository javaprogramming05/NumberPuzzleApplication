
/**
 * This program creates a Menu for configuring Tile Puzzle, play and
 * save the game.And load the previously saved game.
 * Uses the TilePuzzle class and ExampleFileFilter class.
 *
 * author: Priyanka Narasimha Murthy
 */
/**

 */
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PuzzleMenu extends JFrame implements ActionListener {

    static PuzzleMenu frame = new PuzzleMenu("PuzzleMenu by Priyanka");
    //declare GUI Components and other variables
    private TilePuzzle puzzle;
    private TilePuzzle currentPuzzle;
    private JButton txtBtnColor;
    private JButton tileBtnColor;
    private JButton createButton;
    private JButton saveButton;
    private JButton loadButton;
    private JComboBox numOfRowsComboBox;
    private JComboBox numOfColumnsComboBox;
    private JComboBox fontSizeComboBox;
    private JComboBox tileSizeComboBox;
    private JComboBox fontFamilyComboBox;
    private JTextField nameTxt;
    private int row = 3;
    private int col = 3;
    private int tileSize = 50;
    private int fontSize = 12;
    private String fontFamily = "TimesRoman";
    private Font myFont;
    private String fonts[];
    private Color textColor = Color.BLUE;
    private Color tileColor = Color.WHITE;
    private String name = "Priyanka";

    //constructor
    public PuzzleMenu(String name) {
        super(name);
        fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    //main method
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

    private static void createAndShowGUI() {
        //create and set up the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //set up the content pane
        frame.addComponentsToPane();

        //display the window
        frame.pack();
        frame.setVisible(true);

    }

    private void addComponentsToPane() {

        final JTabbedPane tabbedPane = new JTabbedPane();
        //images for tabs
        ImageIcon newIcon = new ImageIcon(
                this.getClass().getResource("/images/new.png"));
        ImageIcon loadIcon = new ImageIcon(
                this.getClass().getResource("/images/load.png"));
        ImageIcon helpIcon = new ImageIcon(
                this.getClass().getResource("/images/help.png"));
        ImageIcon aboutIcon = new ImageIcon(
                this.getClass().getResource("/images/about.png"));

        //creating tabs- new, load,help,about
        tabbedPane.addTab("New", newIcon, newPanel());
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        tabbedPane.addTab("Load", loadIcon, new JPanel());
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        //when load-tab is pressed, open the dioalog and goback to new-tab.
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index == 1) {

                    tabbedPane.setSelectedIndex(0);
                    callLoadAction();

                }

            }
        });

        tabbedPane.addTab("Help", helpIcon, helpPanel());
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        tabbedPane.addTab("About", aboutIcon, aboutPanel());
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    //to load only pzl files
    public void callLoadAction() {
        final JFileChooser jFileChooser = new JFileChooser(".");
        ExampleFileFilter exampleFileFilter = new ExampleFileFilter();
        exampleFileFilter.addExtension("pzl");
        exampleFileFilter.setDescription("puzzles only");
        jFileChooser.setFileFilter(exampleFileFilter);

        jFileChooser.setFileSelectionMode(0);
        if (jFileChooser.showOpenDialog(null) != 0) {
        }
        String string = jFileChooser.getSelectedFile().getName();

        try {
            FileInputStream fileInputStream = new FileInputStream(string);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            puzzle = (TilePuzzle) objectInputStream.readObject();
            objectInputStream.close();
            puzzle.setVisible(true);
            puzzle.addWindowFocusListener(new WindowFocusListener() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    PuzzleMenu.this.currentPuzzle = (TilePuzzle) e.getSource();
                }

                @Override
                public void windowLostFocus(WindowEvent e) {
//                    throw new UnsupportedOperationException("Not supported yet."); 
                }
            });

        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }catch (NullPointerException ex) {
            //System.out.println(ex);
        }
    }

    //new-tab panel
    public JComponent newPanel() {

        //Setup the panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.WEST;
        GridBagConstraints right = new GridBagConstraints();
        right.weightx = 2.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.gridwidth = GridBagConstraints.REMAINDER;
        GridBagConstraints center = new GridBagConstraints();
        center.gridwidth = GridBagConstraints.EAST;

        //setup nameLabel and nameTextField
        JLabel nameLabel = new JLabel("Name: ");
        nameTxt = new JTextField(20);
        nameTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                name = ((JTextField) evt.getSource()).getText() + String.valueOf(evt.getKeyChar());
            }
        });

        //set number of rows
        JLabel rowLabel = new JLabel("Row: ");
        Vector<Integer> integerVector = new Vector<Integer>();
        for (int i = 1; i <= 10; ++i) {
            integerVector.add(i);
        }
        numOfRowsComboBox = new JComboBox(integerVector);
        numOfRowsComboBox.setSelectedIndex(2);
        numOfRowsComboBox.addActionListener(this);

        //set number of columns
        JLabel columnLabel = new JLabel("Column: ");
        integerVector = new Vector<Integer>();
        for (int i = 1; i <= 10; ++i) {
            integerVector.add(i);
        }
        numOfColumnsComboBox = new JComboBox(integerVector);
        numOfColumnsComboBox.setSelectedIndex(2);
        numOfColumnsComboBox.addActionListener(this);

        JLabel txtColorLabel = new JLabel("Text Color: ");
        txtBtnColor = new JButton("Change Color");
        txtBtnColor.setBackground(textColor);
        txtBtnColor.setOpaque(true);
        txtBtnColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                Color color = JColorChooser.showDialog(frame, "Choose a color", textColor);

                if (color != null) { // new color selected
                    textColor = color;
                }
                txtBtnColor.setBackground(textColor);
            }
        });

        JLabel tileColorLabel = new JLabel("Tile Color: ");
        tileBtnColor = new JButton("Change Color");
        tileBtnColor.setBackground(tileColor);
        tileBtnColor.setOpaque(true);

        tileBtnColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                Color color = JColorChooser.showDialog(frame, "Choose a color", tileColor);

                if (color != null) { // new color selected
                    tileColor = color;
                }
                tileBtnColor.setBackground(tileColor);
            }
        });

        //set text size
        JLabel fontSizeLabel = new JLabel("Font size: ");
        integerVector = new Vector<Integer>();
        for (int i = 14; i <= 24; i = i + 2) {
            integerVector.add(i);
        }
        fontSizeComboBox = new JComboBox(integerVector);
        fontSizeComboBox.addActionListener(this);

        //set tile size
        JLabel tileSizeLabel = new JLabel("Tile size: ");
        integerVector = new Vector<Integer>();
        for (int i = 50; i <= 100; i = i + 10) {
            integerVector.add(i);
        }
        tileSizeComboBox = new JComboBox(integerVector);
        tileSizeComboBox.addActionListener(this);

        //set font family
        JLabel fontFamilyLabel = new JLabel("Font Family: ");
        Vector<String> stringVector = new Vector<String>();
        for (int i = 0; i < fonts.length; i++) {
            stringVector.add(fonts[i]);
        }
        fontFamilyComboBox = new JComboBox(stringVector);
        fontFamilyComboBox.addActionListener(this);

        //create button
        createButton = new JButton("Create");
        createButton.addActionListener(this);

        //save button
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);

        //add components
        panel.add(nameLabel, left);
        panel.add(nameTxt, right);
        panel.add(rowLabel, left);
        panel.add(numOfRowsComboBox, right);
        panel.add(columnLabel, left);
        panel.add(numOfColumnsComboBox, right);
        panel.add(txtColorLabel, left);
        panel.add(txtBtnColor, right);
        panel.add(tileColorLabel, left);
        panel.add(tileBtnColor, right);
        panel.add(fontSizeLabel, left);
        panel.add(fontSizeComboBox, right);
        panel.add(tileSizeLabel, left);
        panel.add(tileSizeComboBox, right);
        panel.add(fontFamilyLabel, left);
        panel.add(fontFamilyComboBox, right);
        panel.add(createButton, right);
        panel.add(saveButton, right);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return panel;

    }

    //help-tab content
    static String helpContent = "Guide:\n\n"
            + "Create        : To create a puzzle\n"
            + "Load          : To load an existing puzzle\n"
            + "Save          : To save the current puzzle\n\n"
            + "This is a basic 3x3 Puzzle and can be configured\n"
            + "further for required size.";

    // help-tab panel
    public JComponent helpPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        JTextArea jt = new JTextArea(helpContent, 6, 10);
        jt.setEditable(false);
        jt.setBackground(Color.LIGHT_GRAY);

        panel.add(jt);
        return panel;
    }

    //about-tab content
    static String aboutContent = "    Puzzle Menu V1.0.0 \n\n"
            + "Enjoy the Tile Puzzle Game";

    //about-tab panel        
    private JComponent aboutPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        JTextArea jt = new JTextArea(aboutContent, 6, 10);
        jt.setEditable(false);
        jt.setBackground(Color.LIGHT_GRAY);

        panel.add(jt);
        return panel;

    }

    //listener method
    @Override
    public void actionPerformed(ActionEvent e) {

        puzzle = new TilePuzzle(this.name);

        if (e.getSource() == createButton) {

            if (nameTxt.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Give file name");
            } else {

                myFont = new Font(this.fontFamily, Font.PLAIN, this.fontSize);
                puzzle.setMyFont(myFont);
                puzzle.setRowsCols(this.row, this.col);
                puzzle.setTileColor(this.tileColor);
                puzzle.setHeight(this.tileSize * this.row + 100);
                puzzle.setWidth(this.tileSize * this.col + 100);
                puzzle.setTextColor(this.textColor);
                puzzle.addWindowFocusListener(new WindowFocusListener() {
                    @Override
                    public void windowGainedFocus(WindowEvent e) {
                        PuzzleMenu.this.currentPuzzle = (TilePuzzle) e.getSource();
                    }

                    @Override
                    public void windowLostFocus(WindowEvent e) {
                    }
                });

                puzzle.layIt();
            }
        } else if (e.getSource() == numOfRowsComboBox) {
            this.row = (int) numOfRowsComboBox.getSelectedItem();
        } else if (e.getSource() == numOfColumnsComboBox) {
            this.col = (int) numOfColumnsComboBox.getSelectedItem();
        } else if (e.getSource() == fontSizeComboBox) {
            this.fontSize = (int) fontSizeComboBox.getSelectedItem();
        } else if (e.getSource() == tileSizeComboBox) {
            this.tileSize = (int) tileSizeComboBox.getSelectedItem();
        } else if (e.getSource() == fontFamilyComboBox) {
            this.fontFamily = (String) fontFamilyComboBox.getSelectedItem();
        } else if (e.getSource() == nameTxt) {
            this.name = (String) nameTxt.getText();
        } else if (e.getSource() == saveButton) {

            try {
                String string = this.currentPuzzle.getTitle();
                FileOutputStream fileOutputStream = new FileOutputStream(string + ".pzl");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(this.currentPuzzle);
                objectOutputStream.close();
                this.currentPuzzle.dispose();
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "No file to save");
            }
        } else if (e.getSource() == loadButton) {

        } else {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates
        }
    }

}
