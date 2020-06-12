import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.plaf.basic.BasicScrollBarUI;

public class SimpleChatClient{
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    JButton sendButton;
    JButton saveButton;
    Socket sock;
    JFrame frame;
    static String name;
    public static void main(String[] args){
        SimpleChatClient client = new SimpleChatClient();
        name = client.getName();
        client.go(name);
    }

    public String getName(){
        String user = "";
        while(user.equals("")){
            user = JOptionPane.showInputDialog(
            "What is your name?", null);
            System.out.println(user);
        }
        return user;
    }

    public void go(String name){
        frame = new JFrame("Ludicrously Simple Chat Client");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        incoming = new JTextArea();
        incoming.setFont(new Font("Serif", Font.BOLD, 28));
        incoming.setBackground(new Color(13, 2, 8));
        incoming.setForeground(new Color(0, 143, 17)); //Islamic Green
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);

        qScroller.getVerticalScrollBar().setBackground(new Color(13, 2, 8));
        /*qScroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(13, 2, 8);
            }
        });*/
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing = new JTextField(50);
        outgoing.setFont(new Font("Ariel", Font.BOLD,20));
        outgoing.setBackground(new Color(13, 2, 8)); 
        outgoing.setForeground(Color.RED);
        outgoing.addActionListener(new EnterKeyListener());
        outgoing.setCaretColor(new Color(0, 255, 65));

        sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());

        mainPanel.add(qScroller);
        //mainPanel.add(outgoing);
        JPanel southPanel = new JPanel();
        southPanel.add(outgoing);
        southPanel.add(sendButton);
        //mainPanel.add(sendButton);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
        frame.setLocation((int)width/8, (int)height/8);
        frame.setSize((int)(3*width/4), (int)(3*height/4));

        frame.setVisible(true);
        southPanel.setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        outgoing.requestFocus();

        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

    private void setUpNetworking(){
        try{
            incoming.append("Connecting");
            Thread.sleep(500);
            incoming.append(".");
            Thread.sleep(500);
            incoming.append(".");
            Thread.sleep(500);
            incoming.append(".\n");
            sock = new Socket("127.0.0.1", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("Networking established");
            Thread.sleep(2000);
            incoming.append("Network Established!\n");
            Thread.sleep(1500);
            incoming.append("Welcome ");
            Thread.sleep(500);
            incoming.append("to ");
            Thread.sleep(500);
            incoming.append("Ayush's ");
            Thread.sleep(500);
            incoming.append("Secret ");
            Thread.sleep(500);
            incoming.append("Portal\n\n");

        } catch(IOException ex){ex.printStackTrace();}
          catch(InterruptedException ex){ex.printStackTrace();}
    }
    
    public class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent ae){
            try{
                writer.println(name + " : " + outgoing.getText());
                writer.flush();
            } catch(Exception ex){
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class EnterKeyListener implements ActionListener{
        public void actionPerformed(ActionEvent ae){
            sendButton.doClick();
        }
    }

    public class IncomingReader implements Runnable{
        public void run(){
            String message;
            try{
                while ((message = reader.readLine()) != null){
                    System.out.println("read " + message);
                    incoming.append(message + "\n");
                }
            } catch(Exception ex){
                ex.printStackTrace();
                frame.dispose();
            }
        }
    }
}
