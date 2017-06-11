import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList; 
import java.io.*;
import javax.sound.sampled.*;


        
public class Main extends BackgroundPanel{
/*   String[] type = { "Serif","SansSerif","宋体"}; 
   int[] styles = { Font.PLAIN, Font.ITALIC, Font.BOLD, Font.ITALIC + Font.BOLD };
   String[] stylenames = { "Plain", "Italic", "Bold", "Bold & Italic" };
   
   public void paint(Graphics g) {
      for (int f = 0; f < type.length; f++) { 
         for (int s = 0; s < styles.length; s++) { 
            Font font = new Font(type[f], styles[s], 18);
            g.setFont(font); 
            String name = type[f] + " " + stylenames[s];
            g.drawString(name, 20, (f * 4 + s + 1) * 20); 
         }
      }
   }*/
String SOUND_FILENAME1 = "heart.wav"; 
String SOUND_FILENAME2 = "Pickup_Coin.wav"; 
private Clip		m_clip;

JTextArea listLabel = new JTextArea("");
ArrayList<String> list = new ArrayList<String>();
int number_of_peopel=200;

public Main(){
        super(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("background.jpg")));
        getNumOfPeople();
        this.listLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        this.listLabel.setEnabled(false);
        this.listLabel.setOpaque(false);
      
        playSound();
        
}
public void getNumOfPeople()
{
        boolean check;

        do
        {
            check = true;
        try
        {
            number_of_peopel = Integer.parseInt(JOptionPane.showInputDialog("参加抽奖人数: "));
        }
        catch(NumberFormatException nfe)
        {
            //nfe.printStackTrace();
            check = false;
        }
        }while(!check);
}
public void playSound() 
{
        AudioInputStream	audioInputStream = null;
        try
        {
                InputStream audioSrc = getClass().getResourceAsStream(SOUND_FILENAME1);
                //add buffer for mark/reset support
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
        }
        catch (Exception e)
        {
                JOptionPane.showMessageDialog(null, e.toString()); 
        }
        if (audioInputStream != null)
        {
                AudioFormat	format = audioInputStream.getFormat();
                DataLine.Info	info = new DataLine.Info(Clip.class, format);
                try
                {
                        m_clip = (Clip) AudioSystem.getLine(info);
                        m_clip.open(audioInputStream);
                }
                catch (Exception e)
                {
                        JOptionPane.showMessageDialog(null, e.toString()); 
                }

                m_clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else
        {
               JOptionPane.showMessageDialog(null, "no audio file"); 
        }
}
public void stopSound() 
{
    m_clip.close();
}
public void bell()
{
        AudioInputStream	audioInputStream = null;
        try
        {
                InputStream audioSrc = getClass().getResourceAsStream(SOUND_FILENAME2);
                //add buffer for mark/reset support
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
        }
        catch (Exception e)
        {
                JOptionPane.showMessageDialog(null, e.toString()); 
        }
        if (audioInputStream != null)
        {
                AudioFormat	format = audioInputStream.getFormat();
                DataLine.Info	info = new DataLine.Info(Clip.class, format);
                try
                {
                        m_clip = (Clip) AudioSystem.getLine(info);
                        m_clip.open(audioInputStream);
                }
                catch (Exception e)
                {
                        JOptionPane.showMessageDialog(null, e.toString()); 
                }

                m_clip.loop(1);
        }
        else
        {
               JOptionPane.showMessageDialog(null, "no audio file"); 
        }  
}
public void autoSize()
{
      String listString = String.join("\n     ", list);
      listLabel.setText("中奖号码：\n     "+listString);
      Dimension frameSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension size = listLabel.getPreferredSize();
      if(size.height + 20 < frameSize.height)
      listLabel.setBounds(frameSize.width - 20 - size.width, 20,
             size.width, size.height);        
}


   static boolean stop = false;
   public static void main(String[] a) {
        

        Main pane = new Main();
        JFrame f = new JFrame();
        //f.setLayout(new GridLayout(3, 1));
        f.setIconImage(Toolkit.getDefaultToolkit().getImage(pane.getClass().getClassLoader().getResource("logo.png")));        
        JLabel numberLabel = new JLabel("         ",JLabel.CENTER);
        numberLabel.setFont(new Font("Serif", Font.PLAIN, 200));
      
        
        f.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
        });
      
        f.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch( keyCode )
                {
                        case KeyEvent.VK_SPACE:
                        //JOptionPane.showMessageDialog(null, "你好");
                        stop = !stop;
                        if(stop){
                                pane.stopSound();
                                pane.bell();
                                pane.list.add(numberLabel.getText());
                        }else{
                                pane.playSound();
                        }
                        break;
                        case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                        default:
                }
            }
        });
        
        
      f.setContentPane(pane);
      f.setExtendedState(JFrame.MAXIMIZED_BOTH); 
      f.setUndecorated(true);
      
      f.getContentPane().setLayout(null);

      Dimension frameSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension size = numberLabel.getPreferredSize();
      numberLabel.setBounds((frameSize.width-size.width)/2, (frameSize.height-size.height)/2,
             size.width, size.height);
     
      f.add(pane.listLabel);
      f.add(numberLabel);
      f.setVisible(true);
      
        Timer timer = new Timer();
        Random rand = new Random();   
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
            //Random rand = new Random();
            pane.repaint();
            pane.autoSize();
            if(stop){
             numberLabel.setForeground(Color.red);
             
            }else{
             numberLabel.setForeground(new Color(rand.nextFloat(),
                                            rand.nextFloat(),
                                            rand.nextFloat()));
             String numStr;
             do{numStr = ""+(rand.nextInt(pane.number_of_peopel)+1);
             }while(pane.list.contains(numStr));
             
             numberLabel.setText(numStr);
            }
          }
        }, 30, 30); 
      

      
   }
   


}