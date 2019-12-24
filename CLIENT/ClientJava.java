import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
public class ClientJava implements ActionListener {

    private static Socket sock;
    private static int PORT = 10337;
    static BufferedReader keyRead;
    static PrintWriter pwrite;
    static InputStream istream;
    static BufferedReader receiveRead;
    OutputStream ostream;
    static String logIn;
    static String receiveMessage, sendMessage;
    static String gamesArray[] = {"Game 1", "Game 2", "Game 3"};

    // GUI elements
    JFrame frame;
    JSplitPane splitPane;
    static JPanel mainPanel;
    static JPanel infoPanel;
    static JPanel commandsPanel;
    static JPanel centralPanel;
    static JPanel resultPanel;
    static JButton rollDice;
    static JButton yes;
    static JButton no;
    static JButton ok;
    static JButton startGame;
    JMenu options;
    JMenuItem quit;
    JMenuItem explanation;
    JMenuBar menuBar;
    static JComboBox comboBox;
    static JLabel askToRoll;
    static JLabel chooseGame;
    static JLabel d1; // lables for dices
    static JLabel d2;
    static JLabel d3;
    static JLabel rs; // lable for roundScore
    static JLabel gs1; //l ables for gameScore1 and gameScore2
    static JLabel gs2;
    static JTextArea textArea;
    static JTextField textField;
    static JTextField playersInGame;
    static JTextField dice1;
    static JTextField dice2;
    static JTextField dice3;
    static JTextField roundScore;
    static JTextField gameScore1;
    static JTextField gameScore2;
    static JLabel loginLable;
    static JLabel winLable;
    JLabel diceLabel2;
    JScrollPane scrollPane;

