package com.android.monkeyrunner.recorder;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class getImgLocations extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JTextField imgXTextField;
	private JTextField imgYTextField;
	private JTextField imgWidthTextField;
	private JTextField imgHighTextField;
	private JLabel imgX;
	private JLabel imgY;
	private JLabel imgWidth;
	private JLabel imgHigh;
	private Frame MRF = null;
	String imgXValue;
	String imgYValue;
	String imgWidthValue;
	String imgHighValue;
	int xValue;
	int yValue;
	int widthValue;
	int highValue;
	int flagEndNum;
	String sNum;
	String eNum;
	int size;
	int flagSize;
	int num;


	public void actionPerformed(ActionEvent arg0) {
		imgXValue = imgXTextField.getText();
		imgYValue = imgYTextField.getText();
		imgWidthValue = imgWidthTextField.getText();
		imgHighValue = imgHighTextField.getText();
		
		if(imgXValue.equals("") || imgYValue.equals("") || imgWidthValue.equals("") || imgHighValue.equals("") ){
				JOptionPane.showMessageDialog(null, "任意值都不能为空", "提示", 1);
		}else if(!(Pattern.matches("^[0-9]+$", imgXValue) && Pattern.matches("^[0-9]+$", imgYValue) && Pattern.matches("^[0-9]+$", imgWidthValue) && Pattern.matches("^[0-9]+$", imgHighValue))){
				JOptionPane.showMessageDialog(null, "所有输入的值只能为大于等于0的整数！", "提示", 1);
		}else if(!(Pattern.matches("^[0-9]+$", imgWidthValue) && Pattern.matches("^[0-9]+$", imgHighValue))){
			JOptionPane.showMessageDialog(null, "长宽值只能为大于0的整数！", "提示", 1);
		}else{
			xValue = Integer.parseInt(imgXValue);
			yValue = Integer.parseInt(imgYValue);
			widthValue = Integer.parseInt(imgWidthValue);
			highValue = Integer.parseInt(imgHighValue);
			System.out.println();
			this.setVisible(false);
//			Select select = new Select();
			int i = num;
			System.out.println("aaaaaaa"+i);
			MRF.cutPicture(xValue,yValue,widthValue,highValue,num);
//			MRF.playScript(startNum, flagEndNum);
			// Frame2 f2=new Frame2(this);
			// f2.setVisible(true);
		}

	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(122, 152, 70, 30));
			jButton.setText("确定");
			jButton.addActionListener(this);
		}
		return jButton;
	}

	private JLabel getImgX() {
		if (imgX == null) {
			imgX = new JLabel();
			imgX.setBounds(new Rectangle(32, 22, 70, 25));
			imgX.setText("起始横坐标：");
		}
		return imgX;
	}

	private JLabel getImgY() {
		if (imgY == null) {
			imgY = new JLabel();
			imgY.setBounds(new Rectangle(32, 52, 70, 25));
			imgY.setText("起始纵坐标：");
		}
		return imgY;
	}
	
	private JLabel getImgWidth() {
		if (imgWidth == null) {
			imgWidth = new JLabel();
			imgWidth.setBounds(new Rectangle(32, 82, 70, 25));
			imgWidth.setText("截图宽度：");
		}
		return imgWidth;
	}

	private JLabel getImgHigh() {
		if (imgHigh == null) {
			imgHigh = new JLabel();
			imgHigh.setBounds(new Rectangle(32, 122, 70, 25));
			imgHigh.setText("截图高度：");
		}
		return imgHigh;
	}

	private JTextField getImgXTextField() {
		if (imgXTextField == null) {
			imgXTextField = new JTextField();
			imgXTextField.setBounds(new Rectangle(122, 22, 115, 25));
		}
		return imgXTextField;
	}

	private JTextField getImgYTextField() {
		if (imgYTextField == null) {
			imgYTextField = new JTextField();
			imgYTextField.setBounds(new Rectangle(122, 52, 115, 25));
		}
		return imgYTextField;
	}
	
	private JTextField getImgWidthTextField() {
		if (imgWidthTextField == null) {
			imgWidthTextField = new JTextField();
			imgWidthTextField.setBounds(new Rectangle(122, 82, 115, 25));
		}
		return imgWidthTextField;
	}

	private JTextField getImgHighTextField() {
		if (imgHighTextField == null) {
			imgHighTextField = new JTextField();
			imgHighTextField.setBounds(new Rectangle(122, 122, 115, 25));
		}
		return imgHighTextField;
	}

	public static void main(String[] args) {
		// TODO 自动生成方法存根

		new getImgLocations();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getImgLocations thisClass = new getImgLocations();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	public getImgLocations() {
		super();
		initialize();
		// startLine = new JLabel("开始行号：");
		// endLine = new JLabel("结束行号：");
	}

	public getImgLocations(Frame frame,int num) {
		this();
		MRF = frame;
		this.num = num;
		System.out.println("getImgLocations num :"+this.num);
	}

	private void initialize() {
		this.setSize(300, 250);
		this.setContentPane(getJContentPane());
		this.setTitle("请指定图片比对位置：");
		this.setResizable(false);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getImgX());
			jContentPane.add(getImgY());
			jContentPane.add(getImgWidth());
			jContentPane.add(getImgHigh());
			jContentPane.add(getImgXTextField());
			jContentPane.add(getImgYTextField());
			jContentPane.add(getImgWidthTextField());
			jContentPane.add(getImgHighTextField());
			// jContentPane.add(endTextField);
			jContentPane.add(getJButton(), null);
		}
		return jContentPane;
	}

}
