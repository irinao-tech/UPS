import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
public class ClientJava implements ActionListener {

    private static Socket sock;
    private static int PORT = 10187;
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
    JPanel mainPanel;
    JPanel infoPanel;
    JPanel commandsPanel;
    JPanel centralPanel;
    static JButton rollDice;
    static JButton yes;
    static JButton no;
    static JButton ok;
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
    JLabel diceLabel;
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
        infoPanel = new JPanel();
        commandsPanel = new JPanel();
        centralPanel = new JPanel();

        //layout for central pannel
//        GridBagConstraints left = new GridBagConstraints();
//        left.anchor = GridBagConstraints.EAST;
//        GridBagConstraints right = new GridBagConstraints();
//        right.weightx = 2.0;
//        right.fill = GridBagConstraints.HORIZONTAL;
//        right.gridwidth = GridBagConstraints.REMAINDER;

        //buttons
        rollDice = new JButton("Roll Dice");
        yes = new JButton("Yes");
        no = new JButton("No");
        ok = new JButton("Ok");
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

        options.add(quit);
        options.add(explanation);

        menuBar.add(options, BorderLayout.EAST);

        infoPanel.add(menuBar);
        //infoPanel.add(writeLogin);
        //infoPanel.add(textField);
        infoPanel.setPreferredSize(new Dimension(600, 100));

        commandsPanel.add(rollDice);
        askToRoll.setVisible(false);
        yes.setVisible(false);
        no.setVisible(false);

        //commandsPanel.add(diceLabel);
        commandsPanel.add(askToRoll);
        commandsPanel.add(yes);
        commandsPanel.add(no);
        //commandsPanel.setPreferredSize(new Dimension(400,100));

        centralPanel.add(textArea);
        //centralPanel.add(dice1);
        centralPanel.add(textField);
        centralPanel.add(ok);
        centralPanel.add(new JSeparator());
        centralPanel.add(chooseGame);
        //chooseGame.setLabelFor(comboBox);
        centralPanel.add(comboBox);
        centralPanel.add(playersInGame);

        centralPanel.add(new JSeparator());
        centralPanel.add(d1);
        centralPanel.add(dice1);
        //d1.setLabelFor(dice1);
        centralPanel.add(d2);
        //d2.setLabelFor(dice2);
        centralPanel.add(dice2);
        centralPanel.add(d3);
        //d3.setLabelFor(dice3);
        centralPanel.add(dice3);
        d1.setVisible(false);
        d2.setVisible(false);
        d3.setVisible(false);
        dice1.setVisible(false);
        dice2.setVisible(false);
        dice3.setVisible(false);
        //centralPanel.add(rs);
        rs.setLabelFor(roundScore);
        centralPanel.add(roundScore);
        //centralPanel.add(gs1);
        gs1.setLabelFor(gameScore1);
        centralPanel.add(gameScore1);
        //centralPanel.add(gs2);
        gs2.setLabelFor(gameScore2);
        centralPanel.add(gameScore2);
        rs.setVisible(false);
        gs1.setVisible(false);
        gs2.setVisible(false);
        roundScore.setVisible(false);
        gameScore1.setVisible(false);
        gameScore2.setVisible(false);

        centralPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        mainPanel.setPreferredSize(new Dimension(600, 600));
        //mainPanel.setLayout(new BorderLayout());
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(commandsPanel, BorderLayout.SOUTH);
        mainPanel.add(centralPanel, BorderLayout.CENTER);


        quit.addActionListener(this);
        explanation.addActionListener(this);
        rollDice.addActionListener(this);
        yes.addActionListener(this);
        no.addActionListener(this);
        ok.addActionListener(this);

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
            askToRoll.setVisible(false);
            yes.setVisible(false);
            no.setVisible(false);
            //TODO
            commandsPanel.updateUI();
        }

        if (e.getSource() == yes) {
            pwrite.println( logIn + ": " + "A"); // send to server
            askToRoll.setVisible(false);
            yes.setVisible(false);
            no.setVisible(false);
            commandsPanel.updateUI();
        }
        if (e.getSource() == no) {
            pwrite.println( logIn + ": " + "N"); // send to server
            askToRoll.setVisible(false);
            yes.setVisible(false);
            no.setVisible(false);
            commandsPanel.updateUI();
        }

        if (e.getSource() == ok) {
            sendMessage = textField.getText();
            pwrite.println( logIn + ": " + sendMessage);
            textField.setVisible(false);
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
            commandsPanel.updateUI();

        }


    }

    //
    public void sendMess(String buff) throws IOException {
        // sending to client (pwrite object)
        pwrite.println( logIn + ": " + "buff");
    }

    public static void main(String[] args) throws Exception
    {
        ClientJava client = new ClientJava();
        String str; // string for message

        //textField.setText(keyRead.readLine());
        //logIn = textField.getText();
        logIn = JOptionPane.showInputDialog("Login:");

//        System.out.println("Login: " + logIn);
        System.out.println("Login: " + logIn);
        //logIn = keyRead.readLine();


        pwrite.println(logIn + ": new");
        pwrite.flush();
        receiveMessage = receiveRead.readLine();



        if((receiveMessage) != "0") //receive from server
        {
            //  receiveMessage = replaceUnreadable(receiveMessage);
            receiveMessage = receiveMessage.replace('+','\n');
            textArea.setText(receiveMessage);
            //System.out.println("Server : "+receiveMessage); // displaying at DOS prompt
        }

        System.out.println("Mistnost:");
        boolean waitForAnswer = true;

        while(true)
        {
            if (waitForAnswer) {

                //sendMessage = keyRead.readLine();  // keyboard reading
                //sendMessage = textField.getText();
                //pwrite.println(logIn + ": " + sendMessage);       // sending to server
                pwrite.flush();                    // flush the data
            }

            waitForAnswer = false;
//            for(int i=0; i<1024;i++){
//                String s=receiveRead.read();
//                receiveMessage[i]=s;
//                if(s == ">") {
//                    return i;
//                }
//            }
//            System.out.println(recei  veMessage);


            receiveMessage = receiveRead.readLine();
            if (receiveMessage != null) {
                if (receiveMessage.length() > 0) //receive from server
                {
                    receiveMessage = replaceUnreadable(receiveMessage);
                    receiveMessage = receiveMessage.replace('+','\n');
                    textArea.setText(receiveMessage + "\n");
                    //textArea.append("----------------\n");
                    //textArea.append(receiveMessage + "\n");
                    System.out.println("Server : " + receiveMessage); // displaying at DOS prompt
                    // pokud je posledni znak : pak server ocekava od klienta odpoved
                    if (receiveMessage.endsWith(":")) {
                        waitForAnswer = true;
                        askToRoll.setVisible(true);
                        yes.setVisible(true);
                        no.setVisible(true);
                        //textArea.append("\n----------------\n");
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

                    if (receiveMessage.startsWith("roundScore")){
                        roundScore.setText(receiveMessage.substring(13));
                    }
                    if (receiveMessage.startsWith("gameScore1")){
                        gameScore1.setText(receiveMessage.substring(11,13));
                    }
                    if (receiveMessage.startsWith("gameScore2")){
                        gameScore2.setText(receiveMessage.substring(11,13));
                    }

                }
            }
            //removeNonAscii(receiveMessage);
            //replaceUnreadable(receiveMessage);
            //receiveMessage = receiveMessage.substring(0,0);


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