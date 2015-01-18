import java.awt.*;          
import javax.swing.*;     
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.*;    // new import for timer


import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
 

 class MyPanel extends JPanel implements ActionListener {
	Image img;
 
   private Timer myTimer;
   int x=50;
   int y=50;

	 
	 public MyPanel(){
		 System.out.println("constructor");
		         myTimer = new Timer(120, this ); // 120 – delay in milliseconds
        myTimer.start();

		 try{
			 img = ImageIO.read(new File("seemlegit.jpg"));
		}
		catch(Exception e){
			System.err.println(e);
		}

	  }
	  
	  
   public void paintComponent( Graphics g ){
       super.paintComponent( g );
	    g.drawImage(img, 0, 0, null);
		 g.drawRect(x,y,200,50);
		 g.drawOval(x+100,y+10,100,100);
   }
   

   public void actionPerformed( ActionEvent e ){//is called every interval
       y++;
	 repaint();
   }

	
	
}


public class BrickBreaker{         

  public static void main ( String[] args )   {

		JFrame frame = new JFrame();    
        frame.setSize(1000,1200);          
        frame.setVisible( true );                       
        frame.setTitle("NotePad++");
        Image img = null;
		 try{
			  img = ImageIO.read(new File("icon.bmp"));
		}
		catch(Exception e){
			System.err.println(e);
		}
        
        frame.setIconImage(img);
        
        
        
        frame.setDefaultCloseOperation ( WindowConstants.EXIT_ON_CLOSE ); 
        MyPanel panel=new MyPanel();
        frame.getContentPane().add(panel);
        
        
        

    }                                                                        
  }
