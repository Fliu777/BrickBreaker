/*
By Frank Liu
Brickbreaker
April 9th, 2013
Using classes to make a simple version

*/



import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.event.*; // new import for timer

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

class MyPanel extends JPanel implements  ActionListener {
	Image background;
	Image img;
	Image ballas;

	private Timer myTimer;
	int x = 50;
	int y = 50;
	ArrayList<String[]> map = new java.util.ArrayList<String[]>();
	ArrayList<Brick> bricklvl = new java.util.ArrayList<Brick>();
	int screenadjust = 85;
	int space=10;
	int characterspace=7;
	Paddle p;
	Ball balls;
	public MyPanel() {		
		try {
			ballas = ImageIO.read(new File("ball.bmp"));
			System.out.println("I GOT");
		} catch (Exception e) {
			System.err.println(e);
		}
		//balls=new Ball(0,0,ballas);

		Brick b;

		p=new Paddle();
		//balls=new Ball(p.getX()+150,p.getY()-10,ballas);
		balls=new Ball(0,0,ballas);
		
		try {
			background = ImageIO.read(new File("seemlegit.jpg"));
		} catch (Exception e) {
			System.err.println(e);
		}


		Scanner lvl = null;
		try {
			lvl = new Scanner(new FileReader("BrickBreaker.java"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp;


		while (lvl.hasNext()) {
			temp = lvl.nextLine();
			temp = temp.replace('\t', ' ');
			if (temp.length() == 0) {
				continue;
			}

			map.add(temp.split(" "));
		}
		lvl.close();

		int oldlength =60;

		for (int i = 0; i < Math.min(40,map.size()); i++) {
			for (int j = 0; j < map.get(i).length; j++) {

				
				if (map.get(i)[j].length()==0) {
					//ystem.out.println("not included^");
					continue;
				}
				System.out.println("///*"+map.get(i)[j]+"*////");
				map.get(i)[j]=map.get(i)[j].trim();
				b = new Brick(map.get(i)[j], (oldlength+characterspace), screenadjust
						+ i * 12, map.get(i)[j].length(), true);
				oldlength += map.get(i)[j].length()*characterspace+characterspace;
				bricklvl.add(b);
			}
			oldlength = 60;
		}
		myTimer = new Timer(120, this); // 120 – delay in milliseconds
		myTimer.start();

	}

	public void paintComponent(Graphics g) {
		g.setFont(new Font("Courier", Font.PLAIN, 12));
		g.drawImage(background,0, 0,null);
		p.draw(g);
		for (int i = 0; i < bricklvl.size(); i++) {
			if (bricklvl.get(i).isVisible())
				bricklvl.get(i).draw(g);
		}
		balls.draw(g);

	}

	public void actionPerformed(ActionEvent e) {// is called every interval
		
		boolean exit=false;
		PointerInfo point = MouseInfo.getPointerInfo();
		Point b = point.getLocation();
		int w = (int) b.getX();
		if (w>p.getX()){
			p.move(1,w-p.getXplace());
		}
		else{
			p.move(-1,p.getXplace()-w);
		}
		balls.move();
		for (int i=0;i<bricklvl.size();i++){
			if (bricklvl.get(i).isVisible()&&balls.collides(bricklvl.get(i).rect)){
				bricklvl.get(i).setInvisible();
				break;
			}
		}
		int count=0;
		for (int i=0;i<bricklvl.size();i++){
			if (bricklvl.get(i).isVisible()==false){
				count++;
			}

		}
		System.out.println(count);
		System.out.println(bricklvl.size());
		if (count==bricklvl.size()){
			System.out.println("you win");
			exit=true;
		}
		
		if (balls.collides(p.getRect())){
			System.out.println("hit");
		}
		int yloc=balls.getYplace();
		if (yloc>p.getYplace()+15){
			balls.restart(p.getXplace()+150, p.getYplace()-10);
			p.loselife();
		}
		if (p.getlives()<=0){
			System.out.println("you lose");
			exit=true;
		}
		if (exit){
			System.out.println("good bye");
			myTimer.stop();
		}
		

		
		repaint();
		

	}


}

public class BrickBreaker {

	public static void main(String[] args) {
		//Declaring variable(s) and making scanner
		

		JFrame frame = new JFrame();

		frame.setTitle("NotePad++");
		Image img = null;
		try {
			img = ImageIO.read(new File("icon.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		frame.setIconImage(img);
		JButton startButton = new JButton("Start");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container c=frame.getContentPane();
		MyPanel p2=new MyPanel();
		MyPanel panel = new MyPanel();
		c.setLayout(new BorderLayout());
		c.add(panel, BorderLayout.CENTER);
		c.add(startButton,BorderLayout.NORTH);
				c.add(startButton,BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}

}

class Brick extends Rectangle {
	private int xplace;
	private int yplace;
	private boolean alive;
	Rectangle rect;
	String text;
	int length;
	
	public Brick(int x, int y){
		setXplace(x);
		setYplace(y);
	}

	public Brick(String word, int x, int y, int length, boolean alive) {
		setXplace(x);
		setYplace(y);
		this.length = length;
		this.alive = alive;
		text = word;
		rect = new Rectangle(getXplace(), getYplace()-12, length*7, 12);
	}

	public void draw(Graphics g) {
		g.drawString(text, getXplace(), getYplace());
	//	g.drawRect(xplace,yplace-12, length*7, 12);
	}

	public void setInvisible() {
		alive = false;
	}

	public boolean isVisible() {
		return alive;
	}

	public int getXplace() {
		return xplace;
	}

	public void setXplace(int xplace) {
		this.xplace = xplace;
	}

	public int getYplace() {
		return yplace;
	}

	public void setYplace(int yplace) {
		this.yplace = yplace;
	}

}

class Paddle extends Brick{
	private int dx, dy;
	private int xsize, ysize;
	Rectangle rect;
	private int lives;
	
	public Paddle(){
		super(300,650);
		lives=5;
		dx=dy=30;
		xsize=200;
		ysize=50;

	}
	
	public void move(int multiply, int distance){
		//distance+=xsize/2;
		dx=60;
		if (distance<60){
			dx=distance;
		}
		setXplace(getXplace() + dx*multiply);
		if (getXplace()>=1000){
			setXplace(1000);
		}
		else if (getXplace()<=0){
			setXplace(0);
		}
		
	}
	public Rectangle getRect(){

		rect = new Rectangle(getXplace(), getYplace(), xsize, ysize);

		return rect;
	}
	public void loselife(){
		lives--;
	}
	public int getlives(){
		return lives;
	}
	
	
	public void draw(Graphics g) {
		g.drawString("Lives left:"+Integer.toString(lives), 1000, 150);
		g.drawString("while (true){", getXplace(), getYplace()+10);
		g.drawString("     return True;",getXplace(),getYplace()+20);
		g.drawString("     return False && True;",getXplace(),getYplace()+30);
		g.drawString("}",getXplace(),getYplace()+40);
		//g.drawRect(getXplace(),getYplace(), xsize, ysize);
	   // g.setColor(Color.BLACK);
	}
}




class Ball extends Rectangle {
	private int xplace;
	private int yplace;
	private int dx;
	private int dy;
	private int ballsize;
	Image img;

	public Ball(int x, int y) {
		xplace = x;
		yplace = y;
		dx = 1;
		dy = 1;
		ballsize=30;
	}

	public Ball(int x, int y, Image img) {
		System.out.println("Initialize here--------------------------");
		xplace = x;
		yplace = y;
		dx = 15;
		dy = 15;
		this.img=img;
		ballsize=img.getHeight(null);
	}
	public void move(){
		xplace+=dx;
		yplace+=dy;
		if (xplace<0){
			dx=-dx;
		}
		else if (xplace>1280){
			dx=-dx;
		}
		if (yplace<0 || yplace>969){
			dy=-dy;
		}
	}
	public void restart(int x,int y){
		xplace=x;
		yplace=y;
		dx=dy=-15;
	}

	public void changespeed(int change) {
		dx += change;
		dy += change;
	}

	public void draw(Graphics g) {
		g.drawImage(img, xplace, yplace, null);
		g.drawRect(xplace, yplace, ballsize, ballsize);
	}

	public boolean collides(Rectangle object1) {
		boolean collision;
		Rectangle r=new Rectangle(xplace,yplace,ballsize,ballsize);
		
		if (r.intersects(object1)){
			System.out.println((object1.getMaxX()-object1.getMinX())/2);
			System.out.println(xplace);
			collision=true;
			if (xplace<(object1.getMaxX()-object1.getMinX())/2+object1.getMinX()){
				dy=-dy;
				System.out.println("hi there");
			}
			else{
				dy=-dy;
				dx=-dx;
			}
		}
		else{
			collision=false;
		}
		return collision;
	}
	public int getYplace() {
		return yplace;
	}

}