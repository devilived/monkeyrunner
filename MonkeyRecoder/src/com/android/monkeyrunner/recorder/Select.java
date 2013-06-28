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

public class Select extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JTextField startTextField;
	private JLabel startLine;

	private Frame MRF = null;
	static int startNum = 1;
	String sNum;

	public void actionPerformed(ActionEvent arg0) {
		sNum = startTextField.getText();
		

		if(sNum.equals("") ){
				JOptionPane.showMessageDialog(null, "不能为空", "提示", 1);
		}else if(!(Pattern.matches("^[0-9]+$", sNum)
				)){
				JOptionPane.showMessageDialog(null, "只能为大于等于0的整数！", "提示", 1);
		}else if(sNum.equals("0") ){
			JOptionPane.showMessageDialog(null, "不能为0", "提示", 1);
		}else{
			startNum =Integer.parseInt(sNum);
			this.setVisible(false);
		}
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(122, 92, 70, 30));
			jButton.setText("确定");
			jButton.addActionListener(this);
		}
		return jButton;
	}

	private JLabel getStartLine() {
		if (startLine == null) {
			startLine = new JLabel();
			startLine.setBounds(new Rectangle(32, 42, 70, 25));
			startLine.setText("对比个数：");
		}
		return startLine;
	}

//	private JLabel getEndLine() {
//		if (endLine == null) {
//			endLine = new JLabel();
//			endLine.setBounds(new Rectangle(32, 52, 70, 25));
//			endLine.setText("结束行号：");
//		}
//		return endLine;
//	}

	private JTextField getStartTextField() {
		if (startTextField == null) {
			startTextField = new JTextField();
			startTextField.setBounds(new Rectangle(122, 42, 115, 25));
		}
		return startTextField;
	}

//	private JTextField getEndTextField() {
//		if (endTextField == null) {
//			endTextField = new JTextField();
//			endTextField.setBounds(new Rectangle(122, 52, 115, 25));
//		}
//		return endTextField;
//	}

	public static void main(String[] args) {
		// TODO 自动生成方法存根

		new Select();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Select thisClass = new Select();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	public Select() {
		super();
		initialize();
		// startLine = new JLabel("开始行号：");
		// endLine = new JLabel("结束行号：");
	}

	public Select(Frame m) {
		this();
		MRF = m;
	}

	private void initialize() {
		this.setSize(300, 180);
		this.setContentPane(getJContentPane());
		this.setTitle("请指定要对比次数：");
		this.setResizable(false);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getStartLine());
//			jContentPane.add(getEndLine());
			jContentPane.add(getStartTextField());
	//		jContentPane.add(getEndTextField());
			// jContentPane.add(endTextField);
			jContentPane.add(getJButton(), null);
		}
		return jContentPane;
	}

}
