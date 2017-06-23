import java.awt.*;
import java.lang.Math;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;



public class Main extends JPanel {

        private String SOUND_FILENAME1 = "heart.wav";
        private String SOUND_FILENAME2 = "Pickup_Coin.wav";
        private int LOGO_SIZE = 138;
        private int gap = 15;
        private Clip		m_clip;


        private JLabel numLabel = new JLabel("123456",JLabel.CENTER);
        private JTextArea listArea = new JTextArea("");

        private ArrayList<String> list = new ArrayList<String>();
        private int number_of_peopel=200;
        static boolean stop = false;
        static int count = 0;
        static int shift_x = 0;
        
        static int fire_life_time = 0;
        private void fireworks(Graphics graph,int w,int h){
                if(fire_life_time<90){
                        fire_life_time++;
                        //fire_life_time%=90;
                        double acc_y = 0.5;
                        double v0 = 40;
                        double v_y = v0-fire_life_time*acc_y;
                        double sum_y = v_y*fire_life_time;
                        int R = fire_life_time*w/90;//firework max radius
                        
                        for(int i=0;i<5000;i++){
                        int r = (((int)Math.round(Math.random()*4321))%200)+55;
                        int g = (((int)Math.round(Math.random()*4321))%200)+55;
                        int b = (((int)Math.round(Math.random()*4321))%200)+55;
                        graph.setColor(new Color(r,g,b));
                        
                        double alfa = Math.random()*6.28;
                        double rad = Math.random()*R/2;
                        int x = (int)(R/2+rad*Math.cos(alfa));
                        int y = (int)(R/2+rad*Math.sin(alfa));
                        x = x+w/2-R/2;
                        y = y+h-R/2-(int)sum_y;
                        graph.fillOval(x,y,(int)(Math.random()*20),(int)(Math.random()*20));
                        }
                }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension frameSize = getSize();

            Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("background.jpg"));//first method to get image
            g.drawImage(image, 0, 0, frameSize.width,frameSize.height, this);
            
            image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("100Y.jpg"));//first method to get image
            int h = image.getHeight(null);
            int w = image.getWidth(null);
            float x_over_y = (float)w/(float)h;
            int width100Y = (int)(x_over_y*LOGO_SIZE);
            g.drawImage(image, gap, gap,width100Y,LOGO_SIZE, this);
            
            int logo_move_distance = frameSize.width - width100Y -LOGO_SIZE - gap;
            InputStream is = new BufferedInputStream(getClass().getResourceAsStream(("logo.png")));//second method to get image
            if (stop){
                shift_x = 0;//stop logo
                fireworks(g,frameSize.width,frameSize.height);
            }else{
                shift_x = (int)(0.5*logo_move_distance+0.5*logo_move_distance*Math.sin((float)count/30*3.14*2));
                
            }//shift_x = shift_x % frameSize.width;
            try {
                image = ImageIO.read(is);
                g.drawImage(image, 
                        gap+width100Y+shift_x -5, gap, // start with an overlap position
                        LOGO_SIZE, LOGO_SIZE, this); // see javadoc for more info on the parameters
            } catch (Exception e) {}

            Dimension size = numLabel.getPreferredSize();
            numLabel.setBounds((frameSize.width-size.width)/2, (frameSize.height-size.height)/2,
                               size.width, size.height);
                               
            
        }

        public Main() {
            //super(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("background.jpg")));
            setLayout(null);
            if (!getNumOfPeople())System.exit(0);

            listArea.setFont(new Font("Serif", Font.PLAIN, 20));
            listArea.setEnabled(false);
            listArea.setOpaque(false);

            add(listArea);

            numLabel.setFont(new Font("Serif", Font.PLAIN, 200));
            add(numLabel);

            playSound(SOUND_FILENAME1,Clip.LOOP_CONTINUOUSLY);
        }
        public boolean getNumOfPeople() {
            boolean check;
            do {
                check = true;
                try {
                    Object response  = JOptionPane.showInputDialog(null,
                                       "ÖÐ½±ºÅÂë·¶Î§1~", "²Î¼Ó³é½±ÈËÊý: ",
                                       JOptionPane.QUESTION_MESSAGE,null,null,
                                       ""+number_of_peopel);
                    if (response == null)return false;//process cancel
                    number_of_peopel = Integer.parseInt(response.toString());
                } catch (NumberFormatException nfe) {
                    //nfe.printStackTrace();
                    check = false;
                }
            } while (!check);

            return true;
        }
        public void playSound(String sound_file,int loop) {
            AudioInputStream	audioInputStream = null;
            try {
                InputStream audioSrc = getClass().getResourceAsStream(sound_file);
                //add buffer for mark/reset support
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
            if (audioInputStream != null) {
                AudioFormat	format = audioInputStream.getFormat();
                DataLine.Info	info = new DataLine.Info(Clip.class, format);
                try {
                    m_clip = (Clip) AudioSystem.getLine(info);
                    m_clip.open(audioInputStream);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.toString());
                }

                m_clip.loop(loop);//Clip.LOOP_CONTINUOUSLY);
            } else {
                JOptionPane.showMessageDialog(null, "no audio file");
            }
        }
        public void stopSound() {
            m_clip.close();
        }

        public void autoSize() {
            String listString = String.join("\n     ", list);
            listArea.setText("ÖÐ½±ºÅÂë£º\n     "+listString);
            Dimension frameSize = this.getSize();
            Dimension size = listArea.getPreferredSize();
            if (size.height + 20 < frameSize.height)
                listArea.setBounds(frameSize.width - 20 - size.width, 20,
                                   size.width, size.height);



        }

        public void generateNumber(boolean stop)

        {

            Random rand = new Random();

            if (stop) {
                numLabel.setForeground(Color.red);
            } else {
                numLabel.setForeground(new Color(rand.nextFloat(),
                                                 rand.nextFloat(),
                                                 rand.nextFloat()));
                String numStr;
                do {
                    numStr = ""+(rand.nextInt(number_of_peopel)+1);

                } while (list.contains(numStr));
                numLabel.setText(numStr);
            }

        }


        public static void main(String[] a) {


            Main pane = new Main();
            JFrame f = new JFrame();
            //f.setLayout(new GridLayout(3, 1));
            f.setIconImage(Toolkit.getDefaultToolkit().getImage(pane.getClass().getClassLoader().getResource("logo.png")));

            f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
                               );

            f.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {}

                @Override
                public void keyReleased(KeyEvent e) {}

                @Override
                public void keyPressed(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    switch ( keyCode ) {
                    case KeyEvent.VK_SPACE:
                        //JOptionPane.showMessageDialog(null, "ÄãºÃ");
                        stop = !stop;
                        if (stop) {
                            fire_life_time = 0;//init fireworks
                            pane.stopSound();
                            pane.playSound(pane.SOUND_FILENAME2,1);
                            pane.list.add(pane.numLabel.getText());
                            pane.autoSize();
                        } else {
                            pane.playSound(pane.SOUND_FILENAME1,Clip.LOOP_CONTINUOUSLY);
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    default:
                    }
                }
            }
                            );


            f.setContentPane(pane);

            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            f.setUndecorated(true);

            f.setVisible(true);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    pane.count++;
                    pane.count %= 30;
                    pane.generateNumber(stop);
                    pane.repaint();
                }
            }
            , 30, 30);



        }




}