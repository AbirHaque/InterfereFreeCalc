import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.datatransfer.*;
import java.awt.geom.*;
import java.net.*;
import javax.sound.sampled.*;
import java.text.*;  
public class GUILoader
{
    public static Double time = 0.0;
    public static ArrayList<Double> audioValues = new ArrayList<Double>();
    public static int n = 0;
    public static BufferedImage graphPicture;
    
    public static JFrame frame = new JFrame("Interfere Free Calc");
    public static JButton calculateButton = new JButton("Calculate");
    public static JButton playButton = new JButton("Play");
    public static JPanel panel = new JPanel(new BorderLayout());
    public static JLabel label = new JLabel("f(x) = ");//JLabel("ƒ(x) = ");
    public static JLabel answerLabel = new JLabel();
    public static JTextField textField = new JTextField(30);
    
    
    public static File graphFile = new File("graph.jpg");//500x200 pixels

    
    public static JLabel graphIcon;
    public static JPanel graphPanel = new JPanel(new BorderLayout());
    public static JPanel buttonPanel = new JPanel();

    
    
    private static JMenuItem exportWAV;
    private static JMenuItem exportAU;
    private static JMenuItem exportAIFF;
    private static JMenuItem moreAbout;
    private static JMenuItem moreHomePage;
    private static JMenuItem moreLicense;

