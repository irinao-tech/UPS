import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
public class ClientJava implements ActionListener {

    private static Socket sock;
    private static int PORT = 10157;
    static BufferedReader keyRead;
    static PrintWriter pwrite;
    static InputStream istream;
    static BufferedReader receiveRead;
    OutputStream ostream;
    static String logIn;
    static String receiveMessage, sendMessage;

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
    static JLabel askToRoll;
    static JLabel writeLogin;
    static JTextArea textArea;
    static JTextField textField;
    JLabel diceLabel;
    JLabel diceLabel2;

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
        //writeLogin = new JLabel("Login:");
        diceLabel = new JLabel();
        diceLabel2 = new JLabel();
        //text area
        textArea = new JTextArea();
        textField = new JTextField(10);
        textField.setText("");

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

        commandsPanel.add(diceLabel);
        commandsPanel.add(askToRoll);
        commandsPanel.add(yes);
        commandsPanel.add(no);
        commandsPanel.setPreferredSize(new Dimension(400,100));

        centralPanel.add(textArea);
        centralPanel.add(textField);
        centralPanel.add(ok);


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

            JOptionPane.showMessageDialog(mainPanel,
                    "YES" + commandsPanel, "Rules", JOptionPane.INFORMATION_MESSAGE);
            diceLabel.updateUI();

            commandsPanel.updateUI();
        }

        if (e.getSource() == yes) {
            pwrite.println( logIn + ": " + "A");

            commandsPanel.updateUI();
        }
        if (e.getSource() == no) {
            pwrite.println( logIn + ": " + "N");

            commandsPanel.updateUI();
        }

        if (e.getSource() == ok) {
            sendMessage = textField.getText();

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
                askToRoll.setVisible(true);
                yes.setVisible(true);
                no.setVisible(true);
                //sendMessage = keyRead.readLine();  // keyboard reading
                //sendMessage = textField.getText();
                pwrite.println(logIn + ": " + sendMessage);       // sending to server
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
                    System.out.println("Server : " + receiveMessage); // displaying at DOS prompt
                    // pokud je posledni znak : pak server ocekava od klienta odpoved
                    if (receiveMessage.endsWith(":")) {
                        waitForAnswer = true;
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