    public ClientJava() throws IOException {
        //create connection and objects for reading and sending
        sock = new Socket("127.0.0.1", PORT);
        keyRead = new BufferedReader(new InputStreamReader(System.in));
        ostream = sock.getOutputStream();
        pwrite = new PrintWriter(ostream, true);
        istream = sock.getInputStream();
        receiveRead = new BufferedReader(new InputStreamReader(istream), 256);

        // -----------------create GUI components--------------------------------------
        frame = new JFrame();
        frame.setTitle("Dice Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        //splitPane = new JSplitPane();

        //panels
        mainPanel = new JPanel(new BorderLayout());
        infoPanel = new JPanel(new BorderLayout());
        centralPanel = new JPanel();
        commandsPanel = new JPanel();


        //resultPanel = new JPanel();

        //layout for rcentral pannel
        centralPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;


        //buttons
        rollDice = new JButton("Roll Dice");
        yes = new JButton("Yes");
        no = new JButton("No");
        ok = new JButton("Ok");
        startGame = new JButton("Start");
        //menu and menu items
        options = new JMenu("Options");
        menuBar = new JMenuBar();
        quit = new JMenuItem("Quit");
        explanation = new JMenuItem("How to play?");
        //lables
        askToRoll = new JLabel("Do you want to roll again?");
        chooseGame= new JLabel("Choose a game:");
        d1 = new JLabel("Dice 1:");
        d2 = new JLabel("Dice 2:");
        d3 = new JLabel("Dice 3:");
        rs = new JLabel("Round Score:");
        gs1 = new JLabel("Game Score Player 1");
        gs2 = new JLabel("Game Score Player 2");
        loginLable = new JLabel();
        loginLable.setVisible(false);
        winLable = new JLabel();
        winLable.setVisible(false);
        //text area
        textArea = new JTextArea(10,25);
        textField = new JTextField(10);
        textField.setText("");
        playersInGame =  new JTextField(5);
        dice1 = new JTextField(5);
        dice2 = new JTextField(5);
        dice3 = new JTextField(5);
        roundScore = new JTextField(5);
        gameScore1 = new JTextField(5);
        gameScore2 = new JTextField(5);
        roundScore.setText("0");
        gameScore1.setText("0");
        gameScore2.setText("0");
        scrollPane = new JScrollPane( textArea );
        //combo box
        comboBox = new JComboBox(gamesArray);


        // Add and set GUI components
        mainPanel.setBackground(Color.WHITE);
        commandsPanel.setBackground(Color.GRAY);
        infoPanel.setBackground(Color.LIGHT_GRAY);
        centralPanel.setBackground(Color.BLUE);
        //resultPanel.setBackground(Color.WHITE);

        options.add(quit);
        options.add(explanation);

        menuBar.add(options, BorderLayout.EAST);

        infoPanel.add(loginLable, BorderLayout.CENTER);
        infoPanel.add(menuBar, BorderLayout.NORTH);
        infoPanel.add(winLable, BorderLayout.SOUTH);
        //infoPanel.add(writeLogin);
        //infoPanel.add(textField);
        infoPanel.setPreferredSize(new Dimension(600, 100));

        commandsPanel.add(rollDice);
        askToRoll.setVisible(false);
        yes.setVisible(false);
        no.setVisible(false);

        commandsPanel.add(askToRoll);
        commandsPanel.add(yes);
        commandsPanel.add(no);
        //commandsPanel.setPreferredSize(new Dimension(400,100));


        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 6;
        c.weightx = 0.5;
        c.insets = new Insets(0,5,0,5);
        centralPanel.add(textArea,c);
        //centralPanel.add(textField);
        //centralPanel.add(ok);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(10,5,0,5);
        centralPanel.add(chooseGame, c);
        //chooseGame.setLabelFor(comboBox);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 2;
        centralPanel.add(comboBox,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 2;
        centralPanel.add(startGame , c);

        //centralPanel.add(resultPanel, BorderLayout.SOUTH);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(10,5,0,5);
        centralPanel.add(d1, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        centralPanel.add(d2, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 2;
        c.gridwidth = 1;
        centralPanel.add(d3, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        centralPanel.add(dice1, c);
        //centralPanel.add(d2);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 1;
        centralPanel.add(dice2, c);
        //centralPanel.add(d3);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 2;
        c.gridwidth = 1;
        centralPanel.add(dice3, c);
        d1.setVisible(false);
        d2.setVisible(false);
        d3.setVisible(false);
        dice1.setVisible(false);
        dice2.setVisible(false);
        dice3.setVisible(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 2;
        centralPanel.add(rs, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 1;
        centralPanel.add(roundScore, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 2;
        centralPanel.add(gs1, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 5;
        c.gridwidth = 1;
        centralPanel.add(gameScore1, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 2;
        centralPanel.add(gs2, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 6;
        c.gridwidth = 1;
        centralPanel.add(gameScore2, c);
        rs.setVisible(false);
        gs1.setVisible(false);
        gs2.setVisible(false);
        roundScore.setVisible(false);
        gameScore1.setVisible(false);
        gameScore2.setVisible(false);

        //centralPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        mainPanel.setPreferredSize(new Dimension(600, 600));
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(commandsPanel, BorderLayout.SOUTH);
        mainPanel.add(centralPanel, BorderLayout.CENTER);


        quit.addActionListener(this);
        explanation.addActionListener(this);
        rollDice.addActionListener(this);
        yes.addActionListener(this);
        no.addActionListener(this);
        ok.addActionListener(this);
        startGame.addActionListener(this);

        frame.getContentPane().add(mainPanel);

        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quit)
            System.exit(0);

        if (e.getSource() == explanation) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Roll 6 dices.\n" +
                            "If you get a positive score you can roll again or you can save your points and pass the turn\n" +
                            "You can roll as many times as you want and gain points\n" +
                            "If you get a at least one 1 you loose all the points you gained in this round and your turn is over\n" +
                            "If you don't want to roll again the second player will roll\n" +
                            "First player who reaches the goal score wins! "
                    , "Rules", JOptionPane.INFORMATION_MESSAGE);

        }

        if (e.getSource() == rollDice) {
            dice1.setText(" ");
            dice2.setText(" ");
            dice3.setText(" ");
            roundScore.setText(" ");
            askToRoll.setVisible(false);
            yes.setVisible(false);
            no.setVisible(false);
            //sendMess("roll");
            pwrite.println( "//:"+ logIn + ":" + "roll");
            rollDice.setEnabled(false);
            commandsPanel.updateUI();
        }

        if (e.getSource() == yes) {
            //sendMess("roll");
            pwrite.println("//:" + logIn + ":" + "roll"); // send to server
            dice1.setText(" ");
            dice2.setText(" ");
            dice3.setText(" ");
            askToRoll.setVisible(false);
            yes.setVisible(false);
            no.setVisible(false);
            rollDice.setVisible(true);
            rollDice.setEnabled(false);
            commandsPanel.updateUI();
        }
        if (e.getSource() == no) {
            //sendMess("none");
            pwrite.println( "//:" + logIn + ":" + "none"); // send to server
            dice1.setText(" ");
            dice2.setText(" ");
            dice3.setText(" ");
            askToRoll.setVisible(false);
            yes.setVisible(false);
            no.setVisible(false);
            rollDice.setVisible(true);
            rollDice.setEnabled(false);
            commandsPanel.updateUI();
        }


        if (e.getSource() == startGame) {
            System.out.println("before selectedIndex");
            sendMessage = Integer.toString(1 + comboBox.getSelectedIndex());
            //sendMess("room " + sendMessage);
            pwrite.println( "//:" + logIn + ":room " + sendMessage);
            textField.setVisible(false);
            comboBox.setVisible(false);
            startGame.setVisible(false);
            chooseGame.setVisible(false);
            d1.setVisible(true);
            d2.setVisible(true);
            d3.setVisible(true);
            ok.setVisible(false);
            rs.setVisible(true);
            gs1.setVisible(true);
            gs2.setVisible(true);
            dice1.setVisible(true);
            dice2.setVisible(true);
            dice3.setVisible(true);
            roundScore.setVisible(true);
            gameScore1.setVisible(true);
            gameScore2.setVisible(true);
            rollDice.setVisible(true);
            winLable.setVisible(false);
            commandsPanel.updateUI();


        }


    }

    //
    static public void sendMess(String buff) {
        pwrite.println( "//:" + logIn + ": " + buff);
        pwrite.flush();
    }

    public static void main(String[] args) throws Exception
    {
        ClientJava client = new ClientJava();
        String str; // string for message

        logIn = JOptionPane.showInputDialog("Login:");
        loginLable.setText("Player: " + logIn);
        loginLable.setVisible(true);

        System.out.println("Login: " + logIn);

        sendMess("new");
//        pwrite.println("//:" + logIn + ": new");
//        pwrite.flush();
        receiveMessage = receiveRead.readLine();

        if((receiveMessage) != "0") //receive from server
        {
            System.out.println("Server: "+receiveMessage);
            receiveMessage = receiveMessage.replace('+','\n');
            textArea.setText(receiveMessage);
        }

        System.out.println("Mistnost:");
        boolean waitForAnswer = true;

        while(true)
        {
            if (waitForAnswer) {
                pwrite.flush();
            }
            waitForAnswer = false;

            receiveMessage = receiveRead.readLine();
            //System.out.println("Server : original mess: " + receiveMessage);
            if (receiveMessage != null) {
                if (receiveMessage.length() > 0) //receive from server
                {
                    receiveMessage = replaceUnreadable(receiveMessage);
                    receiveMessage = receiveMessage.replace('+','\n');
                    if (!receiveMessage.startsWith("OK") || !receiveMessage.startsWith("dice") || !receiveMessage.startsWith("vysledek") || !receiveMessage.startsWith("game") || !receiveMessage.startsWith("round")){
                        textArea.setText(receiveMessage + "\n");
                    }
                    //textArea.append("----------------\n");
                    //textArea.append(receiveMessage + "\n");
                    System.out.println("Server : " + receiveMessage); // displaying at DOS prompt
                    // pokud je posledni znak : pak server ocekava od klienta odpoved

                    if (receiveMessage.endsWith(":")) {
                        waitForAnswer = true;
                        //askToRoll.setVisible(true);
                        //yes.setVisible(true);
                       // no.setVisible(true);
                        //textArea.append("\n----------------\n");
                    }


                    if (receiveMessage.startsWith("Ahoj")){
                        dice1.setVisible(false);
                        dice2.setVisible(false);
                        dice3.setVisible(false);
                        d1.setVisible(false);
                        d2.setVisible(false);
                        d3.setVisible(false);
                        roundScore.setVisible(false);
                        gameScore1.setVisible(false);
                        gameScore2.setVisible(false);
                        rs.setVisible(false);
                        gs1.setVisible(false);
                        gs2.setVisible(false);
                        rollDice.setVisible(false);
                        askToRoll.setVisible(false);
                        yes.setVisible(false);
                        no.setVisible(false);
                        comboBox.setVisible(true);
                        startGame.setVisible(true);
                        chooseGame.setVisible(true);
                        commandsPanel.updateUI();

                    }

                    if (receiveMessage.startsWith("PLAY")) { // hra zacina
                        rollDice.setVisible(true);
                        rollDice.setEnabled(true);
                    }

                    if (receiveMessage.startsWith("dice")){
                        str = receiveMessage.substring(5,6);
                        if( str.compareTo("1") == 0){
                            dice1.setText(receiveMessage.substring(7));
                        }
                        if( str.compareTo("2") == 0){
                            dice2.setText(receiveMessage.substring(7));
                        }
                        if( str.compareTo("3") == 0){
                            dice3.setText(receiveMessage.substring(7));
                        }
                    }
                    if (receiveMessage.startsWith("ASKTOROLL")){
                        askToRoll.setVisible(true);
                        yes.setVisible(true);
                        no.setVisible(true);
                    }

                    if (receiveMessage.startsWith("roundScore")){
                        roundScore.setText(receiveMessage.substring(13));
                        int roundScore =Integer.parseInt(receiveMessage.substring(13));
                        if ( roundScore > 0){
                            rollDice.setEnabled(false);
                            //askToRoll.setVisible(true);
                            //yes.setVisible(true);
                            //no.setVisible(true);
                        }
                    }
                    if (  receiveMessage.startsWith("gameScore")){
                        dice1.setText(" ");
                        dice2.setText(" ");
                        dice3.setText(" ");
                        roundScore.setText(" ");
                    }
                    if (receiveMessage.startsWith("gameScore1")){
                        gameScore1.setText(receiveMessage.substring(13,15));
                    }
                    if (receiveMessage.startsWith("gameScore2")){
                        gameScore2.setText(receiveMessage.substring(13,15));
                    }
                    if(receiveMessage.startsWith("player")){
                        textArea.setText("Now is " + receiveMessage.substring(7) + " turn to roll");
                        rollDice.setEnabled(true);
                    }
                    if (receiveMessage.startsWith("X"+logIn)){
                        winLable.setText("Congratulations! You won!");
                        winLable.setVisible(true);
                        JOptionPane.showMessageDialog(mainPanel, "Congratulations! You won!");
                    }
                    else if( receiveMessage.startsWith("X")){
                        winLable.setText("Oops! You lost!");
                        winLable.setVisible(true);
                        JOptionPane.showMessageDialog(mainPanel, "Oops! You lost...try next time...");
                    }

                }
            }

        }

    }
    private static String removeNonAscii(String s){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<s.length(); ++i){
            if(s.charAt(i) < 128){
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }
    private static String replaceUnreadable(String s){
        String clean = s.replaceAll("\\P{Print}", "");
        return clean;
    }
}