    public static void main(String[] args) throws Exception
    {
        GUILoader guiLoader = new GUILoader();

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon logo = new ImageIcon("logo.png");
        
       
        
        //graphFile.createNewFile();
        graphPicture = ImageIO.read(new File(graphFile.getName()));

        
        buttonPanel.add(calculateButton);
        buttonPanel.add(playButton);
        graphPanel.add(buttonPanel, BorderLayout.PAGE_END);
        
        graphIcon = new JLabel(new ImageIcon(graphPicture));
        
        graphPanel.add(graphIcon, BorderLayout.CENTER);
        panel.add(label, BorderLayout.LINE_START);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(answerLabel, BorderLayout.LINE_END);
        panel.add(graphPanel, BorderLayout.PAGE_END);

        calculateButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                answerLabel.setForeground(new Color(0,200,0));
                try
                {
                    calcGraph();
                }
                catch (Exception x)
                {
                    answerLabel.setText("Error");
                    answerLabel.setForeground(Color.RED);
                    //System.out.println(x);
                }  
            }
        });
        playButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                answerLabel.setForeground(new Color(0,200,0));
                try
                {
                    calcGraph();
                    if (n>0)
                    {
                        Double[] values = new Double[audioValues.size()];
                        for (int i = 0; i < audioValues.size(); i++)
                        {
                            values[i] = audioValues.get(i);
                        }
                        InputStream inputStream = new ByteArrayInputStream(SimpleWaveManager.getMonoBytes(values));
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream); 
                        AudioFormat format = audioInputStream.getFormat();
                        DataLine.Info info = new DataLine.Info(Clip.class, format);
                        Clip clip = (Clip) AudioSystem.getLine(info);
                        clip.open(audioInputStream);
                        clip.start();
                    }
                    else    
                    {
                        frame.setState(Frame.NORMAL);
                    }
                }
                catch (Exception x)
                {
                    answerLabel.setText("Error");
                    answerLabel.setForeground(Color.RED);
                    //System.out.println(x);
                }  
            }
        });
        

        /*
         * Menu bar components
         */
        JMenuBar menuBar = new JMenuBar();
        JMenu projectMenu = new JMenu("Project");
        
        /*
         * Export menu components
         */
        JMenu exportMenu = new JMenu("Export");
            exportWAV = new JMenuItem("WAV");
            exportAU = new JMenuItem("AU");
            exportAIFF = new JMenuItem("AIFF");
            exportMenu.add(exportWAV);
            exportMenu.add(exportAU);
            exportMenu.add(exportAIFF);
            menuBar.add(exportMenu);
        
        /*
         * More menu components
         */
        JMenu moreMenu = new JMenu("More");
            moreAbout = new JMenuItem("About");
            moreHomePage = new JMenuItem("Home Page");
            moreLicense = new JMenuItem("License Information");
            moreMenu.add(moreAbout);
            moreMenu.add(moreHomePage);
            moreMenu.add(moreLicense);
            menuBar.add(moreMenu);

        
        exportWAV.addActionListener(new ActionListener() 
        {
              public void actionPerformed(ActionEvent e)
              {
                  try
                  {
                      writeWav();
                  }
                  catch(Exception x)
                  {
                  }
              }
        });
        exportAU.addActionListener(new ActionListener() 
        {
              public void actionPerformed(ActionEvent e)
              {
                  try
                  {
                      writeAu();
                  }
                  catch(Exception x)
                  {
                  }
              }
        });
        exportAIFF.addActionListener(new ActionListener() 
        {
              public void actionPerformed(ActionEvent e)
              {
                  try
                  {
                      writeAiff();
                  }
                  catch(Exception x)
                  {
                  }
              }
        });
        
        moreAbout.addActionListener(new ActionListener() 
        {
              public void actionPerformed(ActionEvent e)
              {
                  //Display information about software
                  JOptionPane.showMessageDialog(null, "Version: "+"0.0.1"+"\nAuthor: Abir Haque\nContact: abir.haque.usa@gmail.com\nHomepage: abirhaque.github.io", "About", JOptionPane.PLAIN_MESSAGE);
              }
        });
        moreHomePage.addActionListener(new ActionListener() 
        {
              public void actionPerformed(ActionEvent e)
              {
                  //Redirect user to homepage
                  Desktop desktop = java.awt.Desktop.getDesktop();
                  try 
                  {
                      desktop.browse(new URI("https://interfere.abirhaque.repl.co"));
                  }
                  catch (Exception x) 
                  {
                      JOptionPane.showMessageDialog(null, "Failed to open homepage. Navigate to https://interfere.abirhaque.repl.co to reach the homepage.", "Error", JOptionPane.PLAIN_MESSAGE);
                  }
              }
        });
        moreLicense.addActionListener(new ActionListener() 
        {
              public void actionPerformed(ActionEvent e)
              {
                  //Bring licence information panel
                  try
                  {
                      BufferedReader in = new BufferedReader(new FileReader("LICENSE.txt"));
                      String licenseString = "";
                      while(in.ready())
                      {
                          licenseString+=in.readLine()+"\n";
                      }
                      in.close();
                      JTextArea text = new JTextArea(licenseString);
                      text.setEditable(false);
                      Dimension newDim = new Dimension(700,400);
                      JScrollPane scroll = new JScrollPane(text);  
                      scroll.setPreferredSize(newDim);
                      scroll.setMinimumSize(newDim);
                      scroll.setPreferredSize(newDim);
                      scroll.setMaximumSize(newDim);
                      scroll.setSize(newDim);
                      scroll.revalidate();
                      scroll.getVerticalScrollBar().setUnitIncrement(20);
                      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                      JOptionPane.showMessageDialog(null, scroll, "License Information", JOptionPane.PLAIN_MESSAGE);
                  }
                  catch(Exception x)
                  {
                      JOptionPane.showMessageDialog(null, "Failed to display license information. You can still view license information in the <LICENSE.txt> file.", "Error", JOptionPane.PLAIN_MESSAGE);
                  }
              }
        });
        
        /*
        * Frame attributes
        */
        frame.setLayout(new FlowLayout());
        frame.add(panel);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setIconImage(logo.getImage());
    }
    public static void writeWav() throws Exception
    { 
        try
        {
            calcGraph();
            if (n>0)
            {
                Double[] values = new Double[audioValues.size()];
                for (int i = 0; i < audioValues.size(); i++)
                {
                    values[i] = audioValues.get(i);
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
                Date date = new Date();  
                String dateString = formatter.format(date);  
                SimpleWaveManager.write(values,dateString+".wav");
                String currentDirectory = System.getProperty("user.dir");
                JOptionPane.showMessageDialog(null, dateString+".wav exported to: "+currentDirectory, "Success", JOptionPane.PLAIN_MESSAGE);
            }
            else    
            {
                frame.setState(Frame.NORMAL);
            }
        }
        catch (Exception x)
        {
            JOptionPane.showMessageDialog(null, "Failed to create file.", "Error", JOptionPane.PLAIN_MESSAGE);
            //System.out.println(x);
        } 
    }
    public static void writeAu() throws Exception
    { 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
        Date date = new Date();  
        String dateString = formatter.format(date);  
        calcGraph();
        if (n>0)
        {
            Double[] values = new Double[audioValues.size()];
            for (int i = 0; i < audioValues.size(); i++)
            {
                values[i] = audioValues.get(i);
            }
            SimpleWaveManager.write(values,dateString+".wav");
        }
        else    
        {
            frame.setState(Frame.NORMAL);
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(dateString+".wav"));
        AudioSystem.write(audioStream, AudioFileFormat.Type.AU, new File(dateString+".au"));
        String currentDirectory = System.getProperty("user.dir");
        JOptionPane.showMessageDialog(null, dateString+".au exported to: "+currentDirectory, "Success", JOptionPane.PLAIN_MESSAGE);
    }
    public static void writeAiff() throws Exception
    { 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
        Date date = new Date();  
        String dateString = formatter.format(date);  
        calcGraph();
        if (n>0)
        {
            Double[] values = new Double[audioValues.size()];
            for (int i = 0; i < audioValues.size(); i++)
            {
                values[i] = audioValues.get(i);
            }
            SimpleWaveManager.write(values,dateString+".wav");
        }
        else    
        {
            frame.setState(Frame.NORMAL);
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(dateString+".wav"));
        AudioSystem.write(audioStream, AudioFileFormat.Type.AIFF, new File(dateString+".aiff"));
        String currentDirectory = System.getProperty("user.dir");
        JOptionPane.showMessageDialog(null, dateString+".aiff exported to: "+currentDirectory, "Success", JOptionPane.PLAIN_MESSAGE);
    }

    public static void calcGraph() throws Exception
    {
        answerLabel.setForeground(new Color(0,200,0));
        if (textField.getText().equals(""))
        {
            textField.setText("0");
        }
        try
        {
            graphPicture = ImageIO.read(new File(graphFile.getName()));
            Graphics2D graphGraphics = (Graphics2D) graphPicture.getGraphics();
            graphGraphics.setStroke(new BasicStroke(3));
            
            graphGraphics.setColor(Color.BLUE);
            EquationReader.readyFunctionList();
            EquationReader.readyMultiParamFunctions();
            EquationReader.readyLeftAssociative();
            EquationReader.readyPrecedence();
            EquationReader.readyConstants();
            String equation = textField.getText();
            ArrayList<String> postFix = EquationReader.evalShuntingYard(equation);
            if (postFix.contains("x"))
            {
                answerLabel.setText("");
                time = 1.0;
                audioValues = new ArrayList<Double>();
                n = (int) (44100 * time);
                for (int i = 0; i <= n; i++)
                {
                    ArrayList<String> postFixTemp = new ArrayList<String>();
                    for (int k = 0; k < postFix.size(); k++)
                    {
                        if(postFix.get(k).equals("x"))
                        {
                            postFixTemp.add(String.valueOf(i));
                        }
                        else
                        {
                            postFixTemp.add(postFix.get(k));
                        }
                    }
                    audioValues.add(Double.valueOf(EquationReader.evalPostFix(postFixTemp)));
                }
                int xCoord = 0;
                for (int i = 0; i <= 500; i+=1)
                {
                    graphGraphics.drawLine(xCoord, (int)(-100 * audioValues.get(i))+100, xCoord+5, (int)(-100 * audioValues.get(i+1))+100);
                    xCoord+=5;
                }
                graphIcon.setIcon(new ImageIcon(graphPicture));
                graphPanel.add(graphIcon);
            }
            else
            {
                String answer = EquationReader.evalPostFix(postFix);
                answerLabel.setText(" = " + answer);
                graphGraphics.drawLine(0, (int)(-100 * Double.valueOf(answer))+100, graphPicture.getWidth(), (int)(-100 * Double.valueOf(answer))+100);
                graphIcon.setIcon(new ImageIcon(graphPicture));
                graphPanel.add(graphIcon);
            }
        }
        catch (Exception x)
        {
            answerLabel.setText("Error");
            answerLabel.setForeground(Color.RED);
            //System.out.println(x);
        } 
    }
}